package es.xdec0de.mcutils.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

/**
 * Represents a hexadecimal color pattern which can be applied to a String.
 * <p>
 * This is an adaptation to MCUtils from the
 * <a href="https://github.com/Iridium-Development/IridiumColorAPI">IridiumColorAPI</a>
 * <p>
 * Patterns used are: #([A-Fa-f0-9]{6}) and #([A-Fa-f0-9]{3})
 * <p>
 * Example: "#FFFFFFTest string" or "#FFFTest string"
 * 
 * @since MCUtils 1.0.0
 */
public class Hex implements ColorPattern {

	private final Pattern pattern, simplePattern;

	protected Hex() {
		pattern = Pattern.compile("#([A-Fa-f0-9]{6})");
		simplePattern = Pattern.compile("#([A-Fa-f0-9]{3})");
	}

	/**
	 * Applies hexadecimal colors to the provided <b>string</b>.
	 * Output might me the same as the input if this pattern is not present.
	 * If the <b>string</b> is null, null will be returned.
	 *
	 * @param string the string to which gradients should be applied to.
	 * 
	 * @return The new string with applied gradient.
	 */
	@Nullable
	public String process(@Nullable String string) {
		if(string == null)
			return null;
		String res = string;
		final Matcher fullMatcher = pattern.matcher(res), simpleMatcher = simplePattern.matcher(res);
		final StringBuffer buffer = new StringBuffer(res.length() + 4 * 8);
		while (fullMatcher.find()) {
			final String group = fullMatcher.group(1);
			fullMatcher.appendReplacement(buffer, 0x00A7 + "x"
					+ 0x00A7 + group.charAt(0) + 0x00A7 + group.charAt(1)
					+ 0x00A7 + group.charAt(2) + 0x00A7 + group.charAt(3)
					+ 0x00A7 + group.charAt(4) + 0x00A7 + group.charAt(5));
		}
		while (simpleMatcher.find()) {
			final String group = simpleMatcher.group(1);
			simpleMatcher.appendReplacement(buffer, 0x00A7 + "x"
					+ 0x00A7 + group.charAt(0) + 0x00A7 + group.charAt(0)
					+ 0x00A7 + group.charAt(1) + 0x00A7 + group.charAt(1)
					+ 0x00A7 + group.charAt(2) + 0x00A7 + group.charAt(2));
		}
		return res;
	}
}
