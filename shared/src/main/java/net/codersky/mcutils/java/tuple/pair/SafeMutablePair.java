package net.codersky.mcutils.java.tuple.pair;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An extension of {@link MutablePair} that implements the
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
 * @see SafeImmutablePair
 */
public class SafeMutablePair<F, S> extends MutablePair<F, S> implements SafePair<F, S> {

	/**
	 * Constructs a new {@link SafeMutablePair} that contains
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
	public SafeMutablePair(@NotNull F first, @NotNull S second) {
		super(Objects.requireNonNull(first), Objects.requireNonNull(second));
	}

	// Doc override //

	/**
	 * Gets the first element stored in this {@link SafeMutablePair},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The first element stored in this {@link SafeMutablePair},
	 * never {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	@Override
	@SuppressWarnings("DataFlowIssue")
	public F getFirst() {
		return super.getFirst();
	}

	/**
	 * Sets the first element stored on this {@link SafeMutablePair}
	 * to {@code first}, which can't be {@code null}.
	 *
	 * @param first The new value of the first element.
	 *
	 * @return {@code first}, for convenience.
	 *
	 * @throws NullPointerException if {@code first} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public F setFirst(@NotNull F first) {
		return super.setFirst(Objects.requireNonNull(first));
	}

	/**
	 * Gets the second element stored in this {@link SafeMutablePair},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The second element stored in this {@link SafeMutablePair},
	 * never {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	@Override
	@SuppressWarnings("DataFlowIssue")
	public S getSecond() {
		return super.getSecond();
	}

	/**
	 * Sets the second element stored on this {@link SafeMutablePair}
	 * to {@code second}, which can't be {@code null}.
	 * 
	 * @param second The new value of the second element.
	 * 
	 * @return {@code second}, for convenience.
	 *
	 * @throws NullPointerException if {@code second} is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public S setSecond(@Nullable S second) {
		return super.setSecond(Objects.requireNonNull(second));
	}
}