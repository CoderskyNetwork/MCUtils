package me.xdec0de.mcutils.events.player;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import me.xdec0de.mcutils.events.MCEvent;

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
}
