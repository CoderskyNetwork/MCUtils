package net.codersky.mcutils.crossplatform;

import net.codersky.mcutils.java.strings.Replacer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Cross-platform interface used to handle objects that
 * may receive messages such as players.
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 * 
 * @see #sendMessage(String)
 * @see #sendMessage(String, Replacer)
 * @see #sendMessage(String, Object...) 
 */
public interface MessageReceiver {

	/*
	 * Legacy messages (String)
	 */

	/**
	 * Sends a player to this {@link MessageReceiver}.
	 *
	 * @param message The message to send to this {@link MessageReceiver}. This message
	 * <b>won't</b> be sent to the receiver if {@code null}.
	 *
	 * @return Always {@code true}, no matter if the message is sent or not.
	 * This is to make it easier to create {@link net.codersky.mcutils.cmd.MCCommand MCCommands}.
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean sendMessage(@Nullable String message);

	/**
	 * Sends a player to this {@link MessageReceiver}.
	 *
	 * @param message The message to send to this {@link MessageReceiver}. This message
	 * <b>won't</b> be sent to the receiver if {@code null}.
	 * @param replacer The {@link Replacer} to apply to the {@code player} before sending it.
	 *
	 * @return Always {@code true}, no matter if the message is sent or not.
	 * This is to make it easier to create {@link net.codersky.mcutils.cmd.MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code replacer} is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean sendMessage(@Nullable String message, @NotNull Replacer replacer) {
		return sendMessage(replacer.replaceAt(message));
	}

	/**
	 * Sends a player to this {@link MessageReceiver}.
	 *
	 * @param message The message to send to this {@link MessageReceiver}. This message
	 * <b>won't</b> be sent to the receiver if {@code null}.
	 * @param replacements The replacements used to build a {@link Replacer} that will then be
	 * applied to the {@code player} before sending it. The amount of replacements must be even
	 * as specified on the {@link Replacer} {@link Replacer#Replacer constructor}.
	 *
	 * @return Always {@code true}, no matter if the message is sent or not.
	 * This is to make it easier to create {@link net.codersky.mcutils.cmd.MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code replacements} are {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean sendMessage(@Nullable String message, @NotNull Object... replacements) {
		return sendMessage(message, new Replacer(replacements));
	}

	/*
	 * Adventure messages
	 */

	/**
	 * Sends an Adventure {@link Component} to this {@link MessageReceiver}.
	 *
	 * @param message The {@link Component} to send to this {@link MessageReceiver}.
	 * This message <b>won't</b> be sent to the receiver if {@code null}.
	 *
	 * @return Always {@code true}, no matter if the message is sent or not.
	 * This is to make it easier to create {@link net.codersky.mcutils.cmd.MCCommand MCCommands}.
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean sendMessage(@Nullable Component message);
}
