package net.codersky.mcutils.java.tuple;

import javax.annotation.Nullable;

/**
 * An extension of {@link Triple} that doesn't allow the modification
 * of its elements, so it doesn't contain setters as
 * {@link MutableTriple} does.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * @param <T> The type of the third element to store.
 * 
 * @since MCUtils 1.0.0
 * 
 * @see MutableTriple
 */
public class ImmutableTriple<F, S, T> extends Triple<F, S, T> {

	private final F first;
	private final S second;
	private final T third;

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
	public ImmutableTriple(@Nullable F first, @Nullable S second, @Nullable T third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	@Nullable
	public F getFirst() {
		return first;
	}

	@Nullable
	public S getSecond() {
		return second;
	}

	@Nullable
	public T getThird() {
		return third;
	}
}
