package net.codersky.mcutils.cmd;

import net.codersky.mcutils.MCUtils;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public interface MCCommandSender extends MessageReceiver {

	/*
	 * Player related
	 */

	boolean isPlayer();

	@Nullable
	MCPlayer asPlayer();

	@Nullable
	default Object asPlayerHandle() {
		final MCPlayer player = asPlayer();
		return player == null ? null : player.getHandle();
	}

	/*
	 * Console related
	 */

	default boolean isConsole() {
		return !isPlayer();
	}

	@Nullable
	MCConsole asConsole();

	@Nullable
	default Object asConsoleHandle() {
		final MCConsole console = asConsole();
		return console == null ? null : console.getHandle();
	}

	/*
	 * Utilities
	 */

	boolean hasPermission(@NotNull String permission);

	@NotNull
	MCUtils<?> getUtils();
}
