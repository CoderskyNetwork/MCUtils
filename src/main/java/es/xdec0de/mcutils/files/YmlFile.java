package es.xdec0de.mcutils.files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.mcutils.MCUtils;

/**
 * Represents a basic yml file, not necessarily present
 * on the plugin's jar file as a resource.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class YmlFile extends CharsetYamlConfiguration {

	/** The {@link JavaPlugin} that initialized this MCFile */
	protected final JavaPlugin plugin;
	private final String path;

	private File file;

	public YmlFile(JavaPlugin plugin, String path, Charset charset) {
		super(charset);
		if (plugin == null)
			throw new NullPointerException("Plugin cannot be null.");
		MCUtils mcUtils = JavaPlugin.getPlugin(MCUtils.class);
		if (!mcUtils.strings().hasContent(path))
			path = "file";
		path += ".yml";
		this.file = new File(plugin.getDataFolder(), path);
		this.plugin = plugin;
		this.path = path;
	}

	public YmlFile(JavaPlugin plugin, String path) {
		this(plugin, path, StandardCharsets.UTF_8);
	}

	/**
	 * Creates this file, the file is not required to
	 * be present on the plugin's jar. For this reason
	 * the file created will be empty. If you want to use
	 * a plugin source file use {@link PluginFile} instead.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void create() {
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		reload();
	}

	/**
	 * Gets the path of this yml file, including the .yml extension.
	 * Files are always inside of the data folder of the plugin
	 * that created them, which isn't a part of this String.
	 * 
	 * @return The path of this yml file.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String getPath() {
		return path;
	}

	public File getFile() {
		return file;
	}

	/**
	 * Saves this file. If there is any error saving the file,
	 * the errors will be logged and false will be returned.
	 * 
	 * @return true if no errors occurred while saving, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean save() {
		try {
			save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Reloads this file. Any non saved value contained within this configuration will be removed
	 * and the new values will be loaded from the given file. If there is any error reloading the file,
	 * the errors will be logged and false will be returned.
	 * 
	 * @return true if no errors occurred while reloading, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean reload() {
		try {
			load(file);
			return true;
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			return false;
		}
	}
}
