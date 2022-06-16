package es.xdec0de.mcutils.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.mcutils.MCUtils;

/**
 * A string utility class, also contains
 * methods for string lists.
 * 
 * @author xDec0de_
 * 
 * @see #applyColor(String)
 */
public class MCStrings {

	private final Pattern hexPattern;

	public MCStrings() {
		if (JavaPlugin.getPlugin(MCUtils.class).strings() != null)
			throw new SecurityException("Illegal constructor call, access this class using MCUtils#strings()");
		hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
	}

	/**
	 * Applies colors to a <b>string</b> with hexadecimal
	 * color support, the pattern used is #([A-Fa-f0-9]{6}),
	 * then, applies "traditional" colors with char '&' using
	 * {@link ChatColor#translateAlternateColorCodes(char, String)}
	 * 
	 * @param string the string to apply colors.
	 * @return The string, colored.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String applyColor(String string) {
		if(string == null)
			return null;
		final char COLOR_CHAR = 0x00A7;
		final Matcher matcher = hexPattern.matcher(string);
		final StringBuffer buffer = new StringBuffer(string.length() + 4 * 8);
		while (matcher.find()) {
			String group = matcher.group(1);
			matcher.appendReplacement(buffer, COLOR_CHAR + "x"
					+ COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
					+ COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
					+ COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));
		}
		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}

	/**
	 * Checks if a string has any content on it. If
	 * {@link String#isBlank()} returns true or the
	 * string is null, false will be returned.
	 * 
	 * @param str the string to check.
	 * 
	 * @return true if the string has content, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean hasContent(@Nullable String str) {
		return str == null ? false : (!str.isBlank());
	}
}
