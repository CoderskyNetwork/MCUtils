package es.xdec0de.mcutils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import es.xdec0de.mcutils.files.MessagesFile;
import es.xdec0de.mcutils.files.PluginFile;
import es.xdec0de.mcutils.files.YmlFile;
import es.xdec0de.mcutils.general.MCStrings;
import es.xdec0de.mcutils.server.MCVersion;

public class MCPlugin extends JavaPlugin {

	private final List<YmlFile> files = new ArrayList<>();

	public static <T extends MCPlugin> T getMCPlugin(Class<T> plugin) {
		return JavaPlugin.getPlugin(plugin);
	}

	/**
	 * 
	 * @param <T> must extend {@link YmlFile}
	 * @param file the file to be registered, {@link T#create()} will be called to ensure that the file exists,
	 * the actual method being called depends on the type of file, for example, if the file is a {@link YmlFile},
	 * {@link YmlFile#create()} will be called, if the file is a {@link PluginFile}, then {@link PluginFile#create()}
	 * gets called and so on.
	 * 
	 * @return The registered file, the <b>file</b> parameter.
	 */
	public <T extends YmlFile> T registerFile(T file) {
		file.create();
		files.add(file);
		return file;
	}

	/**
	 * Registers a file to this plugin, if the file doesn't exist it will be created.
	 * 
	 * @param <T> must extend {@link YmlFile}
	 * @param path the path of the file to register, if {@link MCStrings#hasContent(String)} returns false, "file" will be used.
	 * File extension is automatically added and will be .yml.
	 * @param type the type of {@link YmlFile} to create.
	 * 
	 * @return The registered file, null if any error occurred.
	 * 
	 * @see {@link YmlFile}
	 * @see {@link MessagesFile}
	 */
	public <T extends YmlFile> T registerFile(String path, Class<T> type) {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor(JavaPlugin.class, String.class, String.class);
			T file = constructor.newInstance(this, path, "file");
			file.create();
			files.add(file);
			return file;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

	/**
	 * Reloads all files registered to the plugin by
	 * calling {@link PluginFile#reload(boolean)} on them.
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
		files.forEach(file -> {
			if (file instanceof PluginFile)
				((PluginFile)file).reload(true);
		});
	}

	/**
	 * Reloads all files registered to the plugin by
	 * calling {@link PluginFile#reload(List)} on them.
	 * Which makes the file updater ignore the paths present
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
		files.forEach(file -> {
			if (file instanceof PluginFile)
				((PluginFile)file).reload(ignoredUpdatePaths);
		});
	}

	/**
	 * Gets the current server version as an {@link MCVersion}
	 * 
	 * @return the current server version.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCVersion getServerVersion() {
		String bukkitVer = Bukkit.getVersion();
		for(MCVersion version : MCVersion.values())
			if(bukkitVer.contains(version.getFormatName()))
				return version;
		return MCVersion.UNKNOWN;
	}

	/**
	 * <b>WARNING:</b> This method does <i>NOT</i> run asynchronously by itself, it's recommended to do so by using:
	 * {@link BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Consumer)}
	 * <p><p>
	 * Gets the latest version of a plugin on <b>consumer</b> using spigotmc's API.
	 * Version can take a time to update after a new resource update.
	 * <p><p>
	 * Example usage from a class extending {@link MCPlugin}:
	 * <p>
	 * <code>Bukkit.getScheduler().runTaskAsynchronously(this, () -> getLatestVersion(42, ver -> { } ));</code>
	 * 
	 * @param resourceId the ID of the resource to retrieve, the ID can be found at your resource url:
	 * <i>{@literal https://www.spigotmc.org/resources/<name>.<ID IS HERE>/}</i>
	 * @param consumer the consumer that will accept the version string, note that this string can be null if
	 * any error occurred while retrieving the latest version.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Bukkit#getScheduler()
	 * @see BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Consumer)
	 */
	public void getLatestVersion(int resourceId, Consumer<String> consumer) {
		try {
			InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
			Scanner scanner = new Scanner(inputStream);
			if (scanner.hasNext())
				consumer.accept(scanner.next());
			scanner.close();
		} catch (IOException ex) {
			consumer.accept(null);
		}
	}
}
