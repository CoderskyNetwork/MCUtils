package net.codersky.mcutils.crossplatform.player;

import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.java.strings.Replacement;
import net.codersky.mcutils.java.strings.Replacer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Cross-platform interface used to represent an <b>online</b> player.
 * This interface allows developers to use common methods
 * on all platforms without needing to actually use a
 * platform specific player type.
 * <p>
 * This interface extends the {@link Replacement} interface. Overriding
 * {@link Replacement#asReplacement()} with a call to {@link #getName()}
 * by default.
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface MCPlayer<T> extends MessageReceiver, Replacement {

	/**
	 * Gets the platform-specific object that is being wrapped by
	 * this {@link MCPlayer} instance. This can be, for example, a
	 * Bukkit Player instance.
	 *
	 * @return The platform-specific object that is being wrapped by
	 * this {@link MCPlayer} instance.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	T getHandle();

	/**
	 * Gets the {@link UUID} that belongs to this {@link MCPlayer}.
	 * This {@link UUID} is persistent and can be used as a way to
	 * identify players in the future, as player names can change.
	 *
	 * @return The {@link UUID} that belongs to this {@link MCPlayer}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	UUID getUniqueId();

	/**
	 * Gets the name of this {@link MCPlayer}. Keep in mind that player
	 * names may change at any given time and should not be used
	 * for storage. {@link #getUniqueId() UUIDs} can be used for that purpose
	 * instead, names are only guaranteed to remain unchanged for a single session
	 * (Until the player logs out).
	 *
	 * @return The name of this {@link MCPlayer}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	String getName();

	/*
	 * Legacy actionbar messages (String)
	 */

	/**
	 * Sends an ActionBar {@code message} to this {@link MCPlayer}.
	 *
	 * @param message The message to send to this {@link MCPlayer}.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean sendActionBar(@NotNull String message);

	/**
	 * Sends an ActionBar {@code message} to this {@link MCPlayer}, applying {@code replacer}
	 * to {@code message} before sending it.
	 *
	 * @param message The ActionBar message to send to this {@link MCPlayer}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull String message, @NotNull Replacer replacer) {
		return sendActionBar(replacer.replaceAt(message));
	}

	/**
	 * Sends an ActionBar {@code message} to this {@link MessageReceiver}, applying a {@link Replacer} made
	 * with the specified {@code replacements} to {@code message} before sending it.
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
	default boolean sendActionBar(@NotNull String message, @NotNull Object... replacements) {
		return sendActionBar(message, new Replacer(replacements));
	}

	/*
	 * Actionbar Adventure messages
	 */

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link MCPlayer}.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MCPlayer}.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if {@code message} is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean sendActionBar(@NotNull Component message);

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link MCPlayer}, applying {@code replacer}
	 * to {@code message} before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MCPlayer}.
	 * @param replacer The {@link Replacer} to apply to the {@code message} before sending it.
	 *
	 * @return Always {@code true} to make it easier to create {@link MCCommand MCCommands}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	default boolean sendActionBar(@NotNull Component message, @NotNull Replacer replacer) {
		return sendActionBar(replacer.replaceAt(message));
	}

	/**
	 * Sends an ActionBar {@link Component} {@code message} to this {@link MCPlayer}, applying
	 * a {@link Replacer} made with the specified {@code replacements} to {@code message} before sending it.
	 *
	 * @param message The Adventure {@link Component} to send to this {@link MCPlayer}.
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
	default boolean sendActionBar(@NotNull Component message, @NotNull Object... replacements) {
		return sendActionBar(message, new Replacer(replacements));
	}

	/*
	 * Replacement
	 */

	@NotNull
	@Override
	default String asReplacement() {
		return getName();
	}
}
