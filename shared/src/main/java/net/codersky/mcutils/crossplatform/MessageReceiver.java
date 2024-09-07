package net.codersky.mcutils.crossplatform;

import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.java.strings.Replacer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * Cross-platform interface used to handle objects that
 * may receive messages such as {@link MCPlayer} or {@link MCConsole}.
 *
 * @since MCUtils 1.0.0
 *
 * @see #sendMessage(String)
 * @see #sendMessage(String, Replacer)
 * @see #sendMessage(String, Object...)
 *
 * @author xDec0de_
 */
public interface MessageReceiver {

	/*
	 * Legacy messages (String)
	 */

	/**
	 * Sends a {@code message} to this {@link MessageReceiver}.
	 *
	 * @param message The message to send to this {@link MessageReceiver}.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean sendMessage(@NotNull String message);

	/**
	 * Sends a {@code message} to this {@link MessageReceiver}, applying {@code replacer}
	 * to {@code message} before sending it.
	 *
	 * @param message The message to send to this {@link MessageReceiver}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean sendMessage(@NotNull String message, @NotNull Replacer replacer) {
		return sendMessage(replacer.replaceAt(message));
	}

	/**
	 * Sends a {@code message} to this {@link MessageReceiver}, applying a {@link Replacer} made
	 * with the specified {@code replacements} to {@code message}.
	 *
	 * @param message The message to send to this {@link MessageReceiver}.
	 * @param replacements The replacements used to build a {@link Replacer} that will then be
	 * applied to the {@code message} before sending it. The amount of replacements must be even
	 * as specified on the {@link Replacer} {@link Replacer#Replacer constructor}.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean sendMessage(@NotNull String message, @NotNull Object... replacements) {
		return sendMessage(message, new Replacer(replacements));
	}

	/*
	 * Adventure messages
	 */

	/**
	 * Sends an Adventure {@link Component} to this {@link MessageReceiver}.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MessageReceiver}.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean sendMessage(@NotNull Component message);

	/**
	 * Sends an Adventure {@link Component} to this {@link MessageReceiver}, applying {@code replacer}
	 * to it before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MessageReceiver}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean sendMessage(@NotNull Component message, @NotNull Replacer replacer) {
		return sendMessage(replacer.replaceAt(message));
	}

	/**
	 * Sends an Adventure {@link Component} to this {@link MessageReceiver}, applying a {@link Replacer} made
	 * with the specified {@code replacements} to it before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MessageReceiver}.
	 * @param replacements The replacements used to build a {@link Replacer} that will then be
	 * applied to the {@code message} before sending it. The amount of replacements must be even
	 * as specified on the {@link Replacer} {@link Replacer#Replacer constructor}.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean sendMessage(@NotNull Component message, @NotNull Object... replacements) {
		return sendMessage(message, new Replacer(replacements));
	}
}
