package net.codersky.mcutils.cmd;

import net.codersky.mcutils.MCUtils;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GlobalCommand<P> implements MCCommand<P, MCCommandSender> {

	private final MCUtils<P> utils;
	private final String name;
	private final List<String> aliases;
	private final SubCommandHandler<P, MCCommandSender> subCmdHandler = new SubCommandHandler<>();

	public GlobalCommand(MCUtils<P> utils, @NotNull String name, List<String> aliases) {
		this.utils = utils;
		this.name = name;
		this.aliases = aliases;
	}

	public GlobalCommand(MCUtils<P> utils, @NotNull String name) {
		this(utils, name, List.of());
	}

	public GlobalCommand(MCUtils<P> utils, @NotNull String name, String... aliases) {
		this(utils, name, List.of(aliases));
	}

	@NotNull
	@Override
	public String getName() {
		return name;
	}

	@Override
	public @NotNull List<String> getAliases() {
		return aliases;
	}

	@NotNull
	@Override
	public MCUtils<P> getUtils() {
		return utils;
	}

	@Override
	public @NotNull MCCommand<P, MCCommandSender> inject(@NotNull MCCommand<P, MCCommandSender>... commands) {
		subCmdHandler.inject(commands);
		return this;
	}
}
