package net.codersky.mcutils.spigot.cmd;

import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.cmd.MCCommandSender;
import net.codersky.mcutils.cmd.SubCommandHandler;
import net.codersky.mcutils.spigot.java.strings.MCStrings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SpigotCommand<P extends JavaPlugin> extends Command implements MCCommand<SpigotCommandSender>, PluginIdentifiableCommand, TabExecutor {

	private final P plugin;
	private boolean removeEvents = true;
	private final SubCommandHandler<SpigotCommandSender> subCommandHandler = new SubCommandHandler();

	public SpigotCommand(@NotNull P plugin, @NotNull String name) {
		super(name);
		this.plugin = plugin;
	}

	@NotNull
	@Override
	public final P getPlugin() {
		return this.plugin;
	}

	// Command logic //

	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return subCommandHandler.onCommand(this, new SpigotCommandSender(sender), args);
	}

	@Override
	public final boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		return subCommandHandler.onCommand(this, new SpigotCommandSender(sender), args);
	}

	@Override
	@Nullable
	public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return subCommandHandler.onTab(this, new SpigotCommandSender(sender), args);
	}

	// Event pattern removal //

	/**
	 * Returns whether this {@link net.codersky.mcutils.spigot.general.MCCommand} removes
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from string getters or not. This is enabled by default and it is recommended.
	 * <p>
	 * Keep in mind that this doesn't modify the {@code args} {@link String} array from the
	 * {@link #onCommand(MCCommandSender, String[])} and {@link #onTab(MCCommandSender, String[])}
	 * methods but instead affects string getter methods such as {@link #asString(int, String[])},
	 * methods that convert arguments to other objects such as {@link #asNumber(int, String[], Class)}
	 * remain unaffected because they don't have this issue.
	 *
	 * @return Returns whether this {@link net.codersky.mcutils.spigot.general.MCCommand} removes
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from string getters or not.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #setEventPatternRemoval(boolean)
	 */
	public final boolean removesEventPatterns() {
		return this.removeEvents;
	}

	/**
	 * Sets whether this {@link net.codersky.mcutils.spigot.general.MCCommand} removes
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from string getters or not. This is enabled by default and it is recommended.
	 * <p>
	 * Keep in mind that this doesn't modify the {@code args} {@link String} array from the
	 * {@link #onCommand(MCCommandSender, String[])} and {@link #onTab(MCCommandSender, String[])}
	 * methods but instead affects string getter methods such as {@link #asString(int, String[])},
	 * methods that convert arguments to other objects such as {@link #asNumber(int, String[], Class)}
	 * remain unaffected because they don't have this issue.
	 *
	 * @param removeEvents whether to enable this feature or not.
	 *
	 * @return This {@link net.codersky.mcutils.spigot.general.MCCommand}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #removesEventPatterns()
	 */
	@NotNull
	public final SpigotCommand<P> setEventPatternRemoval(boolean removeEvents) {
		this.removeEvents = removeEvents;
		return this;
	}

	@NotNull
	@Override
	public final String cleanArgument(@NotNull String arg) {
		return MCStrings.removeEventPatterns(arg, true);
	}

	// ARGUMENT CONVERSION //

	/*
	 * Players
	 */

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link Player}.
	 * If the {@link Player} exists but is offline, <b>def</b> will be returned anyway.
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
	public Player asPlayer(int arg, @NotNull String[] args, @Nullable Player def) {
		return asGeneric(arg, args, def, Bukkit::getPlayerExact);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to a {@link Player}.
	 * If the {@link Player} exists but is offline, null will be returned anyway.
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
	public Player asPlayer(int arg, @NotNull String[] args) {
		return asGeneric(arg, args, Bukkit::getPlayerExact);
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an {@link OfflinePlayer}.
	 * If the {@link OfflinePlayer} has never played before, <b>def</b> will be returned anyway.
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
	public OfflinePlayer asOfflinePlayer(int arg, @NotNull String[] args, @Nullable OfflinePlayer def) {
		@SuppressWarnings("deprecation")
		final OfflinePlayer offline = Bukkit.getOfflinePlayer(args.length > arg ? args[arg] : null);
		return offline == null || !offline.hasPlayedBefore() ? def : offline;
	}

	/**
	 * Converts the specified <b>arg</b> of the <b>args</b> array to an {@link OfflinePlayer}.
	 * If the {@link OfflinePlayer} has never played before, null will be returned anyway.
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
	public OfflinePlayer asOfflinePlayer(int arg, @NotNull String[] args) {
		return asOfflinePlayer(arg, args, null);
	}
}
