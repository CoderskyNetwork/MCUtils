package net.codersky.mcutils.java.tuple.triple;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * An extension of {@link ImmutableTriple} that implements the
 * {@link SafeTriple} interface, meaning that the elements stored
 * by this {@link Triple} cannot be {@code null}.
 * 
 * @author xDec0de_
 *
 * @param <F> The type of the first element to store.
 * @param <S> The type of the second element to store.
 * @param <T> The type of the third element to store.
 *
 * @since MCUtils 1.0.0
 * 
 * @see SafeMutableTriple
 */
public class SafeImmutableTriple<F, S, T> extends ImmutableTriple<F, S, T> implements SafeTriple<F, S, T> {

	/**
	 * Constructs a new {@link SafeImmutableTriple} that contains
	 * two elements which can't be {@code null}.
	 *
	 * @param first the first element to store.
	 * @param second the second element to store.
	 * @param third the third element to store.
	 *
	 * @throws NullPointerException if either {@code first},
	 * {@code second} or {@code third} are {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	public SafeImmutableTriple(@NotNull F first, @NotNull S second, @NotNull T third) {
		super(Objects.requireNonNull(first), Objects.requireNonNull(second), Objects.requireNonNull(third));
	}

	// Doc override //

	/**
	 * Gets the first element stored in this {@link SafeImmutableTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The first element stored in this {@link SafeImmutableTriple},
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
	 * Gets the second element stored in this {@link SafeImmutableTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The second element stored in this {@link SafeImmutableTriple},
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
	 * Gets the third element stored in this {@link SafeImmutableTriple},
	 * which will <b>never</b> be {@code null}.
	 *
	 * @return The third element stored in this {@link SafeImmutableTriple},
	 * never {@code null}
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	@Override
	@SuppressWarnings("DataFlowIssue")
	public T getThird() {
		return super.getThird();
	}
}
