package me.xdec0de.mcutils.java;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A utility class for numbers, this class doesn't have many methods as
 * other classes such as {@link Math} and {@link Integer} already contain them.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public abstract class MCNumbers {

	private static Random random;

	// For better code organization, if a method requires different primitive numeric types
	// We use this order: int, long, float, double. So small to big, integers to decimals.

	/**
	 * Gets a range of <code>int</code>s between <b>from</b>
	 * and <b>to</b>. It doesn't matter if <b>from</b> is the
	 * bigger or smaller number, here are some examples:
	 * <p>
	 * <ul>
	 * <li>(1, 1): [1]</li>
	 * <li>(1, 5): [1, 2, 3, 4, 5]</li>
	 * <li>(5, 1): [5, 4, 3, 2, 1]</li>
	 * <li>(0, -4): [0, -1, -2, -3, -4]</li>
	 * <li>(-4, 0): [-4, -3, -2, -1, 0]</li>
	 * </ul>
	 * 
	 * @param from the first number of the array.
	 * @param to the last number of the array.
	 * 
	 * @return An <code>int</code> array within the defined range.
	 */
	@Nonnull
	public static int[] range(int from, int to) {
		final int size = to - from;
		if (size == 0)
			return new int[] {to};
		final int[] res = new int[size > 0 ? size + 1 : (size * -1) + 1];
		int start = from;
		if (from > to)
			for (int i = 0; i < res.length; i++)
				res[i] = start--;
		else
			for (int i = 0; i < res.length; i++)
				res[i] = start++;
		return res;
	}

	/**
	 * <b>Note:</b> Ranges longer than {@link Integer#MAX_VALUE}
	 * will not be complete as arrays cannot be of a length
	 * bigger than {@link Integer#MAX_VALUE}.
	 * <p>
	 * Gets a range of <code>long</code>s between <b>from</b>
	 * and <b>to</b>. It doesn't matter if <b>from</b> is the
	 * bigger or smaller number, here are some examples:
	 * <p>
	 * <ul>
	 * <li>(1, 1): [1]</li>
	 * <li>(1, 5): [1, 2, 3, 4, 5]</li>
	 * <li>(5, 1): [5, 4, 3, 2, 1]</li>
	 * <li>(0, -4): [0, -1, -2, -3, -4]</li>
	 * <li>(-4, 0): [-4, -3, -2, -1, 0]</li>
	 * </ul>
	 * 
	 * @param from the first number of the array.
	 * @param to the last number of the array.
	 * 
	 * @return A <code>long</code> array within the defined range.
	 */
	@Nonnull
	public static long[] range(long from, long to) {
		long size = to - from;
		if (size == 0)
			return new long[] {to};
		size = size > 0 ? size + 1 : (size * -1) + 1;
		if (size > Integer.MAX_VALUE)
			size = Integer.MAX_VALUE;
		final long[] res = new long[(int)size];
		long start = from;
		if (from > to)
			for (int i = 0; i < res.length; i++)
				res[i] = start--;
		else
			for (int i = 0; i < res.length; i++)
				res[i] = start++;
		return res;
	}

	/*
	 * Random
	 */

	/**
	 * Gets the {@link Random} instance being used by {@link MCNumbers}.
	 * This method doesn't create a new {@link Random} instance
	 * every time it gets called, only the first time.
	 * 
	 * @return A never null {@link Random} instance.
	 */
	@Nonnull
	public static Random random() {
		return random == null ? (random = new Random()) : random;
	}	

	/**
	 * Returns either <b>one</b> or <b>two</b> randomly.
	 * 
	 * @param one the first number.
	 * @param two the second number.
	 * 
	 * @return Either <b>one</b> or <b>two</b>.
	 */
	public static int random(int one, int two) {
		if (one == two)
			return one;
		return random().nextBoolean() ? one : two;
	}

	/**
	 * Returns either <b>one</b> or <b>two</b> randomly.
	 * 
	 * @param one the first number.
	 * @param two the second number.
	 * 
	 * @return Either <b>one</b> or <b>two</b>.
	 */
	public static long random(long one, long two) {
		if (one == two)
			return one;
		return random().nextBoolean() ? one : two;
	}

	/**
	 * Returns either <b>one</b> or <b>two</b> randomly.
	 * 
	 * @param one the first number.
	 * @param two the second number.
	 * 
	 * @return Either <b>one</b> or <b>two</b>.
	 */
	public static float random(float one, float two) {
		if (one == two)
			return one;
		return random().nextBoolean() ? one : two;
	}

	/**
	 * Returns either <b>one</b> or <b>two</b> randomly.
	 * 
	 * @param one the first number.
	 * @param two the second number.
	 * 
	 * @return Either <b>one</b> or <b>two</b>.
	 */
	public static double random(double one, double two) {
		if (one == two)
			return one;
		return random().nextBoolean() ? one : two;
	}

	/**
	 * Returns either <b>one</b> or <b>two</b> randomly.
	 * 
	 * @param one the first number.
	 * @param two the second number.
	 * 
	 * @return Either <b>one</b> or <b>two</b>.
	 */
	@Nullable
	public static <T extends Object> T random(@Nullable T one, @Nullable T two) {
		// We don't check equality here to avoid NullPointerExceptions.
		return random().nextBoolean() ? one : two;
	}
}
