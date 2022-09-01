package es.xdec0de.mcutils.strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
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

	private LinkedList<ColorPattern> patterns = new LinkedList<>();

	public MCStrings(MCUtils plugin) {
		if (plugin.strings() != null)
			throw new SecurityException("Illegal constructor call, access this class using MCUtils#strings()");
		addPattern(new Gradient());
		addPattern(new Hex());
	}

	/**
	 * Gets a <b>previously registered</b> {@link ColorPattern} by class, that is,
	 * a {@link ColorPattern} that has been added using either {@link #addPattern(ColorPattern)}
	 * or {@link #addPatternBefore(ColorPattern, Class)}, if no {@link ColorPattern} matching
	 * <b>pattern</b> has been added, null will be returned.
	 * 
	 * @param <T> must implement {@link ColorPattern}.
	 * @param pattern the class of the {@link ColorPattern} to return.
	 * 
	 * @throws NullPointerException If <b>pattern</b> is null.
	 * 
	 * @return an instance of <b>pattern</b> if registered, null otherwise.
	 */
	public <T extends ColorPattern> ColorPattern getPattern(@Nonnull Class<T> pattern) {
		if (pattern == null)
			throw new NullPointerException("Pattern cannot be null");
		for (ColorPattern implPattern : patterns)
			if (implPattern.getClass().equals(pattern))
				return implPattern;
		return null;
	}

	/**
	 * Adds a new {@link ColorPattern} to be used on {@link #applyColor(String)}
	 * and {@link #applyColor(List)} before the specified <b>pattern</b>, it's
	 * important to take into account pattern order as patterns might conflict with
	 * one another. For example the {@link Hex} pattern directly conflicts with the
	 * {@link Gradient} pattern overwriting it because {@link Gradient} uses
	 * hexadecimal colors, so {@link Gradient} needs to be added before {@link Hex}.
	 * <p>
	 * Normally, this method won't be needed unless you are adding a pattern that uses
	 * hexadecimal colors on it's syntax as {@link Gradient} does. If that's not the case,
	 * you can just use {@link #addPattern(ColorPattern)} to improve performance a bit.
	 * 
	 * @param <T> must implement {@link ColorPattern}
	 * @param pattern the {@link ColorPattern} to add.
	 * @param before the {@link Class} of the {@link ColorPattern} that will
	 * go after <b>pattern</b>, putting <b>pattern</b> before it.
	 * 
	 * @throws NullPointerException If <b>pattern</b> or <b>before</b> are null.
	 * 
	 * @see #addPattern(ColorPattern)
	 */
	public <T extends ColorPattern> void addPatternBefore(@Nonnull ColorPattern pattern, @Nonnull Class<T> before) {
		if (pattern == null)
			throw new NullPointerException("Added pattern cannot be null");
		if (before == null)
			throw new NullPointerException("Before pattern class cannot be null");
		final LinkedList<ColorPattern> tempPatterns = new LinkedList<>();
		boolean added = false;
		for (ColorPattern implPattern : patterns) {
			if (implPattern.getClass().equals(before))
				added = tempPatterns.add(pattern); // Always true as the Collection changes.
			tempPatterns.add(implPattern);
		}
		if (!added)
			tempPatterns.add(pattern);
		patterns = tempPatterns;
	}

	/**
	 * Adds a new {@link ColorPattern} to be used on {@link #applyColor(String)}
	 * and {@link #applyColor(List)}, it's important to take into account pattern
	 * order as patterns might conflict with one another. For example the {@link Hex}
	 * pattern directly conflicts with the {@link Gradient} pattern overwriting it 
	 * because {@link Gradient} uses hexadecimal colors, so {@link Gradient} needs to
	 * be added first as it doesn't overwrite {@link Hex}.
	 * <p>
	 * Want to add a {@link ColorPattern} that would be overwritten by a MCUtils
	 * {@link ColorPattern} like {@link Hex}? Use {@link #addPatternBefore(ColorPattern, Class)}
	 * 
	 * @param pattern the {@link ColorPattern} to add.
	 * 
	 * @throws NullPointerException If <b>pattern</b> is null.
	 * 
	 * @see #addPatternBefore(ColorPattern, Class)
	 */
	public void addPattern(@Nonnull ColorPattern pattern) {
		if (pattern == null)
			throw new NullPointerException("Pattern cannot be null");
		patterns.add(pattern);
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
	 */
	@Nullable
	public String applyColor(@Nullable String string) {
		if (string == null)
			return null;
		String res = string;
		for (ColorPattern pattern : patterns)
			res = pattern.process(res);
		return ChatColor.translateAlternateColorCodes('&', res);
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
	public boolean isNumeric(String str) {
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
}
