package net.codersky.mcutils.velocity.cmd;

import com.velocitypowered.api.command.SimpleCommand;
import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.cmd.SubCommandHandler;
import net.codersky.mcutils.velocity.VelocityUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class VelocityCommand<U extends VelocityUtils> implements SimpleCommand, MCCommand<VelocityCommandSender> {

	private final String name;
	private final SubCommandHandler<VelocityCommandSender> subCmdHandler = new SubCommandHandler();

	public VelocityCommand(@NotNull String name) {
		this.name = Objects.requireNonNull(name).toLowerCase();
	}

	@Override
	public @NotNull String getName() {
		return name;
	}

	// Command logic //

	public VelocityCommand<U> inject(VelocityCommand... commands) {
		subCmdHandler.inject(commands);
		return this;
	}

	@Override
	@ApiStatus.Internal
	public final void execute(@NotNull final Invocation invocation) {
		subCmdHandler.onCommand(this, new VelocityCommandSender(invocation.source()), invocation.arguments());
	}

	@Override
	@ApiStatus.Internal
	public final CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
		final VelocityCommandSender sender = new VelocityCommandSender(invocation.source());
		return CompletableFuture.supplyAsync(() -> subCmdHandler.onTab(this, sender, invocation.arguments()));
	}
}
