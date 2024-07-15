package net.codersky.mcutils.spigot.java.tuple.triple;

import javax.annotation.Nullable;

/**
 * An implementation of {@link Triple} that allows changing the
 * values of the stored elements, if you need these elements
 * to be final, use {@link ImmutableTriple}.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * @param <T> The type of the third element to store.
 * 
 * @since MCUtils 1.0.0
 * 
 * @see ImmutableTriple
 */
public class MutableTriple<F, S, T> implements Triple<F, S, T> {

	private F first;
	private S second;
	private T third;

	/**
	 * Constructs a new {@link ImmutableTriple} that contains
	 * three elements which may be {@code null}.
	 * 
	 * @param first the first element to store.
	 * @param second the second element to store.
	 * @param third the third element to store.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MutableTriple(@Nullable F first, @Nullable S second, @Nullable T third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@Nullable
	@Override
	public F getFirst() {
		return first;
	}

	/**
	 * Sets the first element stored on this {@link MutableTriple}
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

	@Nullable
	@Override
	public S getSecond() {
		return second;
	}

	/**
	 * Sets the second element stored on this {@link MutableTriple}
	 * to <b>second</b>, which can be {@code null}.
	 * 
	 * @param second The new value of the first element.
	 * 
	 * @return <b>second</b>, for convenience.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public S setSecond(@Nullable S second) {
		return (this.second = second);
	}
	
	@Nullable
	@Override
	public T getThird() {
		return third;
	}

	/**
	 * Sets the third element stored on this {@link MutableTriple}
	 * to <b>third</b>, which can be {@code null}.
	 * 
	 * @param third The new value of the third element.
	 * 
	 * @return <b>third</b>, for convenience.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public T setThird(@Nullable T third) {
		return (this.third = third);
	}
}