package net.codersky.mcutils.java.strings;

import net.codersky.mcutils.crossplatform.player.MCPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * An interface used to indicate that an {@link Object} can be directly used by a {@link Replacer} with
 * the {@link #asReplacement()} method. {@link MCPlayer} is a good example, as a {@link Replacer}
 * will use the {@link MCPlayer#asReplacement()} method instead of {@link Object#toString()}, which
 * in this case returns the player name, meaning that you can just add an {@link MCPlayer} instance
 * to the replacements of a {@link Replacer} instead of painfully having to call
 * {@link MCPlayer#getName()} every single time.
 * 
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public interface Replacement {

	/**
	 * A {@link String} representing how this {@link Replacement} should be represented
	 * by a {@link Replacer}. A good example for this is the {@link MCPlayer} interface,
	 * whose {@link MCPlayer#asReplacement()} method returns the player name, meaning that you
	 * there is no need to call {@link MCPlayer#getName()} every single time you want to
	 * use a player name on a {@link Replacer}.
 	 *
	 * @return A {@link String} identifying this {@link Replacement} which will be used by
	 * {@link Replacer Replacers} to represent it when this object is used as a replacement.
	 * This {@link String} is required to <b>never</b> be {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	String asReplacement();
}
