package net.codersky.mcutils.java.tuple;

import javax.annotation.Nonnull;

/**
 * A class capable of storing three instance of generic
 * types. Containing a {@link #getFirst() first},
 * a {@link #getSecond() second} and a {@link #getThird()} element.
 * Elements may be mutable or immutable depending on the implementation.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * @param <T> The type of the third element to store.
 * 
 * @since MCUtils 1.0.0
 */
public abstract class Triple<F, S, T> extends Pair<F, S> {

	/**
	 * Gets the third element stored in this {@link Triple},
	 * which may be {@code null}.
	 * 
	 * @return The third element stored in this {@link Triple},
	 * possibly {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	public abstract T getThird();

	/**
	 * Compares the third element of <b>other</b> with the third
	 * element of this {@link Triple}, {@code null} types are supported
	 * and this method will still return {@code true} if both third
	 * elements are {@code null}.
	 * 
	 * @param other The other {@link Pair} to compare against.
	 * 
	 * @return {@code true} if the third element of this {@link Triple}
	 * and the third element of the <b>other</b> {@link Triple} are equal,
	 * {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getThird()
	 */
	public boolean isThirdEqual(@Nonnull Triple<?, ?, T> other) {
		return getThird() == null ? other.getThird() == null : getThird().equals(other.getThird());
	}

	/*
	 * Java
	 */

	@Override
	public String toString() {
		return "Triple[" + getFirst().toString() + ", " + getSecond().toString() + ", " + getThird().toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Triple))
			return false;
		final Triple<?, ?, ?> other = (Triple<?, ?, ?>)obj;
		boolean first = getFirst() == null ? other.getFirst() == null : getFirst().equals(other.getFirst());
		boolean second = getSecond() == null ? other.getSecond() == null : getSecond().equals(other.getSecond());
		boolean third = getThird() == null ? other.getThird() == null : getThird().equals(other.getThird());
		return first && second && third;
	}
}
