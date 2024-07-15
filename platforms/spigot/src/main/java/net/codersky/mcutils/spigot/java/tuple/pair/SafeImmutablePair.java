package net.codersky.mcutils.spigot.java.tuple.pair;

import javax.annotation.Nonnull;

import java.util.Objects;

/**
 * An extension of {@link ImmutablePair} that implements the
 * {@link SafePair} interface, meaning that the elements stored
 * by this {@link Pair} cannot be {@code null}.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * 
 * @since MCUtils 1.0.0
 * 
 * @see SafeMutablePair
 */
public class SafeImmutablePair<F, S> extends ImmutablePair<F, S> implements SafePair<F, S> {

	/**
	 * Constructs a new {@link SafeImmutablePair} that contains
	 * two elements which can't be {@code null}.
	 *
	 * @param first the first element to store.
	 * @param second the second element to store.
	 *
	 * @throws NullPointerException if either {@code first}
	 * or {@code second} are {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	public SafeImmutablePair(@Nonnull F first, @Nonnull S second) {
		super(Objects.requireNonNull(first), Objects.requireNonNull(second));
	}

	// Doc override //

	/**
	 * Gets the first element stored in this {@link SafeImmutablePair},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The first element stored in this {@link SafeImmutablePair},
	 * never {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@Override
	@SuppressWarnings("DataFlowIssue")
	public F getFirst() {
		return super.getFirst();
	}

	/**
	 * Gets the second element stored in this {@link SafeImmutablePair},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The second element stored in this {@link SafeImmutablePair},
	 * never {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@Override
	@SuppressWarnings("DataFlowIssue")
	public S getSecond() {
		return super.getSecond();
	}
}
