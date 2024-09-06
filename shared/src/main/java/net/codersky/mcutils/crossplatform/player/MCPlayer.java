package net.codersky.mcutils.crossplatform.player;

import net.codersky.mcutils.crossplatform.MessageReceiver;
import org.jetbrains.annotations.NotNull;

/**
 * Cross-platform interface used to represent a player.
 * This interface allows developers to use common methods
 * on all platforms without needing to actually use a
 * platform specific player type.
 *
 * @since MCUtils 1.0.0
 */
public interface MCPlayer<T> extends MessageReceiver {

	@NotNull
	T getHandle();
}
