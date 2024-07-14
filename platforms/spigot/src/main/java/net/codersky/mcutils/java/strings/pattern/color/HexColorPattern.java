package net.codersky.mcutils.java.strings.pattern.color;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;

import net.codersky.mcutils.java.strings.pattern.ColorPattern;

/**
 * Represents a hexadecimal color pattern which can be applied to a String.
 * <p>
 * The format to use can be either #RRGGBB or #RGB, the first (full) one will
 * always be applied, while the second (simple) one will only be applied if
 * {@code simple} mode is enabled on {@link #process(String, boolean)}.
 * <p>
 * Example: "#FFFFFFTest string" or "#FFFTest string"
 * <p>
 * This format contains some optimizations to ensure that colors are applied
 * as fast as possible. It doesn't use regular expressions and checks if the
 * string to be processed contains at least one '#' character before doing anything.
 * 
 * @since MCUtils 1.0.0
 */
public class HexColorPattern implements ColorPattern {

	/**
	 * Applies hexadecimal colors (#RRGGBB) to the provided {@code string}.
	 * Output will be the same as the input if no hex code is present.
	 * If the {@code string} is {@code null}, {@code null} will be returned.
	 * 
	 * The hexadecimal color pattern supports a "simple" mode, that also applies
	 * a three-character pattern (#RGB), useful when string length matters.
	 *
	 * @param string the string to which gradients should be applied to,
	 * this string object won't be modified.
	 * 
	 * @return The new string with applied hexadecimal colors.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String process(@Nullable final String string, boolean simple) {
		if (string == null || string.indexOf('#') < 0)
			return string;
		final int len = string.length();
		final StringBuilder result = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			final char current = string.charAt(i);
			if (current == '#') {
				final int hexSize = getHexSize(string, i + 1, len, simple);
				if (hexSize != 0) {
					final int[] positions = hexSize == 3 ? new int[] {1, 1, 2, 2, 3, 3} : new int[] {1, 2, 3, 4, 5, 6};
					result.append(ChatColor.COLOR_CHAR + "x");
					for (final int pos : positions)
						result.append(ChatColor.COLOR_CHAR + Character.toString(string.charAt(i + pos)));
					i += hexSize;
				} else
					result.append(current);
			} else
				result.append(current);
		}
		return result.toString();
	}

	private int getHexSize(String str, int start, int len, boolean simple) {
		int size = 0;
		for (int i = start; i < len && size <= 6; i++, size++)
			if (!isHexChar(str.charAt(i)))
				break;
		if (size == 6 || (simple && size == 3))
			return size;
		return size > 3 ? 3 : 0;
	}
}
