package net.codersky.mcutils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.function.Consumer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import net.codersky.mcutils.files.FileHolder;
import net.codersky.mcutils.files.FileUpdater;
import net.codersky.mcutils.files.MessagesFileHolder;
import net.codersky.mcutils.files.yaml.MessagesFile;
import net.codersky.mcutils.files.yaml.PluginFile;
import net.codersky.mcutils.files.yaml.YmlFile;
import net.codersky.mcutils.general.Feature;
import net.codersky.mcutils.general.MCCommand;
import net.codersky.mcutils.gui.GUI;
import net.codersky.mcutils.gui.GUIHandler;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.reflection.RefObject;
import net.codersky.mcutils.worldgen.SingleBiomeProvider;
import net.codersky.mcutils.worldgen.VoidGenerator;

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

	private final LinkedList<FileHolder> files = new LinkedList<>();
	private final HashMap<Feature, SimpleEntry<String, Boolean>> features = new HashMap<>();
	private GUIHandler guiHandler;

	/**
	 * Registers a new {@link FileHolder} to this plugin, if the file doesn't exist it will be created.
	 * <p>
	 * The <b>recommended</b> registration order of files is: config.yml as {@link PluginFile}, any
	 * {@link MessagesFile} and then any other files you may need, this is done so {@link #getConfig()}
	 * and {@link #getMessages()} perform the faster as they iterate over {@link #getFiles()}.
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
		return file;
	}

	/**
	 * <b>Note:</b> This method can only register yml files, if you want to register other file types,
	 * please use {@link #registerFile(FileHolder)}, which is the preferred (faster) method anyways.
	 * <p>
	 * Registers a file to this plugin, if the file doesn't exist it will be created.
	 * The <b>recommended</b> registration order of files is: config.yml as {@link PluginFile}, any
	 * {@link MessagesFile} and then any other files you may need, this is done so {@link #getConfig()}
	 * and {@link #getMessages()} perform the faster as they iterate over {@link #getFiles()}.
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
			Constructor<T> constructor = type.getConstructor(JavaPlugin.class, String.class);
			return registerFile(constructor.newInstance(this, path));
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logException(e, "&8[&4MCUtils&8] &cAn error occured while registering &e"+path+".yml &cfrom &6"+getName()+"&8:");
			return null;
		}
	}

	/**
	 * Gets the files currently being handled by this {@link MCPlugin}. The list returned
	 * is a new list, not the actual list used by the plugin, meaning that you can change
	 * it but the changes won't be applied to the original list. You can also use this
	 * list to create new file getters such as {@link #getConfig()} or {@link #getMessages()},
	 * those methods do work that way. Executing something over all or some files of the list
	 * as {@link #reload()} or {@link #update()} do is also a valid usage of this list.
	 * 
	 * @return The list files currently being handled by this {@link MCPlugin}, this list will
	 * never be null but may be empty, the order of the elements is the same order that the plugin
	 * used to register any files, meaning that if the plugin registers config.yml first (As recommended),
	 * config.yml will be on the first position of this list.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public LinkedList<FileHolder> getFiles() {
		return new LinkedList<>(files);
	}

	/**
	 * Gets the configuration file of this {@link MCPlugin} only if
	 * a file with the name <b>"config.yml"</b> that extends {@link PluginFile}
	 * has been registered. {@link MCPlugin MCPlugins} may override this
	 * method to return their config file if it uses another name.
	 * <p>
	 * <b>Performance note</b>: Even though this <b>very</b> insignificant, this
	 * method does iterate through all the registered {@link FileHolder FileHolders}
	 * of this {@link MCPlugin} and checks if they are an instance of
	 * {@link PluginFile} and are named "config.yml", so you may want to store the file instead of
	 * calling this method more than once on a listener or something like that.
	 * 
	 * @return The <b>config.yml</b> file being used by this {@link MCPlugin} as a {@link PluginFile}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	@Override
	public PluginFile getConfig() {
		for (FileHolder holder : files) {
			if (holder instanceof PluginFile) {
				final PluginFile pFile = (PluginFile)holder;
				if (pFile.getName().equals("config.yml"))
					return pFile;
			}
		}
		return null;
	}

	/**
	 * Gets the first {@link MessagesFileHolder} registered to this
	 * {@link MCPlugin}. Plugins may override this method to
	 * return their own {@link MessagesFileHolder} type.
	 * <p>
	 * <b>Performance note</b>: Even though this <b>very</b> insignificant, this
	 * method does iterate through all the registered {@link FileHolder FileHolders}
	 * of this {@link MCPlugin} and checks if they implement the {@link MessagesFileHolder}
	 * interface, so you may want to store the file instead of
	 * calling this method more than once on a listener or something like that. This would
	 * also allow you to return directly whatever implementation you are using such
	 * as {@link MessagesFile}.
	 * 
	 * @return The last {@link MessagesFileHolder} registered by this
	 * {@link MCPlugin}, may be null if no {@link MessagesFileHolder}
	 * has been registered yet.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public MessagesFileHolder getMessages() {
		for (FileHolder holder : files)
			if (MessagesFileHolder.class.isAssignableFrom(holder.getClass()))
				return (MessagesFileHolder) holder;
		return null;
	}

	/**
	 * Reloads all {@link #reloadFiles() files} and
	 * {@link #reloadFeatures() features} registered
	 * to this {@link MCPlugin}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reloadFiles()
	 * @see #reloadFeatures()
	 * @see #update()
	 * @see #update(List)
	 * 
	 * @return The amount of errors that occurred while
	 * reloading files and features. 0 means no errors
	 * occurred.
	 */
	public int reload() {
		return reloadFiles() + reloadFeatures();
	}

	/**
	 * Reloads all {@link FileHolder files} registered
	 * to this {@link MCPlugin}.
	 * 
	 * @return The amount of files that failed when
	 * {@link FileHolder#reload() reloading}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int reloadFiles() {
		int failures = 0;
		for (FileHolder file : files)
			if (!file.reload())
				failures++;
		return failures;
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
		if (listener != null)
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
	public MCPlugin registerEvents(@Nullable Listener... listeners) {
		if (listeners != null)
			for (Listener listener : listeners)
				registerEvents(listener);
		return this;
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

	/**
	 * Gets the {@link GUIHandler} used by this {@link MCPlugin}, note
	 * that you don't need to register the events of the handler, this
	 * method will do that for you the first time you call it.
	 * 
	 * @return The {@link GUIHandler} used by this {@link MCPlugin}, which
	 * will never be null as one will be created the first time this method
	 * is called.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUI(GUI)
	 * @see #registerGUIs(GUI...)
	 * @see #unregisterGUIs()
	 * @see #unregisterGUIs(GUI...)
	 */
	@Nonnull
	public GUIHandler getGUIHandler() {
		return guiHandler == null ? (guiHandler = registerEvents(new GUIHandler(this))) : guiHandler;
	}

	/*
	 * World creation
	 */

	/**
	 * Creates or loads a new {@link World} with the specified
	 * <b>creator</b>. If <b>creator</b> is {@code null},
	 * nothing will be done.
	 * 
	 * @param creator the {@link WorldCreator} to use
	 * for the new {@link World}.
	 * 
	 * @return The newly created or loaded {@link World}, {@code null}
	 * if <b>creator</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public World createWorld(@Nullable WorldCreator creator) {
		return creator != null ? Bukkit.createWorld(creator) : null;
	}

	/**
	 * Creates or loads a new {@link World} with the specified
	 * <b>name</b> and world <b>type</b>. If <b>type</b>
	 * is {@code null}, {@link WorldType#NORMAL} will be used.
	 * 
	 * @param name the name of the new {@link World}.
	 * @param type the {@link WorldType} of the new {@link World}.
	 * 
	 * @return The newly created or loaded {@link World}, {@code null}
	 * if <b>name</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public World createWorld(@Nullable String name, @Nullable WorldType type) {
		if (name == null)
			return null;
		return createWorld(WorldCreator.name(name).type(type == null ? WorldType.NORMAL : type));
	}

	/**
	 * Creates or loads a new {@link World} with the specified
	 * <b>name</b> and world <b>generator</b>.
	 * 
	 * @param name the name of the new {@link World}.
	 * @param generator the {@link WorldGenerator} of the new {@link World}.
	 * 
	 * @return The newly created or loaded {@link World}, {@code null}
	 * if <b>name</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see VoidGenerator
	 */
	@Nullable
	public World createWorld(@Nonnull String name, @Nullable ChunkGenerator generator) {
		if (name == null)
			return null;
		return createWorld(WorldCreator.name(name).generator(generator));
	}

	/**
	 * Creates or loads a new {@link World} with the specified
	 * <b>name</b>, world <b>generator</b> and <b>biomeProvider</b>.
	 * 
	 * @param name the name of the new {@link World}.
	 * @param generator the {@link WorldGenerator} of the new {@link World}. If
	 * {@code null}, the "natural" generator for the {@link World} will be used.
	 * @param biomeProvider the {@link BiomeProvider} of the new {@link World}.
	 * If {@code null}, the {@link BiomeProvider} of the <b>generator</b> will be used,
	 * if said <b>generator</b> doesn't specify a provider, the "natural" provider for
	 * the {@link World} will be used.
	 * 
	 * @return The newly created or loaded {@link World}, {@code null}
	 * if <b>name</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see VoidGenerator
	 * @see SingleBiomeProvider
	 */
	@Nonnull
	public World createWorld(@Nullable String name, @Nullable ChunkGenerator generator, @Nullable BiomeProvider biomeProvider) {
		if (name == null)
			return null;
		return createWorld(WorldCreator.name(name).generator(generator).biomeProvider(biomeProvider));
	}

	/*
	 * Features
	 */

	/**
	 * Gets all the {@link Feature features} that have been
	 * {@link #registerFeature(Feature, String) registered} to this {@link MCPlugin}.
	 * Note that this method won't filter disabled features and every
	 * {@link #registerFeature(Feature, String) registered} {@link Feature} will be returned by it.
	 * 
	 * @return All the {@link Feature features} that have been
	 * {@link #registerFeature(Feature, String) registered} to this {@link MCPlugin}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public List<Feature> getFeatures() {
		return new ArrayList<Feature>(features.keySet());
	}

	/**
	 * Gets a {@link Feature} registered to this {@link MCPlugin} via
	 * <b>configPath</b>. Note that there is no way to get {@link Feature features}
	 * that have been {@link #registerFeature(Feature, String) registered}
	 * with a {@code null} {@link #getConfig() config} path as those are
	 * assumed to not change their status unexpectedly anyway.
	 * Also note that if two {@link Feature features} are 
	 * {@link #registerFeature(Feature, String) registered} under the
	 * same {@link #getConfig() config} path for some reason, only one of them will be returned.
	 * 
	 * @param configPath the {@link #getConfig() config} path that was used to
	 * {@link #registerFeature(Feature, String) register}
	 * the {@link Feature} that this method will try to return.
	 * 
	 * @return A {@link Feature} matching the specified <b>configPath</b>,
	 * {@code null} if no match is found or if <b>configPath</b> is {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Feature getFeature(@Nonnull String configPath) {
		if (configPath == null)
			for (Entry<Feature, SimpleEntry<String, Boolean>> entry : features.entrySet()) {
				final String key = entry.getValue().getKey();
				if (key != null && key.equals(configPath))
					return entry.getKey();
			}
		return null;
	}

	/**
	 * Checks if a {@link Feature} is enabled on this {@link MCPlugin}.
	 * If <b>feature</b> is {@code null}, {@code false} will be returned.
	 * Note that a feature may be disabled even if it is registered, see
	 * {@link #registerFeature(Feature, String)} for more details.
	 * 
	 * @param feature the {@link Feature} to check.
	 * 
	 * @return {@code true} if the feature is enabled, {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean isEnabled(@Nullable Feature feature) {
		// entry.getValue().getValue() checks if the feature is enabled, it's ugly, I know :)
		if (feature != null)
			for (Entry<Feature, SimpleEntry<String, Boolean>> entry : features.entrySet())
				if (entry.getKey().equals(feature))
					return entry.getValue().getValue();
		return false;
	}

	/**
	 * Reloads every registered {@link Feature} on this {@link MCPlugin}. If a feature
	 * is {@link #isEnabled() enabled}, it will be {@link Feature#onDisable() disabled}
	 * and then re-enabled if it can still be enabled (If the config allows it), see
	 * {@link #registerFeature(Feature, String)} for more details about when a
	 * {@link Feature} cannot be enabled.
	 * <p>
	 * Note that if any error occurs while {@link #onDisable() disabiling} a {@link Feature},
	 * (Feature{@link #onDisable()} returns {@code false}), the feature won't be re-enabled.
	 * This is to avoid unexpected behavior on said {@link Feature}.
	 * 
	 * @return The amount of failures that occurred while {@link Feature#onDisable() disabling}
	 * and {@link Feature#onEnable() re-enabling} {@link Feature features}. 0 means every
	 * {@link Feature} reloaded correctly.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int reloadFeatures() {
		int failures = 0;
		for (Entry<Feature, SimpleEntry<String, Boolean>> entry : features.entrySet()) {
			final Feature feature = entry.getKey();
			final SimpleEntry<String, Boolean> info = entry.getValue();
			if (!setFeatureStatus(feature, info, false)) {
				failures++;
				continue;
			}
			if (!setFeatureStatus(feature, info, true))
				failures++;
		}
		return failures;
	}

	/**
	 * Disables all <b>enabled</b> {@link Feature features} on this {@link MCPlugin}.
	 * This will call {@link Feature#onDisable()} on all <b>enabled</b> and only
	 * <b>enabled</b> {@link Feature features}.
	 * 
	 * @return the amount of failures ({@link Feature#onDisable()} returning {@code false})
	 * that happened when disabling all <b>enabled</b> {@link Feature features}. 0 means
	 * every {@link Feature} disabled successfully.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int disableFeatures() {
		int failures = 0;
		for (Entry<Feature, SimpleEntry<String, Boolean>> entry : features.entrySet())
			if (!setFeatureStatus(entry.getKey(), entry.getValue(), false))
				failures++;
		return failures;
	}

	/**
	 * Registers a new {@link Feature} that will optionally require
	 * a certain boolean value to be true on the {@link #getConfig() config}
	 * of this {@link MCPlugin plugin} if <b>configPath</b> is not {@code null}.
	 * Note that {@link Feature features} are not disabled automatically, you will
	 * need to call {@link #disableFeatures()} on your {@link #onDisable()} method
	 * in order for them to disable when the {@link MCPlugin plugin} disables.
	 * <p>
	 * If both {@link #getConfig()} and <b>configPath</b> are {@code null}, the
	 * <b>feature</b> will be enabled.
	 * <p>
	 * If <b>configPath</b> isn't {@code null} but {@link #getConfig()} is
	 * {@code null}, the feature will not be enabled.
	 * <p>
	 * If <b>configPath</b> and {@link #getConfig()} aren't null, then the feature
	 * will only be enabled if {@link PluginFile#getBoolean(String)} returns
	 * {@code true} with the {@link String} being <b>configPath</b>.
	 * 
	 * @param feature the {@link Feature} to register.
	 * @param configPath the {@link #getConfig() config} path to optionally use
	 * to decide whether to enable the feature or not, can be {@code null}.
	 * 
	 * @return {@code true} if {@link Feature#onEnable()} returned {@code true},
	 * {@code false} otherwise or if <b>feature</b> is {@code null}.
	 * Note that {@link true} will also be returned if the feature doesn't get
	 * enabled because of {@link #getConfig() config} options, this is intentional
	 * as this value is used to check for errors, not the status of the <b>feature</b>,
	 * use {@link #isEnabled(Feature)} for that.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #isEnabled(Feature)
	 * @see #reloadFeatures()
	 * @see #disableFeatures()
	 */
	public boolean registerFeature(@Nonnull Feature feature, @Nullable String configPath) {
		return feature == null ? false : setFeatureStatus(feature, new SimpleEntry<>(configPath, true), true);
	}

	/**
	 * Changes the <b>status</b> of a {@link Feature} to either enabled or
	 * disabled. Note that for this to work the <b>feature</b> needs to be
	 * previously {@link #registerFeature(Feature, String) registered} to this {@link MCPlugin}.
	 * If the specified <b>feature</b> isn't registered, this {@code false} will be
	 * returned by this method to indicate an error.
	 * 
	 * @param feature The {@link Feature} to change the status of, see {@link #getFeature(String)}.
	 * @param status The new status of the <b>feature</b>, {@code true} to enable it
	 * and {@code false} to disable it. Note that if the feature is already on the chosen state,
	 * nothing will happen and {@code true} will be returned.
	 * 
	 * @return {@code true} if no errors occurred or if the specified <b>feature</b> was already
	 * on the selected <b>state</b>, {@code false} in case any error occurs. This is handled
	 * by the {@link Feature} itself on either {@link Feature#onEnable()} or {@link Feature#onDisable()}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean setFeatureStatus(@Nonnull Feature feature, boolean status) {
		if (feature == null)
			return false;
		SimpleEntry<String, Boolean> info = features.get(feature);
		if (info == null)
			return false;
		return setFeatureStatus(feature, info, status);
	}

	private boolean setFeatureStatus(Feature feature, SimpleEntry<String, Boolean> info, boolean status) {
		boolean result = true;
		if (status && !info.getValue()) {
			if (info.getKey() != null) {
				final PluginFile cfg = getConfig();
				if (cfg != null && cfg.getBoolean(info.getKey(), false))
					result = feature.onEnable();
			} else
				result = feature.onEnable();
		} else if (!status && info.getValue())
			result = feature.onDisable();
		info.setValue(status);
		features.put(feature, info);
		return result;
	}
}
