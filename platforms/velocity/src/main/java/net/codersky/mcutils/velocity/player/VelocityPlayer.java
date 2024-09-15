package net.codersky.mcutils.velocity.player;

import com.velocitypowered.api.proxy.Player;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class VelocityPlayer implements MCPlayer {

	private final Player handle;

	public VelocityPlayer(@NotNull Player handle) {
		this.handle = handle;
	}

	/*
	 * MCPlayer implementation
	 */

	// Player identification //

	@NotNull
	@Override
	public Player getHandle() {
		return handle;
	}

	@NotNull
	@Override
	public UUID getUniqueId() {
		return handle.getUniqueId();
	}

	@NotNull
	@Override
	public String getName() {
		return handle.getUsername();
	}

	// Messages //

	@Override
	public boolean sendActionBar(@NotNull String message) {
		return sendActionBar(Component.text(message));
	}

	@Override
	public boolean sendActionBar(@NotNull Component message) {
		handle.sendActionBar(message);
		return true;
	}

	/*
	 * MessageReceiver implementation
	 */

	@Override
	public boolean sendMessage(@NotNull String message) {
		return sendMessage(Component.text(message));
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		handle.sendMessage(message);
		return true;
	}
}
