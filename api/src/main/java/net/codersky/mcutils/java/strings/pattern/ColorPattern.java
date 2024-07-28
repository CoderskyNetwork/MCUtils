package net.codersky.mcutils.java.strings.pattern;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ColorPattern {

	/**
	 * Applies this pattern to the provided {@code string} with an optional {@code simple} mode.
	 * Output might be the same as the input if this pattern is not present.
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
	 * @throws NullPointerException if {@code string} is {@code null}.
	 *
	 * @return By convention all color patterns will return a completely new {@link String} with
	 * this pattern applied to it if and <b>only</b> if the pattern is found on the provided
	 * {@code string}, if the pattern isn't found, the {@code string} parameter must be returned as
	 * is without creating a new one.
	 */
	@NotNull
	String applyColor(@NotNull final String string, boolean simple);

	default boolean isHexChar(char ch) {
		return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
	}
}
