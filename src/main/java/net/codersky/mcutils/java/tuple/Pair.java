package net.codersky.mcutils.java.tuple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A class capable of storing two instance of generic
 * types. Containing a {@link #getFirst() first} and
 * a {@link #getSecond() second} element, elements may
 * be mutable or immutable depending on the implementation.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * 
 * @since MCUtils 1.0.0
 */
public abstract class Pair<F, S> {

	/**
	 * Gets the first element stored in this {@link Pair},
	 * which may be {@code null}.
	 * 
	 * @return The first element stored in this {@link Pair},
	 * possibly {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public abstract F getFirst();

	/**
	 * Gets the second element stored in this {@link Pair},
	 * which may be {@code null}.
	 * 
	 * @return The second element stored in this {@link Pair},
	 * possibly {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public abstract S getSecond();

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
	public boolean isFirstEqual(@Nonnull Pair<F, ?> other) {
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
	 * @see #geSecond()
	 */
	public boolean isSecondEqual(@Nonnull Pair<?, S> other) {
		return getSecond() == null ? other.getSecond() == null : getSecond().equals(other.getSecond());
	}

	/*
	 * Java
	 */

	@Override
	public String toString() {
		return "Pair[" + getFirst() + ", " + getSecond() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Pair))
			return false;
		final Pair<?, ?> other = (Pair<?, ?>)obj;
		boolean first = getFirst() == null ? other.getFirst() == null : getFirst().equals(other.getFirst());
		boolean second = getSecond() == null ? other.getSecond() == null : getSecond().equals(other.getSecond());
		return first && second;
	}
}
