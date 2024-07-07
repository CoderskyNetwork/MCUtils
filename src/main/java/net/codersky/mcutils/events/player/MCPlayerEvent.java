package net.codersky.mcutils.events.player;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.codersky.mcutils.events.CancellableMCEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.codersky.mcutils.events.MCEvent;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

/**
 * A simple type of {@link MCEvent} that
 * adds a {@link #getPlayer()} method.
 * See {@link MCEvent} for more details.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public abstract class MCPlayerEvent extends MCEvent {

	private final Player player;

	/**
	 * Construts a {@link MCPlayerEvent} required to be synchronous
	 * 
	 * @param who the player involved on the event.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @throws NullPointerException if <b>who</b> is null.
	 */
	public MCPlayerEvent(Player who) {
		this(who, false);
	}

	/**
	 * Construts a {@link MCPlayerEvent} that can be synchronous
	 * or asynchronous depending on the value of <b>async</b>.
	 * 
	 * @param who the player involved on the event.
	 * @param async true indicates the event will fire asynchronously,
	 * false by default from default constructor.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @throws NullPointerException if <b>who</b> is null.
	 */
	public MCPlayerEvent(Player who, boolean async) {
		super(async);
		player = Objects.requireNonNull(who, "Player cannot be null.");
	}

	/**
	 * Gets the player involved on this {@link MCPlayerEvent}.
	 * 
	 * @return the player involved on this {@link MCPlayerEvent}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Player getPlayer() {
		return player;
	}

	/**
	 * Calls this {@link MCPlayerEvent}, a shortcut to
	 * {@link PluginManager#callEvent(Event)} that returns this
	 * {@link MCPlayerEvent} to be used further instead of {@code void}.
	 *
	 * @return This {@link MCPlayerEvent}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@Override
	public MCPlayerEvent call() {
		Bukkit.getPluginManager().callEvent(this);
		return this;
	}
}
