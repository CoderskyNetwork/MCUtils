package net.codersky.mcutils.spigot.java.tuple.triple;

import javax.annotation.Nonnull;

/**
 * An extension of the {@link Triple} interface that requires
 * stored elements to <b>never</b> be {@code null}.
 *
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * @param <T> The type of the third element to store.
 *
 * @since MCUtils 1.0.0
 *
 * @see Triple
 * @see SafeMutableTriple
 * @see SafeImmutableTriple
 */
public interface SafeTriple<F, S, T> extends Triple<F, S, T> {

	/**
	 * Gets the first element stored in this {@link SafeTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The first element stored in this {@link SafeTriple},
	 * never {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	F getFirst();

	/**
	 * Gets the second element stored in this {@link SafeTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The second element stored in this {@link SafeTriple},
	 * never {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	S getSecond();

	/**
	 * Gets the third element stored in this {@link SafeTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The second element stored in this {@link SafeTriple},
	 * never {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	T getThird();
}
