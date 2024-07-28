package net.codersky.mcutils.java.strings;

import javax.annotation.Nonnull;

/**
 * An interface to indicate that an {@link Object}
 * can directly be used by a {@link Replacer} with
 * the {@link #asReplacement()} method. This is useful,
 * for example, for custom player types that
 * don't implement {@link Player} but are treated
 * as such. As a {@link Replacer} will use the
 * {@link #asReplacement()} method instead of {@link #toString()}
 * to get the replacement for the object.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public interface Replacement {

	/**
	 * A {@link String} representing how this
	 * {@link Replacement} should be represented
	 * on a {@link String} by a {@link Replacer}.
	 * A good example for this is a custom player
	 * class, this method would return the player
	 * name to make replacements with it easier.
	 * 
	 * @return A {@link String} identifying this
	 * {@link Replacement} which will be used by
	 * {@link Replacer Replacers} to represent it
	 * when this object is used as a replacement on
	 * a {@link String}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String asReplacement();
}
