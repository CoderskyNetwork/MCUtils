package me.xdec0de.mcutils;

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
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.xdec0de.mcutils.files.MessagesFile;
import me.xdec0de.mcutils.files.PluginFile;
import me.xdec0de.mcutils.files.YmlFile;
import me.xdec0de.mcutils.general.MCCommand;
import me.xdec0de.mcutils.guis.GUI;
import me.xdec0de.mcutils.guis.GUIHandler;
import me.xdec0de.mcutils.strings.MCStrings;

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
	private PluginFile config;

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
	 * Registers an existing {@link YmlFile} to this plugin, if the file doesn't exist it will be created,
	 * if the file type is {@link PluginFile} and it's {@link YmlFile#getName() name} is "config.yml"
	 * (Or just config as the extension will be added), {@link #getConfig()} will return it automatically as specified on said method.
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
		if (file instanceof PluginFile && file.getName().equals("config.yml"))
			this.config = (PluginFile) file;
		return file;
	}

	/**
	 * Registers a file to this plugin, if the file doesn't exist it will be created, if the file type is {@link PluginFile}
	 * and it's registered as "config.yml" (Or just config as the extension will be added),
	 * {@link #getConfig()} will return it automatically as specified on said method.
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
		if (!MCStrings.hasContent(path))
			return null;
		try {
			Constructor<T> constructor = type.getDeclaredConstructor(JavaPlugin.class, String.class);
			return registerFile(constructor.newInstance(this, path));
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logException(e, "&8[&4MCUtils&8] &cAn error occured while registering &e"+path+".yml &cfrom &6"+getName()+"&8:");
			return null;
		}
	}

	/**
	 * Gets the configuration file of this {@link MCPlugin} only if
	 * a file with the name <b>"config.yml"</b> that extends {@link PluginFile}
	 * has been registered. {@link MCPlugin MCPlugins} may override this
	 * method to return their config file if it uses another name.
	 * 
	 * @return The <b>config.yml</b> file being used by this {@link MCPlugin} as a {@link PluginFile}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	@Override
	public PluginFile getConfig() {
		return config;
	}

	/**
	 * Reloads all files registered to the plugin by
	 * calling {@link YmlFile#reload()} on them.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload(List)
	 * @see #update()
	 * @see #update(List)
	 */
	public void reload() {
		files.forEach(file -> file.reload());
	}

	/**
	 * Reloads all files registered to the plugin by
	 * calling {@link PluginFile#reload(List)} on them.
	 * Which makes the file updater ignore the paths present
	 * on <b>updateIgnored</b>, this is specially useful
	 * if you want administrators to create their own paths
	 * without them being removed, for example, on a {@link GUI} plugin.
	 * 
	 * @param updateIgnored a list with the paths to
	 * be ignored by the file updater, update is assumed
	 * to be true with this method, if you want to reload
	 * without updating use {@link #reload()}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload()
	 * @see #update()
	 * @see #update(List)
	 */
	public void reload(@Nullable List<String> updateIgnored) {
		files.forEach(file -> {
			if (file instanceof PluginFile)
				((PluginFile)file).reload(updateIgnored);
			else
				file.reload();
		});
	}

	/**
	 * Updates all files registered to the plugin by calling
	 * {@link PluginFile#update()} on them, 
	 * {@link YmlFile}s will be ignored by this method.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload()
	 * @see #reload(List)
	 * @see #update(List)
	 */
	public void update() {
		files.forEach(file -> {
			if (file instanceof PluginFile)
				((PluginFile)file).update();
		});
	}

	/**
	 * Updates all files registered to the plugin by
	 * calling {@link PluginFile#update(List)} on them.
	 * Which makes the file updater ignore the paths present
	 * on <b>updateIgnored</b>, this is specially useful
	 * if you want administrators to create their own paths
	 * without them being removed, for example, on a {@link GUI} plugin.
	 * <p>
	 * {@link YmlFile}s will be ignored by this method.
	 * 
	 * @param updateIgnored a list with the paths to
	 * be ignored by the file updater.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload()
	 * @see #reload(List)
	 * @see #update()
	 */
	public void update(@Nullable List<String> updateIgnored) {
		files.forEach(file -> {
			if (file instanceof PluginFile)
				((PluginFile)file).update(updateIgnored);
		});
	}

	/**
	 * Gets the user-friendly name of the server version, for example, <i>"1.19.3"</i>.
	 * 
	 * @return The current server version.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String getServerVersion() {
		String ver = Bukkit.getBukkitVersion();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ver.length(); i++) {
			char c = ver.charAt(i);
			if (c == '-')
				break;
			builder.append(c);
		}
		return builder.toString();
	}

	/**
	 * Checks if the server supports the specified <b>version</b>.
	 * Supported formats are <i>"X.X"</i> and <i>"X.X.X"</i>.
	 * <br><br>
	 * <b>Note about 1.7.10</b>:
	 * <br>
	 * Because of the way this method works, if the server is running on
	 * 1.7.10 and you check if the server supports 1.7.9, the result will
	 * be false, that is because 1.7.10 gets translated to 17.1f, which is
	 * lower to 17.9f, this is the only version with this issue and this is
	 * highly unlikely to be fixed as this version is currently unsupported.
	 * 
	 * @param version the server version to check, for example, <i>"1.19"</i>.
	 * 
	 * @return true if the server version is higher or equal to the specified
	 * <b>version</b>, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public boolean serverSupports(@Nonnull String version) {
		if (version == null || version.isBlank())
			return false;
		float[] versions = new float[2]; // Convert to float, so 1.19.3 would be 119.3
		String server = getServerVersion();
		for (int v = 0; v <= 1; v++) {
			String ver = v == 0 ? server : version;
			int points = 0;
			for (int i = 0; i < ver.length(); i++) {
				char c = ver.charAt(i);
				if (c == '.')
					points++;
				else if (c >= '0' && c <= '9')
					versions[v] = points >= 2 ? versions[v] + ((c - '0') / 10.0f) : (versions[v] * 10) + (c - '0');
			}
		}
		return versions[0] >= versions[1];
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
	 * Sends <b>strings</b> to the console.
	 * 
	 * @param stings the strings to send.
	 * 
	 * @return Always true, to make sending messages on commands easier.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean log(@Nullable String... stings) {
		Bukkit.getConsoleSender().sendMessage(stings);
		return true;
	}

	/**
	 * Sends <b>strings</b> to the console with applied colors using {@link MCStrings#applyColor(String)}
	 * 
	 * @param strings the strings to send.
	 * 
	 * @return Always true, to make sending messages on commands easier.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean logCol(@Nullable String... strings) {
		for (String str : strings)
			Bukkit.getConsoleSender().sendMessage(MCStrings.applyColor(str));
		return true;
	}

	public void logException(@Nullable Throwable throwable, @Nullable String header) {
		if (throwable == null)
			return;
		log(" ");
		if (header != null)
			logCol(header);
		logCol("&4"+throwable.getClass().getSimpleName()+"&8: &c" + throwable.getMessage());
		for(StackTraceElement element : throwable.getStackTrace()) {
			String error = element.toString().trim();
			if (error.contains(".jar")) {
				error = error.substring(error.lastIndexOf(".jar")+6);
				String path = error.substring(0, error.lastIndexOf('('));
				int lastPoint = path.lastIndexOf('.');
				path = "&cAt&8: &c"+path.substring(0, lastPoint) + "&6#&e" + path.substring(lastPoint + 1);
				final String line = error.substring(error.lastIndexOf(':')+1, error.length()-1);
				logCol(path+"&8 - &bline "+line);
			}
		}
		log(" ");
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
	 * @param <T> must implement {@link Listener}
	 * @param listener the listener to register.
	 * 
	 * @return The registered <b>listener</b>, null if <b>listener</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public <T extends Listener> T registerEvents(@Nullable T listener) {
		if (listener == null)
			return null;
		Bukkit.getPluginManager().registerEvents(listener, this);
		return listener;
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

	/**
	 * Creates and registers a new {@link PluginCommand} to be further modified at runtime.
	 * 
	 * @param label name or alias of the command
	 * @param executor the {@link CommandExecutor} for this command.
	 * 
	 * @throws IllegalArgumentException if <b>label</b> or <b>executor</b> are null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCCommand registerCommand(@Nonnull String label, @Nonnull CommandExecutor executor) {
		return new MCCommand(this, label, executor);
	}

	/**
	 * Registers the specified {@link GUI} to be handled, if
	 * <b>gui</b> is null or already registered, null will
	 * be returned.
	 * 
	 * @param gui the {@link GUI} to register.
	 * 
	 * @return The <b>gui</b> itself if it was successfully registered,
	 * null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUIs(GUI...)
	 * @see #unregisterGUIs()
	 * @see #unregisterGUIs(GUI...)
	 */
	public GUI registerGUI(GUI gui) {
		return getMCUtils().getGUIHandler(this).registerGUI(gui);
	}

	/**
	 * Register any number of {@link GUI GUIs} easily, at the cost
	 * of not returning anything as opposed to {@link #registerGUI(GUI)}.
	 * 
	 * @param guis the {@link GUI GUIs} to be registered, if null, nothing
	 * will be done.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUI(GUI)
	 * @see #unregisterGUIs()
	 * @see #unregisterGUIs(GUI...)
	 */
	public void registerGUIs(GUI... guis) {
		getMCUtils().getGUIHandler(this).registerGUIs(guis);
	}

	/**
	 * Unregisters all specified {@link GUI GUIs} from this {@link GUIHandler}.
	 * 
	 * @param guis the {@link GUI GUIs} to be unregistered, the instance doesn't
	 * need to be the same as the registered {@link GUI} as this is checked by class.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUI(GUI)
	 * @see #registerGUIs(GUI...)
	 * @see #unregisterGUIs()
	 */
	public void unregisterGUIs(GUI... guis) {
		getMCUtils().getGUIHandler(this).unregisterGUIs(guis);
	}

	/**
	 * Unregisters all the {@link GUI GUIs} registered on this {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUI(GUI)
	 * @see #registerGUIs(GUI...)
	 * @see #unregisterGUIs(GUI...)
	 */
	public void unregisterGUIs() {
		getMCUtils().getGUIHandler(this).unregisterGUIs();
	}
}
