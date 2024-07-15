package net.codersky.mcutils.files;

import net.codersky.mcutils.java.strings.Replacer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface that is generally implemented with either
 * {@link FileHolder} or {@link FileUpdater}.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public interface MessagesFileHolder<T> {

	/*
	 * String getters
	  */

	@Nullable
	String getString(@NotNull String path);

	@Nullable
	default String getString(@NotNull String path, @NotNull Replacer replacer) {
		final String str = getString(path);
		return str == null ? null : replacer.replaceAt(str);
	}

	@Nullable
	default String getString(@NotNull String path, @NotNull Object... replacements) {
		return getString(path, new Replacer(replacements));
	}

	/*
	 * Message senders
	 */

	void sendMessage(@NotNull T target, @NotNull String raw);

	private boolean filterMessage(@NotNull T target, @Nullable String message) {
		if (message != null && !message.isBlank())
			sendMessage(target, message);
		return true;
	}

	default boolean send(@NotNull T target, @NotNull String path) {
		return filterMessage(target, getString(path));
	}

	default boolean send(@NotNull T target, @NotNull String path, @NotNull Replacer replacer) {
		return filterMessage(target, getString(path, replacer));
	}

	default boolean send(@NotNull T target, @NotNull String path, @NotNull Object... replacements) {
		return filterMessage(target, getString(path, replacements));
	}
}