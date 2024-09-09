package net.codersky.mcutils.spigot.cmd;

import net.codersky.mcutils.cmd.MCCommandSender;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.spigot.SpigotConsole;
import net.codersky.mcutils.spigot.SpigotUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotCommandSender implements MCCommandSender<Player, ConsoleCommandSender> {

	private final CommandSender sender;
	private final SpigotUtils<?> utils;

	public SpigotCommandSender(@NotNull CommandSender sender, @NotNull SpigotUtils<?> utils) {
		this.sender = sender;
		this.utils = utils;
	}

	/*
	 * Player related
	 */

	@Override
	public boolean isPlayer() {
		return sender instanceof Player;
	}

	@Nullable
	@Override
	public MCPlayer<Player> asPlayer() {
		return sender instanceof Player player ? utils.getPlayer(player.getUniqueId()) : null;
	}

	/*
	 * Console related
	 */

	@Nullable
	@Override
	public SpigotConsole asConsole() {
		return isConsole() ? utils.getConsole() : null;
	}

	/*
	 * Messages
	 */

	@Override
	public boolean sendMessage(@NotNull String message) {
		sender.sendMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		final MCPlayer<Player> player = asPlayer();
		if (player != null)
			return player.sendMessage(message);
		final MCConsole<ConsoleCommandSender> console = asConsole();
		if (console != null)
			return console.sendMessage(message);
		return true;
	}

	/*
	 * Utilities
	 */

	@Override
	public boolean hasPermission(@NotNull String permission) {
		return sender.hasPermission(permission);
	}

	@Override
	public @NotNull SpigotUtils<?> getUtils() {
		return utils;
	}
}
