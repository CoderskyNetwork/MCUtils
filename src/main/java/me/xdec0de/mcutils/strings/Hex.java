package me.xdec0de.mcutils.strings;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;

/**
 * Represents a hexadecimal color pattern which can be applied to a String.
 * <p>
 * Patterns used are: #([A-Fa-f0-9]{6}) and #([A-Fa-f0-9]{3})
 * <p>
 * Example: "#FFFFFFTest string" or "#FFFTest string"
 * 
 * @since MCUtils 1.0.0
 */
public class Hex implements ColorPattern {

	/**
	 * Applies hexadecimal colors (#RRGGBB) to the provided <b>string</b>.
	 * Output will be the same as the input if no hex code is present.
	 * If the <b>string</b> is null, null will be returned.
	 * 
	 * The hexadecimal color pattern supports a "simple" mode, that also applies
	 * a three-character pattern (#RGB), useful when string length matters.
	 *
	 * @param string the string to which gradients should be applied to.
	 * 
	 * @return The new string with applied hexadecimal colors.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String process(@Nullable String string, boolean simple) {
		if (string == null || string.indexOf('#') < 0)
			return string;
		int len = string.length();
		StringBuilder result = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			final char current = string.charAt(i);
			if (current == '#') {
				int hexSize = getHexSize(string, i + 1, len, simple);
				if (hexSize != 0) {
					int[] positions = hexSize == 3 ? new int[]{1, 1, 2, 2, 3, 3} : new int[]{1, 2, 3, 4, 5, 6};
					result.append(ChatColor.COLOR_CHAR + "x");
					for (int pos : positions)
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
			if (!isHex(str.charAt(i)))
				break;
		if (size == 6 || (simple && size == 3))
			return size;
		return size > 3 ? 3 : 0;
	}

	private boolean isHex(char ch) {
		return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
	}
}
