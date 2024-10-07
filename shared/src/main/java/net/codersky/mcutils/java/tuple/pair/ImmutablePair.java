package net.codersky.mcutils.java.tuple.pair;

import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link Pair} that doesn't allow the modification
 * of its elements, so it doesn't contain setters as
 * {@link MutablePair} does.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * 
 * @since MCUtils 1.0.0
 * 
 * @see MutablePair
 */
public class ImmutablePair<F, S> implements Pair<F, S> {

	private final F first;
	private final S second;

	/**
	 * Constructs a new {@link ImmutablePair} that contains
	 * two elements which may be {@code null}.
	 * 
	 * @param first the first element to store.
	 * @param second the second element to store.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public ImmutablePair(@Nullable F first, @Nullable S second) {
		this.first = first;
		this.second = second;
	}

	@Nullable
	@Override
	public F getFirst() {
		return first;
	}

	@Nullable
	@Override
	public S getSecond() {
		return second;
	}
}
