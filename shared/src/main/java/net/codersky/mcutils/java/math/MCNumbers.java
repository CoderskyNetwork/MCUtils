package net.codersky.mcutils.java.math;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
	@NotNull
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
	@NotNull
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
	@NotNull
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
	public static <N extends Number> N sum(@NotNull N x, @NotNull N y) {
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
	@NotNull
	public static <N extends Number> N subtract(@NotNull N x, @NotNull N y) {
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
	@NotNull
	public static <N extends Number> N multiply(@NotNull N x, @NotNull N y) {
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
	@NotNull
	public static <N extends Number> N divide(@NotNull N x, @NotNull N y) {
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

	/*
	 * Limits
	 */

	/**
	 * Limits <b>n</b> to a <b>min</b> and a <b>max</b> value.
	 * <p>
	 * For performance reasons this method doesn't check if <b>min</b>
	 * is actually smaller tan <b>max</b>. So using for example <b>min</b>
	 * with a value of 5, <b>max</b> with a value of 0 and <b>n</b>
	 * with a value of 3 will return 5, not 3.
	 * 
	 * @param n the {@code int} to limit.
	 * @param min the minimum value allowed for <b>n</b>
	 * @param max the maximum value allowed for <b>n</b>
	 * 
	 * @return <b>min</b> if <b>n</b> is smaller than <b>min</b>,
	 * <b>max</b> if <b>n</b> is bigger than <b>max</b>. <b>n</b>
	 * otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static int limit(int n, int min, int max) {
		if (n < min)
			return min;
		return n > max ? max : n;
	}

	/**
	 * Limits <b>n</b> to a <b>min</b> and a <b>max</b> value.
	 * <p>
	 * For performance reasons this method doesn't check if <b>min</b>
	 * is actually smaller tan <b>max</b>. So using for example <b>min</b>
	 * with a value of 5, <b>max</b> with a value of 0 and <b>n</b>
	 * with a value of 3 will return 5, not 3.
	 * 
	 * @param n the {@code long} to limit.
	 * @param min the minimum value allowed for <b>n</b>
	 * @param max the maximum value allowed for <b>n</b>
	 * 
	 * @return <b>min</b> if <b>n</b> is smaller than <b>min</b>,
	 * <b>max</b> if <b>n</b> is bigger than <b>max</b>. <b>n</b>
	 * otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static long limit(long n, long min, long max) {
		if (n < min)
			return min;
		return n > max ? max : n;
	}

	/**
	 * Limits <b>n</b> to a <b>min</b> and a <b>max</b> value.
	 * <p>
	 * For performance reasons this method doesn't check if <b>min</b>
	 * is actually smaller tan <b>max</b>. So using for example <b>min</b>
	 * with a value of 5, <b>max</b> with a value of 0 and <b>n</b>
	 * with a value of 3 will return 5, not 3.
	 * 
	 * @param n the {@code float} to limit.
	 * @param min the minimum value allowed for <b>n</b>
	 * @param max the maximum value allowed for <b>n</b>
	 * 
	 * @return <b>min</b> if <b>n</b> is smaller than <b>min</b>,
	 * <b>max</b> if <b>n</b> is bigger than <b>max</b>. <b>n</b>
	 * otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static float limit(float n, float min, float max) {
		if (n < min)
			return min;
		return n > max ? max : n;
	}

	/**
	 * Limits <b>n</b> to a <b>min</b> and a <b>max</b> value.
	 * <p>
	 * For performance reasons this method doesn't check if <b>min</b>
	 * is actually smaller tan <b>max</b>. So using for example <b>min</b>
	 * with a value of 5, <b>max</b> with a value of 0 and <b>n</b>
	 * with a value of 3 will return 5, not 3.
	 * 
	 * @param n the {@code double} to limit.
	 * @param min the minimum value allowed for <b>n</b>
	 * @param max the maximum value allowed for <b>n</b>
	 * 
	 * @return <b>min</b> if <b>n</b> is smaller than <b>min</b>,
	 * <b>max</b> if <b>n</b> is bigger than <b>max</b>. <b>n</b>
	 * otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static double limit(double n, double min, double max) {
		if (n < min)
			return min;
		return n > max ? max : n;
	}

	/*
	 * Numeric checkers
	 */

	/**
	 * Checks if <b>str</b> is numeric, meaning that it should
	 * only contain digits ({@link Character#isDigit(char)}),
	 * both integers and decimals will return true with this method,
	 * the first character of <b>str</b> can also optionally be a sign ('+' or '-').
	 * If <b>str</b> is null or has a length of zero, null will be returned.
	 *
	 * @param str the string to check.
	 *
	 * @return True if <b>str</b> is numeric, false otherwise.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #isInteger(CharSequence)
	 * @see #isDecimal(CharSequence)
	 */
	public static boolean isNumeric(@Nullable CharSequence str) {
		return isInteger(str) || isDecimal(str);
	}

	/**
	 * Checks if <b>str</b> is numeric, meaning that it should
	 * only contain digits ({@link Character#isDigit(char)}),
	 * the first character of <b>str</b> can also optionally be a sign ('+' or '-'),
	 * Decimal numbers will return false with this method.
	 * If <b>str</b> is null or has a length of zero, null will be returned.
	 *
	 * @param str the string to check.
	 *
	 * @return True if <b>str</b> is numeric, false otherwise.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #isNumeric(CharSequence)
	 * @see #isDecimal(CharSequence)
	 */
	public static boolean isInteger(@Nullable CharSequence str) {
		final int size = str == null ? 0 : str.length();
		if (size == 0)
			return false;
		final char sign = str.charAt(0);
		for (int i = (sign == '-' || sign == '+') ? 1 : 0; i < size; i++)
			if (!Character.isDigit(str.charAt(i)))
				return false;
		return true;
	}

	/**
	 * Checks if <b>str</b> is a decimal number, meaning that it should
	 * only contain digits ({@link Character#isDigit(char)}) and
	 * one decimal separator '.', the first character of
	 * <b>str</b> can also optionally be a sign ('+' or '-'),
	 * integers will return false with this method, only decimals return true.
	 * If <b>str</b> is null or has a length of zero, null will be returned.
	 *
	 * @param str the string to check.
	 *
	 * @return True if <b>str</b> is numeric, false otherwise.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #isNumeric(CharSequence)
	 * @see #isInteger(CharSequence)
	 */
	public static boolean isDecimal(@Nullable CharSequence str) {
		final int size = str == null ? 0 : str.length();
		if (size == 0)
			return false;
		final char sign = str.charAt(0);
		boolean decimal = false;
		for (int i = (sign == '-' || sign == '+') ? 1 : 0; i < size; i++) {
			final char ch = str.charAt(i);
			if (!Character.isDigit(ch)) {
				if (ch != '.' || decimal)
					return false;
				decimal = true;
			}
		}
		return decimal;
	}



	/*
	 * Number conversion
	 */

	/**
	 * Returns a {@link Number} of any (java.lang) number type
	 * with the value of <b>str</b>. That means that this method can return
	 * {@link Integer integers}, {@link Float floats}, {@link Double doubles},
	 * {@link Long longs}, {@link Short shorts} and {@link Byte bytes}, this
	 * method will use their parseX method, so for example for {@link Integer integers},
	 * {@link Integer#parseInt(String)} will be used, verifying whether <b>str</b>
	 * {@link #isNumeric(CharSequence)} or {@link #isInteger(CharSequence)} if necessary.
	 * If <b>str</b> can't be parsed for the desired numeric type for whatever reason,
	 * <b>def</b> will be returned. 
	 *
	 * @param <N> the type of {@link Number} to return
	 * @param str the {@link CharSequence} to convert.
	 * @param def the default value to return if <b>str</b> could not be parsed.
	 *
	 * @return Returns a {@link Number} of any (java.lang) number type
	 * with the value of <b>str</b>, <b>def</b> if <b>str</b> could not be parsed.
	 */
	@Nullable
	public static <N extends Number> N asNumber(@Nullable CharSequence str, @Nullable N def) {
		if (def == null)
			return null;
		@SuppressWarnings("unchecked")
		final N number = asNumber(str, (Class<N>) def.getClass());
		return number == null ? def : number;
	}

	/**
	 * Returns a {@link Number} of any (java.lang) number type
	 * with the value of <b>str</b>. That means that this method can return
	 * {@link Integer integers}, {@link Float floats}, {@link Double doubles},
	 * {@link Long longs}, {@link Short shorts} and {@link Byte bytes}, this
	 * method will use their parseX method, so for example for {@link Integer integers},
	 * {@link Integer#parseInt(String)} will be used, verifying whether <b>str</b>
	 * {@link #isNumeric(CharSequence)} or {@link #isInteger(CharSequence)} if necessary.
	 * If <b>str</b> can't be parsed for the desired numeric type for whatever reason,
	 * <code>null</code> will be returned. 
	 *
	 * @param <N> the type of {@link Number} to return
	 * @param str the {@link CharSequence} to convert.
	 * @param type the type of {@link Number} to return.
	 *
	 * @return Returns a {@link Number} of any (java.lang) number type
	 * with the value of <b>str</b>, <code>null</code> if <b>str</b> could not be parsed.
	 */
	@Nullable
	public static <N extends Number> N asNumber(@NotNull CharSequence str, @NotNull Class<N> type) {
		try {
			if (type.equals(Integer.class))
				return isInteger(str) ? type.cast(Integer.parseInt(str.toString())) : null;
			else if (type.equals(Float.class))
				return isNumeric(str) ? type.cast(Float.parseFloat(str.toString())) : null;
			else if (type.equals(Double.class))
				return isNumeric(str) ? type.cast(Double.parseDouble(str.toString())) : null;
			else if (type.equals(Long.class))
				return isInteger(str) ? type.cast(Long.parseLong(str.toString())) : null;
			else if (type.equals(Short.class))
				return isInteger(str) ? type.cast(Short.parseShort(str.toString())) : null;
			else if (type.equals(Byte.class))
				return isInteger(str) ? type.cast(Byte.parseByte(str.toString())) : null;
			return null;
		} catch (NumberFormatException outOfRange) {
			return null;
		}
	}

	/**
	 * Converts a {@link String} to a {@link Number} with numeric string format support.
	 * This means that for example "2k" can be converted to 2000. The exact characters
	 * used as <b>modifiers</b> can be customized. Assuming the <b>modifiers</b> list
	 * is set to ['k', 'm', 'b'], the 'k' character will have a multiplier of 1000 over
	 * the resulting number, while 'm' will have a multiplier of 1000000 and so on by
	 * adding 3 zeros to each element on the list. Results overflowing the specified
	 * <b>type</b> will return {@code null}.
	 *
	 * @param <N> the type of {@link Number} to return
	 *
	 * @param str the {@link String} to convert.
	 * @param type the type of {@link Number} to return.
	 * @param modifiers the modifier characters, in order. You can use the {@link Arrays#asList(Object...)}
	 * method for this parameter.
	 *
	 * @return The specified <b>str</b>ing converted to a {@link Number} of the desired <b>type</b> if the
	 * format was correct and the result didn't overflow the <b>type</b>. {@code null} otherwise.
	 *
	 * @throws NullPointerException if <b>type</b> is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #asNumber(CharSequence, Class)
	 */
	@Nullable
	public static <N extends Number> N asNumberFormat(@Nullable String str, @NotNull Class<N> type, @Nullable List<Character> modifiers) {
		int len = str == null ? 0 : str.length();
		if (len <= 1 || modifiers == null)
			return asNumber(str, type);
		int mod = modifiers.indexOf(Character.toLowerCase(str.charAt(len - 1))) + 1;
		if (mod == 0)
			return asNumber(str, type);
		mod *= 3;
		final String numStr = str.substring(0, len - 1);
		final int decIdx = numStr.indexOf('.');
		final StringBuilder result = new StringBuilder();
		len--;
		if (decIdx == -1) {
			for (int i = 0; i < mod + len; i++)
				result.append(i >= len ? '0' : numStr.charAt(i));
		} else {
			result.append(numStr.substring(0, decIdx));
			for (int i = decIdx + 1; i <= decIdx + mod; i++)
				result.append(i >= len ? '0' : numStr.charAt(i));
		}
		return asNumber(result, type);
	}

	/**
	 * Converts a {@link String} to a {@link Number} with numeric string format support.
	 * This means that for example "2k" can be converted to 2000. The exact characters
	 * used as <b>modifiers</b> can be customized. Assuming the <b>modifiers</b> list
	 * is set to ['k', 'm', 'b'], the 'k' character will have a multiplier of 1000 over
	 * the resulting number, while 'm' will have a multiplier of 1000000 and so on by
	 * adding 3 zeros to each element on the list. Results overflowing the class of
	 * <b>def</b> will return <b>def</b>.
	 *
	 * @param <N> the type of {@link Number} to return
	 *
	 * @param str the {@link String} to convert.
	 * @param def the default {@link Number} to return if anything goes wrong. This is also the parameter
	 * used to guess what {@link Number} class to use.
	 * @param modifiers the modifier characters, in order. You can use the {@link Arrays#asList(Object...)}
	 * method for this parameter.
	 *
	 * @return The specified <b>str</b>ing converted to a {@link Number} of the desired <b>type</b> if the
	 * format was correct and the result didn't overflow the <b>type</b>. {@code null} otherwise.
	 *
	 * @throws NullPointerException if <b>type</b> is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #asNumber(CharSequence, Number)
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <N extends Number> N asNumberFormat(@Nullable String str, @NotNull N def, @Nullable List<Character> modifiers) {
		final Number n = asNumberFormat(str, def.getClass(), modifiers);
		return n == null ? def : (N) n;
	}

	/**
	 * Converts a {@link String} to a {@link Number} with numeric string format support.
	 * This means that for example "2k" can be converted to 2000. The exact characters
	 * used as modifiers are set to ['k', 'm', 'b']. So the 'k' character will have a multiplier of 1000 over
	 * the resulting number, while 'm' will have a multiplier of 1000000 and so on by
	 * adding 3 zeros to each element on the list. Results overflowing the specified
	 * <b>type</b> will return {@code null}.
	 *
	 * @param <N> the type of {@link Number} to return
	 *
	 * @param str the {@link String} to convert.
	 * @param type the type of {@link Number} to return.
	 *
	 * @return The specified <b>str</b>ing converted to a {@link Number} of the desired <b>type</b> if the
	 * format was correct and the result didn't overflow the <b>type</b>. {@code null} otherwise.
	 *
	 * @throws NullPointerException if <b>def</b> is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #asNumber(CharSequence, Class)
	 * @see #asNumberFormat(String, Class, List)
	 */
	@Nullable
	public static <N extends Number> N asNumberFormat(@Nullable String str, @NotNull Class<N> type) {
		return asNumberFormat(str, type, Arrays.asList('k', 'm', 'b'));
	}

	/**
	 * Converts a {@link String} to a {@link Number} with numeric string format support.
	 * This means that for example "2k" can be converted to 2000. The exact characters
	 * used as modifiers are set to ['k', 'm', 'b']. So the 'k' character will have a multiplier of 1000 over
	 * the resulting number, while 'm' will have a multiplier of 1000000 and so on by
	 * adding 3 zeros to each element on the list. Results overflowing the class of
	 * <b>def</b> will return <b>def</b>.
	 *
	 * @param <N> the type of {@link Number} to return
	 *
	 * @param str the {@link String} to convert.
	 * @param def the default {@link Number} to return if anything goes wrong. This is also the parameter
	 * used to guess what {@link Number} class to use.
	 *
	 * @return The specified <b>str</b>ing converted to a {@link Number} of the type of <b>def</b> if the
	 * format was correct and the result didn't overflow said type. <b>def</b> otherwise.
	 *
	 * @throws NullPointerException if <b>def</b> is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #asNumber(CharSequence, Number)
	 * @see #asNumberFormat(String, Number, List)
	 */
	@Nullable
	public static <N extends Number> N asNumberFormat(@Nullable String str, @NotNull N def) {
		return asNumberFormat(str, def, Arrays.asList('k', 'm', 'b'));
	}

}
