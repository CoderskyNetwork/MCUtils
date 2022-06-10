package es.xdec0de.mcutils.files;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.mcutils.MCPlugin;
import es.xdec0de.mcutils.MCUtils;

public class YmlFile {

	/**
	 * The {@link MCPlugin} that initialized this MCFile
	 */
	protected final MCPlugin plugin;
	private final String path;

	private File file;
	private FileConfiguration cfg;

	YmlFile(MCPlugin plugin, String path, String pathIfInvalid) {
		MCUtils mcUtils = JavaPlugin.getPlugin(MCUtils.class);
		if (!mcUtils.strings().hasContent(path))
			path = pathIfInvalid;
		path += ".yml";
		this.plugin = plugin;
		this.path = path;
	}

	protected YmlFile(MCPlugin plugin, String path) {
		this(plugin, path, "default");
	}

	/**
	 * Creates this file, the file is not required to
	 * be present on the plugin's jar. For this reason
	 * the file created will be empty. If you want to use
	 * a plugin source file use {@link PluginFile} instead.
	 */
	public void create() {
		this.file = new File(plugin.getDataFolder(), path);
		file.mkdirs();
		cfg = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), path));
		reload();
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
	 */
	public void reload() {
		cfg = YamlConfiguration.loadConfiguration(file);
	}
}
