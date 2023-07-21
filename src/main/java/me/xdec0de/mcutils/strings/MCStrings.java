package me.xdec0de.mcutils.strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.xdec0de.mcutils.strings.builders.Click;
import me.xdec0de.mcutils.strings.builders.Hover;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
public class MCStrings {

	private static HashMap<String, ColorPattern> colorPatterns = new HashMap<>();
	private static LinkedList<FormatPattern> formatPatterns = new LinkedList<>();

	private static final Pattern actionPattern = Pattern.compile("<(.*?)>(.*?)[/]>");

	/**
	 * Sends <b>str</b> to <b>target</b> using the dynamic message format. This feature
	 * allows administrators to choose how and where a message will be sent, player specific
	 * message types such as {@link ActionBar} will be sent as a raw message to the console.
	 * <p>
	 * Here is the documentation of every format pattern:
	 * <p>
	 * - <a href=https://mcutils.xdec0de.me/chat-features/target-patterns>Target patterns</a>.
	 * <p>
	 * - <a href=https://mcutils.xdec0de.me/chat-features/event-patterns>Event patterns</a>.
	 * 
	 * @param target the {@link CommandSender} that will receive the message.
	 * @param str the message to send, if null or empty, nothing will be done.
	 * 
	 * @return Always true, to make sending messages on commands easier.
	 * 
	 * @throws IllegalArgumentException if <b>target</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static boolean sendFormattedMessage(@Nonnull CommandSender target, @Nullable String str) {
		if (target == null)
			throw new IllegalArgumentException("target cannot be null.");
		if (str == null || str.isEmpty())
			return true;
		String toChat = str;
		for (FormatPattern pattern : formatPatterns)
			toChat = pattern.process(target, toChat);
		if (toChat.isEmpty())
			return true;
		if (target instanceof Player)
			((Player)target).spigot().sendMessage(applyEventPatterns(toChat));
		else
			target.sendMessage(toChat);
		return true;
	}

	/**
	 * Applies <a href=https://mcutils.xdec0de.me/chat-features/event-patterns>Event patterns</a>
	 * to the specified string, note that this method won't apply the {@link TargetPattern} nor
	 * the {@link ActionBar} pattern as both patterns require a target to be used and send the
	 * message to said target when applied.
	 * 
	 * @param str the string that will have the events applied.
	 * 
	 * @return A {@link BaseComponent} array with the events applied to it,
	 * {@link TextComponent#toLegacyText()} will be used on <b>str</b> if no event was applied,
	 * null if <b>str</b> is null or empty.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static BaseComponent[] applyEventPatterns(@Nullable String str) {
		if (str == null || str.isEmpty())
			return null;
		// Group 1: event args, Group 2: event text.
		ComponentBuilder res = new ComponentBuilder();
		Matcher matcher = actionPattern.matcher(str);
		int prevEnd = 0;
		while (matcher.find()) {
			final String prev = str.substring(prevEnd, matcher.start());
			final String[] args = matcher.group(1).split(";");
			if (args.length == 0 || args.length % 2 != 0)
				return null;
			res.append(TextComponent.fromLegacyText(prev), FormatRetention.FORMATTING).append(getAppliedContent(matcher.group(2), args));
			prevEnd = matcher.end();
		}
		return prevEnd != 0 ? res.append(TextComponent.fromLegacyText(str.substring(prevEnd))).create() : TextComponent.fromLegacyText(str);
	}

	private static BaseComponent[] getAppliedContent(String text, String [] args) {
		BaseComponent[] content = TextComponent.fromLegacyText(text);
		for (int i = 0; i < args.length; i += 2) {
			switch(args[i].toLowerCase()) {
			case "text", "show_text" -> new Hover(HoverEvent.Action.SHOW_TEXT, args[i + 1]).apply(content);
			case "item", "show_item" -> new Hover(HoverEvent.Action.SHOW_ITEM, args[i + 1]).apply(content);
			case "entity", "show_entity" -> new Hover(HoverEvent.Action.SHOW_ENTITY, args[i + 1]).apply(content);
			case "url", "open_url" -> new Click(ClickEvent.Action.OPEN_URL, args[i + 1]).apply(content);
			case "file", "open_file" -> new Click(ClickEvent.Action.OPEN_FILE, args[i + 1]).apply(content);
			case "run", "run_cmd", "run_command" -> new Click(ClickEvent.Action.RUN_COMMAND, args[i + 1]).apply(content);
			case "suggest", "suggest_cmd", "suggest_command" -> new Click(ClickEvent.Action.SUGGEST_COMMAND, args[i + 1]).apply(content);
			case "copy", "copy_to_clipboard" -> new Click(ClickEvent.Action.COPY_TO_CLIPBOARD, args[i + 1]).apply(content);
			}
		}
		return content;
	}

	/**
	 * Gets a <b>previously registered</b> {@link FormatPattern} by class, that is,
	 * a {@link FormatPattern} that has been added using either {@link #addChatPattern(ColorPattern)}
	 * or {@link #addChatPatternBefore(FormatPattern, Class)}, if no {@link FormatPattern} matching
	 * <b>pattern</b> has been added, null will be returned.
	 * 
	 * @param <T> must implement {@link FormatPattern}.
	 * @param pattern the class of the {@link FormatPattern} to return.
	 * 
	 * @throws IllegalArgumentException If <b>pattern</b> is null.
	 * 
	 * @return an instance of <b>pattern</b> if registered, null otherwise.
	 * 
	 * @see #addChatPattern(FormatPattern)
	 */
	public static <T extends FormatPattern> FormatPattern getFormatPattern(@Nonnull Class<T> pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Pattern cannot be null");
		for (FormatPattern implPattern : formatPatterns)
			if (implPattern.getClass().equals(pattern))
				return implPattern;
		return null;
	}

	/**
	 * Adds a new {@link FormatPattern} to be used on {@link #sendFormattedMessage(CommandSender, String)},
	 * it's important to take into account pattern order as patterns might conflict with one another.
	 * <p>
	 * Want to add a {@link FormatPattern} that would be overwritten by a MCUtils
	 * {@link FormatPattern}? Use {@link #addChatPatternBefore(FormatPattern, Class)}
	 * 
	 * @param pattern the {@link FormatPattern} to add.
	 * 
	 * @throws IllegalArgumentException If <b>pattern</b> is null.
	 * 
	 * @see #addChatPatternBefore(ColorPattern, Class)
	 */
	public static void addFormatPattern(@Nonnull FormatPattern pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Pattern cannot be null");
		formatPatterns.add(pattern);
	}

	/**
	 * Adds a new {@link FormatPattern} to be used on {@link #applyColor(String)}
	 * and {@link #applyColor(List)} before the specified <b>pattern</b>, it's
	 * important to take into account pattern order as patterns might conflict with
	 * one another. For example the {@link Hex} pattern directly conflicts with the
	 * {@link Gradient} pattern overwriting it because {@link Gradient} uses
	 * hexadecimal colors, so {@link Gradient} needs to be added before {@link Hex}.
	 * <p>
	 * Normally, this method won't be needed unless you are adding a pattern that uses
	 * hexadecimal colors on it's syntax as {@link Gradient} does. If that's not the case,
	 * you can just use {@link #addColorPattern(ColorPattern)} to improve performance a bit.
	 * 
	 * @param <T> must implement {@link ColorPattern}
	 * @param pattern the {@link ColorPattern} to add.
	 * @param before the {@link Class} of the {@link ColorPattern} that will
	 * go after <b>pattern</b>, putting <b>pattern</b> before it.
	 * 
	 * @throws IllegalArgumentException If <b>pattern</b> or <b>before</b> are null.
	 * 
	 * @see #addChatPattern(ColorPattern)
	 */
	public static <T extends FormatPattern> void addFormatPatternBefore(@Nonnull FormatPattern pattern, @Nonnull Class<T> before) {
		if (pattern == null)
			throw new IllegalArgumentException("Added pattern cannot be null");
		if (before == null)
			throw new IllegalArgumentException("Before pattern class cannot be null");
		final LinkedList<FormatPattern> tempPatterns = new LinkedList<>();
		boolean added = false;
		for (FormatPattern implPattern : formatPatterns) {
			if (implPattern.getClass().equals(before))
				added = tempPatterns.add(pattern); // Always true as the Collection changes.
			tempPatterns.add(implPattern);
		}
		if (!added)
			tempPatterns.add(pattern);
		formatPatterns = tempPatterns;
	}

	/*
	 * Chat color methods
	 */

	/**
	 * Gets a <b>previously registered</b> {@link ColorPattern} by <b>id</b>, that is,
	 * a {@link ColorPattern} that has been added using either {@link #addColorPattern(ColorPattern)}
	 * or {@link #addColorPatternBefore(ColorPattern, Class)}, if no {@link ColorPattern} matching
	 * <b>pattern</b> has been added, null will be returned.
	 * 
	 * @param id the id of the registered {@link ColorPattern} to get, these are always lower case and
	 * this method will convert this parameter to lower case.
	 * 
	 * @throws IllegalArgumentException If <b>id</b> is null or blank.
	 * 
	 * @return An instance of a {@link ColorPattern} with the specified <b>id</b> if registered, null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static ColorPattern getColorPattern(@Nonnull String id) {
		if (!hasContent(id))
			throw new IllegalArgumentException("Pattern id cannot be null or blank");
		return colorPatterns.get(id);
	}

	/**
	 * Adds a new {@link ColorPattern} to be used on {@link #applyColor(String)}
	 * and {@link #applyColor(List)}, note that if a pattern with the specified <b>id</b>
	 * is already registered, it will be replaced. It's important to take into account pattern
	 * order as patterns might conflict with one another. For example the {@link Hex}
	 * pattern directly conflicts with the {@link Gradient} pattern overwriting it 
	 * because {@link Gradient} uses hexadecimal colors, so {@link Gradient} needs to
	 * be added first as it doesn't overwrite {@link Hex}.
	 * <p>
	 * Want to add a {@link ColorPattern} that would be overwritten by a MCUtils
	 * {@link ColorPattern} like {@link Hex}? Use {@link #addColorPatternBefore(ColorPattern, Class)}
	 * 
	 * @param id the id of the {@link ColorPattern} to register, these are always lower case and
	 * this method will convert this parameter to lower case.
	 * @param pattern the {@link ColorPattern} to register.
	 * 
	 * @throws IllegalArgumentException If <b>pattern</b> is null.
	 * 
	 * @see #addColorPatternBefore(ColorPattern, Class)
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static void addColorPattern(@Nonnull String id, @Nonnull ColorPattern pattern) {
		if (!hasContent(id))
			throw new IllegalArgumentException("Pattern id cannot be null or blank");
		if (pattern == null)
			throw new IllegalArgumentException("Pattern cannot be null");
		colorPatterns.put(id.toLowerCase(), pattern);
	}

	/**
	 * Adds a new {@link ColorPattern} to be used on {@link #applyColor(String)}
	 * and {@link #applyColor(List)} before the specified <b>pattern</b>, note that if
	 * a pattern with the specified <b>id</b> is already registered, it will be replaced. 
	 * It's important to take into account pattern order as patterns might conflict with
	 * one another. For example the {@link Hex} pattern directly conflicts with the
	 * {@link Gradient} pattern overwriting it because {@link Gradient} uses
	 * hexadecimal colors, so {@link Gradient} needs to be added before {@link Hex}.
	 * <p>
	 * Normally, this method won't be needed unless you are adding a pattern that uses
	 * hexadecimal colors on it's syntax as {@link Gradient} does. If that's not the case,
	 * you can just use {@link #addColorPattern(ColorPattern)} to improve performance a bit.
	 * 
	 * @param id the id of the {@link ColorPattern} to register, these are always lower case and
	 * this method will convert this parameter to lower case.
	 * @param pattern the {@link ColorPattern} to register.
	 * @param beforeId the id of the {@link ColorPattern} that will go after <b>pattern</b>. 
	 * Putting <b>pattern</b> before it.
	 * 
	 * @throws IllegalArgumentException If <b>pattern</b> or <b>before</b> are null.
	 * 
	 * @see #addColorPattern(ColorPattern)
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static void addColorPatternBefore(@Nonnull String id, @Nonnull ColorPattern pattern, @Nonnull String beforeId) {
		if (!hasContent(id))
			throw new IllegalArgumentException("Pattern id cannot be null or blank");
		if (pattern == null)
			throw new IllegalArgumentException("Added pattern cannot be null");
		if (!hasContent(beforeId))
			throw new IllegalArgumentException("Before pattern id cannot be null or blank");
		final HashMap<String, ColorPattern> tempPatterns = new HashMap<>();
		boolean added = false;
		for (String implId : colorPatterns.keySet()) {
			if (implId.equals(beforeId)) {
				tempPatterns.put(id.toLowerCase(), pattern);
				added = true;
			}
			tempPatterns.put(implId, colorPatterns.get(implId));
		}
		if (!added)
			tempPatterns.put(id, pattern);
		colorPatterns = tempPatterns;
	}

	/**
	 * Applies all registered patterns to a <b>string</b>.
	 * MCUtils registers "classic", "{@link Hex hex}" and 
	 * "{@link Gradient gradient}" by default, but
	 * any pattern can be disabled by the user on MCUtil's config.yml.
	 * <p>
	 * The "classic" color pattern is just {@link #applyColorChar(char, String)}
	 * with the famous '&' character.
	 * 
	 * @param string the string to apply colors.
	 * @return The string, colored.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static String applyColor(@Nullable String string) {
		if (string == null)
			return null;
		String res = string;
		for (ColorPattern pattern : colorPatterns.values())
			res = pattern.process(res, true);
		return res;
	}

	/**
	 * Applies colors to every string of <b>lst</b>
	 * using {@link #applyColor(String)}. If <b>lst</b>
	 * is null, null will be returned.
	 * null elements on the list will be kept as null.
	 * 
	 * @param lst the list to apply colors.
	 * 
	 * @return The list, colored.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static List<String> applyColor(@Nullable List<String> lst) {
		if (lst == null)
			return null;
		List<String> res = new ArrayList<>(lst.size());
		for (String str : lst)
			res.add(applyColor(str));
		return res;
	}

	/**
	 * Similar to {@link ChatColor#translateAlternateColorCodes(char, String)},
	 * replaces every occurrence of <b>ch</b> with {@link ChatColor#COLOR_CHAR} if
	 * followed by a valid color character ({@link #isColorChar(char)}),
	 * performance differences aren't noticeable, this method exists for accessibility
	 * purposes... And to have a shorter name.
	 * 
	 * @param ch the character to replace, normally '&'.
	 * @param str the string to apply color characters to.
	 * 
	 * @return The string with translated color characters, null if <b>str</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static String applyColorChar(@Nonnull char ch, @Nullable String str) {
		if (str == null)
			return null;
		final int length = str.length() - 1;
		char[] arr = str.toCharArray();
		for (int i = 0; i < length; i++) {
			char strCh = str.charAt(i);
			if (strCh == ch && isColorChar(str.charAt(i + 1)))
				arr[i++] = ChatColor.COLOR_CHAR;
		}
		return new String(arr);
	}

	/**
	 * Strips all vanilla chat formatting from the specified string,
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
	 * @return The string with stripped colors, null if <b>str</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static String stripColor(String str, char colorChar) {
		if (str == null)
			return null;
		final int length = str.length();
		StringBuilder result = new StringBuilder();
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
	 * {@link String#isBlank()} returns true or the
	 * string is null, false will be returned.
	 * 
	 * @param str the string to check.
	 * 
	 * @return true if the string has content, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static boolean hasContent(@Nullable String str) {
		return str == null ? false : (!str.isBlank());
	}

	/**
	 * Gets a string list as a string with all elements separated by
	 * the specified <b>separator</b>, null elements will be ignored.
	 * If <b>lst</b> is null, null will be returned, if <b>separator</b>
	 * is null, "null" will be used as a separator.
	 * <p>
	 * This method doesn't treat strings without content as elements, meaning
	 * no separator will be added to them (See {@link #hasContent(String)})
	 * <p>
	 * Example:
	 * <p>
	 * Array: [" ", "a", null, "b", "c"]<br>
	 * Separator: ", "<br>
	 * Returns: " a, b, c"
	 * 
	 * @param lst the list to use.
	 * @param separator the separator to use.
	 * 
	 * @return A string list as a string with all elements separated by
	 * the specified <b>separator</b>.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public static String asString(@Nullable List<String> lst, @Nullable String separator) {
		String res = "";
		if (lst == null)
			return null;
		for (String str : lst) {
			if (str == null)
				continue;
			res += hasContent(res) ? separator+str : str;
		}
		return res;
	}

	/**
	 * Check if <b>str</b> is numeric, meaning that it should
	 * only contain numeric characters between '0' and '9', the
	 * first character of the string can be a sign ('+' or '-').
	 * If {@link #hasContent(String)} returns false, this method
	 * will also return false.
	 * 
	 * @param str the string to check.
	 * 
	 * @return True if <b>str</b> is numeric, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public static boolean isNumeric(String str) {
		if (!hasContent(str))
			return false;
		final char sign = str.charAt(0);
		for (int i = (sign == '-' || sign == '+') ? 1 : 0; i < str.length(); i++) {
			final char ch = str.charAt(i);
			if (ch < '0' || ch > '9')
				return false;
		}
		return true;
	}

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
}
