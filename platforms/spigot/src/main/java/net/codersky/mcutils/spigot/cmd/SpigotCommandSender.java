package net.codersky.mcutils.spigot.cmd;

import net.codersky.mcutils.cmd.MCCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotCommandSender extends MCCommandSender<CommandSender> {

	public SpigotCommandSender(@NotNull CommandSender sender) {
		super(sender);
	}

	/*
	 * MCCommandSender implementation
	 */

	@Override
	public boolean sendMessage(@NotNull String message) {
		getOriginal().sendMessage(message);
		return true;
	}

	@Override
	public boolean hasPermission(@NotNull String permission) {
		return getOriginal().hasPermission(permission);
	}

	/*
	 * Additional spigot only methods
	 */

	@Nullable
	public Player asPlayer() {
		return getOriginal() instanceof Player ? (Player) getOriginal() : null;
	}

	@Nullable
	public ConsoleCommandSender asConsole() {
		return getOriginal() instanceof ConsoleCommandSender ? (ConsoleCommandSender) getOriginal() : null;
	}
}
