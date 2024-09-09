package net.codersky.mcutils.velocity.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.codersky.mcutils.cmd.MCCommandSender;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.velocity.VelocityConsole;
import net.codersky.mcutils.velocity.VelocityUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VelocityCommandSender implements MCCommandSender<Player, ConsoleCommandSource> {

	private final CommandSource source;
	private final VelocityUtils<?> utils;

	public VelocityCommandSender(@NotNull CommandSource source, @NotNull VelocityUtils<?> utils) {
		this.source = source;
		this.utils = utils;
	}

	/*
	 * Player related
	 */

	@Override
	public boolean isPlayer() {
		return source instanceof Player;
	}

	@Override
	public @Nullable MCPlayer<Player> asPlayer() {
		return source instanceof Player player ? utils.getPlayer(player.getUniqueId()) : null;
	}

	/*
	 * Console related
	 */

	@Nullable
	@Override
	public VelocityConsole asConsole() {
		return isConsole() ? utils.getConsole() : null;
	}

	/*
	 * Messages
	 */

	@Override
	public boolean sendMessage(@NotNull String message) {
		source.sendPlainMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		source.sendMessage(message);
		return true;
	}

	/*
	 * Utilities
	 */

	@Override
	public boolean hasPermission(@NotNull String permission) {
		return source.hasPermission(permission);
	}

	@NotNull
	@Override
	public VelocityUtils<?> getUtils() {
		return utils;
	}
}
