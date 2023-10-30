package net.codersky.mcutils.events;

import org.bukkit.event.Cancellable;

/**
 * A simple type of {@link MCEvent} that
 * implements the {@link Cancellable} interface
 * so you don't have to manually do it, see
 * {@link MCEvent} for more details.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public abstract class CancellableMCEvent extends MCEvent implements Cancellable {

	private boolean cancelled = false;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
