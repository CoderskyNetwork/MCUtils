package net.codersky.mcutils.events;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

/**
 * A very simple {@link Event} class that provides a {@link #call()}
 * method to to create and call an {@link Event} in just one line.
 * Here is how to create an asynchronous {@link MCEvent}:
 * <p>
 * <pre>
 * public class TestEvent extends MCEvent {
 * 
 *     private final static HandlerList handlers = new HandlerList();
 * 
 *     public TestEvent() {
 *         super(true); // This makes the event asynchronous.
 *     }
 * 
 *     &#64;Nonnull
 *     public static HandlerList getHandlerList() {
 *         return handlers;
 *     }
 * }
 * </pre>
 * And that's it, now you can add constructor parameters and whatever
 * getters or setters your events may need, the {@link #getHandlers()}
 * method will return {@link #getHandlerList()}.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 * 
 * @see CancellableMCEvent
 */
public abstract class MCEvent extends Event {

	/**
	 * This constructor assumes the {@link MCEvent} is synchronous.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #MCEvent(boolean)
	 */
	public MCEvent() {
		super();
	}

	/**
	 * This constructor is used to explicitly declare an event as synchronous or asynchronous.
	 * 
	 * @param isAsync true indicates the event will fire asynchronously,
	 * false by default from default constructor.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #MCEvent()
	 */
	public MCEvent(boolean isAsync) {
		super(isAsync);
	}

	/**
	 * Calls this {@link MCEvent}, a shortcut of
	 * {@link PluginManager#callEvent(Event)}.
	 * 
	 * @return This {@link MCEvent}.
	 */
	public MCEvent call() {
		Bukkit.getPluginManager().callEvent(this);
		return this;
	}

	@Nonnull
	public HandlerList getHandlers() {
		return getHandlerList();
	}

	@Nonnull
	public static HandlerList getHandlerList() {
		throw new UnsupportedOperationException("Called #getHandlerList on a MCEvent that doesn't implement it.");
	}
}
