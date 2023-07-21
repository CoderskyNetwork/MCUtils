package me.xdec0de.mcutils.general.commands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Enums;

import me.xdec0de.mcutils.MCPlugin;
import me.xdec0de.mcutils.strings.MCStrings;

public abstract class BaseMCCommand<P extends MCPlugin> extends Command {

	private final P plugin;

	protected BaseMCCommand(P plugin, @Nonnull String name) {
		super(name);
		this.plugin = plugin;
	}

	public final P getPlugin() {
		return plugin;
	}

	/** @deprecated In favor of {@link #onCommand(CommandSender, String[])}
	 * @hidden */
	@Deprecated
	@Override
	@Nullable
	public abstract boolean execute(CommandSender sender, String commandLabel, String[] args);

	@Nullable
	public abstract boolean onCommand(@Nonnull CommandSender sender, @Nonnull String[] args);

	/** @deprecated In favor of {@link #onTab(CommandSender, String[])}, do not call this method manually.
	 * @hidden */
	@Deprecated
	@Override
	@Nullable
	public abstract List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args);

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
