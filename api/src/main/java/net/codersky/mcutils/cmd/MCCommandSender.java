package net.codersky.mcutils.cmd;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class MCCommandSender<T> {

	private final T original;

	public MCCommandSender(@NotNull T original) {
		this.original = Objects.requireNonNull(original);
	}

	@NotNull
	public final T getOriginal() {
		return original;
	}

	public abstract boolean sendMessage(@NotNull String message);

	public abstract boolean hasPermission(@NotNull String permission);
}
