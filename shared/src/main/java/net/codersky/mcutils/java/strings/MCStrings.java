package net.codersky.mcutils.java.strings;

import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.java.strings.pattern.ColorPattern;
import net.codersky.mcutils.java.strings.pattern.TargetPattern;
import net.codersky.mcutils.java.strings.pattern.color.GradientColorPattern;
import net.codersky.mcutils.java.strings.pattern.color.HexColorPattern;
import net.codersky.mcutils.java.strings.pattern.target.ActionBarTargetPattern;
import net.codersky.mcutils.java.strings.pattern.target.ConsoleTargetPattern;
import net.codersky.mcutils.java.strings.pattern.target.PlayerTargetPattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class MCStrings {

	/** The color character used for Minecraft color codes. */
	public static char COLOR_CHAR = '§';

	protected static List<ColorPattern> colorPatterns;
	protected static List<TargetPattern> targetPatterns;

	static {
		colorPatterns = List.of(
				new GradientColorPattern(),
				new HexColorPattern(),
				(str, simple) -> applyColorChar('&', str)
		);
		targetPatterns = List.of(
				new ActionBarTargetPattern(),
				new ConsoleTargetPattern(),
				new PlayerTargetPattern()
		);
	}

	/*
	 * Color patterns
	 */

	/**
	 * Applies all {@link ColorPattern color patterns} to the provided {@code string}
	 *
	 * @param str the {@link String} to apply colors to.
	 * @param simple whether to use simple mode or not, read
	 * {@link ColorPattern#applyColor(String, boolean)} for more information.
	 *
	 * @throws NullPointerException if {@code str} is {@code null}.
	 *
	 * @return A new {@link String} with all {@link ColorPattern color patterns}
	 * applied to it.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public static String applyColor(@NotNull String str, boolean simple) {
		String colored = Objects.requireNonNull(str, "The string to process cannot be null");
		for (ColorPattern pattern : colorPatterns)
			colored = pattern.applyColor(str, simple);
		return colored;
	}

	/**
	 * Applies all {@link ColorPattern color patterns} to the provided {@code string}
	 * <p>
	 * Simple mode is enabled on this method, read {@link ColorPattern#applyColor(String, boolean)}
	 * for more information.
	 *
	 * @param str the {@link String} to apply colors to.
	 *
	 * @throws NullPointerException if {@code str} is {@code null}.
	 *
	 * @return A new {@link String} with all {@link ColorPattern color patterns}
	 * applied to it.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public static String applyColor(@NotNull String str) {
		return applyColor(str, true);
	}

	/*
	 * Color utilities
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
	 * @throws NullPointerException if {@code sequence} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public static String stripColor(@NotNull CharSequence sequence, char colorChar) {
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
	 * Target patterns
	 */

	/**
	 * Applies all {@link TargetPattern target patterns} to the provided {@code string}
	 *
	 * @param target The {@link MessageReceiver} that will receive any matching message.
	 * @param str the {@link String} to process.
	 * @param applyEventPatterns Whether to {@link MCStrings#applyEventPatterns(String) apply} event patterns
	 * to the content matched by {@link TargetPattern target patterns}. Details about this can be found
	 * {@link TargetPattern here}, under the "<b>ABOUT EVENT PATTERNS</b>" section.
	 *
	 * @throws NullPointerException if {@code string} is {@code null}.
	 *
	 * @return A new {@link String} with all {@link TargetPattern target patterns}
	 * removed from it.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public static String applyTargetPatterns(@NotNull MessageReceiver target, @NotNull String str, boolean applyEventPatterns) {
		String result = Objects.requireNonNull(str, "The string to process cannot be null");
		for (TargetPattern pattern : targetPatterns)
			result = pattern.process(target, result, applyEventPatterns);
		return result;
	}

	/*
	 * Event patterns
	 */

	/**
	 * Removes all event patterns from the provided {@code string}.
	 * This can be used to control user input when necessary, as event
	 * patterns can be used in malicious ways, such as making other
	 * users execute dangerous commands.
	 *
	 * @param string The {@link String} to remove event patterns from.
	 *
	 * @return A new {@link String} with all event patterns removed from it.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public static String stripEventPatterns(@NotNull String string) {
		final StringBuilder builder = new StringBuilder();
		searchEventPatterns(string,
				builder::append,
				(event, txt) -> builder.append(txt));
		return builder.toString();
	}

	/**
	 * Applies event patterns to the provided {@code string}.
	 *
	 * @param string The {@link String} to apply event patterns to.
	 *
	 * @return A {@link Component} with all event patterns applied to it.
	 * This {@link Component} can then be sent to any {@link MessageReceiver}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see MessageReceiver
	 */
	@NotNull
	public static Component applyEventPatterns(@NotNull String string) {
		final TextComponent.Builder builder = Component.text();
		searchEventPatterns(string,
				txt -> builder.append(Component.text(txt)),
				(event, txt) -> applyEvents(builder, event, txt));
		return builder.build();
	}

	private static void applyEvents(TextComponent.Builder builder, String eventData, String text) {
		final List<String> eventList = splitEvents(eventData);
		final int safeLen = eventList.size() - 1;
		final Component toAppend = Component.text(text);
		for (int i = 0; i < safeLen; i += 2) {
			final String content = eventList.get(i + 1);
			switch (eventList.get(i).toLowerCase()) {
				case "text", "show_text" -> toAppend.hoverEvent(HoverEvent.showText(Component.text(content)));
				case "url", "open_url" -> toAppend.clickEvent(ClickEvent.openUrl(content));
				case "file", "open_file" -> toAppend.clickEvent(ClickEvent.openFile(content));
				case "run", "run_cmd", "run_command" -> toAppend.clickEvent(ClickEvent.runCommand(content));
				case "suggest", "suggest_cmd", "suggest_command" -> toAppend.clickEvent(ClickEvent.suggestCommand(content));
				case "copy", "copy_to_clipboard" -> toAppend.clickEvent(ClickEvent.copyToClipboard(content));
			};
		}
		builder.append(toAppend);
	}

	// Utility method to split events ignoring string literals, for example
	// text;"x;y;z" will be split as ["text", "x;y;z"]
	private static List<String> splitEvents(String eventData) {
		final List<String> eventList = new ArrayList<>();
		boolean literal = false;
		StringBuilder current = new StringBuilder();
		for (int i = 0; i < eventData.length(); i++) {
			final char ch = eventData.charAt(i);
			if (ch == '"')
				literal = !literal;
			else if (ch == ';' && !literal) {
				eventList.add(current.toString());
				current = new StringBuilder();
			} else
				current.append(ch);
		}
		if (!current.isEmpty())
			eventList.add(current.toString());
		return eventList;
	}

	// Search utility //

	private static void searchEventPatterns(String str, Consumer<String> append, BiConsumer<String, String> replace) {
		final int first = str.indexOf('<');
		if (first == -1) {
			append.accept(str);
			return;
		}
		int lastAppend = 0;
		for (int start = first; start != -1; start = str.indexOf('<', start + 1)) {
			final int eventEnd = str.indexOf('>', start);
			if (eventEnd == -1)
				continue;
			final int textEnd = str.indexOf("\\>", eventEnd);
			if (textEnd == -1)
				continue;
			append.accept(str.substring(lastAppend, start));
			replace.accept(str.substring(start + 1, eventEnd), str.substring(eventEnd + 1, textEnd));
			lastAppend = textEnd + 2;
		}
		if (lastAppend != str.length())
			append.accept(str.substring(lastAppend));
	}

	/*
	 * General string methods
	 */

	/**
	 * Checks if a {@link CharSequence} has any content on it. This will
	 * return {@code false} if {@code seq} is {@code null}, empty
	 * or contains only {@link Character#isWhitespace(char) whitespace}
	 * characters, {@code true} otherwise.
	 *
	 * @param seq the {@link CharSequence} to check.
	 *
	 * @return {@code true} if {@code seq} has content, {@code false} otherwise.
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
	 * Gets a {@link CharSequence} {@link Iterable} as a {@link String} with all
	 * elements separated by the specified {@code separator}, Elements that
	 * don't match the specified {@code filter} will be ignored, meaning that they won't
	 * be included on the resulting {@code String}.
	 * <p>
	 * Example:
	 * <p>
	 * Iterable: [" ", "a", null, "b", "c"]<br>
	 * Separator: ", "<br>
	 * Filter: {@link #hasContent(CharSequence)}<br>
	 * Returns: " a, b, c"
	 *
	 * @param iterable the {@link Iterable} to use, for arrays, just use {@link List#of(Object[])}.
	 * @param separator the separator to use, if {@code null} is used then <i>null</i> will be
	 * 	 * used as a separator as specified by {@link StringBuilder#append(CharSequence)}.
	 * @param filter the {@link Predicate} to use in order to filter {@link CharSequence CharSequences}.
	 * using a {@code null} filter means that every {@link CharSequence} present on the {@code iterator}
	 * will be included. If the {@link Predicate} returns {@code true} for a {@link CharSequence},
	 * said {@link CharSequence} will be included, if {@code false} is returned, it will be ignored.
	 *
	 * @return A {@link CharSequence} {@link Iterator} as a {@link String} with all elements that match
	 * {@code filter} separated by {@code separator}.
	 *
	 * @throws NullPointerException if {@code iterable} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #asString(Iterable, CharSequence)
	 */
	@NotNull
	public static String asString(@NotNull Iterable<CharSequence> iterable, @Nullable CharSequence separator, @Nullable Predicate<CharSequence> filter) {
		final StringBuilder builder = new StringBuilder();
		for (CharSequence seq : iterable)
			if (filter == null || filter.test(seq))
				(builder.isEmpty() ? builder : builder.append(separator)).append(seq);
		return builder.toString();
	}

	/**
	 * Gets a {@link CharSequence} {@link Iterable} as a {@link String} with all
	 * elements separated by the specified {@code separator}, Elements that
	 * don't have content (See {@link #hasContent(CharSequence)}) will be ignored,
	 * if you want to specify your own filter, use {@link #asString(Iterable, CharSequence, Predicate)}.
	 * <p>
	 * Example:
	 * <p>
	 * Iterable: [" ", "a", null, "b", "c"]<br>
	 * Separator: ", "<br>
	 * Returns: " a, b, c"
	 *
	 * @param iterable the {@link Iterable} to use, for arrays, just use {@link List#of(Object[])}.
	 * @param separator the separator to use, if {@code null} is used then <i>null</i> will be
	 * used as a separator as specified by {@link StringBuilder#append(CharSequence)}.
	 *
	 * @return A {@link CharSequence} {@link Iterator} as a {@link String} with all elements that
	 * have content (See {@link #hasContent(CharSequence)}) separated by {@code separator}.
	 *
	 * @throws NullPointerException if {@code iterable} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #asString(Iterable, CharSequence)
	 */
	@NotNull
	public static String asString(@NotNull Iterable<CharSequence> iterable, @Nullable CharSequence separator) {
		return asString(iterable, separator, MCStrings::hasContent);
	}

	/*
	 * CharSequence utility
	 */

	/**
	 * Gets the index of {@code toFind} on {@code seq}, starting to search at {@code beginIndex}.
	 * In other words, this method will search for the first occurrence of {@code toFind} inside of
	 * {@code seq} starting at {@code beginIndex}, then return the index at which {@code toFind} <b>starts</b>.
	 *
	 * @param seq the {@link CharSequence} to search on.
	 * @param toFind the {@link CharSequence} to find inside {@code seq}.
	 * @param beginIndex the index of {@code seq} to start searching from, negative values
	 * will make this method <b>always</b> return {@code -1}.
	 *
	 * @return the index of {@code seq} at which {@code toFind} is located, if found. {@code -1} if
	 * {@code toFind} could not be found inside {@code seq}. Another instance where this method returns
	 * {@code -1} is when {@code beginIndex} is negative or higher than the {@link CharSequence#length()
	 * length} of {@code seq}. If {@code toFind}'s {@link CharSequence#length() length} is {@code 0},
	 * then {@code 0} will be returned as an empty string is considered to always match.
	 *
	 * @throws NullPointerException if {@code seq} or {@code toFind} are {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	public static int indexOf(@NotNull CharSequence seq, @NotNull CharSequence toFind, int beginIndex) {
		final int seqLen = seq.length();
		final int toFindLen = toFind.length();
		if (beginIndex < 0)
			return -1;
		if (toFindLen == 0)
			return 0;
		for (int i = beginIndex; i < seqLen; i++) {
			for (int j = 0; j < toFindLen && i + j < seqLen; j++) {
				if (seq.charAt(i + j) != toFind.charAt(j))
					break;
				if (toFindLen == j + 1)
					return i;
			}
		}
		return -1;
	}

	public static int indexOf(@NotNull CharSequence seq, @NotNull CharSequence toFind) {
		return indexOf(seq, toFind, 0);
	}

	/*
	 * Substrings
	 */

	/**
	 * Gets a substring of {@code src}, starting at {@code from} and ending at {@code to}.
	 *
	 * @param src the source {@link String} to cut.
	 * @param from the {@link String} to match at the beginning of the new substring.
	 * @param to the {@link String} to match at the end of the new substring.
	 * @param inclusive if {@code true}, {@code from} and {@code to} will be included in the
	 * resulting substring, if {@code false}, they won't.
	 *
	 * @return A substring of {@code src} starting at {@code from} and ending at {@code to},
	 * {@code null} if no match is found. Keep in mind that both {@code from} and {@code to}
	 * need to be found in order to return a substring.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #substring(String, CharSequence, CharSequence)
	 */
	public static String substring(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to, boolean inclusive) {
		final int start = indexOf(src, from, 0);
		final int end = indexOf(src, to, start + 1);
		if (start == -1 || end == -1)
			return null;
		return inclusive ? src.substring(start, end + to.length()) : src.substring(start + from.length(), end);
	}

	/**
	 * Gets a substring of {@code src}, starting at {@code from} and ending at {@code to}.
	 * This is equivalent to calling {@link #substring(String, CharSequence, CharSequence, boolean)}
	 * with {@code inclusive} set to {@code true}, meaning that both {@code from} and {@code to} will
	 * be included in the resulting substring.
	 *
	 * @param src the source {@link String} to cut.
	 * @param from the {@link String} to match at the beginning of the new substring.
	 * @param to the {@link String} to match at the end of the new substring.
	 *
	 * @return A substring of {@code src} starting at {@code from} and ending at {@code to},
	 * {@code null} if no match is found. Keep in mind that both {@code from} and {@code to}
	 * need to be found in order to return a substring.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #substring(String, CharSequence, CharSequence, boolean)
	 */
	public static String substring(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to) {
		return substring(src, from, to, true);
	}

	/*
	 * Match
	 */

	/**
	 * This method will attempt to get any number of substrings of {@code src} between {@code from}
	 * and {@code to}. If a substring is found, {@code function} will be {@link Function#apply(Object) applied}
	 * on it, replacing the matched substring with the return value of {@code function}.
	 * Let's see an example:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> "done";</code>
	 * <p>
	 * The returning String of this will be "Match done". {@code from}
	 * and {@code to} won't be included on {@code match}, as only the content
	 * between {@code from} and {@code to} is considered to be relevant.
	 *
	 * @param src the source {@link String} to use.
	 * @param from the {@link CharSequence} to match at the beginning of the pattern.
	 * @param to the {@link CharSequence} to match at the end of the pattern.
	 * @param function a {@link Function} that may accept any matching
	 * substrings of {@code src} between {@code from} and {@code to}, returning
	 * the {@link String} that will be used to replace the matching substring.
	 *
	 * @return A clone of {@code src} with any match from the specified pattern replaced with
	 * the return value of the provided {@code function} only if the pattern is found.
	 * If not, {@code src} will be returned as is without creating a new {@link String}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #match(String, CharSequence, CharSequence, Consumer)
	 * @see #match(String, CharSequence, CharSequence, Consumer, boolean)
	 */
	@NotNull
	public static String match(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to, @NotNull Function<String, String> function) {
		int start = indexOf(src, from, 0);
		if (start == -1)
			return src;
		final StringBuilder res = new StringBuilder(src);
		final int toLen = to.length();
		final int fromLen = from.length();
		while (start != -1) {
			final int end = indexOf(res, to, start);
			if (end != -1)
				res.replace(start, end + toLen, function.apply(res.substring(start + fromLen, end)));
			start = indexOf(res, from, start + 1);
		}
		return res.toString();
	}

	/**
	 * This method will attempt to get any amount of substrings of {@code src} between {@code from} and {@code to}.
	 * If a substring is found, {@code action} will {@link Consumer#accept(Object) accept}
	 * it, removing said substring as well as {@code from} and {@code to} from the
	 * returning {@code String} only if {@code remove} is set to {@code true}.
	 * Let's see an example where {@code print} stands for {@code System.out.println}:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> print("Match: " + match), true);</code>
	 * <p>
	 * The output of this line will be "Match: this", the returning {@link String} of
	 * the method will be "Match ". If {@code remove} is set to {@code false}, the output will stay the
	 * same, but the returning string will be "Match (this)", without any change.
	 * As you can see, {@code from} and {@code to} will never be sent to the {@link Consumer}.
	 *
	 * @param src the source {@link CharSequence} to use.
	 * @param from the {@link CharSequence} to match at the beginning of the pattern.
	 * @param to the {@link CharSequence} to match at the end of the pattern.
	 * @param action a {@link Consumer} that may accept any matching
	 * substrings of {@code src} between {@code from} and {@code to}.
	 * @param remove if {@code true}, the matching content will be removed
	 * from the resulting {@link String}, if {@code false}, the resulting {@link String}
	 * will be an exact copy of {@code src}.
	 *
	 * @return A clone of {@code src} with any match from the specified pattern removed from it only if
	 * {@code remove} is set to {@code true}, if set to {@code false}, a clone of {@code src} will be returned.
	 * If the pattern isn't found, {@code src} will be returned as is without creating a new {@link String}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #match(String, CharSequence, CharSequence, Consumer)
	 * @see #match(String, CharSequence, CharSequence, Function)
	 */
	@NotNull
	public static String match(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to, @NotNull Consumer<String> action, boolean remove) {
		return match(src, from, to, remove ? match -> "" : match -> match);
	}

	/**
	 * This method will attempt to get any amount of substrings of {@code src} between {@code from} and {@code to}.
	 * If a substring is found, {@code action} will {@link Consumer#accept(Object) accept}
	 * it, removing said substring as well as {@code from} and {@code to} from the returning {@code String}.
	 * Let's see an example where {@code print} stands for {@code System.out.println}:
	 * <p>
	 * <code>match("Match (this)", "(", ")", match -> print("Match: " + match));</code>
	 * <p>
	 * The output of this line will be "Match: this", the returning {@link String} of
	 * the method will be "Match ". As you can see, {@code from} and {@code to} will
	 * never be sent to the {@link Consumer}.
	 *
	 * @param src the source {@link String} to use.
	 * @param from the {@link CharSequence} to match at the beginning of the pattern.
	 * @param to the {@link CharSequence} to match at the end of the pattern.
	 * @param action a {@link Consumer} that may accept any matching
	 * substrings of {@code src} between {@code from} and {@code to}.
	 *
	 * @return A clone of {@code src} with any match from the specified pattern removed from it.
	 * If the pattern isn't found, {@code src} will be returned as is without creating a new {@link String}.
	 *
	 * @throws NullPointerException if any parameter is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #match(String, CharSequence, CharSequence, Consumer, boolean)
	 * @see #match(String, CharSequence, CharSequence, Function)
	 */
	public static String match(@NotNull String src, @NotNull CharSequence from, @NotNull CharSequence to, @NotNull Consumer<String> action) {
		return match(src, from, to, action, true);
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
