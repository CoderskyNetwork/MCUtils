package me.xdec0de.mcutils.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represents a replacer to replace parts of a string with other objects, if you want to use the same replacements for multiple strings, you should 
 * create a replacer variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also, 
 * make sure that the amount of strings added to the replacer are <b>even</b>, otherwise, an {@link IllegalArgumentException} will be thrown.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #Replacer(Object...)
 */
public class Replacer {

	private final ArrayList<Object> replaceList = new ArrayList<>();
	private Pattern numPattern = null;

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
		if (replaceList.isEmpty() || str == null || str.isEmpty())
			return str;
		String res = str;
		for (int i = 0; i <= replaceList.size() - 1; i += 2)
			res = res.replace(replaceList.get(i).toString(), replaceList.get(i + 1).toString());
		if (this.numPattern != null) {
			Matcher matcher = numPattern.matcher(res);
			while (matcher.find()) {
				// 1 = value, 2 = singular, 3 = plural. 
				final int value = Integer.valueOf(matcher.group(1));
				final String replacement = value == 1 ? matcher.group(2) : matcher.group(3);
				res = res.replace(matcher.group(), replacement);
			}
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

	/**
	 * This feature adds the possibility to apply different replacements
	 * depending on numeric values, here is a common example:
	 * <p>
	 * Assuming you already have a replacement for the string "%points%" that replaces
	 * it with a numeric string, you would be able to replace at the following string:
	 * <p>
	 * "<b>You have %points% <%points%:point:points></b>"
	 * <p>
	 * With this feature, the returning string will either be "<b>You have 1 point</b>" or "<b>You have 2 points</b>",
	 * same happens with signed numbers (+1 or -1).
	 * <p>
	 * You don't need to worry about non-numeric replacements here, for example <b>"&#60;string:singular:plural>"</b>, it
	 * won't match the regex and nothing will be done.
	 * 
	 * @param support true to enable, false to disable.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void setNumSupport(boolean support) {
		this.numPattern = support ? Pattern.compile("<([+-]?[0-9]{1,}):(.{1,}):(.{1,})>") : null;
	}
}
