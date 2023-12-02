package net.codersky.mcutils.java.tuple;

import javax.annotation.Nullable;

/**
 * An extension of {@link Pair} that allows changing the
 * values of the stored elements, if you need this elements
 * to be final, use {@link ImmutablePair}.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * 
 * @since MCUtils 1.0.0
 * 
 * @see ImmutablePair
 */
public class MutablePair<F, S> extends Pair<F, S> {

	private F first;
	private S second;

	/**
	 * Constructs a new {@link MutablePair} that contains
	 * two elements which may be {@code null}.
	 * 
	 * @param first the first element to store.
	 * @param second the second element to store.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MutablePair(@Nullable F first, @Nullable S second) {
		this.first = first;
		this.second = second;
	}

	@Override
	@Nullable
	public F getFirst() {
		return first;
	}

	/**
	 * Sets the first element stored on this {@link MutablePair}
	 * to <b>first</b>, which can be {@code null}.
	 * 
	 * @param first The new value of the first element.
	 * 
	 * @return <b>first</b>, for convenience.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public F setFirst(@Nullable F first) {
		return (this.first = first);
	}

	@Override
	@Nullable
	public S getSecond() {
		return second;
	}

	/**
	 * Sets the second element stored on this {@link MutablePair}
	 * to <b>second</b>, which can be {@code null}.
	 * 
	 * @param second The new value of the second element.
	 * 
	 * @return <b>second</b>, for convenience.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public S setSecond(@Nullable S second) {
		return (this.second = second);
	}
}