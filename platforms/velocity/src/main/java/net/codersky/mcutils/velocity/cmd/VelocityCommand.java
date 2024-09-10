package net.codersky.mcutils.velocity.cmd;

import com.velocitypowered.api.command.SimpleCommand;
import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.cmd.SubCommandHandler;
import net.codersky.mcutils.java.MCCollections;
import net.codersky.mcutils.velocity.VelocityUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class VelocityCommand<P> implements SimpleCommand, MCCommand<P, VelocityCommandSender> {

	private final VelocityUtils<P> utils;
	private final String name;
	private final String[] aliases;
	private final SubCommandHandler<P, VelocityCommandSender> subCmdHandler = new SubCommandHandler<>();

	public VelocityCommand(@NotNull VelocityUtils<P> utils, @NotNull String name, @NotNull String... aliases) {
		this.utils = utils;
		this.name = Objects.requireNonNull(name).toLowerCase();
		this.aliases = Objects.requireNonNull(aliases);
	}

	public VelocityCommand(@NotNull VelocityUtils<P> utils, @NotNull String name) {
		this(utils, name, new String[0]);
	}

	@NotNull
	public VelocityUtils<P> getUtils() {
		return utils;
	}

	@NotNull
	public P getPlugin() {
		return utils.getPlugin();
	}

	@NotNull
	@Override
	public final String getName() {
		return name;
	}

	@NotNull
	@Override
	public final List<String> getAliases() {
		return MCCollections.asArrayList(aliases);
	}

	@NotNull
	public final String[] getAliasesArray() {
		return aliases;
	}

	// Command logic //

	@SafeVarargs
	public final VelocityCommand<P> inject(VelocityCommand<P>... commands) {
		subCmdHandler.inject(commands);
		return this;
	}

	@Override
	@ApiStatus.Internal
	public final void execute(@NotNull final Invocation invocation) {
		final VelocityCommandSender sender = new VelocityCommandSender(invocation.source(), getUtils());
		subCmdHandler.onCommand(this, sender, invocation.arguments());
	}

	@Override
	@ApiStatus.Internal
	public final CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
		final VelocityCommandSender sender = new VelocityCommandSender(invocation.source(), getUtils());
		return CompletableFuture.supplyAsync(() -> subCmdHandler.onTab(this, sender, invocation.arguments()));
	}
}
