package net.codersky.mcutils.spigot.player;

import net.codersky.mcutils.crossplatform.player.MCPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotPlayer implements MCPlayer<Player> {

	private final Player handle;

	SpigotPlayer(@NotNull Player handle) {
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
			handle.sendMessage(message);
		return true;
	}
}
