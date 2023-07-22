package me.xdec0de.mcutils.general.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Enums;

import me.xdec0de.mcutils.MCPlugin;
import me.xdec0de.mcutils.files.MessagesFile;
import me.xdec0de.mcutils.strings.MCStrings;

/**
 * A class shared by {@link MCCommand} and {@link MCSubCommand}
 * to provide utilities to both of them.
 * 
 * @param <P> An {@link MCPlugin} that owns this command.
 * 
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 * 
 * @see #onCommand(CommandSender, String[])
 * @see #onTab(CommandSender, String[])
 * @see MCCommand
 * @see MCSubCommand
 */
abstract class BaseMCCommand<P extends MCPlugin> extends Command {

	private final P plugin;
	private final HashMap<MCSubCommand<?>, Integer> subCommands = new HashMap<>();

	private Class<?> restrictedSenderClass = null;

	/**
	 * Creates a new instance of a {@link BaseMCCommand} with the
	 * specified <b>name</b>, owned by <b>plugin</b>.
	 * 
	 * @param plugin the plugin that owns this command.
	 * @param name the name of this command.
	 * 
	 * @since MCUtils 1.0.0
	 */
	BaseMCCommand(P plugin, @Nonnull String name) {
		super(name);
		this.plugin = plugin;
	}

	/**
	 * Creates a new instance of a {@link BaseMCCommand} with the
	 * specified <b>name</b> and <b>aliases</b>, owned by <b>plugin</b>.
	 * 
	 * @param plugin the plugin that owns this command.
	 * @param name the name of this command.
	 * @param aliases additional names for the command
	 * 
	 * @since MCUtils 1.0.0
	 */
	BaseMCCommand(P plugin, String name, String... aliases) {
		this(plugin, name);
		setAliases(Arrays.asList(aliases));
	}

	/**
	 * Gets the {@link MCPlugin} that owns this command.
	 * 
	 * @return The {@link MCPlugin} that owns this command.
	 */
	@Nonnull
	public final P getPlugin() {
		return plugin;
	}

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
	 * @return This {@link BaseMCCommand}
	 * 
	 * @since MCUtils 1.0.0
	 */
	public BaseMCCommand<P> inject(int position, @Nullable MCSubCommand<?>... commands) {
		if (commands == null || commands.length == 0)
			return this;
		for (MCSubCommand<?> subCmd : commands)
			if (subCmd != null)
				subCommands.put(subCmd, position);
		return this;
	}

	/**
	 * Sets the restricted {@link CommandSender} class, this means that any
	 * {@link CommandSender} from the specified <b>senderClass</b> won't be
	 * able to run or tab complete this command and MCUtils directly won't
	 * call {@link #onCommand(CommandSender, String[])} nor {@link #onTab(CommandSender, String[])}
	 * for that command if the sender class doesn't match, so you don't have to worry about it.
	 * You can restrict either the {@link Player} class or the {@link ConsoleCommandSender} class
	 * to make player or console only commands, using <code>null</code> (The default
	 * value) will remove any restrictions.
	 * <p>
	 * Note that the parent command will always take priority with this, meaning that if the
	 * sender has access to a sub command but not to the parent command, the sender won't
	 * have access anyways as the parent command won't allow it.
	 * <p>
	 * For command execution, both commands and sub commands work the same way, respecting
	 * priority, the first command to restrict the sender will send it a custom message specified
	 * by MCUtils at messages.yml, this message may vary if the sender is a player or the console.
	 * <p>
	 * Regarding restricted tab completion, commands and sub commands behave a bit differently, both will
	 * suggest null to the sender on their own tab completions, however, if the sender is tab completing
	 * a parent command that has one or more sub commands that don't match the sender class, <b>only</b>
	 * those sub commands will be removed from the parent suggestions.
	 * <p>
	 * As additional information, using the {@link CommandSender} class itself won't have any effect
	 * as that would make the command impossible to use.
	 * 
	 * @param <T> Must be {@link Player} or {@link ConsoleCommandSender}
	 * @param senderClass the class to restrict from using this command.
	 * 
	 * @return This {@link BaseMCCommand}
	 * 
	 * @see Player#getClass()
	 * @see ConsoleCommandSender#getClass()
	 */
	@Nonnull
	public <T extends CommandSender> BaseMCCommand<P> setRestrictedSenderClass(@Nullable Class<T> senderClass) {
		if (senderClass == null || !senderClass.equals(CommandSender.class))
			this.restrictedSenderClass = senderClass;
		return this;
	}

	/**
	 * Checks if <b>sender</b> is restricted to use this command,
	 * check {@link #setRestrictedSenderClass(Class)} for more information.
	 * 
	 * @param sender the sender to check.
	 * 
	 * @return true if <b>sender</b>'s class is restricted, false otherwise.
	 * 
	 * @throws NullPointerException if <b>sender</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public final boolean isRestricted(@Nonnull CommandSender sender) {
		return sender.getClass() == restrictedSenderClass;
	}

	/** @deprecated In favor of {@link #onCommand(CommandSender, String[])}
	 * @hidden */
	@Deprecated
	@Override
	public final boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (isRestricted(sender))
			return getPlugin().getMCUtils().getMessages().send(sender, sender instanceof Player ? "commands.noPlayer" : "commands.noConsole");
		for (MCSubCommand<?> subCmd : subCommands.keySet()) {
			int subCmdPos = subCommands.get(subCmd);
			for (int i = 0; i < args.length; i++) {
				String arg = args[i].toLowerCase();
				if (i == subCmdPos && (subCmd.getName().equals(arg) || subCmd.getAliases().contains(arg)))
					return subCmd.execute(sender, commandLabel, Arrays.copyOfRange(args, subCmdPos + 1, args.length));
			}
		}
		return onCommand(sender, args);
	}

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
	 * @see MessagesFile#send(CommandSender, String)
	 * @see #asString(int, String[])
	 * @see #asInt(int, String[])
	 * @see #asPlayer(int, String[])
	 * @see #asOfflinePlayer(int, String[])
	 * @see #asEnum(int, String[], Class)
	 */
	@Nullable
	public abstract boolean onCommand(@Nonnull CommandSender sender, @Nonnull String[] args);

	/** @deprecated In favor of {@link #onTab(CommandSender, String[])}.
	 * @hidden */
	@Deprecated
	@Override
	@Nullable
	public final List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) {
		if (isRestricted(sender))
			return null;
		List<String> tabs = new ArrayList<String>();
		for (MCSubCommand<?> subCmd : subCommands.keySet()) {
			int subCmdPos = subCommands.get(subCmd);
			if (subCmdPos == args.length - 1) { // One argument before subCmd, we suggest its name.
				if (!subCmd.isRestricted(sender))
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
		return tabs.isEmpty() ? null : tabs;
	}

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
	 * @see #asInt(int, String[])
	 * @see #asPlayer(int, String[])
	 * @see #asOfflinePlayer(int, String[])
	 * @see #asEnum(int, String[], Class)
	 */
	@Nullable
	public abstract List<String> onTab(@Nonnull CommandSender sender, @Nonnull String[] args);

	// ARGUMENT CONVERSION //

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
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asString(int arg, @Nonnull String[] args, @Nullable String def) {
		return args.length > arg ? args[arg] : def;
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
	public String asString(int arg, @Nonnull String[] args) {
		return asString(arg, args, null);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a lower case {@link String}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds.
	 * 
	 * @return The argument as a lower case {@link String} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asLowerString(int arg, @Nonnull String[] args, @Nullable String def) {
		String original = asString(arg, args, def);
		return original != null ? original.toLowerCase() : null;
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a lower case {@link String}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as a lower case {@link String} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asLowerString(int arg, @Nonnull String[] args) {
		return asLowerString(arg, args, null);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an upper case {@link String}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds.
	 * 
	 * @return The argument as an upper case {@link String} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asUpperString(int arg, @Nonnull String[] args, @Nullable String def) {
		String original = asString(arg, args, def);
		return original != null ? original.toUpperCase() : null;
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an upper case {@link String}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as an upper case {@link String} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asUpperString(int arg, @Nonnull String[] args) {
		return asUpperString(arg, args, null);
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
	 */
	@Nullable
	public String asStringRange(int fromArg, @Nonnull String[] args, @Nullable String def) {
		String str = asString(fromArg, args, null);
		if (str == null)
			return def;
		StringBuffer buff = new StringBuffer().append(str);
		for (int i = fromArg + 1; i < args.length; i++)
			buff.append(' ').append(args[i]);
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
	 */
	@Nullable
	public String asStringRange(int fromArg, @Nonnull String[] args) {
		return asStringRange(fromArg, args, null);
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command, finally converting the range to lower case.
	 * 
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>fromArg</b> is out of bounds.
	 * 
	 * @return A lower {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * <b>def</b> if <b>fromArg</b> is out of bounds.
	 */
	@Nullable
	public String asLowerStringRange(int fromArg, @Nonnull String[] args, @Nullable String def) {
		String range = asStringRange(fromArg, args, def);
		return range != null ? range.toLowerCase() : null;
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command, finally converting the range to lower case.
	 * 
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return A lower {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * null if <b>fromArg</b> is out of bounds.
	 */
	@Nullable
	public String asLowerStringRange(int fromArg, @Nonnull String[] args) {
		return asLowerStringRange(fromArg, args, null);
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command, finally converting the range to upper case.
	 * 
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>fromArg</b> is out of bounds.
	 * 
	 * @return An upper {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * <b>def</b> if <b>fromArg</b> is out of bounds.
	 */
	@Nullable
	public String asUpperStringRange(int fromArg, @Nonnull String[] args, @Nullable String def) {
		String range = asStringRange(fromArg, args, def);
		return range != null ? range.toUpperCase() : null;
	}

	/**
	 * Gets a range of arguments starting at <b>fromArg</b> all the way to
	 * the end of <b>args</b>, then, converts that range of arguments to a
	 * {@link String}, separating them by a space character, exactly as typed
	 * by the user that ran the command, finally converting the range to lower case.
	 * 
	 * @param fromArg the array position of the first argument to get a range from, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return A lower {@link String} of the range of arguments from <b>fromArg</b> to the end of <b>args</b>,
	 * null if <b>fromArg</b> is out of bounds.
	 */
	@Nullable
	public String asUpperStringRange(int fromArg, @Nonnull String[] args) {
		return asUpperStringRange(fromArg, args, null);
	}

	/*
	 * Numbers
	 */

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an {@link Integer}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds or the argument isn't a valid number.
	 * 
	 * @return The argument as an {@link Integer} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCStrings#asInteger(String, Integer)
	 */
	@Nullable
	public Integer asInt(int arg, @Nonnull String[] args, @Nullable Integer def) {
		return MCStrings.asInteger(args.length > arg ? args[arg] : null, def);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an {@link Integer}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as an {@link Integer} if found on the <b>args</b> array, -1 otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCStrings#asInteger(String)
	 */
	public int asInt(int arg, @Nonnull String[] args) {
		return MCStrings.asInteger(args.length > arg ? args[arg] : null, -1);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an unsigned {@link Integer}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds or the argument isn't a valid number.
	 * 
	 * @return The argument as an unsigned {@link Integer} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCStrings#asUnsignedInteger(String, int)
	 */
	public int asUnsignedInt(int arg, @Nonnull String[] args, int def) {
		return MCStrings.asUnsignedInteger(args.length > arg ? args[arg] : null, def);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an unsigned {@link Integer}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as an unsigned {@link Integer} if found on the <b>args</b> array, 0 otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCStrings#asUnsignedInteger(String)
	 */
	public double asUnsignedInt(int arg, @Nonnull String[] args) {
		return MCStrings.asUnsignedInteger(args.length > arg ? args[arg] : null, 0);
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
	public Player asPlayer(int arg, @Nonnull String[] args, @Nullable Player def) {
		Player online = Bukkit.getPlayerExact(args.length > arg ? args[arg] : null);
		return online == null ? def : online;
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
	public Player asPlayer(int arg, @Nonnull String[] args) {
		return asPlayer(arg, args, null);
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
	public OfflinePlayer asOfflinePlayer(int arg, @Nonnull String[] args, @Nullable OfflinePlayer def) {
		@SuppressWarnings("deprecation")
		OfflinePlayer offline = Bukkit.getOfflinePlayer(args.length > arg ? args[arg] : null);
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
	public OfflinePlayer asOfflinePlayer(int arg, @Nonnull String[] args) {
		return asOfflinePlayer(arg, args, null);
	}

	/*
	 * Enums
	 */

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an {@link Enum}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * @param def the default value to return if <b>arg</b> is out of bounds or the argument isn't a valid enum constant of the specified class.
	 * 
	 * @return The argument as an {@link Enum} if found on the <b>args</b> array, null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Enums#getIfPresent(Class, String)
	 */
	@Nullable
	public <T extends Enum<T>> T asEnum(int arg, @Nonnull String[] args, @Nullable Class<T> enumClass) {
		String name = asUpperString(arg, args);
		if (name == null || enumClass == null)
			return null;
		return Enums.getIfPresent(enumClass, name).orNull();
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an {@link Enum}.
	 * 
	 * @param arg the array position of the argument to get, can be out of bounds.
	 * @param args the array of arguments to use.
	 * 
	 * @return The argument as an {@link Enum} if found on the <b>args</b> array, <b>def</b> otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Enums#getIfPresent(Class, String)
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public <T extends Enum<T>> T asEnum(int arg, @Nonnull String[] args, @Nullable T def) {
		String name = asUpperString(arg, args);
		if (name == null || def == null)
			return def;
		return (T) Enums.getIfPresent(def.getClass(), name).or(def);
	}
}
