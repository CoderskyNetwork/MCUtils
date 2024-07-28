package net.codersky.mcutils.java.strings.pattern.color;

import net.codersky.mcutils.java.strings.pattern.ColorPattern;
import net.codersky.mcutils.java.strings.MCStrings;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a gradient color pattern which can be applied to a String.
 * <p>
 * This is an adaptation to MCUtils from the
 * <a href="https://github.com/Iridium-Development/IridiumColorAPI">IridiumColorAPI</a>
 * <p>
 * Patterns used are: <#([0-9A-Fa-f]{6})(.*?)#([0-9A-Fa-f]{6})> and <#([0-9A-Fa-f]{3})(.*?)#([0-9A-Fa-f]{3})>
 * <p>
 * Example: <#FFFFFFTest string#000000> or <#FFFTest string#000>
 * 
 * @since MCUtils 1.0.0
 */
public class GradientColorPattern implements ColorPattern {

	// TODO This pattern needs to be optimized, specially we should stop using regex.

	private final Pattern pattern = Pattern.compile("<#([0-9A-Fa-f]{6})(.*?)#([0-9A-Fa-f]{6})>");
	private final Pattern simplePattern = Pattern.compile("<#([0-9A-Fa-f]{3})(.*?)#([0-9A-Fa-f]{3})>");

	@NotNull
	@Override
	public String applyColor(@NotNull final String string, boolean simple) {
		String res = string;
		for (int i = simple ? 2 : 1; i > 0; i--) { // i will be 1 for simplePattern, 2 for pattern.
			final Matcher matcher = i == 1 ? simplePattern.matcher(res) : pattern.matcher(res);
			while (matcher.find()) {
				final int step = MCStrings.stripColor(matcher.group(2), '&').length();
				final Color start = i == 1 ? getSimpleColor(matcher.group(1)) : new Color(Integer.parseInt(matcher.group(1), 16));
				final Color end = i == 1 ? getSimpleColor(matcher.group(3)) : new Color(Integer.parseInt(matcher.group(3), 16));
				res = matcher.replaceFirst(apply(matcher.group(2), createGradient(start, end, step)));
				matcher.reset(res);
			}
		}
		return res;
	}

	private Color getSimpleColor(String group) {
		StringBuilder builder = new StringBuilder(22); // 22 is the capacity of a 6 character string buffer.
		for (int i = 0; i < 3; i++) {
			builder.append(group.charAt(i));
			builder.append(group.charAt(i));
		}
		return new Color(Integer.parseInt(builder.toString(), 16));
	}

	// TODO I'm pretty sure the two methods below this comment can be simplified.

	/**
	 * Returns a gradient array of chat colors.
	 *
	 * @param start the starting color.
	 * @param end the ending color.
	 * @param step how many colors we return.
	 * 
	 * @author TheViperShow
	 */
	@NotNull
	private String[] createGradient(@NotNull Color start, @NotNull Color end, int step) {
		String[] colors = new String[step];
		if (step == 1) {
			colors[0] = toHexString(start);
			return colors;
		}
		int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
		int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
		int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
		int[] direction = new int[] {
				start.getRed() < end.getRed() ? +1 : -1,
				start.getGreen() < end.getGreen() ? +1 : -1,
				start.getBlue() < end.getBlue() ? +1 : -1
		};

		for (int i = 0; i < step; i++) {
			Color color = new Color(start.getRed() + ((stepR * i) * direction[0]), start.getGreen() + ((stepG * i) * direction[1]), start.getBlue() + ((stepB * i) * direction[2]));
			colors[i] = toHexString(color);
			//colors[i] = plugin.getServerVersion().supports(MCVersion.V1_16) ? ChatColor.of(color) : getClosestColor(color);
		}
		return colors;
	}

	private String toHexString(@NotNull Color color) {
		return String.format("%02X", color.getRed()) +
				String.format("%02X", color.getGreen()) +
				String.format("%02X", color.getBlue());
	}

	/**
	 * Applies the specified list of <b>colors</b> to <b>source</b>,
	 * this method is designed for patterns like {@link GradientColorPattern}.
	 * 
	 * @param source the string to apply colors.
	 * @param colors the colors to apply.
	 * 
	 * @return <b>Source</b> with <b>colors</b> applied to it.
	 */
	@NotNull
	String apply(@NotNull String source, @NotNull String[] colors) {
		final StringBuilder res = new StringBuilder();
		StringBuilder formatting = new StringBuilder();
		final char[] characters = source.toCharArray();
		int colorIndex = 0;
		for (int strIndex = 0; strIndex < characters.length; strIndex++) {
			char current = characters[strIndex];
			if (current == '&' || current == MCStrings.COLOR_CHAR) {
				char next = characters[++strIndex];
				if (next >= 'k' && next <= 'o') {// if next == k, l, m, n or o
					formatting.append(MCStrings.COLOR_CHAR);
					formatting.append(next);
					continue;
				} else if (next == 'r') {
					formatting = new StringBuilder();
					continue;
				}
				else
					strIndex--;
			}
			res.append(colors[colorIndex++]);
			res.append(formatting);
			res.append(current);
		}
		return res.toString();
	}
}
