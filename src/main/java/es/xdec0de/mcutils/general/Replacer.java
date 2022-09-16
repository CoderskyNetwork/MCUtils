package es.xdec0de.mcutils.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a replacer to replace parts of a string with other strings, if you want to use the same replacements for multiple strings, you should 
 * create a replacer variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also, 
 * make sure that the amount of strings added to the replacer are <b>even</b>, otherwise, an {@link IllegalArgumentException} will be thrown.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #Replacer(String...)
 */
public class Replacer {

	private final ArrayList<String> replaceList = new ArrayList<>();
	private Pattern numPattern = null;

	/**
	 * Creates a replacer to replace parts of a string with other strings, if you want to use the same replacements for multiple strings, you should 
	 * create a {@link Replacer} variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also, 
	 * make sure that the amount of strings added to the {@link Replacer} are <b>even</b>, otherwise, an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param replacements the strings to be replaced, the format is <i>"%placeholder1%", "replacement1", "%placeholder2%", "replacement2"...</i>
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
	public Replacer(String... replacements) {
		if (replacements == null)
			throw new IllegalArgumentException("Replacements cannot be null.");
		replaceList.addAll(Arrays.asList(replacements));
		if(replaceList.size() % 2 != 0)
			throw new IllegalArgumentException(replaceList.get(replaceList.size() - 1) + "does not have a replacer! Add one more element to the replacer.");
	}

	/**
	 * Adds new strings to an existing replacer, the amount of strings must also be even, note that existing replacements
	 * will be added to the list but the new replacer won't overwrite them. Because of the way replacements work, only the first
	 * replacement added for a placeholder will take effect if there is another replacement added to said placeholder later on.
	 * If <b>replacements</b> is null, nothing will be done.<br><br>
	 * 
	 * Example: text is <i>"Replace %placeholder%"</i>, we add <i>"%placeholder%", "Hello"</i> and <i>"%placeholder%", "World"</i>. The
	 * result will be <i>"Replace Hello"</i>, as only the first replacement over <i>%placeholder%</i> will take effect.
	 * 
	 * @param replacements the new strings to be replaced, the format is <i>"%placeholder1%", "replacement1", "%placeholder2%", "replacement2"...</i>
	 * 
	 * @return The old {@link Replacer} with the new <b>replacements</b> added to it.
	 * 
	 * @throws IllegalArgumentException if the amount of strings is not even, more technically, if replacements size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #replaceAt(String)
	 * @see #replaceAt(List)
	 */
	@Nonnull
	public Replacer add(@Nullable String... replacements) {
		if (replacements == null)
			return this;
		if(replacements.length % 2 != 0)
			throw new IllegalArgumentException(replacements[replacements.length -1] + "does not have a replacer! Add one more element to the replacer.");
		replaceList.addAll(Arrays.asList(replacements));
		return this;
	}

	/**
	 * Adds the replacements of the specified <b>replacer</b> to this {@link Replacer}, joining them, note that existing replacements
	 * will be added to the list but the new replacer won't overwrite them. Because of the way replacements work, only the first
	 * replacement added for a placeholder will take effect if there is another replacement added to said placeholder later on.
	 * If <b>replacer</b> is null, nothing will be done.<br><br>
	 * 
	 * Example: text is <i>"Replace %placeholder%"</i>, we add <i>"%placeholder%", "Hello"</i> and <i>"%placeholder%", "World"</i>. The
	 * result will be <i>"Replace Hello"</i>, as only the first replacement over <i>%placeholder%</i> will take effect.
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
	@Nonnull
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
		if(str == null)
			return null;
		String res = str;
		for(int i = 0; i <= replaceList.size() - 1; i += 2)
			res = res.replace(replaceList.get(i), replaceList.get(i + 1));
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
	 * Applies this {@link Replacer} to the specified list of strings, it the list is null, null will be returned.
	 * 
	 * @param list the string list to apply the replacements to.
	 * 
	 * @return A new string list with the replacements applied to it.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #add(Replacer)
	 * @see #add(String...)
	 */
	@Nullable
	public List<String> replaceAt(@Nullable List<String> list) {
		if(list == null)
			return null;
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
	@Nonnull
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
	 * @param support
	 */
	public void setNumSupport(boolean support) {
		this.numPattern = support ? Pattern.compile("<([+-]?[0-9]{1,}):(.{1,}):(.{1,})>") : null;
	}
}
