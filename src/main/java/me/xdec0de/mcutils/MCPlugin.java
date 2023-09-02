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
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.xdec0de.mcutils.files.FileHolder;
import me.xdec0de.mcutils.files.FileUpdater;
import me.xdec0de.mcutils.files.yaml.MessagesFile;
import me.xdec0de.mcutils.files.yaml.PluginFile;
import me.xdec0de.mcutils.files.yaml.YmlFile;
import me.xdec0de.mcutils.general.MCCommand;
import me.xdec0de.mcutils.gui.GUI;
import me.xdec0de.mcutils.gui.GUIHandler;
import me.xdec0de.mcutils.java.strings.MCStrings;
import me.xdec0de.mcutils.reflection.RefObject;

/**
 * Represents a {@link JavaPlugin} using the MCUtils API.
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

	private final List<FileHolder> files = new ArrayList<>();
	private PluginFile config;
	private MessagesFile messages;
	private GUIHandler guiHandler;

	/**
	 * Registers a new {@link FileHolder} to this plugin, if the file doesn't exist it will be created,
	 * if the file type is {@link PluginFile} and its {@link PluginFile#getName() name} is "config.yml",
	 * {@link #getConfig()} will return it automatically as specified on said method. This also happens
	 * with {@link #getMessages()} whenever you register a {@link MessagesFile}.
	 * 
	 * @param <T> must implement {@link FileHolder}
	 * @param file the file to be registered, {@link T#create()} will be called to ensure that the file exists.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @return The registered file, the <b>file</b> parameter.
	 */
	@Nullable
	public <T extends FileHolder> T registerFile(@Nonnull T file) {
		if (file == null)
			return null;
		file.create();
		files.add(file);
		if (file instanceof PluginFile) {
			final PluginFile pFile = (PluginFile)file;
			if (pFile.getPath().equals("config.yml"))
				this.config = pFile;
		} else if (file instanceof MessagesFile)
			this.messages = (MessagesFile) file;
		return file;
	}

	/**
	 * <b>Note:</b> This method can only register .yml files, if you want to register other file types,
	 * please use {@link #registerFile(FileHolder)}, which is the preferred (faster) method anyways.
	 * 
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
	 * @see #registerFile(FileHolder)
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
	 * Gets the last {@link MessagesFile} registered by this
	 * {@link MCPlugin}. Plugins may override this method to
	 * return their own {@link MessagesFile} type.
	 * 
	 * @return The last {@link MessagesFile} registered by this
	 * {@link MCPlugin}, may be null if no {@link MessagesFile}
	 * has been registered yet.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public MessagesFile getMessages() {
		return messages;
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
	 * Updates all files registered to the plugin by calling
	 * {@link PluginFile#update(List)} with null on them, 
	 * {@link YmlFile}s will be ignored by this method.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload()
	 * @see #reload(List)
	 * @see #update(List)
	 */
	public MCPlugin update() {
		files.forEach(file -> {
			if (file instanceof FileUpdater)
				((FileUpdater)file).update(null);
		});
		return this;
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
			if (file instanceof FileUpdater)
				((FileUpdater)file).update(updateIgnored);
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
	public static String getServerVersion() {
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
	public static boolean serverSupports(@Nonnull String version) {
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
	 * Gets the latest version of a resource (Premium resources are supported too) using SpigotMC's API.
	 * The version can take some time to update after a new resource update.
	 * 
	 * @param resourceId the ID of the resource to retrieve, the ID can be found at your resource url:
	 * <i>{@literal https://www.spigotmc.org/resources/<name>.<ID IS HERE>/}</i>
	 * 
	 * @return The latest available version name at SpigotMC with the specified <b>resourceId</b>,
	 * may be null if no resource with that id exists or any I/O exception occurs.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Bukkit#getScheduler()
	 * @see BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Consumer)
	 */
	public String getLatestVersion(@Nonnegative int resourceId) {
		try {
			InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
			Scanner scanner = new Scanner(inputStream);
			String ver = null;
			if (scanner.hasNext())
				ver = scanner.next();
			scanner.close();
			return ver;
		} catch (IOException ex) {
			return null;
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
		for (Listener listener : listeners)
			registerEvents(listener);
	}

	/**
	 * Registers the specified <b>command</b>, allowing it to be executed.
	 * <p>
	 * <b>Important note</b>: MCUtils registers commands in a pretty unusual
	 * way, if the names of the commands you are trying to register are present
	 * on your <b>plugin.yml</b>, MCUtils will just register them the "traditional"
	 * way, if not, it will use some reflection to register it through CraftBukkit's
	 * getCommandMap (Respecting encapsulation!), meaning that you can register commands without adding them
	 * to your <b>plugin.yml</b>. <i>However</i>, this may break on future versions
	 * of the game (Read below) so still, if it fails for some reason, it will attempt to register
	 * commands via <b>plugin.yml</b>, if both fail, well... Nothing we can do about it.
	 * <p>
	 * About our reflection way of registering commands breaking... Pretty unlikely for Spigot at least,
	 * we are using a <b>public</b> method present on <b>CraftServer</b>, not the private commandMap field that
	 * many plugins use. Paper has deprecated SimpleCommandMap for removal as of 1.20 though, so we may need
	 * to make a check there if they were to change it, but we are aware of it, so no worries, just
	 * make sure to update MCUtils if that ever happens (This will be notified as an important update).
	 * 
	 * @param <P> must extend {@link MCPlugin}
	 * @param command the command to register.
	 * 
	 * @return The specified <b>command</b>.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public <P extends MCPlugin> MCCommand<P> registerCommand(@Nullable MCCommand<P> command) {
		if (command == null)
			return null;
		final PluginCommand plCommand = getCommand(command.getName());
		if (plCommand == null) { // Not in plugin.yml, attempt to register via SimpleCommandMap...
			final RefObject mapObj = new RefObject(getServer()).invoke("getCommandMap");
			final SimpleCommandMap commandMap = mapObj == null ? null : ((SimpleCommandMap)mapObj.getInstance());
			if (commandMap == null) { // Method removed for some reason, notify about it.
				logCol("&8[&6" + getName() + "&8] &cCould not register &e" + command.getName() + " &ccommands, please inform about this&8.",
						" &8- &7MCUtils is at fault here (If outdated), do not contact &e" + getName() + "&7's author(s)&8.",
						" &8- &7Contact&8: &espigotmc.org/members/xdec0de_.178174/ &7or Discord &9@xdec0de_",
						" &8- &7Server info&8: &b" + getServer().getName() + " " + getServerVersion());
				Bukkit.getPluginManager().disablePlugin(this);
			} else
				commandMap.register(getName(), command);
		} else
			plCommand.setExecutor(command);
		return command;
	}

	/**
	 * Registers the specified <b>command</b>, allowing them to be executed.
	 * <p>
	 * <b>Important note</b>: MCUtils registers commands in a pretty unusual
	 * way, if the names of the commands you are trying to register are present
	 * on your <b>plugin.yml</b>, MCUtils will just register them the "traditional"
	 * way, if not, it will use some reflection to register it through CraftBukkit's
	 * getCommandMap (Respecting encapsulation!), meaning that you can register commands without adding them
	 * to your <b>plugin.yml</b>. <i>However</i>, this may break on future versions
	 * of the game (Read below) so still, if it fails for some reason, it will attempt to register
	 * commands via <b>plugin.yml</b>, if both fail, well... Nothing we can do about it.
	 * <p>
	 * About our reflection way of registering commands breaking... Pretty unlikely for Spigot at least,
	 * we are using a <b>public</b> method present on <b>CraftServer</b>, not the private commandMap field that
	 * many plugins use. Paper has deprecated SimpleCommandMap for removal as of 1.20 though, so we may need
	 * to make a check there if they were to change it, but we are aware of it, so no worries, just
	 * make sure to update MCUtils if that ever happens (This will be notified as an important update).
	 * 
	 * @param commands the commands to register.
	 * 
	 * @return This {@link MCPlugin}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCPlugin registerCommands(@Nullable MCCommand<?>... commands) {
		if (commands == null || commands.length == 0)
			return this;
		final List<Command> remaining = new ArrayList<>();
		for (MCCommand<?> command : commands) {
			final PluginCommand plCommand = getCommand(command.getName());
			if (plCommand != null)
				plCommand.setExecutor(command);
			else
				remaining.add(command);
		}
		if (remaining.size() == 0)
			return this;
		final RefObject mapObj = new RefObject(getServer()).invoke("getCommandMap");
		final SimpleCommandMap commandMap = mapObj == null ? null : ((SimpleCommandMap)mapObj.getInstance());
		if (commandMap == null) { // Method removed for some reason, notify about it.
			logCol("&8[&6" + getName() + "&8] &cCould not register &e" + remaining.size() + " &ccommands, please inform about this&8.",
					" &8- &7MCUtils is at fault here (If outdated), do not contact &e" + getName() + "&7's author(s)&8.",
					" &8- &7Contact&8: &espigotmc.org/members/xdec0de_.178174/ &7or Discord &9@xdec0de_",
					" &8- &7Server info&8: &b" + getServer().getName() + " " + getServerVersion());
			Bukkit.getPluginManager().disablePlugin(this);
		} else
			commandMap.registerAll(getName(), remaining);
		return this;
	}

	public GUIHandler getGUIHandler() {
		return guiHandler == null ? (guiHandler = registerEvents(new GUIHandler(this))) : guiHandler;
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
	public <G extends GUI> G registerGUI(G gui) {
		return getGUIHandler().registerGUI(gui);
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
	public GUIHandler registerGUIs(GUI... guis) {
		return getGUIHandler().registerGUIs(guis);
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
		getGUIHandler().unregisterGUIs(guis);
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
		getGUIHandler().unregisterGUIs();
	}
}
