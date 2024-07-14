package net.codersky.mcutils.builders.block;

import javax.annotation.Nonnull;

import org.bukkit.block.sign.Side;

/**
 * A simple enum used to represent a sign side
 * without depending on server version.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 * 
 * @see #BlockBuilder
 * @see #bukkit()
 */
public enum SignSide {

	/** Represents the front side of a sign: {@link Side#FRONT} */
	FRONT,
	/** Represents the back side of a sign: {@link Side#BACK} */
	BACK;

	/**
	 * Gets the bukkit version of this {@link SignSide},
	 * note that this will throw a {@link ClassNotFoundException}
	 * on servers older than 1.20, <b>unless</b> you are
	 * using {@link BlockBuilder}, which checks the server version
	 * for you.
	 * 
	 * @return The bukkit version of this {@link SignSide}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Side bukkit() {
		return switch(this) {
		case FRONT -> Side.FRONT;
		case BACK -> Side.BACK;
		};
	}
}
