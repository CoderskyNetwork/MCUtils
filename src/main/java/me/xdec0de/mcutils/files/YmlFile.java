package me.xdec0de.mcutils.files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import me.xdec0de.mcutils.MCPlugin;

/**
 * Represents a basic yml file, not necessarily present
 * on the plugin's jar file as a resource.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class YmlFile extends CharsetYamlConfiguration {

	private final String path;
	final File file;

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link YmlFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link MCPlugin#registerFile(String, Class)}.
	 * <p>
	 * {@link YmlFile}s aren't required to be on <b>plugin</b>'s jar file as a resource. For this
	 * exact reason they can't copy any default file from <b>plugin</b>'s source. This files can
	 * be used, for example, as storage files for players, where you don't need a "template" file to copy.
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder, if null,
	 * the file will be just in <b>path</b>, not inside a plugin data folder.
	 * @param path the path of the file to create, the ".yml" extension is automatically added if missing,
	 * if the path is null, empty or blank, "file" will be used.
	 * @param charset the charset to use, if null, {@link StandardCharsets#UTF_8} will be used.
	 * 
	 * @throws IllegalArgumentException if <b>plugin</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(YmlFile)
	 * @see MCPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public YmlFile(@Nullable JavaPlugin plugin, @Nullable String path, @Nullable Charset charset) {
		super(charset);
		String modifiedPath = path;
		if (modifiedPath == null || modifiedPath.isBlank())
			modifiedPath = "file.yml";
		else if (!modifiedPath.endsWith(".yml"))
			modifiedPath += ".yml";
		this.file = plugin == null ? new File(modifiedPath) : new File(plugin.getDataFolder(), modifiedPath);
		this.path = modifiedPath;
	}

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link YmlFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link MCPlugin#registerFile(String, Class)}.
	 * <p>
	 * {@link YmlFile}s aren't required to be on <b>plugin</b>'s jar file as a resource. For this
	 * exact reason they can't copy any default file from <b>plugin</b>'s source. This files can
	 * be used, for example, as storage files for players, where you don't need a "template" file to copy.
	 * <p>
	 * This constructor uses {@link StandardCharsets#UTF_8}, to specify use {@link #YmlFile(JavaPlugin, String, Charset)}
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, the ".yml" extension is automatically added if missing,
	 * if the path is null, empty or blank, "file" will be used.
	 * 
	 * @throws IllegalArgumentException if <b>plugin</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(YmlFile)
	 * @see MCPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public YmlFile(@Nonnull JavaPlugin plugin, @Nullable String path) {
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
