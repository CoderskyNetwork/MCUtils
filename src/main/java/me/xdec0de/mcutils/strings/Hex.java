package me.xdec0de.mcutils.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	private final Pattern pattern = Pattern.compile("#([A-Fa-f0-9]{6})");
	private final Pattern simplePattern = Pattern.compile("#([A-Fa-f0-9]{3})");

	Hex() {}

	/**
	 * Applies hexadecimal colors to the provided <b>string</b>.
	 * Output might me the same as the input if this pattern is not present.
	 * If the <b>string</b> is null, null will be returned.
	 * <p>
	 * The hexadecimal color pattern supports a "simple" mode, that also applies
	 * a three-character pattern (#([A-Fa-f0-9]{3})), useful when string length matters.
	 *
	 * @param string the string to which gradients should be applied to.
	 * @param simple whether to apply the simple pattern or not.
	 * 
	 * @return The new string with applied gradient.
	 */
	@Nullable
	public String process(@Nullable String string, boolean simple) {
		if(string == null)
			return null;
		String res = string;
		for (int i = simple ? 2 : 1; i > 0; i--) { // i will be 1 for simplePattern, 2 for pattern.
			final Matcher matcher = i == 1 ? simplePattern.matcher(res) : pattern.matcher(res);
			final StringBuffer buffer = new StringBuffer(res.length() + 4 * 8);
			int[] positions = i == 1 ? new int[]{0, 0, 1, 1, 2, 2} : new int[]{0, 1, 2, 3, 4, 5};
			while (matcher.find()) {
				final String group = matcher.group(1);
				matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
						+ ChatColor.COLOR_CHAR + group.charAt(positions[0]) + ChatColor.COLOR_CHAR + group.charAt(positions[1])
						+ ChatColor.COLOR_CHAR + group.charAt(positions[2]) + ChatColor.COLOR_CHAR + group.charAt(positions[3])
						+ ChatColor.COLOR_CHAR + group.charAt(positions[4]) + ChatColor.COLOR_CHAR + group.charAt(positions[5]));
			}
			res = matcher.appendTail(buffer).toString();
		}
		return res;
	}
}
