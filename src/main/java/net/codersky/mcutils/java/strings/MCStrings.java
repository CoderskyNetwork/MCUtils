package net.codersky.mcutils.java.strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

import net.codersky.mcutils.java.MCLists;
import net.codersky.mcutils.java.strings.pattern.TargetPattern;
import net.codersky.mcutils.java.strings.pattern.color.GradientColorPattern;
import net.codersky.mcutils.java.strings.pattern.color.HexColorPattern;
import net.codersky.mcutils.java.strings.pattern.target.ActionBarTargetPattern;
import net.codersky.mcutils.java.strings.pattern.target.PlayerConsoleTargetPattern;
import net.codersky.mcutils.java.strings.replacers.Replacer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * A string utility class, also contains
 * methods for string lists.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #applyColor(String)
 */
public abstract class MCStrings {

	/** {@code static} instance of {@link GradientColorPattern}, used to apply gradients only. */
	public static final GradientColorPattern GRADIENT_COLOR_PATTERN = new GradientColorPattern();

	/** {@code static} instance of {@link HexColorPattern}, used to apply hexadecimal colors only. */
	public static final HexColorPattern HEX_COLOR_PATTERN = new HexColorPattern();

	protected static final LinkedList<TargetPattern> receiverPatterns = new LinkedList<>();

	static {
		receiverPatterns.add(new ActionBarTargetPattern());
		receiverPatterns.add(new PlayerConsoleTargetPattern());
	}

	/*
	 * Receiver patterns
	 */

	/**
	 * Sends <b>str</b> to <b>target</b> using the dynamic message format. This feature
	 * allows administrators to choose how and where a message will be sent, player specific
	 * message types such as {@link ActionBarTargetPattern} will be sent as a raw message to the console.
	 * <p>
	 * Here is the documentation of every format pattern:
	 * <ul>
	 * <li><a href=https://mcutils.codersky.net/for-server-admins/target-patterns>Target patterns</a>.</li>
	 * <li><a href=https://mcutils.codersky.net/for-server-admins/event-patterns>Event patterns</a>.</li>
	 * </ul>
	 * 
	 * @param target the {@link CommandSender} that will receive the message, if null, nothing will be done.
	 * @param str the message to send, if null or empty, nothing will be done.
	 * 
	 * @return Always true, to make sending messages on commands easier.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static boolean sendMessage(@Nullable CommandSender target, @Nullable String str) {
		if (target == null || str == null || str.isEmpty())
			return true;
		String toChat = str;
		for (TargetPattern pattern : receiverPatterns)
			toChat = pattern.process(target, toChat);
		return true;
	}

	/*
	 * Event patterns
	 */

	// Remove //

	/**
	 * Removes all <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from the specified {@link String}. This can be used to prevent users from using event patterns
	 * on unintended places, this is recommended to be used when getting user input that may be sent on
	 * a chat message later on. This method will just return {@code str} if the '<' character
	 * isn't found on {@code str}.
	 * <p>
	 * Strict mode is disabled on this method, see {@link #removeEventPatterns(String, boolean)}.
	 * 
	 * @param str the {@link String} to remove
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a> from.
	 * 
	 * @return The specified {@link String} with all
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a> removed from it.
	 * 
	 * @throws NullPointerException if {@code str} is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #removeEventPatterns(String, boolean)
	 */
	@Nonnull
	public static String removeEventPatterns(@Nonnull String str) {
		return removeEventPatterns(str, false);
	}

	/**
	 * Removes all <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * from the specified {@link String}. This can be used to prevent users from using event patterns
	 * on unintended places, this is recommended to be used when getting user input that may be sent on
	 * a chat message later on. This method will just return {@code str} if the '<' character
	 * isn't found on {@code str}.
	 * 
	 * @param str the {@link String} to remove
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a> from.
	 * @param strict whether to enable strict mode or not. Strict mode checks if valid event patterns
	 * are actually present on {@code str}, not using strict mode will skip this check so "&#60random text>text\>"
	 * would be replaced with "text"
	 * 
	 * @return The specified {@link String} with all
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a> removed from it.
	 * 
	 * @throws NullPointerException if {@code str} is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #removeEventPatterns(String)
	 */
	@Nonnull
	public static String removeEventPatterns(@Nonnull String str, boolean strict) {
		final StringBuilder builder = new StringBuilder();
		if (!strict)
			return searchEventPatterns(str, txt -> builder.append(txt), (event, txt) -> builder.append(txt)) ? builder.toString() : str;
		return searchEventPatterns(str, txt -> builder.append(txt), (event, txt) -> {
			final List<String> ids = Arrays.asList("url", "open_url", "file", "open_file", "run", "run_cmd",
					"run_command", "suggest", "suggest_cmd","suggest_command", "copy", "copy_to_clipboard");
			final List<String> eventList = splitEvents(event);
			final int safeLen = eventList.size() - 1;
			for (int i = 0; i < safeLen; i += 2) {
				if (ids.contains(eventList.get(i).toLowerCase())) {
					builder.append(txt);
					return;
				}
			}
			builder.append("<" + event + ">" + txt + "\\>");
		}) ? builder.toString() : str;
	}

	// Apply //

	/**
	 * Applies <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * to the specified {@link String}. Note that this method won't apply
	 * <a href=https://mcutils.codersky.net/for-server-admins/target-patterns>target patterns</a> as
	 * those patterns require a target to filter the messages, same happens with
	 * <a href=https://mcutils.codersky.net/for-server-admins/color-patterns>color patterns</a>, except
	 * this time it is because you may want to send messages without colors or you may already have
	 * {@link #applyColor(String) applied} color to the {@link String}.
	 * 
	 * @param str the {@link String} that will have the events applied, if the {@link String} doesn't
	 * contain the '<' character, the method will just convert the {@link String} to a {@link BaseComponent}
	 * array using {@link TextComponent#fromLegacyText(String)}.
	 * 
	 * @return A {@link BaseComponent} array with all found
	 * <a href=https://mcutils.codersky.net/for-server-admins/event-patterns>event patterns</a>
	 * applied to it.
	 * 
	 * @throws NullPointerException if {@code str} is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public static BaseComponent[] applyEventPatterns(@Nonnull String str) {
		final FormatRetention ret = FormatRetention.FORMATTING;
		final ComponentBuilder builder = new ComponentBuilder();
		return searchEventPatterns(str, txt -> builder.append(txt, ret), (event, txt) -> applyEvents(builder, event, txt)) ?
				builder.create() : TextComponent.fromLegacyText(str);
	}

	private static void applyEvents(ComponentBuilder builder, String eventData, String text) {
		final List<String> eventList = splitEvents(eventData);
		final int safeLen = eventList.size() - 1;
		builder.append(text, FormatRetention.FORMATTING);
		for (int i = 0; i < safeLen; i += 2) {
			switch (eventList.get(i).toLowerCase()) {
			case "text", "show_text" -> builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(eventList.get(i + 1))));
			case "url", "open_url" -> builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, eventList.get(i + 1)));
			case "file", "open_file" -> builder.event(new ClickEvent(ClickEvent.Action.OPEN_FILE, eventList.get(i + 1)));
			case "run", "run_cmd", "run_command" -> builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, eventList.get(i + 1)));
			case "suggest", "suggest_cmd", "suggest_command" -> builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, eventList.get(i + 1)));
			case "copy", "copy_to_clipboard" -> builder.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, eventList.get(i + 1)));
			};
		}
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

	private static boolean searchEventPatterns(String str, Consumer<String> append, BiConsumer<String, String> replace) {
		final int first = str.indexOf('<');
		if (first == -1)
			return false;
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
		return true;
	}

	/*
	 * String color methods
	 */

	/**
	 * Applies all color patterns to a <b>string</b>.
	 * MCUtils has an up to date <a href=https://mcutils.codersky.net/for-server-admins/color-patterns>documentation</a>
	 * about color patterns supported, please make sure that the version you are using corresponds with the
	 * mentioned minimum version of the patterns that you want to use, even though
	 * most patterns will be available since 1.0.0.
	 * 
	 * @param string the {@link String} to apply colors to.
	 * 
	 * @return The <b>string</b>, colored, {@code null} of the <b>string</b> itself was {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static String applyColor(@Nullable String string) {
		if (string == null)
			return null;
		String colored = applyColorChar('&', string);
		colored = GRADIENT_COLOR_PATTERN.process(colored, true);
		return HEX_COLOR_PATTERN.process(colored, true);
	}

	/**
	 * Applies colors to every string of <b>lst</b>
	 * using {@link #applyColor(String)}. If <b>lst</b>
	 * is {@code null}, {@code null} will be returned.
	 * {@code null} elements on the list will be kept as {@code null}.
	 * 
	 * @param lst the list to apply colors.
	 * 
	 * @return The list, colored, {@code null} if the list itself was {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static List<String> applyColor(@Nullable List<String> lst) {
		return lst == null ? null : MCLists.map(str -> applyColor(str), lst);
	}

	/**
	 * Similar to {@link ChatColor#translateAlternateColorCodes(char, String)},
	 * replaces every occurrence of <b>ch</b> with {@link ChatColor#COLOR_CHAR} if
	 * followed by a valid color character ({@link #isColorChar(char)}),
	 * performance differences aren't noticeable, this method exists for accessibility
	 * purposes, to support {@link CharSequence CharSequences}... And to have a shorter name.
	 * 
	 * @param ch the character to replace, normally '&'.
	 * @param str the {@link CharSequence} to apply color characters to.
	 * 
	 * @return A {@link String} from the specified {@link CharSequence} with translated color characters,
	 * {@code null} if <b>str</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static String applyColorChar(@Nonnull char ch, @Nullable CharSequence str) {
		if (str == null)
			return null;
		final int length = str.length() - 1;
		final char[] arr = str.toString().toCharArray();
		for (int i = 0; i < length; i++)
			if (str.charAt(i) == ch && isColorChar(str.charAt(i + 1)))
				arr[i++] = ChatColor.COLOR_CHAR;
		return new String(arr);
	}

	/**
	 * Strips all <b>vanilla</b> chat formatting from the specified {@link CharSequence}.
	 * that is, color and text formatting, for example, assuming that
	 * <b>colorChar</b> is '&', this method will remove all occurrences
	 * of &[a-f], &[0-9], &[k-o] and &r, leaving the string as an uncolored,
	 * unformatted, simple string, doing the same with {@link ChatColor#COLOR_CHAR}
	 * <p>
	 * This method has been tested to be quite faster than {@link ChatColor#stripColor(String)}
	 * as {@link ChatColor}'s method uses regex, however, this difference isn't
	 * really noticeable unless millions of strings are stripped.
	 * 
	 * @param str the string to strip colors.
	 * 
	 * @return The string with stripped colors, {@code null} if <b>str</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static String stripColor(CharSequence str, char colorChar) {
		if (str == null)
			return null;
		final int length = str.length();
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);
			if ((ch == colorChar || ch == ChatColor.COLOR_CHAR) && (i + 1 < length) && isColorChar(str.charAt(i + 1)))
				i++;
			else
				result.append(ch);
		}
		return result.toString();
	}

	/**
	 * A simple convenience method that checks if <b>ch</b> is a character
	 * that can be used to apply color or formatting to a string, that is,
	 * r, R, x, X, or a character between this ranges: [a-f], [A-F], [k-o], [K-O] and [0-9].
	 * 
	 * @param ch the character to check.
	 * 
	 * @return true if the character is a color character, false otherwise.
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
			return def;
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
	public static <N extends Number> N asNumber(@Nullable CharSequence str, @Nonnull Class<N> type) {
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
	public static <N extends Number> N asNumberFormat(@Nullable String str, @Nonnull Class<N> type, @Nullable List<Character> modifiers) {
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
	public static <N extends Number> N asNumberFormat(@Nullable String str, @Nonnull N def, @Nullable List<Character> modifiers) {
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
	public static <N extends Number> N asNumberFormat(@Nullable String str, @Nonnull Class<N> type) {
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
	public static <N extends Number> N asNumberFormat(@Nullable String str, @Nonnull N def) {
		return asNumberFormat(str, def, Arrays.asList('k', 'm', 'b'));
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
	 * @throws NulPointerException if any parameter is null.
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
	 * @throws NulPointerException if any parameter is null.
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
	 * @throws NulPointerException if any parameter is null.
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
