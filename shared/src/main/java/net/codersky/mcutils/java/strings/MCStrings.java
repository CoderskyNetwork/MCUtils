package net.codersky.mcutils.java.strings;

import net.codersky.mcutils.java.MCCollections;
import net.codersky.mcutils.java.strings.pattern.ColorPattern;
import net.codersky.mcutils.java.strings.pattern.color.GradientColorPattern;
import net.codersky.mcutils.java.strings.pattern.color.HexColorPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class MCStrings {

	/** The color character used for Minecraft color codes. */
	public static char COLOR_CHAR = '§';

	private static final List<ColorPattern> colorPatterns = new LinkedList<>();

	static {
		addColorPatterns(
				new GradientColorPattern(),
				new HexColorPattern(),
				(str, simple) -> applyColorChar('&', str));
	}

	/*
	 * Color patterns
	 */

	/**
	 * Applies all {@link #addColorPatterns(ColorPattern...) added}
	 * {@link ColorPattern color patterns} to the provided {@code string}
	 *
	 * @param string the {@link String} to apply colors to.
	 * @param simple whether to use simple mode or not, read
	 * {@link ColorPattern#applyColor(String, boolean)} for more information.
	 *
	 * @throws NullPointerException if {@code string} is {@code null}.
	 *
	 * @return A new {@link String} with all {@link ColorPattern color patterns}
	 * applied to it.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public static String applyColor(@NotNull String string, boolean simple) {
		String colored = Objects.requireNonNull(string, "The string to color cannot be null");
		for (ColorPattern pattern : colorPatterns)
			colored = pattern.applyColor(string, simple);
		return colored;
	}

	@NotNull
	public static String applyColor(@NotNull String str) {
		return applyColor(str, true);
	}

	@NotNull
	public static List<ColorPattern> getColorPatterns() {
		return colorPatterns;
	}

	@NotNull
	public static void addColorPatterns(@NotNull ColorPattern... patterns) {
		MCCollections.add(colorPatterns, patterns);
	}

	/*
	 * Color utility methods
	 */

	/**
	 * Replaces every occurrence of <b>ch</b> with {@link MCStrings#COLOR_CHAR} if
	 * followed by a valid color character ({@link #isColorChar(char)}).
	 *
	 * @param ch the character to replace, normally '&', as this is the standard.
	 * @param str the {@link CharSequence} to apply color characters to.
	 *
	 * @return A {@link String} from the specified {@link CharSequence} with translated color characters.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public static String applyColorChar(char ch, @NotNull CharSequence str) {
		final int length = str.length() - 1;
		final char[] arr = str.toString().toCharArray();
		for (int i = 0; i < length; i++)
			if (str.charAt(i) == ch && isColorChar(str.charAt(i + 1)))
				arr[i++] = COLOR_CHAR;
		return new String(arr);
	}

	/**
	 * Strips all <b>vanilla</b> chat formatting from the specified {@link CharSequence}.
	 * that is, color and text formatting, for example, assuming that
	 * {@code colorChar} is '&', this method will remove all occurrences
	 * of &[a-f], &[0-9], &[k-o] and &r, leaving the string as an uncolored,
	 * unformatted, simple string, doing the same with {@link MCStrings#COLOR_CHAR}
	 *
	 * @param sequence the {@link CharSequence} to strip colors from.
	 * @param colorChar an additional color character to use besides {@link MCStrings#COLOR_CHAR},
	 * generally '&'.
	 *
	 * @return A new {@code String} with all the contents of the specified char
	 * {@code sequence} except valid chat colors.
	 *
	 * @throws NullPointerException if {@code str} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	public static String stripColor(@NotNull CharSequence sequence, char colorChar) {
		if (sequence == null)
			return null;
		final int length = sequence.length();
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char ch = sequence.charAt(i);
			if ((ch == colorChar || ch == COLOR_CHAR) && (i + 1 < length) && isColorChar(sequence.charAt(i + 1)))
				i++;
			else
				result.append(ch);
		}
		return result.toString();
	}

	/**
	 * A simple convenience method that checks if {@code c} is a character
	 * that can be used to apply color or formatting to a string, that is,
	 * r, R, x, X, or a character between these ranges: [a-f], [A-F], [k-o], [K-O] and [0-9].
	 *
	 * @param c the character to check.
	 *
	 * @return {@code true} if the character is a color character, {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	public static boolean isColorChar(char c) {
		final char ch = Character.toLowerCase(c);
		return (ch == 'r' || ch == 'x' || (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'k' && ch <= 'o'));
	}

	/*
	 * General string methods
	 */

	/**
	 * Checks if a string has any content on it. If
	 * {@link String#isBlank()} returns {@code true} or the
	 * string is {@code null}, {@code false} will be returned.
	 *
	 * @param str the string to check.
	 *
	 * @return {@code true} if the string has content, {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	public static boolean hasContent(@Nullable String str) {
		return str != null && !str.isBlank();
	}




	// TODO Update all methods below this line.




	/**
	 * Checks if a {@link CharSequence} has any content on it. This will
	 * return {@code false} if the <b>seq</b>uence is {@code null}, empty
	 * or contains only {@link Character#isWhitespace(char) whitespace}
	 * characters, {@code true} otherwise.
	 *
	 * @param seq the {@link CharSequence} to check.
	 *
	 * @return {@code true} if the <b>seq</b>uence has content, {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	public static boolean hasContent(@Nullable CharSequence seq) {
		if (seq == null)
			return false;
		final int len = seq.length();
		for (int i = 0; i < len; i++)
			if (!Character.isWhitespace(seq.charAt(i)))
				return true;
		return false;
	}

	/*
	 * To string conversion
	 */

	/**
	 * Gets a {@link String} {@link Iterator list} as a {@link String} with all
	 * elements separated by the specified <b>separator</b>, null elements will be ignored.
	 * If <b>lst</b> is null, null will be returned, if <b>separator</b>
	 * is null, "null" will be used as a separator.
	 * <p>
	 * This method doesn't treat strings without content as elements, meaning
	 * no separator will be added to them (See {@link #hasContent(String)})
	 * <p>
	 * Example:
	 * <p>
	 * List: [" ", "a", null, "b", "c"]<br>
	 * Separator: ", "<br>
	 * Returns: " a, b, c"
	 *
	 * @param list the list to use.
	 * @param separator the separator to use.
	 *
	 * @return A {@link String} {@link Iterator list} as a {@link String} with all elements
	 * separated by the specified <b>separator</b>.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static String asString(@Nullable Iterable<String> list, @Nullable CharSequence separator) {
		final StringBuilder builder = new StringBuilder();
		if (list == null)
			return null;
		for (String str : list)
			if (hasContent(str))
				(builder.isEmpty() ? builder : builder.append(separator)).append(str);
		return builder.toString();
	}

	/**
	 * Gets a {@link String} array as a {@link String} with all elements separated by
	 * the specified <b>separator</b>, null elements will be ignored.
	 * If <b>lst</b> is null, null will be returned, if <b>separator</b>
	 * is null, "null" will be used as a separator.
	 * <p>
	 * This method doesn't treat strings without content as elements, meaning
	 * no separator will be added to them (See {@link #hasContent(String)})
	 * <p>
	 * Example:
	 * <p>
	 * List: [" ", "a", null, "b", "c"]<br>
	 * Separator: ", "<br>
	 * Returns: " a, b, c"
	 *
	 * @param array the array of {@link String strings} to use.
	 * @param separator the separator to use.
	 *
	 * @return A {@link String} array as a {@link String} with all elements separated by
	 * the specified <b>separator</b>.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static String asString(@Nullable String[] array, @Nullable CharSequence separator) {
		return asString(Arrays.asList(array), separator);
	}

	/*
	 * Substrings
	 */

	/**
	 * Gets a substring of <b>src</b>, starting at <b>from</b> and ending at <b>to</b>.
	 *
	 * @param src the source string to cut.
	 * @param from the string to match at the beginning of the new substring.
	 * @param to the string to match at the end of the new substring.
	 * @param inclusive if true, <b>from</b> and <b>to</b> will be included in the
	 * resulting substring, if false, they won't.
	 *
	 * @return A substring of <b>src</b> starting at <b>from</b> and ending at <b>to</b>,
	 * <code>null</code> if no match is found.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #substring(String, String, String)
	 */
	public static String substring(String src, String from, String to, boolean inclusive) {
		if (src == null || from == null || to == null)
			return src;
		final int start = src.indexOf(from);
		final int end = src.indexOf(to);
		if (start == -1 || end == -1)
			return null;
		return inclusive ? src.substring(start, end + to.length()) : src.substring(start + from.length(), end);
	}

	/**
	 * Gets a substring of <b>src</b>, starting at <b>from</b> and ending
	 * at <b>to</b>, both inclusive, meaning that
	 * <code>MCStrings.substring("From one to two", "one", "two")</code>
	 * will return "one to two", if any parameter is <code>null</code>, <b>src</b> will
	 * be returned, if no substring is found, <code>null</code> will be returned.
	 *
	 * @param src the source string to cut.
	 * @param from the string to match at the beginning of the new substring.
	 * @param to the string to match at the end of the new substring.
	 *
	 * @return A substring of <b>src</b> starting at <b>from</b> and ending at <b>to</b>,
	 * <code>null</code> if no match is found.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #substring(String, String, String, boolean)
	 */
	public static String substring(String src, String from, String to) {
		return substring(src, from, to, true);
	}

	/*
	 * Match
	 */

	/**
	 * This method is mostly designed for patterns. It will attempt to
	 * get a substring of <b>src</b> between <b>from</b> and <b>to</b>.
	 * If a substring is found, <b>action</b> will {@link Consumer#accept(Object) accept}
	 * it, removing said substring from the returning string only if <b>remove</b> is true.
	 * Let's see an example where "print" stands for System.out.println:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> print("Match: " + match), true);</code>
	 * <p>
	 * The output of this one line program will be "Match: this", the returning string of
	 * the method will be "Match ", if <b>remove</b> was false, the output would stay the
	 * same, but the returning string would be "Match (this)", without any change.
	 * as you can see, <b>from</b> and <b>to</b> will never be sent to the {@link Consumer}.
	 * <p>
	 * You can see an example of this method being used on MCUtils here:
	 * <ul>
	 * <li>{@link ActionBarTargetPattern#process(CommandSender, String)}</li>
	 * <li>{@link PlayerReceiverPattern#process(CommandSender, String)}</li>
	 * </ul>
	 *
	 * @param src the source string to use.
	 * @param from the String to match at the beginning of the pattern.
	 * @param to the String to match at the end of the pattern.
	 * @param action a {@link Consumer} that may accept any matching
	 * substrings of <b>src</b> between <b>from</b> and <b>to</b>.
	 * @param remove if true, the matching content will be removed
	 * from the resulting String, if false, the resulting string
	 * will be an exact copy of <b>src</b>.
	 *
	 * @return If <b>remove</b> is true, <b>src</b> with any match from
	 * the specified pattern removed from it, otherwise, a exact copy
	 * of <b>src</b>.
	 *
	 * @throws NullPointerException if any parameter is null.
	 *
	 * @see #substring(String, String, String)
	 * @see #match(String, String, String, Consumer)
	 */
	@Nonnull
	public static String match(@Nonnull String src, @Nonnull String from, @Nonnull String to, @Nonnull Consumer<String> action, boolean remove) {
		return match(src, from, to, remove ? match -> "" : match -> match);
	}

	/**
	 * This method is mostly designed for patterns. It will attempt to
	 * get a substring of <b>src</b> between <b>from</b> and <b>to</b>.
	 * If a substring is found, <b>action</b> will {@link Consumer#accept(Object) accept}
	 * it, removing said substring from the returning string.
	 * Let's see an example where "print" stands for System.out.println:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> print("Match: " + match));</code>
	 * <p>
	 * The output of this one line program will be "Match: this", the returning string of
	 * the method will be "Match ", as you can see, <b>from</b> and <b>to</b> will never
	 * be sent to the {@link Consumer}.
	 * <p>
	 * You can see an example of this method being used on MCUtils here:
	 * <ul>
	 * <li>{@link ActionBarTargetPattern#process(CommandSender, String)}</li>
	 * <li>{@link PlayerReceiverPattern#process(CommandSender, String)}</li>
	 * </ul>
	 *
	 * @param src the source string to use.
	 * @param from the String to match at the beginning of the pattern.
	 * @param to the String to match at the end of the pattern.
	 * @param action a {@link Consumer} that may accept any matching
	 * substrings of <b>src</b> between <b>from</b> and <b>to</b>.
	 *
	 * @return <b>src</b> with any match from the specified pattern removed from it.
	 *
	 * @throws NullPointerException if any parameter is null.
	 *
	 * @see #substring(String, String, String)
	 * @see #match(String, String, String, Consumer, boolean)
	 */
	public static String match(@Nonnull String src, @Nonnull String from, @Nonnull String to, @Nonnull Consumer<String> action) {
		return match(src, from, to, action, true);
	}

	/**
	 * This method can be used like a {@link Replacer} but it allows for more
	 * flexibility on what to do with the matching substrings. It will attempt to
	 * get any number of substrings of <b>src</b> between <b>from</b> and <b>to</b>.
	 * If a substring is found, <b>function</b> will {@link Function#apply(Object) apply}
	 * it, replacing the matched substring with the return value of <b>function</b>.
	 * Let's see an example:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> "done";</code>
	 * <p>
	 * The returning String of this will be "Match done". <b>from</b>
	 * and <b>to</b> aren't considered a part of <b>match</b> here.
	 *
	 * @param src the source string to use.
	 * @param from the String to match at the beginning of the pattern.
	 * @param to the String to match at the end of the pattern.
	 * @param function a {@link Function} that may accept any matching
	 * substrings of <b>src</b> between <b>from</b> and <b>to</b>, returning
	 * the String that will be used to replace the mathing substring.
	 *
	 * @return <b>src</b> with any match from the specified pattern removed from it.
	 *
	 * @throws NullPointerException if any parameter is null.
	 *
	 * @see #substring(String, String, String)
	 * @see #matchAndAccept(String, String, String, Consumer, boolean)
	 */
	public static String match(@Nonnull CharSequence src, @Nonnull String from, @Nonnull String to, @Nonnull Function<String, String> function) {
		final StringBuilder res = new StringBuilder(src);
		final int toLen = to.length();
		final int fromLen = from.length();
		int start = res.indexOf(from, 0);
		while (start != -1) {
			final int end = res.indexOf(to, start);
			if (end != -1)
				res.replace(start, end + toLen, function.apply(res.substring(start + fromLen, end)));
			start = res.indexOf(from, start + 1);
		}
		return res.toString();
	}

	/*
	 * Miscellaneous
	 */

	/**
	 * Converts a {@link String} to a {@link UUID} <b>the safe way</b>,
	 * trying to use {@link UUID#fromString(String)} and catching
	 * an {@link IllegalArgumentException} is <b>not</b> the safest approach, use
	 * this method for safe {@link String} to {@link UUID} conversion.
	 * <p>
	 * This method will also convert {@link UUID}s without '-' characters, this is
	 * because the Mojang's API may provide {@link UUID}s in said format.
	 * <p>
	 * Additionally, this method only allows full player {@link UUID}s, that is,
	 * strings which length is either 36 or 36. {@link UUID#fromString(String)}
	 * accepts, for example 1-1-1-1-1" as "00000001-0001-0001-0001-000000000001",
	 * this method doesn't, as it is designed for player {@link UUID}s.
	 *
	 * @param uuid the {@link String} to be converted to {@link UUID}
	 *
	 * @return A {@link UUID} by the specified {@code uuid} String,
	 * null if the string doesn't have a valid {@link UUID} format.
	 */
	@Nullable
	public static UUID toUUID(@Nullable String uuid) {
		final int len = uuid == null ? 0 : uuid.length();
		if (len == 36)
			return parseFullUUID(uuid);
		if (len == 32)
			return parseSmallUUID(uuid);
		return null;
	}

	private static UUID parseFullUUID(String uuid) {
		final char[] chars = uuid.toCharArray();
		for (int i = 0; i < 36; i++) {
			final char ch = chars[i];
			if (i == 8 || i == 13 || i == 18 || i == 23) {
				if (ch != '-')
					return null;
			} else if (!(ch >= '0' && ch <= '9') && !(ch >= 'a' && ch <= 'f') && !(ch >= 'A' && ch <= 'F'))
				return null;
		}
		return UUID.fromString(uuid);
	}

	private static UUID parseSmallUUID(String uuid) {
		final StringBuilder builder = new StringBuilder(36);
		final char[] chars = uuid.toCharArray();
		for (int i = 0, j = 0; i < 36; i++) {
			if (i == 8 || i == 13 || i == 18 || i == 23)
				builder.append('-');
			else {
				final char ch = chars[j];
				if (!(ch >= '0' && ch <= '9') && !(ch >= 'a' && ch <= 'f') && !(ch >= 'A' && ch <= 'F'))
					return null;
				builder.append(ch);
				j++;
			}
		}
		return UUID.fromString(builder.toString());
	}
}
