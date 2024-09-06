package net.codersky.mcutils.spigot.player;

import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.crossplatform.player.PlayerProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpigotPlayerProvider extends PlayerProvider<Player> {

	@Override
	protected @Nullable MCPlayer<Player> fetchPlayer(@NotNull UUID uuid) {
		final Player bukkit = Bukkit.getPlayer(uuid);
		return bukkit == null ? null : new SpigotPlayer(bukkit);
	}

	@Override
	public @Nullable UUID getUUID(@NotNull Player handle) {
		return handle.getUniqueId();
	}
}
