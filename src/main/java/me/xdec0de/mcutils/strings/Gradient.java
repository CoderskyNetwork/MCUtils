package me.xdec0de.mcutils.strings;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.md_5.bungee.api.ChatColor;

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
public class Gradient implements ColorPattern {

	private final Pattern pattern = Pattern.compile("<#([0-9A-Fa-f]{6})(.*?)#([0-9A-Fa-f]{6})>");
	private final Pattern simplePattern = Pattern.compile("<#([0-9A-Fa-f]{3})(.*?)#([0-9A-Fa-f]{3})>");

	/**
	 * Applies gradients to the provided <b>string</b>.
	 * Output might me the same as the input if this pattern is not present.
	 * If the <b>string</b> is null, null will be returned.
	 * <p>
	 * The gradient color pattern supports a "simple" mode, that also applies
	 * a three-character pattern <i>(See {@link Gradient})</i> useful when string length matters..
	 *
	 * @param string the string to which gradients should be applied to.
	 * @param simple whether to apply the simple pattern or not.
	 * 
	 * @return The new string with applied gradient.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	@Override
	public String process(@Nullable String string, boolean simple) {
		if (string == null)
			return null;
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
		StringBuffer buff = new StringBuffer(22); // 22 is the capacity of a 6 character string buffer.
		for (int i = 0; i < 3; i++) {
			buff.append(group.charAt(i));
			buff.append(group.charAt(i));
		}
		return new Color(Integer.parseInt(buff.toString(), 16));
	}

	/**
	 * Returns a gradient array of chat colors.
	 *
	 * @param start the starting color.
	 * @param end the ending color.
	 * @param step how many colors we return.
	 * 
	 * @author TheViperShow
	 */
	@Nonnull
	private ChatColor[] createGradient(@Nonnull Color start, @Nonnull Color end, int step) {
		ChatColor[] colors = new ChatColor[step];
		if (step == 1) {
			colors[0] = ChatColor.of(start);
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
			colors[i] = ChatColor.of(color);
			//colors[i] = plugin.getServerVersion().supports(MCVersion.V1_16) ? ChatColor.of(color) : getClosestColor(color);
		}
		return colors;
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
		StringBuilder res = new StringBuilder();
		StringBuilder formatting = new StringBuilder();
		final char[] characters = source.toCharArray();
		int colorIndex = 0;
		for (int strIndex = 0; strIndex < characters.length; strIndex++) {
			char current = characters[strIndex];
			if (current == '&' || current == ChatColor.COLOR_CHAR) {
				char next = characters[++strIndex];
				if (next >= 'k' && next <= 'o') {// if next == k, l, m, n or o
					formatting.append(ChatColor.COLOR_CHAR);
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
			res.append(formatting.toString());
			res.append(current);
		}
		return res.toString();
	}
}
