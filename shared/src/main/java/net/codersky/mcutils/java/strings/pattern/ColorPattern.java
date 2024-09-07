package net.codersky.mcutils.java.strings.pattern;

import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.pattern.color.*;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface used to represent a color pattern that {@link MCStrings}
 * will then use on its {@link MCStrings#applyColor(String, boolean)}
 * and {@link MCStrings#applyColor(String)} methods.
 *
 * @see HexColorPattern
 * @see GradientColorPattern
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
@FunctionalInterface
public interface ColorPattern {

	/**
	 * Applies this pattern to the provided {@code string} with an optional {@code simple} mode.
	 * Output might be the same as the input if this pattern is not present.
	 * <p>
	 * Simple mode is designed for hexadecimal based color patterns, adding an extra
	 * step to support a three-character hexadecimal color pattern, for example "#FFF".
	 * If this {@link ColorPattern} doesn't support simple mode this setting won't have any effect.
	 *
	 * @param string The {@link String} to which this pattern should be applied to.
	 * @param simple whether to use the simple color pattern or not, if applicable.
	 *
	 * @throws NullPointerException if {@code string} is {@code null}.
	 *
	 * @return By convention all {@link ColorPattern color patterns} will return a clone of
	 * {@code string} with this pattern applied to it if and <b>only</b> if the pattern
	 * is found on the provided {@code string}. If the pattern isn't found, the {@code string} parameter
	 * must be returned as is without creating a new {@link String}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	String applyColor(@NotNull final String string, boolean simple);

	default boolean isHexChar(char ch) {
		return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
	}
}
