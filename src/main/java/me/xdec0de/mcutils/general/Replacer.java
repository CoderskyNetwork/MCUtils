package me.xdec0de.mcutils.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import me.xdec0de.mcutils.strings.MCStrings;

/**
 * Represents a replacer to replace parts of a string with other objects, if you want to use the same replacements for multiple strings, you should 
 * create a replacer variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also,
 * make sure that the amount of strings added to the replacer are <b>even</b>, otherwise, an {@link IllegalArgumentException} will be thrown.
 * <p>
 * <b>Numeric support:</b>
 * <br>
 * This feature is always enabled with Replacers and adds the possibility to apply different replacements
 * depending on numeric values, don't worry about performance, the impact isn't noticeable unless you are
 * parsing millions of strings as this doesn't use regular expressions. Here is a common example on how to use it:
 * <p>
 * <code>new Replacer("%points%", 10).replaceAt("You have %points% <%points%:point:points>"</code>
 * <p>
 * With this example, the returning string will be "<b>You have 10 points</b>".
 * If instead of 10 we had 1 (or -1) point, the result would be "<b>You have 1 point</b>",
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #Replacer(Object...)
 */
public class Replacer {

	private final ArrayList<Object> replaceList = new ArrayList<>();

	/**
	 * Creates a replacer to replace parts of a string with other strings, if you want to use the same replacements for multiple strings, you should 
	 * create a {@link Replacer} variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also, 
	 * make sure that the amount of strings added to the {@link Replacer} are <b>even</b>, otherwise, an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param replacements the new strings to be replaced, the format is <i>"str1", "obj1", "str2", "obj2"...</i>
	 * 
	 * @throws IllegalArgumentException if <b>replacements</b> is null or the amount of strings is not even, more technically, if replacements
	 * size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #add(Replacer)
	 * @see #add(String...)
	 * @see #replaceAt(String)
	 * @see #replaceAt(List)
	 */
	public Replacer(Object... replacements) {
		if (replacements == null)
			throw new IllegalArgumentException("Replacements cannot be null.");
		for (Object rep : replacements)
			replaceList.add(rep);
		if (replaceList.size() % 2 != 0)
			throw new IllegalArgumentException(replaceList.get(replaceList.size() - 1) + "does not have a replacer! Add one more element to the replacer.");
	}

	/**
	 * Adds new replacements to an existing replacer, the amount of replacements must also be even, note that existing replacements
	 * will be added to the list but the new replacer won't overwrite them. Because of the way replacements work, only the first
	 * replacement added for a string will take effect if there is another replacement added to said string later on.
	 * If <b>replacements</b> is null, nothing will be done.<br><br>
	 * 
	 * Example: text is <i>"Replace %test%"</i>, we add <i>"%test%", "Hello"</i> and <i>"%test%", "World"</i>. The
	 * result will be <i>"Replace Hello"</i>, as only the first replacement over <i>%test%</i> will take effect.
	 * 
	 * @param replacements the new strings to be replaced, the format is <i>"str1", "obj1", "str2", "obj2"...</i>
	 * 
	 * @return This {@link Replacer} with the new <b>replacements</b> added to it.
	 * 
	 * @throws IllegalArgumentException if the amount of replacements is not even, more technically, if <code>replacements.length</code> % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #replaceAt(String)
	 * @see #replaceAt(List)
	 */
	@NonNull
	public Replacer add(@Nullable Object... replacements) {
		if (replacements == null)
			return this;
		if (replacements.length % 2 != 0)
			throw new IllegalArgumentException(replacements[replacements.length -1] + "does not have a replacer! Add one more element to the replacer.");
		replaceList.addAll(Arrays.asList(replacements));
		return this;
	}

	/**
	 * Adds the replacements of the specified <b>replacer</b> to this {@link Replacer}, joining them, note that existing replacements
	 * will be added to the list but the new replacer won't overwrite them. Because of the way replacements work, only the first
	 * replacement added for a string will take effect if there is another replacement added to said string later on.
	 * If <b>replacements</b> is null, nothing will be done.<br><br>
	 * 
	 * Example: text is <i>"Replace %test%"</i>, we add <i>"%test%", "Hello"</i> and <i>"%test%", "World"</i>. The
	 * result will be <i>"Replace Hello"</i>, as only the first replacement over <i>%test%</i> will take effect.
	 * 
	 * @param replacer the {@link Replacer} to join to the existing {@link Replacer}.
	 * 
	 * @return The old {@link Replacer} with the replacements of <b>replacer</b> added to it.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #replaceAt(String)
	 * @see #replaceAt(List)
	 */
	@NonNull
	public Replacer add(@Nullable Replacer replacer) {
		if (replacer != null)
			replaceList.addAll(replacer.replaceList);
		return this;
	}

	/**
	 * Applies this {@link Replacer} to the specified string, it the string is null, null will be returned.
	 * 
	 * @param str the string to apply the replacements to.
	 * 
	 * @return A new string with the replacements applied to it.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #add(Replacer)
	 * @see #add(String...)
	 */
	@Nullable
	public String replaceAt(@Nullable String str) {
		final int replacelistLen = replaceList.size();
		if (replacelistLen == 0 || str == null || str.isEmpty())
			return str;
		final StringBuilder res = new StringBuilder(str);
		for (int i = 0; i <= replacelistLen - 1; i += 2) {
			final String toSearch = replaceList.get(i).toString();
			final int searchLen = toSearch.length();
			final String replacement = replaceList.get(i + 1).toString();
			final int replacementLen = replacement.length();
			int index = res.indexOf(toSearch);
			while (index != -1) {
				res.replace(index, index + searchLen, replacement);
				index += replacementLen;
				index = res.indexOf(toSearch, index);
			}
		}
		return applyNumSupport(res).toString();
	}

	private StringBuilder applyNumSupport(StringBuilder res) {
		int start = 0;
		final int resLen = res.length();
		while (start < resLen) {
			int open = res.indexOf("<", start);
			int close = res.indexOf(">", open);
			if (open == -1 || close == -1)
				break;
			String[] parts = res.substring(open + 1, close).split(":");
			if (parts.length != 3) {
				start = close + 1;
				continue;
			}
			final String numStr = parts[0].startsWith("+") || parts[0].startsWith("-") ? parts[0].substring(1) : parts[0];
			if (MCStrings.isNumeric(numStr)) {
				int num = Integer.parseInt(numStr);
				String replacement = num == 1 ? parts[1] : parts[2];
				res.replace(open, close + 1, replacement);
				start = open + replacement.length();
			} else
				start = close + 1;
		}
		return res;
	}

	/**
	 * Applies this {@link Replacer} to the specified list of {@link String strings},
	 * if the <b>list</b> is null, null will be returned.
	 * 
	 * @param list the {@link String} {@link List} to apply the replacements to.
	 * 
	 * @return A new {@link String} {@link List} with the replacements applied to it.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #add(Replacer)
	 * @see #add(Object...)
	 */
	@Nullable
	public List<String> replaceAt(@Nullable List<String> list) {
		if (replaceList.isEmpty() || list == null || list.isEmpty())
			return list;
		List<String> res = new ArrayList<>();
		list.forEach(str -> res.add(replaceAt(str)));
		return res;
	}

	/**
	 * Clones this {@link Replacer} creating a new one with the same replacements.
	 * 
	 * @return A copy of this {@link Replacer}
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #Replacer(String...)
	 */
	@Override
	@NonNull
	public Replacer clone() {
		Replacer copy = new Replacer();
		copy.replaceList.addAll(replaceList);
		return copy;
	}
}
