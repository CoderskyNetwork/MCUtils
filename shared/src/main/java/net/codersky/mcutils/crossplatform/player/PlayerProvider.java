package net.codersky.mcutils.crossplatform.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

public abstract class PlayerProvider<T> {

	private final HashMap<UUID, MCPlayer> playerCache = new HashMap<>();

	@Nullable
	protected abstract MCPlayer fetchPlayer(@NotNull UUID uuid);

	@Nullable
	public abstract UUID getUUID(@NotNull T handle);

	@Nullable
	public MCPlayer getPlayer(@NotNull UUID uuid) {
		MCPlayer player = playerCache.get(uuid);
		if (player != null)
			return player;
		player = fetchPlayer(uuid);
		if (player != null)
			playerCache.put(uuid, player);
		return player;
	}

	@Nullable
	public MCPlayer getPlayer(@NotNull T original) {
		final UUID uuid = getUUID(original);
		return uuid == null ? null : getPlayer(uuid);
	}
}
