package net.codersky.mcutils.files.yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import net.codersky.mcutils.MCPlugin;
import net.codersky.mcutils.files.FileUpdater;

/**
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class PluginFile extends YmlFile implements FileUpdater {

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
			plugin.saveResource(this.getPath(), false);
		return this.reload();
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
		final String pluginName = plugin.getName();
		final String path = this.getPath();
		final boolean ignores = ignored != null && !ignored.isEmpty();
		int changes = 0;
		try {
			final CharsetYamlConfiguration updated = new CharsetYamlConfiguration(this.getCharset());
			if (plugin.getResource(path) != null)
				updated.load(copyInputStreamToFile(plugin.getDataFolder() + "/" + path, plugin.getResource(path)));
			else
				return log("&8[&4"+pluginName+"&8] > &cCould not update &6" + path + "&8: &4File not found", false);
			final Set<String> oldKeys = this.getKeys(true);
			final Set<String> updKeys = updated.getKeys(true);
			for (String oldPath : oldKeys)
				if (!updKeys.contains(oldPath) && (!ignores || !isIgnored(oldPath, ignored))) {
					this.set(oldPath, null);
					changes++;
				}
			for (String updPath : updKeys)
				if (!oldKeys.contains(updPath) && (!ignores || !isIgnored(updPath, ignored))) {
					this.set(updPath, updated.get(updPath));
					changes++;
				}
			if (changes == 0)
				return true;
			this.save(plugin.getDataFolder() + "/" + path);
			return log("&8[&6"+pluginName+"&8] &6" + path + " &7has been updated to &ev"+plugin.getDescription().getVersion()+"&7 with &b"+changes+" &7changes.", true);
		} catch(InvalidConfigurationException | IOException ex) {
			return log("&8[&4"+pluginName+"&8] > &cCould not update &6" + path + "&8: &4"+ex.getMessage(), false);
		}
	}

	private boolean isIgnored(String path, List<String> ignored) {
		for (String ignoredPath : ignored)
			if (path.startsWith(ignoredPath))
				return true;
		return false;
	}

	private boolean log(String str, boolean ret) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', str));
		return ret;
	}

	/**
	 * Gets the {@link JavaPlugin} that initialized this file.
	 * 
	 * @return The {@link JavaPlugin} that initialized this file.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public JavaPlugin getPlugin() {
		return plugin;
	}
}
