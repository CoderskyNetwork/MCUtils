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
import es.xdec0de.mcutils.MCUtils;

public class MCFile {

	/**
	 * The {@link MCPlugin} that initialized this MCFile
	 */
	protected final MCPlugin plugin;
	private final String path;

	private final File file;
	private FileConfiguration cfg;

	MCFile(MCPlugin plugin, String path, String pathIfInvalid) {
		if (!MCPlugin.getMCPlugin(MCUtils.class).strings().hasContent(path))
			path = pathIfInvalid;
		path += ".yml";
		this.plugin = plugin;
		this.path = path;
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		if(!(file = new File(plugin.getDataFolder(), path)).exists())
			plugin.saveResource(path, false);
		reload(false);
		update(false, new ArrayList<>(0));
	}

	protected MCFile(MCPlugin plugin, String path) {
		this(plugin, path, "default");
	}

	public String getPath() {
		return path;
	}

	public File getFile() {
		return file;
	}

	public FileConfiguration getFileConfig() {
		return cfg;
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
			if(plugin.getResource(path) != null)
				updated.load(copyInputStreamToFile(plugin.getDataFolder()+ "/"+path, plugin.getResource(path)));
			else {
				log("&8[&4"+pluginName+"&8] > &cCould not update &6"+path);
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
			cfg.save(plugin.getDataFolder() + "/"+path);
			if(changes != 0) {
				log(" ");
				if(reload)
					log("&8[&6"+pluginName+"&8] > &6"+path+" &7has been reloaded with &b"+changes+" &7changes.");
				else
					log("&8[&6"+pluginName+"&8] > &6"+path+" &7has been updated to &ev"+plugin.getDescription().getVersion()+"&7 with &b"+changes+" &7changes.");
				return true;
			}
			return false;
		} catch(InvalidConfigurationException | IOException ex) {
			log("&8[&4"+pluginName+"&8] > &cCould not update &6"+path);
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
