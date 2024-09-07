package net.codersky.mcutils.crossplatform.player;

import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.java.strings.Replacement;
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
	 * Replacement
	 */

	@NotNull
	@Override
	default String asReplacement() {
		return getName();
	}
}
