package net.codersky.mcutils.spigot.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.java.MCCollections;
import net.codersky.mcutils.java.math.MCNumbers;
import net.codersky.mcutils.spigot.SpigotUtils;
import net.codersky.mcutils.spigot.java.strings.MCStrings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.base.Enums;

import net.codersky.mcutils.files.MessagesFileHolder;
import net.codersky.mcutils.spigot.files.MessagesFile;
import net.codersky.mcutils.spigot.storage.StorageHandler;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * A class used to represent a command that can
 * be registered by any {@link JavaPlugin}.
 * 
 * @param <U> A {@link JavaPlugin} that owns this command.
 *
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #onCommand(CommandSender, String[])
 * @see #onTab(CommandSender, String[])
 * @see #inject(int, MCCommand...)
 * @see #setSenderClass(Class)
 */
public abstract class MCCommand<P extends JavaPlugin, U extends SpigotUtils<P>> extends Command implements PluginIdentifiableCommand, TabExecutor {

	private final U utils;
	private Class<?> senderClass = null;
	private boolean removeEvents = true;
	private final HashMap<MCCommand<?, ?>, Integer> subCommands = new HashMap<>();

	/**
	 * Creates a new instance of a {@link MCCommand} with the
	 * specified {@code name}, owned by the {@link JavaPlugin}
	 * 
	 * @param utils the plugin that owns this command.
	 * @param name the name of this command.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCCommand(U utils, @Nonnull String name) {
		super(name);
		this.utils = utils;
	}

	/**
	 * Creates a new instance of a {@link MCCommand} with the
	 * specified <b>name</b> and <b>aliases</b>, owned by <b>plugin</b>.
	 * 
	 * @param utils the plugin that owns this command.
	 * @param name the name of this command.
	 * @param aliases additional names for the command
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCCommand(U utils, String name, String... aliases) {
		this(utils, name);
		setAliases(Arrays.asList(aliases));
	}

	/**
	 * Gets the {@link SpigotUtils} instance that was used to
	 * create this command.
	 * 
	 * @return The {@link SpigotUtils} instance that was used to
	 * create this command.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public final U getUtils() {
		return utils;
	}

	/**
	 * Gets the {@link JavaPlugin} provided by {@link #getUtils()}.
	 *
	 * @return The {@link JavaPlugin} provided by {@link #getUtils()}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	@Override
	public P getPlugin() {
		return utils.getPlugin();
	}

	/*
	 * Methods to modify MCCommand behavior
	 */

	// Sender class //

	/**
	 * Checks if <b>sender</b> is allowed to use this command,
	 * check {@link #setSenderClass(Class)} for more information.
	 * 
	 * @param sender the sender to check.
	 * 
	 * @return true if <b>sender</b>'s class is allowed, false otherwise.
	 * 
	 * @throws NullPointerException if <b>sender</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public final boolean isAllowed(@Nonnull CommandSender sender) {
		return senderClass == null ? true : senderClass.isAssignableFrom(sender.getClass());
	}

	/**
	 * Sets the allowed {@link CommandSender} class, this means that only
	 * {@link CommandSender senders} from the specified <b>senderClass</b> will be
	 * able to run or tab complete this command. For other classes MCUtils directly won't
	 * call {@link #onCommand(CommandSender, String[])} nor {@link #onTab(CommandSender, String[])}
	 * for that command, so you don't have to worry about {@link ClassCastException}.
	 * You can allow either the {@link Player} class or the {@link ConsoleCommandSender} class
	 * to make player or console only commands, using <code>null</code> (The default
	 * value) will remove any restrictions.
	 * <p>
	 * Note that the parent command will always take priority with this, meaning that if the
	 * sender has access to a sub command but not to the parent command, the sender won't
	 * have access anyways as the parent command won't allow it.
	 * <p>
	 * For command execution, both commands and sub commands work the same way, respecting
	 * priority, the first command to allow a sender class will send it a custom message specified
	 * by MCUtils at messages.yml, this message may vary if the sender is a player or the console.
	 * <p>
	 * Regarding restricted tab completion, commands and sub commands behave a bit differently, both will
	 * suggest {@code null} to the sender on their own tab completions, however, if the sender is tab completing
	 * a parent command that has one or more sub commands that don't match the sender class, <b>only</b>
	 * those sub commands will be removed from the parent suggestions.
	 * <p>
	 * As additional information, using the {@link CommandSender} class itself won't have any effect
	 * as that would make the command impossible to use.
	 * 
	 * @param <T> Must be {@link Player} or {@link ConsoleCommandSender}
	 * @param senderClass the class to restrict from using this command.
	 * 
	 * @return This {@link MCCommand}
	 */
	@Nonnull
	public <T extends CommandSender> MCCommand<P, U> setSenderClass(@Nullable Class<T> senderClass) {
		if (senderClass == null || !senderClass.equals(CommandSender.class))
			this.senderClass = senderClass;
		return this;
	}

	// Event pattern removal //

	/**
	 * Returns whether this {@link MCCommand} removes
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from string getters or not. This is enabled by default and it is recommended.
	 * <p>
	 * Keep in mind that this doesn't modify the {@code args} {@link String} array from the
	 * {@link #onCommand(CommandSender, String[])} and {@link #onTab(CommandSender, String[])}
	 * methods but instead affects string getter methods such as {@link #asString(int, String[])},
	 * methods that convert arguments to other objects such as {@link #asNumber(int, String[], Class)}
	 * remain unaffected because they don't have this issue.
	 * 
	 * @return Returns whether this {@link MCCommand} removes
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from string getters or not.
	 * 
	 * @sinc MCUtils 1.0.0
	 * 
	 * @see #setEventPatternRemoval(boolean)
	 */
	public boolean removesEventPatterns() {
		return this.removeEvents;
	}

	/**
	 * Sets whether this {@link MCCommand} removes
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from string getters or not. This is enabled by default and it is recommended.
	 * <p>
	 * Keep in mind that this doesn't modify the {@code args} {@link String} array from the
	 * {@link #onCommand(CommandSender, String[])} and {@link #onTab(CommandSender, String[])}
	 * methods but instead affects string getter methods such as {@link #asString(int, String[])},
	 * methods that convert arguments to other objects such as {@link #asNumber(int, String[], Class)}
	 * remain unaffected because they don't have this issue.
	 * 
	 * @param removeEvents whether to enable this feature or not.
	 * 
	 * @return This {@link MCCommand}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #removesEventPatterns()
	 */
	@Nonnull
	public MCCommand<P, U> setEventPatternRemoval(boolean removeEvents) {
		this.removeEvents = removeEvents;
		return this;
	}

	// Sub-commands //

	/**
	 * Inject the specified <b>commands</b> to a certain argument <b>position</b>
	 * of this command.
	 * 
	 * @param position the position that will be used to inject all sub commands.
	 * Assuming we have a "/test" command and a sub command called "one", if the position
	 * is 0, usage will be "/test one"
	 * @param commands the commands to inject, if null or empty nothing will be done.
	 * Commands can be owned by a plugin other than the command owner, this is to allow
	 * add-ons that add sub commands to existing plugin commands.
	 * 
	 * @return This {@link MCCommand}
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getInjectedCmds(int)
	 */
	public MCCommand<P, U> inject(int position, @Nullable MCCommand<?>... commands) {
		if (commands == null || commands.length == 0)
			return this;
		for (MCCommand<?, ?> subCmd : commands)
			if (subCmd != null)
				subCommands.put(subCmd, position);
		return this;
	}

	/**
	 * Gets a list of {@link MCCommand commands} that have been 
	 * {@link #inject(int, MCCommand...) injected} to this {@link MCCommand}
	 * at a specific <b>position</b>. This list of course may be empty
	 * if no {@link MCCommand commands} have been {@link #inject(int, MCCommand...) injected}
	 * at said <b>position</b> or if <b>position</b> is < 0.
	 * 
	 * @param position the position to get {@link #inject(int, MCCommand...) injected} sub commands from.
	 * 
	 * @return A never {@code null} but possibly empty {@link List} of {@link MCCommand commands}
	 * {@link #inject(int, MCCommand...) injected} to the specified <b>position</b>.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #inject(int, MCCommand...)
	 */
	@Nonnull
	public List<MCCommand<?, ?>> getInjectedCmds(int position) {
		final List<MCCommand<?, ?>> commands = new ArrayList<>(subCommands.size());
		if (position < 0)
			return new ArrayList<>(0);
		for (Entry<MCCommand<?, ?>, Integer> entry : subCommands.entrySet())
			if (entry.getValue() == position)
				commands.add(entry.getKey());
		return commands;
	}

	/*
	 * Command execution
	 */

	// Custom access //

	/**
	 * A custom access check for other classes that may extend
	 * {@link MCCommand}, allowing them to filter senders with
	 * custom rules without having to code
	 * {@link #onCommand(CommandSender, String[])} and
	 * {@link #onTab(CommandSender, String[])} themselves.
	 * By default, with MCUtils, this method always returns
	 * true. Note that this access check is only done after
	 * {@link #isAllowed(CommandSender)} returns false,
	 * so there is no need to implement that here either.
	 * <p>
	 * <b>Handling by MCUtils:</b> If this method returns false, MCUtils
	 * will return null when tab completing and will do nothing
	 * as if the command was never sent on command. You can identify
	 * whether the <b>sender</b> is tab completing or sending the
	 * command with the <b>message</b> parameter to send any needed
	 * message if the sender isn't allowed to run the command.
	 * 
	 * @param sender the {@link CommandSender} to check.
	 * @param message whether a message should be sent to
	 * the <b>sender</b>. This will be false {@link #onTab(CommandSender, String[])}
	 * and will be true {@link #onCommand(CommandSender, String[])},
	 * as MCUtils won't send any message to the <b>sender</b>.
	 * 
	 * @return Always true by default.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean hasAccess(@Nonnull CommandSender sender, boolean message) {
		return true;
	}

	// onCommand methods //

	/**
	 * Called automatically when a {@link CommandSender} executes this command,
	 * note that {@link MessagesFile}'s "send methods" will always return true
	 * so you can send a custom message to the sender if the command was used
	 * incorrectly and keep your code clean at the same time.
	 * 
	 * @param sender the {@link CommandSender} that sent this command, can be
	 * the server {@link ConsoleCommandSender console} itself or a {@link Player}.
	 * @param args the arguments used on this command <b>not</b> including the command
	 * name, may be empty if no arguments were specified.
	 * 
	 * @return Should always be true if you want to send custom error messages handled
	 * by a {@link MessagesFile}, returning false will fallback to the usage message
	 * for this command specified at plugin.yml or just the command name if no message
	 * is specified there.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #onTab(CommandSender, String[])
	 * @see #asString(int, String[])
	 * @see #asPlayer(int, String[])
	 * @see #asOfflinePlayer(int, String[])
	 * @see #asEnum(int, String[], Class)
	 */
	@Nullable
	public abstract boolean onCommand(@Nonnull CommandSender sender, @Nonnull String[] args);

	/** @deprecated In favor of {@link #onCommand(CommandSender, String[])}
	 * @hidden */
	@Deprecated
	@Override
	@ApiStatus.Internal
	public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return execute(sender, label, args);
	}

	/** @deprecated In favor of {@link #onCommand(CommandSender, String[])}
	 * @hidden */
	@Deprecated
	@Override
	@ApiStatus.Internal
	public final boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!isAllowed(sender)) {
			sendInvalidSenderMsg(sender);
			return true;
		} else if (!hasAccess(sender, true))
			return true;
		for (MCCommand<?, ?> subCmd : subCommands.keySet()) {
			int subCmdPos = subCommands.get(subCmd);
			for (int i = 0; i < args.length; i++) {
				String arg = args[i].toLowerCase();
				if (i == subCmdPos && (subCmd.getName().equals(arg) || subCmd.getAliases().contains(arg)))
					return subCmd.execute(sender, commandLabel, Arrays.copyOfRange(args, subCmdPos + 1, args.length));
			}
		}
		return onCommand(sender, args);
	}

	protected void sendInvalidSenderMsg(CommandSender sender) {
		final MessagesFileHolder file = utils.getMessages();
		if (file != null) {
			file.send(sender, sender instanceof Player ? "commands.noPlayer" : "commands.noConsole");
			return;
		}
		final StringBuilder msg = new StringBuilder("&8&l[&4&l!&8&l] &cThis command cannot be executed by ");
		msg.append(sender instanceof Player ? "players&8." : "the console&8.");
		sender.sendMessage(MCStrings.applyColor(msg.toString()));
	}

	// Tab complete handling //

	/**
	 * Called automatically when a {@link CommandSender} tab completes this command.
	 * 
	 * @param sender the {@link CommandSender} that sent this command, can be
	 * the server {@link ConsoleCommandSender console} itself or a {@link Player}.
	 * @param args the arguments used on this command <b>not</b> including the command
	 * name, may be empty if no arguments were specified.
	 * 
	 * @return The list of suggestions to display to the <b>sender</b>, can be null but
	 * some servers, depending on configuration, will suggest online player names if null
	 * is returned.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #onCommand(CommandSender, String[])
	 * @see #asString(int, String[])
	 * @see #asNumber(int, String[], Number)
	 * @see #asPlayer(int, String[])
	 * @see #asOfflinePlayer(int, String[])
	 * @see #asEnum(int, String[], Class)
	 */
	@Nullable
	public abstract List<String> onTab(@Nonnull CommandSender sender, @Nonnull String[] args);

	/** @deprecated In favor of {@link #onTab(CommandSender, String[])}
	 * @hidden */
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return tabComplete(sender, label, args);
	}

	/** @deprecated In favor of {@link #onTab(CommandSender, String[])}.
	 * @hidden */
	@Deprecated
	@Override
	@Nullable
	@ApiStatus.Internal
	public final List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) {
		if (!isAllowed(sender) || !hasAccess(sender, false))
			return null;
		List<String> tabs = new ArrayList<String>();
		for (MCCommand<?, ?> subCmd : subCommands.keySet()) {
			int subCmdPos = subCommands.get(subCmd);
			if (subCmdPos == args.length - 1) { // One argument before subCmd, we suggest its name.
				if (subCmd.isAllowed(sender) && subCmd.hasAccess(sender, false))
					tabs.add(subCmd.getName());
				continue;
			}
			for (int i = 0; i < args.length; i++) { // We check all arguments just in case we are in a nested sub command
				String arg = args[i].toLowerCase();
				if (i == subCmdPos && (subCmd.getName().equals(arg) || subCmd.getAliases().contains(arg)))
					return subCmd.tabComplete(sender, alias, Arrays.copyOfRange(args, subCmdPos + 1, args.length));
			}
		}
		List<String> selfTabs = onTab(sender, args);
		if (selfTabs != null)
			tabs.addAll(selfTabs);
		return tabs.isEmpty() ? null : filterTabs(tabs, args[args.length - 1]);
	}

	private List<String> filterTabs(List<String> tabs, String arg) {
		if (arg == null || arg.isEmpty())
			return tabs;
		String argLow = arg.toLowerCase();
		return MCCollections.clone(tabs, tab -> tab.toLowerCase().startsWith(argLow));
	}

	/*
	 * Utility methods
	 */

	/**
	 * Utility method to map all {@link Bukkit#getOnlinePlayers() online players}.
	 * This is generally used {@link #onTab(CommandSender, String[])} with
	 * {@link Player#getName()} as the <b>mapper</b> to get all online player names.
	 * 
	 * @param <R> The type of elements of the new {@link List}.
	 * 
	 * @param mapper The mapper function that will be applied to {@link Bukkit#getOnlinePlayers()}
	 * 
	 * @return A {@link List} consisting of the results of applying the given
	 * <b>mapper</b> function to the elements of {@link Bukkit#getOnlinePlayers()}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Stream#map(Function)
	 */
	@Nonnull
	public <R> List<R> mapOnline(@Nonnull Function<? super Player, R> mapper) {
		return Bukkit.getOnlinePlayers().stream().map(mapper).toList();
	}

	/**
	 * <b>Asynchronous tasks should never access any API in Bukkit. Great care
	 * should be taken to assure the thread-safety of asynchronous tasks.</b>
	 * <p>
	 * Shortcut to {@link BukkitScheduler#runTaskAsynchronously(Plugin, Runnable)}.
	 * Additionally, this method won't create a new task if the thread
	 * that it has been called from already isn't the
	 * {@link Bukkit#isPrimaryThread() primary thread} (Already asynchronous).
	 * <p>
	 * Keep in mind that creating new threads can be expensive. Use this only
	 * for commands that really need asynchronous execution such as commands
	 * that may need to open a connection, do not use this if you are only fetching
	 * data from a cached storage such as any {@link StorageHandler}. Also, messages
	 * can be safely sent to players and the console asynchronously.
	 * 
	 * @param task the task to run.
	 * 
	 * @throws IllegalArgumentException if <b>task</b> is {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void runAsync(@Nonnull Runnable task) {
		if (Bukkit.isPrimaryThread())
			Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), task);
		else
			task.run();
	}

	/**
	 * Shortcut to {@link BukkitScheduler#runTask(Plugin, Runnable)}.
	 * Additionally, this method won't create a new task if the thread
	 * that it has been called from already is the
	 * {@link Bukkit#isPrimaryThread() primary thread} (Already synchronous).
	 * 
	 * @param task the task to run.
	 * 
	 * @throws IllegalArgumentException if <b>task</b> is {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void runSync(@Nonnull Runnable task) {
		if (Bukkit.isPrimaryThread())
			task.run();
		else
			Bukkit.getScheduler().runTask(getPlugin(), task);
	}


	// ARGUMENT CONVERSION //

	/*
	 * Generic
	 */

	/**
	 * Converts the specified {@code arg} of the {@code args} array to a any object by using the
	 * {@code converter} {@link Function}. Returning {@code def} if no argument is found at the
	 * {@code arg} position or if {@code converter} returns {@code null}.
	 * 
	 * @param <T> the type of the result of the {@code converter} {@link Function}.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if {@code arg} is out of bounds or
	 * {@code converter} returns {@code null}.
	 * @param converter the {@link Function} that will convert the {@link String}
	 * found at the specified {@code arg} position. The {@link String} passed
	 * to the {@link Function} will <b>never</b> be {@code null}.
	 * 
	 * @return The argument as converted by {@code converter} if found on the {@code args} array
	 * and {@code converter} doesn't return {@code null}. {@code def} otherwise.
	 * 
	 * @throws NullPointerException if {@code args} or {@code converter} are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public <T> T asGeneric(@Nonnegative int arg, @Nonnull String[] args, @Nullable T def, @Nonnull Function<String, T> converter) {
		if (args.length <= arg)
			return def;
		final T converted = converter.apply(args[arg]);
		return converted == null ? def : converted;
	}

	/**
	 * Converts the specified {@code arg} of the {@code args} array to a any object by using the
	 * {@code converter} {@link Function}. Returning {@code null} if no argument is found at the
	 * {@code arg} position or if {@code converter} returns {@code null}.
	 * 
	 * @param <T> the type of the result of the {@code converter} {@link Function}.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param converter the {@link Function} that will convert the {@link String}
	 * found at the specified {@code arg} position. The {@link String} passed
	 * to the {@link Function} will <b>never</b> be {@code null}.
	 * 
	 * @return The argument as converted by {@code converter} if found
	 * on the {@code args} array, {@code null} otherwise.
	 * 
	 * @throws NullPointerException if {@code args} or {@code converter} are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public <T> T asGeneric(@Nonnegative int arg, @Nonnull String[] args, @Nonnull Function<String, T> converter) {
		return args.length > arg ? converter.apply(args[arg]) : null;
	}

	/*
	 * Strings 
	 */

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link String}, this
	 * method won't do any actual conversion and will just return the argument if found, <b>def</b> if not.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds.
	 * 
	 * @return The argument as a {@link String} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @throws NullPointerException if <b>args</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asString(@Nonnegative int arg, @Nonnull String[] args, @Nullable String def) {
		final String result = args.length > arg ? args[arg] : def;
		return result != null && removeEvents ? MCStrings.removeEventPatterns(result, true) : def;
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link String}, this
	 * method will apply <b>modifier</b> to the argument only if one is found and <b>def</b> isn't {@code null}.
	 * If no argument is found on the <b>arg</b> position and <b>def</b> is {@code null},
	 * {@code null} will be returned and the <b>modifier</b> won't be applied.
	 *
	 * @param modifier A {@link Function} to modify the resulting {@link String} that will be applied
	 * only if the {@link String} is not {@code null}.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds.
	 * 
	 * @return The argument as a {@link String} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @throws NullPointerException if <b>modifier</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asString(@Nonnull Function<String, String> modifier, @Nonnegative int arg, @Nonnull String[] args, @Nullable String def) {
		final String result = asString(arg, args, def);
		return result == null ? null : modifier.apply(result);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link String}, this
	 * method won't do any actual conversion and will just return the argument if found, null if not.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as a {@link String} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asString(@Nonnegative int arg, @Nonnull String[] args) {
		return asString(arg, args, null);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link String}, this
	 * method will apply <b>modifier</b> to the argument only if one is found, if no argument is found on
	 * the <b>arg</b> position, {@code null} will be returned and the <b>modifier</b> won't be applied.
	 *
	 * @param modifier A {@link Function} to modify the resulting {@link String} that will be applied
	 * only if the {@link String} is not {@code null}.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as a {@link String} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @throws NullPointerException if <b>modifier</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asString(@Nonnull Function<String, String> modifier, @Nonnegative int arg, @Nonnull String[] args) {
		final String result = asString(arg, args);
		return result == null ? null : modifier.apply(result);
	}

	/*
	 * String ranges
	 */

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command.
	 * 
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>fromArg</b> is out of bounds.
	 * 
	 * @return A {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * <b>def</b> if <b>fromArg</b> is out of bounds.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asStringRange(@Nonnegative int fromArg, @Nonnull String[] args, @Nullable String def) {
		String str = asString(fromArg, args, null);
		if (str == null)
			return def;
		final StringBuffer buff = new StringBuffer().append(str);
		for (int i = fromArg + 1; i < args.length; i++)
			buff.append(' ').append(removeEvents ? MCStrings.removeEventPatterns(args[i], true) : args[i]);
		return buff.toString();
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command.
	 * 
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return A {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * null if <b>fromArg</b> is out of bounds.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asStringRange(@Nonnegative int fromArg, @Nonnull String[] args) {
		return asStringRange(fromArg, args, null);
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command. If the range isn't out of bounds, then
	 * <b>modifier</b> will be applied to the resulting {@link String}.
	 * 
	 * @param modifier the {@link Function} to apply to the resulting {@link String}. This will not be applied
	 * on <b>def</b> upon returning it.
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>fromArg</b> is out of bounds.
	 * 
	 * @return A {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * <b>def</b> if <b>fromArg</b> is out of bounds.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asStringRange(@Nonnull Function<String, String> modifier, @Nonnegative int fromArg, @Nonnull String[] args, @Nullable String def) {
		final String range = asStringRange(fromArg, args, def);
		return range == null ? def : modifier.apply(range);
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command. If the range isn't out of bounds, then
	 * <b>modifier</b> will be applied to the resulting {@link String}.
	 * 
	 * @param modifier the {@link Function} to apply to the resulting {@link String}.
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return A {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * {@code null} if <b>fromArg</b> is out of bounds.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asStringRange(@Nonnull Function<String, String> modifier, @Nonnegative int fromArg, @Nonnull String[] args) {
		return asStringRange(modifier, fromArg, args);
	}

	/*
	 * List ranges
	 */

	@Nullable
	public <T> List<T> asListRange(@Nonnull Function<String, T> modifier, @Nonnegative int fromArg, @Nonnull String[] args, @Nullable List<T> def) {
		if (fromArg > args.length)
			return def;
		final List<T> lst = new ArrayList<>(args.length - fromArg);
		for (int i = fromArg + 1; i < args.length; i++)
			lst.add(modifier.apply(args[i]));
		return lst;
	}

	@Nullable
	public <T> List<T> asListRange(@Nullable Function<String, T> modifier, @Nonnegative int fromArg, @Nonnull String[] args) {
		return asListRange(modifier, fromArg, args, null);
	}

	@Nullable
	public List<String> asStringListRange(@Nonnegative int fromArg, @Nonnull String[] args, @Nullable List<String> def) {
		if (fromArg > args.length)
			return def;
		final List<String> lst = new ArrayList<>(args.length - fromArg);
		for (int i = fromArg + 1; i < args.length; i++)
			lst.add(removeEvents ? MCStrings.removeEventPatterns(args[i], true) : args[i]);
		return lst;
	}

	@Nullable
	public List<String> asStringListRange(@Nonnegative int fromArg, @Nonnull String[] args) {
		return asStringListRange(fromArg, args, null);
	}

	/*
	 * Numbers
	 */

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link Number}
	 * (See {@link MCNumbers#asNumber(CharSequence, Number)} for more details).
	 * 
	 * @param <T> the type of {@link Number} to return.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds or the argument isn't a valid number.
	 * 
	 * @return The argument as a {@link Number} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCNumbers#asNumber(CharSequence, Number)
	 */
	@Nullable
	public <T extends Number> T asNumber(@Nonnegative int arg, @Nonnull String[] args, @Nullable T def) {
		return asGeneric(arg, args, def, str -> MCNumbers.asNumber(str, def));
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link Number}
	 * (See {@link MCNumbers#asNumber(CharSequence, Class)} for more details).
	 * 
	 * @param <T> the type of {@link Number} to return.
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as an {@link Number} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCNumbers#asNumber(CharSequence, Class)
	 */
	public <T extends Number> T asNumber(@Nonnegative int arg, @Nonnull String[] args, Class<T> type) {
		return asGeneric(arg, args, str -> MCNumbers.asNumber(str, type));
	}

	/*
	 * Players
	 */

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link Player}.
	 * If the {@link Player} exists but is offline, <b>def</b> will be returned anyways.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds or the player isn't online.
	 * 
	 * @return The argument as an online {@link Player} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Bukkit#getPlayerExact(String)
	 */
	@Nullable
	public Player asPlayer(@Nonnegative int arg, @Nonnull String[] args, @Nullable Player def) {
		return asGeneric(arg, args, def, str -> Bukkit.getPlayerExact(str));
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link Player}.
	 * If the {@link Player} exists but is offline, null will be returned anyways.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as an online {@link Player} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Bukkit#getPlayerExact(String)
	 */
	@Nullable
	public Player asPlayer(@Nonnegative int arg, @Nonnull String[] args) {
		return asGeneric(arg, args, str -> Bukkit.getPlayerExact(str));
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an {@link OfflinePlayer}.
	 * If the {@link OfflinePlayer} has never played before, <b>def</b> will be returned anyways.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds or the player has never played before.
	 * 
	 * @return The argument as an {@link OfflinePlayer} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Bukkit#getOfflinePlayer(String)
	 */
	@Nullable
	public OfflinePlayer asOfflinePlayer(@Nonnegative int arg, @Nonnull String[] args, @Nullable OfflinePlayer def) {
		@SuppressWarnings("deprecation")
		final OfflinePlayer offline = Bukkit.getOfflinePlayer(args.length > arg ? args[arg] : null);
		return offline == null || !offline.hasPlayedBefore() ? def : offline;
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an {@link OfflinePlayer}.
	 * If the {@link OfflinePlayer} has never played before, null will be returned anyways.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as an {@link OfflinePlayer} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Bukkit#getOfflinePlayer(String)
	 */
	@Nullable
	public OfflinePlayer asOfflinePlayer(@Nonnegative int arg, @Nonnull String[] args) {
		return asOfflinePlayer(arg, args, null);
	}

	/*
	 * Enums
	 */

	/**
	 * Converts the specified {@code arg} of the {@code args} array to an {@link Enum}.
	 * <p>
	 * The argument is converted {@link String#toUpperCase() to upper case} as enum
	 * constants must be upper case, so you don't have to check if the argument is
	 * upper case or not.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param enumClass the class of the {@link Enum} to get the constant from.
	 * 
	 * @return The argument as an {@link Enum} if found on the {@code args} array, null otherwise.
	 * 
	 * @throws NullPointerException if {@code args} or {@code enumClass} are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Enums#getIfPresent(Class, String)
	 */
	@Nullable
	public <T extends Enum<T>> T asEnum(@Nonnegative int arg, @Nonnull String[] args, @Nonnull Class<T> enumClass) {
		return asGeneric(arg, args, str -> Enums.getIfPresent(enumClass, str.toUpperCase()).orNull());
	}

	/**
	 * Converts the specified {@code arg} of the {@code args} array to an {@link Enum}.
	 * <p>
	 * The argument is converted {@link String#toUpperCase() to upper case} as enum
	 * constants must be upper case, so you don't have to check if the argument is
	 * upper case or not.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if {@code arg} is out of bounds or the argument 
	 * isn't a valid enum constant of the same class.
	 * 
	 * @return The argument as an {@link Enum} if found on the {@code args} array, {@code def} otherwise.
	 * 
	 * @throws NullPointerException if {@code args} or {@code def} are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Enums#getIfPresent(Class, String)
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public <T extends Enum<T>> T asEnum(@Nonnegative int arg, @Nonnull String[] args, @Nonnull T def) {
		final Enum<?> e = asEnum(arg, args, def.getClass());
		return e == null ? def : (T) e;
	}
}
