package net.codersky.mcutils.java.strings.pattern;

import javax.annotation.Nullable;

import net.codersky.mcutils.java.strings.pattern.color.GradientColorPattern;
import net.codersky.mcutils.java.strings.pattern.color.HexColorPattern;

/**
 * Represents a color pattern which can be applied to a String.
 * 
 * @since MCUtils 1.0.0
 * 
 * @see GradientColorPattern
 * @see HexColorPattern
 */
@FunctionalInterface
public interface ColorPattern {

	/**
	 * Applies this pattern to the provided {@code string} with an optional {@code simple} mode.
	 * Output might be the same as the input if this pattern is not present.
	 * If the {@code string} is {@code null}, {@code null} must be returned by convention, without
	 * throwing a {@link NullPointerException}.
	 * <p>
	 * Simple mode is designed for hexadecimal based color patterns, adding an extra
	 * step to support a three-character hexadecimal color pattern, for example "#FFF".
	 * If this color pattern doesn't support simple mode this setting won't have any effect.
	 * 
	 * @param string the string to which this pattern should be applied to, this string
	 * must not be modified by the implementation, a new string must be returned with the
	 * required modifications done to it.
	 * @param simple whether to use the simple color pattern or not, if applicable. 
	 * 
	 * @return A new string with the pattern applied to it.
	 */
	@Nullable
	public String process(@Nullable final String string, final boolean simple);

	default boolean isHexChar(char ch) {
		return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
	}
}
