package net.codersky.mcutils.java.strings.pattern.color;

import net.codersky.mcutils.java.strings.pattern.ColorPattern;
import net.codersky.mcutils.java.strings.MCStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a hexadecimal color pattern which can be applied to a String.
 * <p>
 * The format to use can be either #RRGGBB or #RGB, the first (full) one will
 * always be applied, while the second (simple) one will only be applied if
 * {@code simple} mode is enabled on {@link #applyColor(String, boolean)}.
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

	@NotNull
	public String applyColor(@Nullable final String string, boolean simple) {
		final int len = string.length();
		if (len < (simple ? 5 : 8) || string.indexOf('#') < 0)
			return string;
		final StringBuilder result = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			final char current = string.charAt(i);
			if (current == '#') {
				final int hexSize = getHexSize(string, i + 1, len, simple);
				if (hexSize != 0) {
					final int[] positions = hexSize == 3 ? new int[] {1, 1, 2, 2, 3, 3} : new int[] {1, 2, 3, 4, 5, 6};
					result.append(MCStrings.COLOR_CHAR + "x");
					for (final int pos : positions)
						result.append(MCStrings.COLOR_CHAR + Character.toString(string.charAt(i + pos)));
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
