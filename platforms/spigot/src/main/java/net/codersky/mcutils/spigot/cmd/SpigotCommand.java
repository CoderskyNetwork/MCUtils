package net.codersky.mcutils.spigot.cmd;

import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.cmd.SubCommandHandler;
import net.codersky.mcutils.spigot.SpigotUtils;
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

import java.util.Arrays;
import java.util.List;

public abstract class SpigotCommand<P extends JavaPlugin> extends Command implements MCCommand<P, SpigotCommandSender>, PluginIdentifiableCommand, TabExecutor {

	private final SpigotUtils<P> utils;
	private final SubCommandHandler<P, SpigotCommandSender> subCommandHandler = new SubCommandHandler<>();

	public SpigotCommand(@NotNull SpigotUtils<P> utils, @NotNull String name) {
		super(name);
		this.utils = utils;
	}

	public SpigotCommand(@NotNull SpigotUtils<P> utils, @NotNull String name, @NotNull String... aliases) {
		this(utils, name);
		super.setAliases(Arrays.asList(aliases));
	}

	@NotNull
	@Override
	public SpigotUtils<P> getUtils() {
		return utils;
	}

	@NotNull
	@Override
	public final P getPlugin() {
		return utils.getPlugin();
	}

	// Command logic //

	@SafeVarargs
	public final SpigotCommand<P> inject(SpigotCommand<P>... commands) {
		subCommandHandler.inject(commands);
		return this;
	}

	@Override
	public final boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return subCommandHandler.onCommand(this, new SpigotCommandSender(sender, getUtils()), args);
	}

	@Override
	public final boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		return subCommandHandler.onCommand(this, new SpigotCommandSender(sender, getUtils()), args);
	}

	@Override
	@Nullable
	public final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return subCommandHandler.onTab(this, new SpigotCommandSender(sender, getUtils()), args);
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
		return asGeneric(Bukkit::getPlayerExact, arg, args, def);
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
		return asGeneric(Bukkit::getPlayerExact, arg, args);
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
