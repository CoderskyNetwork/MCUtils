package net.codersky.mcutils.spigot.yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.storage.files.ConfigFileHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class PluginFile extends YmlFile implements ConfigFileHolder {

	final JavaPlugin plugin;

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link PluginFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link SpigotPlugin#registerFile(String, Class)}.
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
	 * @see SpigotPlugin#registerFile(PluginFile)
	 * @see SpigotPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public PluginFile(@Nonnull JavaPlugin plugin, @Nullable String path, @Nullable Charset charset) {
		super(plugin, path, charset);
		this.plugin = plugin;
	}

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link PluginFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link SpigotPlugin#registerFile(String, Class)}.
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
	 * @see SpigotPlugin#registerFile(PluginFile)
	 * @see SpigotPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public PluginFile(@Nonnull JavaPlugin plugin, @Nullable String path) {
		super(plugin, path, StandardCharsets.UTF_8);
		this.plugin = plugin;
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

	/**
	 * Creates this file, the file MUST be present
	 * on the plugin's jar file as a resource.
	 * This is required in order to copy the file from
	 * the plugin source and to update it. If you don't need
	 * this functionality and want an empty file instead, use
	 * {@link YmlFile}
	 * <p>
	 * This method is not required when calling
	 * {@link SpigotPlugin#registerFile(String, Class)}
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

	// Update //

	private int updateSet(Set<String> loopKeys, Set<String> condKeys, List<String> ignored, Function<String, Object> action) {
		int changes = 0;
		for (String key : loopKeys) {
			if (!condKeys.contains(key) && !isIgnored(key, ignored)) {
				this.set(key, action.apply(key));
				changes++;
			}
		}
		return changes;
	}
	
	private boolean isIgnored(String path, List<String> ignored) {
		if (ignored == null)
			return false;
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
		int changes = 0;
		try {
			final InputStream updateStream = plugin.getResource(getPath());
			if (updateStream == null)
				return log("&8[&4" + pluginName + "&8] > &cCould not update &6" + getPath() + "&8: &4File not found", false);
			final CharsetYamlConfiguration updated = new CharsetYamlConfiguration(getCharset());
			updated.load(new InputStreamReader(updateStream));
			final Set<String> oldKeys = this.getKeys(true);
			final Set<String> updKeys = updated.getKeys(true);
			changes += updateSet(oldKeys, updKeys, ignored, key -> null);
			changes += updateSet(updKeys, oldKeys, ignored, key -> updated.get(key));
			if (changes == 0)
				return true;
			this.save(plugin.getDataFolder() + "/" + getPath());
			return log("&8[&6" + pluginName + "&8] &6" + getPath() + " &7has been updated with &b" + changes + " &7changes", true);
		} catch (IOException | InvalidConfigurationException ex) {
			return log("&8[&4" + pluginName + "&8] &cCould not update &6" + getPath() + "&8: &4"+ex.getMessage(), false);
		}
	}
}
