package es.xdec0de.mcutils.strings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.md_5.bungee.api.ChatColor;

/**
 * Represents a color pattern which can be applied to a String.
 * 
 * @since MCUtils 1.0.0
 */
public abstract class ColorPattern {

	final char COLOR_CHAR = 0x00A7;

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
	abstract String process(@Nullable String string);

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
	String process(@Nullable String string, boolean simple) {
		return process(string);
	}

	/**
	 * Applies the specified list of <b>colors</b> to <b>source</b>,
	 * this method is designed for patterns like {@link Gradient}.
	 * 
	 * @param source the string to apply colors.
	 * @param colors the colors to apply.
	 * 
	 * @return <b>Source</b> with <b>colors</b> applied to it.
	 */
	@Nonnull
	String apply(@Nonnull String source, @Nonnull ChatColor[] colors) {
		StringBuilder specialColors = new StringBuilder();
		StringBuilder stringBuilder = new StringBuilder();
		final char[] characters = source.toCharArray();
		int outIndex = 0;
		for (int i = 0; i < characters.length; i++) {
			final char current = characters[i];
			if (current == '&' || current == COLOR_CHAR && i + 1 < characters.length) {
				final char next = characters[++i];
				if (next == 'r')
					specialColors.setLength(0);
				else
					specialColors.append(current + next);
			} else
				stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
		}
		return stringBuilder.toString();
	}
}
