package es.xdec0de.mcutils.strings;

import javax.annotation.Nullable;

/**
 * Represents a color pattern which can be applied to a String.
 * 
 * @since MCUtils 1.0.0
 */
public interface ColorPattern {

	/**
	 * Applies this pattern to the provided <b>string</b>.
	 * Output might be the same as the input if this pattern is not present.
	 * If the <b>string</b> is null, null will be returned.
	 *
	 * @param string the string to which this pattern should be applied to.
	 * 
	 * @return The new string with applied pattern.
	 */
	@Nullable
	String process(@Nullable String string);

	/**
	 * Applies this pattern to the provided <b>string</b> with an optional simple mode.
	 * Output might me the same as the input if this pattern is not present.
	 * If the <b>string</b> is null, null will be returned.
	 * <p>
	 * Simple mode is designed for hexadecimal based color patterns, adding an extra
	 * step to support a three-character hexadecimal color pattern, for example "#FFF".
	 * If this color pattern doesn't support simple mode <i>(Which is likely if you are reading
	 * this instead of it's own documentation)</i>, {@link #process(String)} will be used.
	 * 
	 * @param string the string to which this pattern should be applied to.
	 * @param simple whether to use the simple color pattern or not.
	 * 
	 * @return The new string with applied pattern.
	 */
	@Nullable
	default String process(@Nullable String string, boolean simple) {
		return process(string);
	}
}
