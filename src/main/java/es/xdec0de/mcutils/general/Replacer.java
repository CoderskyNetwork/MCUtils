package es.xdec0de.mcutils.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Represents a replacer to replace parts of a string with other strings, if you want to use the same replacements for multiple strings, you should 
 * create a replacer variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also, 
 * make sure that the amount of strings added to the replacer are <b>even</b>, otherwise, an {@link IllegalArgumentException} will be thrown.
 * 
 * @author xDec0de_
 * 
 * @see #Replacer(String...)
 */
public class Replacer {

	private final ArrayList<String> replaceList = new ArrayList<>();

	/**
	 * Creates a replacer to replace parts of a string with other strings, if you want to use the same replacements for multiple strings, you should 
	 * create a {@link Replacer} variable and apply it to as many strings as you want to <b>avoid creating multiple instances of the same replacements</b>, also, 
	 * make sure that the amount of strings added to the {@link Replacer} are <b>even</b>, otherwise, an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param replacements the strings to be replaced, the format is <i>"%placeholder1%", "replacement1", "%placeholder2%", "replacement2"...</i>
	 * 
	 * @throws IllegalArgumentException if the amount of strings is not even, more technically, if replacements size % 2 is not equal to 0.
	 * 
	 * @see #add(Replacer)
	 * @see #add(String...)
	 * @see #replaceAt(String)
	 * @see #replaceAt(List)
	 */
	public Replacer(String... replacements) {
		replaceList.addAll(Arrays.asList(replacements));
		if(replaceList.size() % 2 != 0)
			throw new IllegalArgumentException(replaceList.get(replaceList.size() - 1) + "does not have a replacer! Add one more element to the replacer.");
	}

	/**
	 * Adds new strings to an existing replacer, the amount of strings must also be even, note that existing replacements
	 * will be added to the list but the new replacer won't overwrite them. Because of the way replacements work, only the first
	 * replacement added for a placeholder will take effect if there is another replacement added to said placeholder later on.<br><br>
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
	 * @see #replaceAt(String)
	 * @see #replaceAt(List)
	 */
	public Replacer add(String... replacements) {
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
	 * result will be <i>"Replace Hello"</i>, as only the first replacement over <i>%placeholder%<i> will take effect.
	 * 
	 * @param replacer the {@link Replacer} to join to the existing {@link Replacer}.
	 * 
	 * @return The old {@link Replacer} with the replacements of <b>replacer</b> added to it.
	 * 
	 * @see #replaceAt(String)
	 * @see #replaceAt(List)
	 */
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
	 * @see #add(Replacer)
	 * @see #add(String...)
	 */
	public String replaceAt(String str) {
		if(str == null)
			return null;
		String res = str;
		for(int i = 0; i <= replaceList.size() - 1; i += 2)
			res = res.replace(replaceList.get(i), replaceList.get(i + 1));
		return res;
	}

	/**
	 * Applies this {@link Replacer} to the specified list of strings, it the list is null, null will be returned.
	 * 
	 * @param list the string list to apply the replacements to.
	 * 
	 * @return A new string list with the replacements applied to it.
	 * 
	 * @see #add(Replacer)
	 * @see #add(String...)
	 */
	public List<String> replaceAt(List<String> list) {
		if(list == null)
			return null;
		List<String> res = new ArrayList<>();
		list.forEach(str -> res.add(replaceAt(str)));
		return res;
	}
}
