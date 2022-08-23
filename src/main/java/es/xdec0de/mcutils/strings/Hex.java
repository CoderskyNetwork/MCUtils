package es.xdec0de.mcutils.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

/**
 * Represents a gradient color pattern which can be applied to a String.
 * <p>
 * This is an adaptation to MCUtils from the
 * <a href="https://github.com/Iridium-Development/IridiumColorAPI">IridiumColorAPI</a>
 * <p>
 * Pattern used is: #([A-Fa-f0-9]{6})
 * <p>
 * Example: #FFFFFFTest string
 * 
 * @since MCUtils 1.0.0
 */
public class Hex implements ColorPattern {

	private final Pattern pattern = Pattern.compile("#([A-Fa-f0-9]{6})");

	protected Hex() {}

	/**
	 * Applies gradients to the provided <b>string</b>.
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
		final Matcher matcher = pattern.matcher(res);
		final StringBuffer buffer = new StringBuffer(res.length() + 4 * 8);
		while (matcher.find()) {
			String group = matcher.group(1);
			matcher.appendReplacement(buffer, 0x00A7 + "x"
					+ 0x00A7 + group.charAt(0) + 0x00A7 + group.charAt(1)
					+ 0x00A7 + group.charAt(2) + 0x00A7 + group.charAt(3)
					+ 0x00A7 + group.charAt(4) + 0x00A7 + group.charAt(5));
		}
		return res;
	}
}
