package net.codersky.mcutils.crossplatform.player;

import net.codersky.mcutils.crossplatform.MessageReceiver;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Cross-platform interface used to represent an <b>online</b> player.
 * This interface allows developers to use common methods
 * on all platforms without needing to actually use a
 * platform specific player type.
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface MCPlayer<T> extends MessageReceiver {

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
}
