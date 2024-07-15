package net.codersky.mcutils.spigot.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nonnull;

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
 *     &#64;Override
 *     public HandlerList getHandlers() {
 *         return handlers;
 *     }
 * 
 *     &#64;Nonnull
 *     public static HandlerList getHandlerList() {
 *         return handlers;
 *     }
 * }
 * </pre>
 * And that's it, now you can add constructor parameters and whatever
 * getters or setters your events may need.
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
	 * Calls this {@link MCEvent}, a shortcut to
	 * {@link PluginManager#callEvent(Event)} that returns this
	 * {@link MCEvent} to be used further instead of {@code void}.
	 * 
	 * @return This {@link MCEvent}.
	 */
	@Nonnull
	public MCEvent call() {
		Bukkit.getPluginManager().callEvent(this);
		return this;
	}
}
