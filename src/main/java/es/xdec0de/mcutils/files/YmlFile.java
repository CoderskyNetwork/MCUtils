package es.xdec0de.mcutils.files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import es.xdec0de.mcutils.MCUtils;

/**
 * Represents a basic yml file, not necessarily present
 * on the plugin's jar file as a resource.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class YmlFile {

	/** The {@link JavaPlugin} that initialized this MCFile */
	protected final JavaPlugin plugin;
	private final String path;

	private File file;
	protected final CharsetYamlConfiguration cfg;

	YmlFile(JavaPlugin plugin, String path, String pathIfInvalid, Charset charset) {
		MCUtils mcUtils = JavaPlugin.getPlugin(MCUtils.class);
		cfg = new CharsetYamlConfiguration(charset);
		if (!mcUtils.strings().hasContent(path))
			path = pathIfInvalid;
		path += ".yml";
		this.plugin = plugin;
		this.path = path;
	}

	protected YmlFile(JavaPlugin plugin, String path) {
		this(plugin, path, "default", Charsets.UTF_8);
	}

	protected YmlFile(JavaPlugin plugin, String path, Charset charset) {
		this(plugin, path, "default", charset);
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
		this.file = new File(plugin.getDataFolder(), path);
		file.mkdirs();
		try {
			cfg.load(new File(plugin.getDataFolder(), path));
			reload();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
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

	public CharsetYamlConfiguration getFileConfig() {
		return cfg;
	}

	/**
	 * Reloads this file.
	 * <p>
	 * Any errors loading the Configuration will be logged and then ignored.If the specified input is not a valid config, a blank config will bereturned. 
	 * <p>
	 * The encoding used may follow the system dependent default.
	 */
	public void reload() {
		try {
			cfg.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
}
