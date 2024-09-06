package net.codersky.mcutils.velocity.player;

import com.velocitypowered.api.proxy.Player;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VelocityPlayer implements MCPlayer<Player> {

	private final Player handle;

	public VelocityPlayer(@NotNull Player handle) {
		this.handle = handle;
	}

	@NotNull
	@Override
	public Player getHandle() {
		return handle;
	}

	@Override
	public boolean sendMessage(@Nullable String message) {
		if (message != null && !message.isBlank())
			handle.sendMessage(Component.text(message));
		return true;
	}
}
