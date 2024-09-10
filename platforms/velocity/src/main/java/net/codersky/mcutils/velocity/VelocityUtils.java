package net.codersky.mcutils.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.mcutils.MCUtils;
import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.crossplatform.player.PlayerProvider;
import net.codersky.mcutils.velocity.cmd.VelocityCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class VelocityUtils<P> extends MCUtils<P> {

	private final ProxyServer proxy;
	private final VelocityConsole console;
	private PlayerProvider<Player> playerProvider;

	public VelocityUtils(@NotNull P plugin, @NotNull ProxyServer proxy) {
		super(plugin);
		this.proxy = Objects.requireNonNull(proxy);
		this.console = new VelocityConsole(proxy.getConsoleCommandSource());
	}

	@NotNull
	public final ProxyServer getProxy() {
		return this.proxy;
	}

	@NotNull
	public VelocityUtils<P> setPlayerProvider(@NotNull PlayerProvider<Player> playerProvider) {
		this.playerProvider = Objects.requireNonNull(playerProvider, "Player provider cannot be null");
		return this;
	}

	@NotNull
	public PlayerProvider<Player> getPlayerProvider() {
		return playerProvider;
	}

	@Nullable
	public MCPlayer<Player> getPlayer(@NotNull UUID uuid) {
		return playerProvider.getPlayer(uuid);
	}

	@Override
	public @NotNull VelocityConsole getConsole() {
		return console;
	}

	@Override
	public void registerCommands(MCCommand<?, P>... commands) {
		final CommandManager manager = getProxy().getCommandManager();
		for (MCCommand<?, P> mcCommand : commands) {
			final VelocityCommand<P> velocityCommand = (VelocityCommand<P>) mcCommand;
			final CommandMeta meta = manager.metaBuilder(velocityCommand.getName())
					.plugin(getPlugin())
					.aliases(velocityCommand.getAliasesArray())
					.build();
			manager.register(meta, velocityCommand);
		}
	}
}
