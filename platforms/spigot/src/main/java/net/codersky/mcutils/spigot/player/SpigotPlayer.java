package net.codersky.mcutils.spigot.player;

import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpigotPlayer implements MCPlayer<Player> {

	private final Player handle;

	SpigotPlayer(@NotNull Player handle) {
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
		return handle.getName();
	}

	/*
	 * MessageReceiver implementation
	 */

	@Override
	public boolean sendMessage(@Nullable String message) {
		if (message != null)
			handle.sendMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@Nullable Component message) {
		if (message != null)
			handle.spigot().sendMessage(BungeeComponentSerializer.get().serialize(message));
		return false;
	}
}
