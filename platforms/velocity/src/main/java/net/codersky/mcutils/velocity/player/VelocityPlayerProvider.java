package net.codersky.mcutils.velocity.player;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.crossplatform.player.PlayerProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class VelocityPlayerProvider extends PlayerProvider<Player> {

	private final ProxyServer server;

	public VelocityPlayerProvider(@NotNull ProxyServer server) {
		this.server = server;
	}

	@Nullable
	@Override
	protected MCPlayer<Player> fetchPlayer(@NotNull UUID uuid) {
		final Optional<Player> player = server.getPlayer(uuid);
		return player.map(VelocityPlayer::new).orElse(null);
	}

	@Nullable
	@Override
	public UUID getUUID(@NotNull Player handle) {
		return handle.getUniqueId();
	}
}
