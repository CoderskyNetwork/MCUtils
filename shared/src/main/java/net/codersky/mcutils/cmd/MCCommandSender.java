package net.codersky.mcutils.cmd;

import net.codersky.mcutils.MCUtils;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public interface MCCommandSender<P, C> extends MessageReceiver {

	/*
	 * Player related
	 */

	boolean isPlayer();

	@Nullable
	MCPlayer<P> asPlayer();

	@Nullable
	default P asPlayerHandle() {
		final MCPlayer<P> player = asPlayer();
		return player == null ? null : player.getHandle();
	}

	/*
	 * Console related
	 */

	default boolean isConsole() {
		return !isPlayer();
	}

	@Nullable
	MCConsole<C> asConsole();

	@Nullable
	default C asConsoleHandle() {
		final MCConsole<C> console = asConsole();
		return console == null ? null : console.getHandle();
	}

	/*
	 * Utilities
	 */

	boolean hasPermission(@NotNull String permission);

	@NotNull
	MCUtils getUtils();
}
