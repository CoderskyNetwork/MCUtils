package es.xdec0de.mcutils.strings;

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
 * Pattern used is: <#([0-9A-Fa-f]{6})(.*?)#([0-9A-Fa-f]{6})>
 * <p>
 * Example: <#FFFFFFTest string#000000>
 * 
 * @since MCUtils 1.0.0
 */
public class Gradient implements ColorPattern {

	private final Pattern pattern = Pattern.compile("<#([0-9A-Fa-f]{6})(.*?)#([0-9A-Fa-f]{6})>");
	private final char COLOR_CHAR = 0x00A7;

	protected Gradient() {}

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
		if (string == null)
			return null;
		String res = string;
		Matcher matcher = pattern.matcher(string);
		while (matcher.find()) {
			String start = matcher.group(1);
			String end = matcher.group(3);
			String content = matcher.group(2);
			res = string.replace(matcher.group(), applyGradient(content, new Color(Integer.parseInt(start, 16)), new Color(Integer.parseInt(end, 16))));
		}
		return res;
	}

	@Nonnull
	private String apply(@Nonnull String source, ChatColor[] colors) {
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

	private String applyGradient(@Nullable String str, @Nonnull Color start, @Nonnull Color end) {
		ChatColor[] colors = createGradient(start, end, str.length());
		return apply(str, colors);
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
}
