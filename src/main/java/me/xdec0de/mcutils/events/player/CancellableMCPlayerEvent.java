package me.xdec0de.mcutils.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import me.xdec0de.mcutils.events.MCEvent;

/**
 * A simple type of {@link MCPlayerEvent} that
 * implements the {@link Cancellable} interface
 * so you don't have to manually do it, see
 * {@link MCEvent} for more details.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public abstract class CancellableMCPlayerEvent extends MCPlayerEvent implements Cancellable {

	private boolean cancelled;

	/**
	 * Construts a {@link CancellableMCPlayerEvent} required to be synchronous
	 * 
	 * @param who the player involved on the event.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @throws NullPointerException if <b>who</b> is null.
	 */
	public CancellableMCPlayerEvent(Player who) {
		super(who);
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
