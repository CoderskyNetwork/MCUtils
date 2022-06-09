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
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import es.xdec0de.mcutils.MCUtils;

public class MCFile {

	/**
	 * The plugin that initialized this MCFile
	 */
	protected final JavaPlugin plugin;
	private final String path;

	private final File file;
	private FileConfiguration cfg;

	MCFile(JavaPlugin plugin, String path, String pathIfInvalid) {
		if (!JavaPlugin.getPlugin(MCUtils.class).strings().hasContent(path))
			path = pathIfInvalid;
		path += ".yml";
		this.plugin = plugin;
		this.path = path;
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		if(!(file = new File(plugin.getDataFolder(), path)).exists())
			plugin.saveResource(path, false);
		reload(false);
		update(false);
	}

	protected MCFile(JavaPlugin plugin, String path) {
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

	public void reload(boolean update) {
		if (update)
			update(true);
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

	private boolean update(boolean reload) {
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
			Set<String> oldKeys = cfg.getKeys(true);
			Set<String> updKeys = updated.getKeys(true);
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