package net.codersky.mcutils.math;

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
@SuppressWarnings("unchecked")
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
	 * 
	 * @since MCUtils 1.0.0
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
	 * 
	 * @since MCUtils 1.0.0
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
	 * 
	 * @since MCUtils 1.0.0
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
	 * 
	 * @since MCUtils 1.0.0
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
	 * 
	 * @since MCUtils 1.0.0
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
	 * 
	 * @since MCUtils 1.0.0
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
	 * 
	 * @since MCUtils 1.0.0
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
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static <T extends Object> T random(@Nullable T one, @Nullable T two) {
		// We don't check equality here to avoid NullPointerExceptions.
		return random().nextBoolean() ? one : two;
	}

	/*
	 * Generic operations
	 */

	/**
	 * Sums two generic numbers. Note that <b>x</b>'s type is the one
	 * that takes priority here, so if you were to sum 1 and 1.1, 2 would
	 * be returned as both values are converted to {@link Integer}
	 * (<b>x</b>'s class).
	 * <p>
	 * If both <b>x</b> and <b>y</b> are of the same type, the operation
	 * will be made as expected and you should not be using this method
	 * but regular operators instead (Unless you are working with generics, of course).
	 * <p>
	 * If <b>x</b>'s type is not decimal ({@link Integer} or {@link Long}) but <b>y</b>'s type is
	 * ({@link Float} or {@link Double}), <b>y</b> will be rounded in order to then cast it to <b>x</b>'s type,
	 * meaning that if we assume that <b>x</b> is 1 ({@link Integer}) and <b>y</b> is 1.4F ({@link Float}), <b>y</b>'s
	 * value will be rounded to 1, if <b>y</b> is 1.5F, it will be rounded to 2.
	 * <p>
	 * <b>Limitations</b>: {@link Short} and {@link Byte} are currently not supported
	 * as the <b>x</b> value for this method as it tries to cast to {@link Integer}
	 * but can't, throwing a {@link ClassCastException}, this exception is prevented by
	 * returning {@code null}, <b>y</b> on the other hand can be of any {@link Number}
	 * class.
	 * 
	 * @param <N> A class extending {@link Number} such as {@link Integer}, {@link Long},
	 * {@link Float} or {@link Double}.
	 * 
	 * @param x Any number to sum with <b>y</b>, the class of this parameter will take
	 * priority for casting, {@link Short} and {@link Byte} are not supported on this parameter.
	 * @param y The number that will be summed to <b>x</b>, this one has no restrictions
	 * other than not being {@code null}, {@link Short} and {@link Byte} are supported.
	 * 
	 * @return The sum of both <b>x</b> and <b>y</b>, without overflowing protection,
	 * {@code null} if <b>x</b>'s class is either {@link Short} or {@link Byte} only.
	 * 
	 * @throws NullPointerException if <b>x</b> or <b>y</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static <N extends Number> N sum(@Nonnull N x, @Nonnull N y) {
		final Class<?> type = x.getClass();
		if (type.equals(Integer.class))
			return (N) type.cast(x.intValue() + Math.round(y.floatValue()));
		else if (type.equals(Long.class))
			return (N) type.cast(x.longValue() + Math.round(y.doubleValue()));
		else if (type.equals(Float.class))
			return (N) type.cast(x.floatValue() + y.floatValue());
		else if (type.equals(Double.class))
			return (N) type.cast(x.doubleValue() + y.doubleValue());
		return null;
	}

	/**
	 * Subtracts two generic numbers. Note that <b>x</b>'s type is the one
	 * that takes priority here, so if you were to subtract 2 and 1.1, 1 would
	 * be returned as both values are converted to {@link Integer}
	 * (<b>x</b>'s class).
	 * <p>
	 * If both <b>x</b> and <b>y</b> are of the same type, the operation
	 * will be made as expected and you should not be using this method
	 * but regular operators instead (Unless you are working with generics, of course).
	 * <p>
	 * If <b>x</b>'s type is not decimal ({@link Integer} or {@link Long}) but <b>y</b>'s type is
	 * ({@link Float} or {@link Double}), <b>y</b> will be rounded in order to then cast it to <b>x</b>'s type,
	 * meaning that if we assume that <b>x</b> is 1 ({@link Integer}) and <b>y</b> is 1.4F ({@link Float}), <b>y</b>'s
	 * value will be rounded to 1, if <b>y</b> is 1.5F, it will be rounded to 2.
	 * <p>
	 * <b>Limitations</b>: {@link Short} and {@link Byte} are currently not supported
	 * as the <b>x</b> value for this method as it tries to cast to {@link Integer}
	 * but can't, throwing a {@link ClassCastException}, this exception is prevented by
	 * returning {@code null}, <b>y</b> on the other hand can be of any {@link Number}
	 * class.
	 * 
	 * @param <N> A class extending {@link Number} such as {@link Integer}, {@link Long},
	 * {@link Float} or {@link Double}.
	 * 
	 * @param x Any number subtract <b>y</b> of, the class of this parameter will take
	 * priority for casting, {@link Short} and {@link Byte} are not supported on this parameter.
	 * @param y The number that will be subtracted from <b>x</b>, this one has no restrictions
	 * other than not being {@code null}, {@link Short} and {@link Byte} are supported.
	 * 
	 * @return <b>x</b> with <b>y</b> subtracted from it, without overflowing protection,
	 * {@code null} if <b>x</b>'s class is either {@link Short} or {@link Byte} only.
	 * 
	 * @throws NullPointerException if <b>x</b> or <b>y</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public static <N extends Number> N subtract(@Nonnull N x, @Nonnull N y) {
		final Class<?> type = x.getClass();
		if (type.equals(Integer.class))
			return (N) type.cast(x.intValue() - Math.round(y.floatValue()));
		else if (type.equals(Long.class))
			return (N) type.cast(x.longValue() - Math.round(y.doubleValue()));
		else if (type.equals(Float.class))
			return (N) type.cast(x.floatValue() - y.floatValue());
		else if (type.equals(Double.class))
			return (N) type.cast(x.doubleValue() - y.doubleValue());
		return null;
	}

	/**
	 * Multiplies two generic numbers. Note that <b>x</b>'s type is the one
	 * that takes priority here, so if you were to multiply 2 and 2.1, 4 would
	 * be returned as both values are converted to {@link Integer}
	 * (<b>x</b>'s class).
	 * <p>
	 * If both <b>x</b> and <b>y</b> are of the same type, the operation
	 * will be made as expected and you should not be using this method
	 * but regular operators instead (Unless you are working with generics, of course).
	 * <p>
	 * If <b>x</b>'s type is not decimal ({@link Integer} or {@link Long}) but <b>y</b>'s type is
	 * ({@link Float} or {@link Double}), <b>y</b> will be rounded in order to then cast it to <b>x</b>'s type,
	 * meaning that if we assume that <b>x</b> is 1 ({@link Integer}) and <b>y</b> is 1.4F ({@link Float}), <b>y</b>'s
	 * value will be rounded to 1, if <b>y</b> is 1.5F, it will be rounded to 2.
	 * <p>
	 * <b>Limitations</b>: {@link Short} and {@link Byte} are currently not supported
	 * as the <b>x</b> value for this method as it tries to cast to {@link Integer}
	 * but can't, throwing a {@link ClassCastException}, this exception is prevented by
	 * returning {@code null}, <b>y</b> on the other hand can be of any {@link Number}
	 * class.
	 * 
	 * @param <N> A class extending {@link Number} such as {@link Integer}, {@link Long},
	 * {@link Float} or {@link Double}.
	 * 
	 * @param x Any number multiply by <b>y</b>, the class of this parameter will take
	 * priority for casting, {@link Short} and {@link Byte} are not supported on this parameter.
	 * @param y The number that will be multiplied with <b>x</b>, this one has no restrictions
	 * other than not being {@code null}, {@link Short} and {@link Byte} are supported.
	 * 
	 * @return <b>x</b> multiplied by <b>y</b>, without overflowing protection,
	 * {@code null} if <b>x</b>'s class is either {@link Short} or {@link Byte} only.
	 * 
	 * @throws NullPointerException if <b>x</b> or <b>y</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public static <N extends Number> N multiply(@Nonnull N x, @Nonnull N y) {
		final Class<?> type = x.getClass();
		if (type.equals(Integer.class))
			return (N) type.cast(x.intValue() * Math.round(y.floatValue()));
		else if (type.equals(Long.class))
			return (N) type.cast(x.longValue() * Math.round(y.doubleValue()));
		else if (type.equals(Float.class))
			return (N) type.cast(x.floatValue() * y.floatValue());
		else if (type.equals(Double.class))
			return (N) type.cast(x.doubleValue() * y.doubleValue());
		return null;
	}

	/**
	 * Divides two generic numbers. Note that <b>x</b>'s type is the one
	 * that takes priority here, so if you were to divide 2 and 2.1, 1 would
	 * be returned as both values are converted to {@link Integer}
	 * (<b>x</b>'s class).
	 * <p>
	 * If both <b>x</b> and <b>y</b> are of the same type, the operation
	 * will be made as expected and you should not be using this method
	 * but regular operators instead (Unless you are working with generics, of course).
	 * <p>
	 * If <b>x</b>'s type is not decimal ({@link Integer} or {@link Long}) but <b>y</b>'s type is
	 * ({@link Float} or {@link Double}), <b>y</b> will be rounded in order to then cast it to <b>x</b>'s type,
	 * meaning that if we assume that <b>x</b> is 1 ({@link Integer}) and <b>y</b> is 1.4F ({@link Float}), <b>y</b>'s
	 * value will be rounded to 1, if <b>y</b> is 1.5F, it will be rounded to 2.
	 * <p>
	 * <b>Limitations</b>: {@link Short} and {@link Byte} are currently not supported
	 * as the <b>x</b> value for this method as it tries to cast to {@link Integer}
	 * but can't, throwing a {@link ClassCastException}, this exception is prevented by
	 * returning {@code null}, <b>y</b> on the other hand can be of any {@link Number}
	 * class.
	 * 
	 * @param <N> A class extending {@link Number} such as {@link Integer}, {@link Long},
	 * {@link Float} or {@link Double}.
	 * 
	 * @param x Any number divide by <b>y</b>, the class of this parameter will take
	 * priority for casting, {@link Short} and {@link Byte} are not supported on this parameter.
	 * @param y The number that will be used to divide <b>x</b>, this one has no restrictions
	 * other than not being {@code null}, {@link Short} and {@link Byte} are supported.
	 * 
	 * @return <b>x</b> divided by <b>y</b>, without overflowing protection,
	 * {@code null} if <b>x</b>'s class is either {@link Short} or {@link Byte} only.
	 * 
	 * @throws NullPointerException if <b>x</b> or <b>y</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public static <N extends Number> N divide(@Nonnull N x, @Nonnull N y) {
		final Class<?> type = x.getClass();
		if (type.equals(Integer.class))
			return (N) type.cast(x.intValue() / Math.round(y.floatValue()));
		else if (type.equals(Long.class))
			return (N) type.cast(x.longValue() / Math.round(y.doubleValue()));
		else if (type.equals(Float.class))
			return (N) type.cast(x.floatValue() / y.floatValue());
		else if (type.equals(Double.class))
			return (N) type.cast(x.doubleValue() / y.doubleValue());
		return null;
	}

	/*
	 * Percentage
	 */

	/**
	 * Randomly returns {@code true} with a set <b>percent</b>
	 * rate of success, numbers higher or equal to 100 will
	 * always return {@code true} while numbers lower or equal
	 * to 0 will always return {@code false}.
	 * 
	 * @param percent the percentage chance of returning {@code true},
	 * for example, 50, will have a 50% chance of returning {@code true}.
	 * 
	 * @return Randomly {@code true} or {@code false}, depending on the
	 * specified <b>percent</b>.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static boolean tryChance(int percent) {
		if (percent >= 100)
			return true;
		if (percent <= 0)
			return false;
		return random().nextInt(1, 101) <= percent;
	}

	/**
	 * Randomly returns {@code true} with a set <b>percent</b>
	 * rate of success, numbers higher or equal to 100 will
	 * always return {@code true} while numbers lower or equal
	 * to 0 will always return {@code false}.
	 * <p>
	 * Decimal values such as 1.5, 0.1 or even 0.00001 are supported.
	 * 
	 * @param percent the percentage chance of returning {@code true},
	 * for example, 50.0, will have a 50% chance of returning {@code true}.
	 * 
	 * @return Randomly {@code true} or {@code false}, depending on the
	 * specified <b>percent</b>.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static boolean tryChance(float percent) {
		if (percent >= 100)
			return true;
		if (percent <= 0)
			return false;
		return random().nextFloat(0, 100) <= percent;
	}
}
