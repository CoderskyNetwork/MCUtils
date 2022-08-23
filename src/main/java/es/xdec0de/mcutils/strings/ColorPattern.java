package es.xdec0de.mcutils.strings;

import javax.annotation.Nullable;

/**
 * Represents a color pattern which can be applied to a String.
 * <p>
 * This is an adaptation to MCUtils from the
 * <a href="https://github.com/Iridium-Development/IridiumColorAPI">IridiumColorAPI</a>
 * 
 * @since MCUtils 1.0.0
 */
public interface ColorPattern {

	/**
	 * Applies this pattern to the provided <b>string</b>.
	 * Output might me the same as the input if this pattern is not present.
	 * If the <b>string</b> is null, null will be returned.
	 *
	 * @param string the string to which this pattern should be applied to.
	 * 
	 * @return The new string with applied pattern.
	 */
	@Nullable
	String process(@Nullable String string);
}
