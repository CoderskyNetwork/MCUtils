package net.codersky.mcutils.java.tuple.pair;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A class capable of storing two instance of generic
 * types. Containing a {@link #getFirst() first} and
 * a {@link #getSecond() second} element. Elements may
 * be mutable or immutable depending on the implementation.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * 
 * @since MCUtils 1.0.0
 *
 * @see SafePair
 * @see MutablePair
 * @see ImmutablePair
 */
public interface Pair<F, S> {

	/**
	 * Gets the first element stored in this {@link Pair},
	 * which may be allowed to be {@code null} or not depending
	 * on the implementation used.
	 * 
	 * @return The first element stored in this {@link Pair}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	F getFirst();

	/**
	 * Gets the second element stored in this {@link Pair},
	 * which may be allowed to be {@code null} or not depending
	 * on the implementation used.
	 * 
	 * @return The second element stored in this {@link Pair}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	S getSecond();

	/**
	 * Compares the first element of <b>other</b> with the first
	 * element of this {@link Pair}, {@code null} types are supported
	 * and this method will still return {@code true} if both first
	 * elements are {@code null}.
	 * 
	 * @param other The other {@link Pair} to compare against.
	 * 
	 * @return {@code true} if the first element of this {@link Pair}
	 * and the first element of the <b>other</b> {@link Pair} are equal,
	 * {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getFirst()
	 */
	default boolean isFirstEqual(@NotNull Pair<F, ?> other) {
		return getFirst() == null ? other.getFirst() == null : getFirst().equals(other.getFirst());
	}

	/**
	 * Compares the second element of <b>other</b> with the second
	 * element of this {@link Pair}, {@code null} types are supported
	 * and this method will still return {@code true} if both second
	 * elements are {@code null}.
	 * 
	 * @param other The other {@link Pair} to compare against.
	 * 
	 * @return {@code true} if the second element of this {@link Pair}
	 * and the second element of the <b>other</b> {@link Pair} are equal,
	 * {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getSecond()
	 */
	default boolean isSecondEqual(@NotNull Pair<?, S> other) {
		return getSecond() == null ? other.getSecond() == null : getSecond().equals(other.getSecond());
	}
}
