package net.codersky.mcutils.java.tuple.pair;

import javax.annotation.Nonnull;

/**
 * An extension of the {@link Pair} interface that requires
 * stored elements to <b>never</b> be {@code null}.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * 
 * @since MCUtils 1.0.0
 *
 * @see Pair
 * @see SafeMutablePair
 * @see SafeImmutablePair
 */
public interface SafePair<F, S> extends Pair<F, S> {

	/**
	 * Gets the first element stored in this {@link SafePair},
	 * which will <b>never</b> be {@code null}.
	 * 
	 * @return The first element stored in this {@link SafePair},
	 * never {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	F getFirst();

	/**
	 * Gets the second element stored in this {@link SafePair},
	 * which will <b>never</b> be {@code null}.
	 * 
	 * @return The second element stored in this {@link SafePair},
	 * never {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	S getSecond();
}
