package es.xdec0de.mcutils.strings;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import es.xdec0de.mcutils.MCUtils;
import net.md_5.bungee.api.ChatColor;

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

	private final Hex hexPattern;
	private final Gradient gradient;

	public MCStrings(MCUtils plugin) {
		if (plugin.strings() != null)
			throw new SecurityException("Illegal constructor call, access this class using MCUtils#strings()");
		hexPattern = new Hex();
		gradient = new Gradient();
	}
	
	/**
	 * Applies all registered patterns to a <b>string</b> with hexadecimal
	 * color support, then, applies "traditional" colors with char '&' using
	 * {@link ChatColor#translateAlternateColorCodes(char, String)}
	 * 
	 * @param string the string to apply colors.
	 * @return The string, colored.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Gradient
	 * @see Hex
	 */
	@Nullable
	public String applyColor(@Nullable String string) {
		if(string == null)
			return null;
		String res = applyGradients(string);
		res = applyHex(res);
		return ChatColor.translateAlternateColorCodes('&', res);
	}

	/**
	 * Applies the {@link Gradient} pattern to this string.
	 * 
	 * @param string the string to apply {@link Gradient}s
	 * @return The string, colored.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String applyGradients(String string) {
		return gradient.process(string);
	}

	/**
	 * Applies the {@link Hex} pattern to this string.
	 * 
	 * @param string the string to apply {@link Hex}adecimal colors
	 * @return The string, colored.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String applyHex(String string) {
		return hexPattern.process(string);
	}

	/**
	 * Applies colors to every string of <b>lst</b>
	 * using {@link #applyColor(String)}. If <b>lst</b>
	 * is null, null elements on the list will be kept
	 * as null.
	 * 
	 * @param lst the list to apply colors.
	 * 
	 * @return The list, colored.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> applyColor(@Nullable List<String> lst) {
		if (lst == null)
			return null;
		List<String> res = new ArrayList<>(lst.size());
		for (String str : lst)
			res.add(applyColor(str));
		return res;
	}

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
	public boolean hasContent(@Nullable String str) {
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
	public String asString(@Nullable List<String> lst, @Nullable String separator) {
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
}
