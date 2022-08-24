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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import es.xdec0de.mcutils.files.MessagesFile;
import es.xdec0de.mcutils.files.PluginFile;
import es.xdec0de.mcutils.files.YmlFile;
import es.xdec0de.mcutils.server.MCVersion;
import es.xdec0de.mcutils.strings.MCStrings;

/**
 * Represents a {@link JavaPlugin} using
 * the {@link MCUtils} API.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #getMCPlugin(Class)
 * @see #registerFile(String, Class)
 * @see #reload(List)
 */
public class MCPlugin extends JavaPlugin {

	private final List<YmlFile> files = new ArrayList<>();

	/**
	 * Gets an instance of {@link MCUtils}
	 * 
	 * @return An instance of {@link MCUtils}
	 */
	@Nonnull
	public MCUtils getMCUtils() {
		return JavaPlugin.getPlugin(MCUtils.class);
	}

	/**
	 * 
	 * @param <T> must extend {@link YmlFile}
	 * @param file the file to be registered, {@link T#create()} will be called to ensure that the file exists,
	 * the actual method being called depends on the type of file, for example, if the file is a {@link YmlFile},
	 * {@link YmlFile#create()} will be called, if the file is a {@link PluginFile}, then {@link PluginFile#create()}
	 * gets called and so on.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @return The registered file, the <b>file</b> parameter.
	 */
	@Nullable
	public <T extends YmlFile> T registerFile(@Nonnull T file) {
		if (file == null)
			return null;
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
	 * @since MCUtils 1.0.0
	 * 
	 * @see {@link YmlFile}
	 * @see {@link PluginFile}
	 * @see {@link MessagesFile}
	 */
	@Nullable
	public <T extends YmlFile> T registerFile(@Nonnull String path, @Nonnull Class<T> type) {
		if (!getMCUtils().strings().hasContent(path))
			return null;
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
	 * @since MCUtils 1.0.0
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
	 * if you want administrators to create their own paths
	 * without them being removed, for example, on a GUI plugin.
	 * 
	 * @param ignoredUpdatePaths a list with the paths to
	 * be ignored by the file updater, update is assumed
	 * to be true with this method, if you want to reload
	 * without updating use {@link #reload(boolean)} with
	 * <b>update</b> as false.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload(boolean)
	 */
	public void reload(@Nullable List<String> ignoredUpdatePaths) {
		if (ignoredUpdatePaths == null)
			ignoredUpdatePaths = new ArrayList<>(0);
		for (YmlFile file : files)
			if (file instanceof PluginFile)
				((PluginFile)file).reload(ignoredUpdatePaths);
	}

	/**
	 * Gets the current server version as an {@link MCVersion}
	 * 
	 * @return the current server version.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
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
	 * @throws IllegalArgumentException if consumer is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Bukkit#getScheduler()
	 * @see BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Consumer)
	 */
	public void getLatestVersion(@Nonnegative int resourceId, @Nonnull Consumer<String> consumer) {
		if (consumer == null)
			throw new IllegalArgumentException("Consumer cannot be null");
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

	/**
	 * Sends <b>str</b> to the console.
	 * 
	 * @param str the string to send.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void log(@Nullable String str) {
		Bukkit.getConsoleSender().sendMessage(str);
	}

	/**
	 * Sends <b>str</b> to the console with applied colors using {@link MCStrings#applyColor(String)}
	 * 
	 * @param str the string to send.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void logCol(@Nullable String str) {
		Bukkit.getConsoleSender().sendMessage(getMCUtils().strings().applyColor(str));
	}

	/**
	 * A shortcut to register the specified executor to the given event class.
	 * If any of the parameters is null, nothing will be done.
	 * 
	 * @param event the {@link Event} type to register.
	 * @param listener the {@link Listener} to register.
	 * @param priority the {@link EventPriority} to register this event at.
	 * @param executor the {@link EventExecutor} to register
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void registerEvent(@Nonnull Class<? extends Event> event, @Nonnull Listener listener, @Nonnull EventPriority priority, @Nonnull EventExecutor executor) {
		if (event == null || listener == null || priority == null || executor == null)
			return;
		Bukkit.getPluginManager().registerEvent(event, listener, priority, executor, this);
	}

	/**
	 * A shortcut to register all the events of a {@link Listener}.
	 * If <b>listener</b> is null, nothing will be done.
	 * 
	 * @param listener the listener to register.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void registerEvents(@Nonnull Listener listener) {
		if (listener == null)
			return;
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	/**
	 * A shortcut to register multiple {@link Listener}s. If
	 * <b>listeners</b> is null, nothing will be done, any null
	 * listener will be ignored.
	 * 
	 * @param listeners the listeners to register.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void registerEvents(@Nullable Listener... listeners) {
		if (listeners == null)
			return;
		for(Listener listener : listeners)
			registerEvents(listener);
	}
}
