package es.xdec0de.mcutils.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import es.xdec0de.mcutils.MCPlugin;

public class PluginFile extends YmlFile {

	private File file;
	private FileConfiguration cfg;

	PluginFile(MCPlugin plugin, String path, String pathIfInvalid) {
		super(plugin, path, pathIfInvalid);
	}

	protected PluginFile(MCPlugin plugin, String path) {
		this(plugin, path, "default");
	}

	/**
	 * Creates this file, the file MUST be present
	 * on the plugin's jar file as a resource.
	 * This is required in order to copy the file from
	 * the plugin source and updating it. If you don't need
	 * this functionality and want an empty file instead, use
	 * {@link YmlFile}<p>
	 * This method is not required when calling
	 * {@link MCPlugin#registerFile(String, Class)}
	 * as it does this automatically.
	 */
	@Override
	public void create() {
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		if(!(file = new File(plugin.getDataFolder(), getPath())).exists())
			plugin.saveResource(getPath(), false);
		reload(false);
		update(false, new ArrayList<>(0));
	}

	/**
	 * Reloads this file without updating,
	 * use {@link #reload(boolean)} to update.
	 */
	@Override
	public void reload() {
		cfg = YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * Reloads this file.
	 * 
	 * @param update whether to update the files or not.
	 * This is recommended to be true to prevent any
	 * missing path after a file modification, if you
	 * want some paths to be ignored by the updater, use
	 * {@link #reload(List)}.
	 * 
	 * @see #reload(List)
	 */
	public void reload(boolean update) {
		if (update)
			update(true, new ArrayList<>(0));
		cfg = YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * Reloads this file. The file updater ignores the paths present
	 * on <b>ignoredUpdatePaths</b>, this is specially useful
	 * if you want administrators to create their own config
	 * paths without them being removed, for example, on a GUI plugin.
	 * 
	 * @param ignoredUpdatePaths a list with the paths to
	 * be ignored by the file updater, update is assumed
	 * to be true with this method, if you want to reload
	 * without updating use {@link #reload(boolean)} with
	 * <b>update</b> as false.
	 * 
	 * @see #reload(boolean)
	 */
	public void reload(List<String> ignoredUpdatePaths) {
		update(true, ignoredUpdatePaths);
		cfg = YamlConfiguration.loadConfiguration(file);
	}

	private File copyInputStreamToFile(String path, InputStream inputStream) {
		File file = new File(path);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int read;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1)
				outputStream.write(bytes, 0, read);
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean update(boolean reload, List<String> ign) {
		String pluginName = plugin.getName();
		try {
			int changes = 0;
			Utf8YamlConfiguration updated = new Utf8YamlConfiguration();
			if(plugin.getResource(getPath()) != null)
				updated.load(copyInputStreamToFile(plugin.getDataFolder()+ "/"+getPath(), plugin.getResource(getPath())));
			else {
				log("&8[&4"+pluginName+"&8] > &cCould not update &6"+getPath());
				return false;
			}
			Set<String> oldKeys = ign.isEmpty() ? cfg.getKeys(true) : cfg.getKeys(true).stream().filter(str -> !ign.contains(str)).collect(Collectors.toSet());
			Set<String> updKeys = ign.isEmpty() ? updated.getKeys(true) : updated.getKeys(true).stream().filter(str -> !ign.contains(str)).collect(Collectors.toSet());
			for(String str : oldKeys)
				if(!updKeys.contains(str)) {
					cfg.set(str, null);
					changes++;
				}
			for(String str : updKeys)
				if(!oldKeys.contains(str)) {
					cfg.set(str, updated.get(str));
					changes++;
				}
			cfg.save(plugin.getDataFolder() + "/"+getPath());
			if(changes != 0) {
				log(" ");
				if(reload)
					log("&8[&6"+pluginName+"&8] > &6"+getPath()+" &7has been reloaded with &b"+changes+" &7changes.");
				else
					log("&8[&6"+pluginName+"&8] > &6"+getPath()+" &7has been updated to &ev"+plugin.getDescription().getVersion()+"&7 with &b"+changes+" &7changes.");
				return true;
			}
			return false;
		} catch(InvalidConfigurationException | IOException ex) {
			log("&8[&4"+pluginName+"&8] > &cCould not update &6"+getPath());
			return false;
		}
	}

	private void log(String str) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', str));
	}

	class Utf8YamlConfiguration extends YamlConfiguration {

		@Override
		public void save(File file) throws IOException {
			if (file == null)
				return;
			Files.createParentDirs(file);
			String data = this.saveToString();
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
			try {
				writer.write(data);
			} finally {
				writer.close();
			}
		}

		@Override
		public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
			if (file == null)
				throw new FileNotFoundException("File is null");
			this.load(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
		}
	}
}
