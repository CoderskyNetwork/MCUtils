package net.codersky.mcutils.java.strings.pattern;

import java.util.List;

import javax.annotation.Nullable;

import net.codersky.mcutils.java.strings.MCStrings;

/**
 * Represents a color pattern which can be applied to a String.
 * <p>
 * Registered color patterns via either {@link MCStrings#addColorPattern(String, ColorPattern)}
 * or {@link MCStrings#addColorPatternBefore(String, ColorPattern, String)} will
 * be applied on {@link MCStrings#applyColor(String)} and {@link MCStrings#applyColor(List)}.
 * 
 * @since MCUtils 1.0.0
 */
@FunctionalInterface
public interface ColorPattern {

	/**
	 * Applies this pattern to the provided <b>string</b> with an optional <b>simple</b> mode.
	 * Output might be the same as the input if this pattern is not present.
	 * If the <b>string</b> is null, null must be returned by convention.
	 * <p>
	 * Simple mode is designed for hexadecimal based color patterns, adding an extra
	 * step to support a three-character hexadecimal color pattern, for example "#FFF".
	 * If this color pattern doesn't support simple mode this setting won't have any effect.
	 * 
	 * @param string the string to which this pattern should be applied to.
	 * @param simple whether to use the simple color pattern or not.
	 * 
	 * @return The new string with applied pattern.
	 */
	@Nullable
	String process(@Nullable String string, boolean simple);
}
