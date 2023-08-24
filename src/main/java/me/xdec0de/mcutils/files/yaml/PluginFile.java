package me.xdec0de.mcutils.files.yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import me.xdec0de.mcutils.MCPlugin;
import me.xdec0de.mcutils.files.FileUpdater;

/**
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class PluginFile extends YmlFile implements FileUpdater {

	/** The {@link JavaPlugin} that initialized this file */
	final JavaPlugin plugin;

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link PluginFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link MCPlugin#registerFile(String, Class)}.
	 * <p>
	 * {@link PluginFile}s are required to be on <b>plugin</b>'s jar file as a resource. For this
	 * exact reason they can be easily updated to newer versions. This files can be used, for example,
	 * as configuration files that require certain content to be present on them and are likely to be
	 * updated on future versions.
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, the ".yml" extension is automatically added if missing,
	 * if the path is null, empty or blank, "file" will be used.
	 * @param charset the charset to use, if null, {@link StandardCharsets#UTF_8} will be used.
	 * 
	 * @throws IllegalArgumentException if <b>plugin</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public PluginFile(@Nonnull JavaPlugin plugin, @Nullable String path, @Nullable Charset charset) {
		super(plugin, path, charset);
		this.plugin = plugin;
	}

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link PluginFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link MCPlugin#registerFile(String, Class)}.
	 * <p>
	 * {@link PluginFile}s are required to be on <b>plugin</b>'s jar file as a resource. For this
	 * exact reason they can be easily updated to newer versions. This files can be used, for example,
	 * as configuration files that require certain content to be present on them and are likely to be
	 * updated on future versions.
	 * <p>
	 * This constructor uses {@link StandardCharsets#UTF_8}, to specify use {@link #PluginFile(JavaPlugin, String, Charset)}
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, the ".yml" extension is automatically added if missing,
	 * if the path is null, empty or blank, "file" will be used.
	 * 
	 * @throws IllegalArgumentException if <b>plugin</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public PluginFile(@Nonnull JavaPlugin plugin, @Nullable String path) {
		super(plugin, path, StandardCharsets.UTF_8);
		this.plugin = plugin;
	}

	/**
	 * Creates this file, the file MUST be present
	 * on the plugin's jar file as a resource.
	 * This is required in order to copy the file from
	 * the plugin source and to update it. If you don't need
	 * this functionality and want an empty file instead, use
	 * {@link YmlFile}
	 * <p>
	 * This method is not required when calling
	 * {@link MCPlugin#registerFile(String, Class)}
	 * as it does this automatically.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload()
	 * @see #update(List)
	 */
	@Override
	public boolean create() {
		file.getParentFile().mkdirs();
		if (!file.exists())
			plugin.saveResource(getPath(), false);
		return reload();
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

	/**
	 * Updates this file, adding any missing path from the plugin
	 * source file to the local file stored on the server.
	 * 
	 * @param ignored a list of paths to ignore on update, if null
	 * or empty, no paths will be ignored.
	 * 
	 * @return True if no errors occurred, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #create()
	 * @see #reload()
	 */
	public boolean update(@Nullable List<String> ignored) {
		String pluginName = plugin.getName();
		try {
			int changes = 0;
			CharsetYamlConfiguration updated = new CharsetYamlConfiguration(getCharset());
			if (plugin.getResource(getPath()) != null)
				updated.load(copyInputStreamToFile(plugin.getDataFolder()+ "/"+getPath(), plugin.getResource(getPath())));
			else
				return log("&8[&4"+pluginName+"&8] > &cCould not update &6"+getPath()+"&8: &4File not found", false);
			final boolean ignores = ignored == null || ignored.isEmpty();
			Set<String> oldKeys = ignores ? getKeys(true) : getKeys(true).stream().filter(str -> !ignored.contains(str)).collect(Collectors.toSet());
			Set<String> updKeys = ignores ? updated.getKeys(true) : updated.getKeys(true).stream().filter(str -> !ignored.contains(str)).collect(Collectors.toSet());
			for (String str : oldKeys)
				if(!updKeys.contains(str)) {
					set(str, null);
					changes++;
				}
			for (String str : updKeys)
				if(!oldKeys.contains(str)) {
					set(str, updated.get(str));
					changes++;
				}
			save(plugin.getDataFolder() + "/"+getPath());
			if (changes != 0)
				return log("&8[&6"+pluginName+"&8] &6"+getPath()+" &7has been updated to &ev"+plugin.getDescription().getVersion()+"&7 with &b"+changes+" &7changes.", true);
			return true;
		} catch(InvalidConfigurationException | IOException ex) {
			return log("&8[&4"+pluginName+"&8] > &cCould not update &6"+getPath()+"&8: &4"+ex.getMessage(), false);
		}
	}

	private boolean log(String str, boolean ret) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', str));
		return ret;
	}
}
