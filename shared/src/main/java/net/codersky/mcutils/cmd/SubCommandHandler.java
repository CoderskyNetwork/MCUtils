package net.codersky.mcutils.cmd;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;

public class SubCommandHandler<S extends MCCommandSender<?>> {

	private final HashSet<MCCommand<S>> subCommands = new HashSet<>();

	@NotNull
	private String[] removeFirstArgument(@NotNull String[] args) {
		if (args.length == 1)
			return new String[0];
		final String[] newArgs = new String[args.length - 1];
		for (int i = 1; i < args.length; i++)
			newArgs[i - 1] = args[1];
		return newArgs;
	}

	private <T> T onUsedCommand(@NotNull MCCommand<S> mainCmd, @NotNull S sender, @NotNull String[] args,
	                            @NotNull BiFunction<MCCommand<S>, String[], T> action, @NotNull T def, boolean message) {
		if (mainCmd.hasAccess(sender, message))
			return def;
		if (args.length == 0)
			return action.apply(mainCmd, args);
		for (MCCommand<S> subCommand : subCommands)
			if (subCommand.getName().toLowerCase().equals(args[0]))
				return subCommand.hasAccess(sender, message) ? action.apply(subCommand, removeFirstArgument(args)) : def;
		return action.apply(mainCmd, args);
	}

	public boolean onCommand(@NotNull MCCommand<S> mainCmd, @NotNull S sender, @NotNull String[] args) {
		return onUsedCommand(mainCmd, sender, args, (cmd, newArgs) -> cmd.onCommand(sender, newArgs), true, true);
	}

	public List<String> onTab(@NotNull MCCommand<S> mainCommand, @NotNull S sender, @NotNull String[] args) {
		return onUsedCommand(mainCommand, sender, args, (cmd, newArgs) -> cmd.onTab(sender, newArgs), List.of(), false);
	}

	@SafeVarargs
	public final void inject(@NotNull MCCommand<S>... commands) {
		Collections.addAll(subCommands, commands);
	}
}
