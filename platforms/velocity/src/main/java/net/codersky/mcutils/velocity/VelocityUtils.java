package net.codersky.mcutils.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.mcutils.MCUtils;
import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.velocity.cmd.VelocityCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VelocityUtils<P> extends MCUtils {

	private final P plugin;
	private final ProxyServer proxy;

	public VelocityUtils(@NotNull P plugin, @NotNull ProxyServer proxy) {
		this.plugin = Objects.requireNonNull(plugin);
		this.proxy = Objects.requireNonNull(proxy);
	}

	@NotNull
	public final P getPlugin() {
		return plugin;
	}

	@NotNull
	public final ProxyServer getProxy() {
		return this.proxy;
	}

	@Override
	public void registerCommands(MCCommand<?>... commands) {
		final CommandManager manager = getProxy().getCommandManager();
		for (MCCommand<?> mcCommand : commands) {
			final VelocityCommand velocityCommand = (VelocityCommand) mcCommand;
			final CommandMeta meta = manager.metaBuilder(velocityCommand.getName()).plugin(plugin).build();
			manager.register(meta, velocityCommand);
		}
	}
}
