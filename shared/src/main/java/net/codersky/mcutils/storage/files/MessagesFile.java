package net.codersky.mcutils.storage.files;

import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.Replacer;

import net.codersky.mcutils.Reloadable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface that is designed to get messages from a file that
 * can then be sent to a {@link MessageReceiver}. This interface
 * extends the {@link Reloadable} interface, though it is generally
 * also implemented with {@link UpdatableFile}.
 * <p>
 * Messages sent by files that implement this interface must apply
 * all patterns provided by MCUtils. This is default behaviour, and
 * it is already provided by the interface itself. The only method
 * that should be implemented is {@link #getMessage(String)}, everything
 * else has a default implementation that shouldn't be modified.
 * 
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface MessagesFile extends Reloadable {

	/*
	 * String getters
	 */

	/**
	 * Gets a message {@link String} from this {@link MessagesFile}.
	 * This {@link String} may be {@code null} if the message isn't found.
	 *
	 * @param path The path of the message to get.
	 *
	 * @return The {@link String} stored at the specified {@code path}.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException If {@code path} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	String getMessage(@NotNull String path);

	/**
	 * Gets a message {@link String} from this {@link MessagesFile},
	 * applying a {@link Replacer} to it. This {@link String} may be
	 * {@code null} if the message isn't found.
	 *
	 * @param path The path of the message to get.
	 * @param replacer The {@link Replacer} to apply to the message. If
	 * no message is found and hence the {@link String} is {@code null},
	 * the {@link Replacer} won't be applied.
	 *
	 * @return The {@link String} stored at the specified {@code path},
	 * with the specified {@link Replacer} applied to it.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	default String getMessage(@NotNull String path, @NotNull Replacer replacer) {
		final String str = getMessage(path);
		return str == null ? null : replacer.replaceAt(str);
	}

	/**
	 * Gets a message {@link String} from this {@link MessagesFile},
	 * applying a {@link Replacer} built from the specified {@code replacements} to it.
	 * This {@link String} may be {@code null} if the message isn't found.
	 *
	 * @param path The path of the message to get.
	 * @param replacements The {@link Object Objects} that will be used in
	 * order to build a new {@link Replacer} that will later be applied to
	 * the {@link String} found at the specified {@code path}. If said
	 * {@link String} is {@code null}, no {@link Replacer} will be created in
	 * order to save a tiny bit of resources. Keep in mind that the amount
	 * of {@code replacements} must be even as specified on the {@link Replacer}
	 * {@link Replacer#Replacer(Object...) constructor}.
	 *
	 * @return The {@link String} stored at the specified {@code path},
	 * with the specified {@code replacements} applied to it.
	 * {@code null} if no {@link String} is stored on said {@code path} or
	 * if the {@code path} itself doesn't exist.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	default String getMessage(@NotNull String path, @NotNull Object... replacements) {
		final String message = getMessage(path);
		return message == null ? null : new Replacer(replacements).replaceAt(message);
	}

	/*
	 * Message senders
	 */

	private boolean processMessage(@NotNull MessageReceiver target, @Nullable String message) {
		if (message != null && !message.isBlank())
			MCStrings.sendMessage(target, message);
		return true;
	}

	/**
	 * Sends the message located at the specified {@code path} to the provided
	 * {@code target}. If said message doesn't exist, or is blank ({@link String#isBlank()}),
	 * then no message will be sent to the {@code target}.
	 * <p>
	 * This method purely relies on the behaviour of {@link #getMessage(String)}.
	 *
	 * @param target the {@link MessageReceiver} that will receive the message.
	 * @param path the path at where to obtain the message, read {@link #getMessage(String)}
	 * for more details.
	 *
	 * @return Always {@code true}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean send(@NotNull MessageReceiver target, @NotNull String path) {
		return processMessage(target, getMessage(path));
	}

	/**
	 * Sends the message located at the specified {@code path} to the provided
	 * {@code target}, applying a {@link Replacer} before sending it.
	 * If said message doesn't exist, or is blank ({@link String#isBlank()}),
	 * then no message will be sent to the {@code target}.
	 * <p>
	 * This method purely relies on the behaviour of {@link #getMessage(String, Replacer)}.
	 *
	 * @param target the {@link MessageReceiver} that will receive the message.
	 * @param path the path at where to obtain the message, read {@link #getMessage(String, Replacer)}
	 * for more details.
	 * @param replacer The {@link Replacer} to apply to the message. If
	 * no message is found, the {@link Replacer} won't be applied.
	 *
	 * @return Always {@code true}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean send(@NotNull MessageReceiver target, @NotNull String path, @NotNull Replacer replacer) {
		return processMessage(target, getMessage(path, replacer));
	}

	/**
	 * Sends the message located at the specified {@code path} to the provided
	 * {@code target}, applying a {@link Replacer} before sending it.
	 * If said message doesn't exist, or is blank ({@link String#isBlank()}),
	 * then no message will be sent to the {@code target}.
	 * <p>
	 * This method purely relies on the behaviour of {@link #getMessage(String, Object...)}.
	 *
	 * @param target the {@link MessageReceiver} that will receive the message.
	 * @param path the path at where to obtain the message, read {@link #getMessage(String, Object...)}
	 * for more details.
	 * @param replacements The {@link Object Objects} that will be used in
	 * order to build a new {@link Replacer} that will later be applied to
	 * the message found at the specified {@code path}. If said
	 * {@link String} is {@code null}, no {@link Replacer} will be created in
	 * order to save a tiny bit of resources. Keep in mind that the amount
	 * of {@code replacements} must be even as specified on the {@link Replacer}
	 * {@link Replacer#Replacer(Object...) constructor}.
	 *
	 * @return Always {@code true}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean send(@NotNull MessageReceiver target, @NotNull String path, @NotNull Object... replacements) {
		return processMessage(target, getMessage(path, replacements));
	}
}