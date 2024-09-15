package net.codersky.mcutils.crossplatform;

import org.jetbrains.annotations.NotNull;

/**
 * Interface used to represent a server or proxy console.
 *
 * @param <T> The platform-specific console object
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface MCConsole extends MessageReceiver {

	/**
	 * Gets the platform-specific object that is being wrapped by
	 * this {@link MCConsole} instance. This can be, for example, a
	 * Bukkit ConsoleCommandSender instance.
	 *
	 * @return The platform-specific object that is being wrapped by
	 * this {@link MCConsole} instance.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	Object getHandle();
}
