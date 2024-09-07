package net.codersky.mcutils.velocity.player;

import com.velocitypowered.api.proxy.Player;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class VelocityPlayer implements MCPlayer<Player> {

	private final Player handle;

	public VelocityPlayer(@NotNull Player handle) {
		this.handle = handle;
	}

	/*
	 * MCPlayer implementation
	 */

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

	/*
	 * MessageReceiver implementation
	 */

	@Override
	public boolean sendMessage(@Nullable String message) {
		if (message != null)
			handle.sendMessage(Component.text(message));
		return true;
	}

	@Override
	public boolean sendMessage(@Nullable Component message) {
		if (message != null)
			handle.sendMessage(message);
		return false;
	}
}
