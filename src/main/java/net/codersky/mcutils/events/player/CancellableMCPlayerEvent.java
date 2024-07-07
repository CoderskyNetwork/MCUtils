package net.codersky.mcutils.events.player;

import net.codersky.mcutils.events.CancellableMCEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import net.codersky.mcutils.events.MCEvent;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nonnull;

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

	/**
	 * Calls this {@link CancellableMCPlayerEvent}, a shortcut to
	 * {@link PluginManager#callEvent(Event)} that returns this
	 * {@link CancellableMCPlayerEvent} to be used further instead of {@code void}.
	 *
	 * @return This {@link CancellableMCPlayerEvent}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@Override
	public CancellableMCPlayerEvent call() {
		Bukkit.getPluginManager().callEvent(this);
		return this;
	}
}
