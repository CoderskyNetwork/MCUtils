package net.codersky.mcutils.spigot.player;

import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
	public boolean sendMessage(@NotNull String message) {
		handle.sendMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		handle.spigot().sendMessage(toBase(message));
		return false;
	}

	/*
	 * BaseComponent array conversion
	 */

	private BaseComponent[] toBase(@NotNull Component component) {
		return BungeeComponentSerializer.get().serialize(component);
	}
}
