package me.xdec0de.mcutils.strings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.xdec0de.mcutils.MCUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;

/**
 * A string utility class, also contains
 * methods for string lists.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #applyColor(String)
 */
public class MCStrings {

	private LinkedList<ColorPattern> colorPatterns = new LinkedList<>();
	private LinkedList<ChatPattern> chatPatterns = new LinkedList<>();

	private final Pattern actionPattern = Pattern.compile("(.*)<a:(.*):(.*)[/]a>(.*)");

	public MCStrings(MCUtils plugin) {
		if (plugin.strings() != null)
			throw new SecurityException("Illegal constructor call, access this class using MCUtils#strings()");
		addColorPattern(new Gradient());
		addColorPattern(new Hex());
		chatPatterns.add(new ActionBar());
		chatPatterns.add(new TargetPattern(this));
	}

	/**
	 * Sends <b>str</b> to <b>target</b> using the dynamic message format. This feature
	 * allows administrators to choose how and where a message will be sent, player specific
	 * message types such as ActionBar will be sent as a chat message to the console, here
	 * is the currently supported syntax:
	 * <p>
	 * <b>ActionBar</b>: <i>"&#60;ab:This will be sent to the actionbar/ab>"</i> - Self explanatory, the message
	 * will be sent as an ActionBar message.
	 * <p>
	 * <b>Action</b>: <i>"You can &#60;a:copy:copied!:click/a> this message"</i> - This will send a message with
	 * the text "You can click this message", once you click "click", "copied!" will be copied to your clipboard.
	 * <p>
	 * <b>Target</b>: <i>"&#60;p:Message for players/p>"&#60;c:Message for the console./c>"</i> - This will send
	 * a message depending on the receiver, anything under the p tag will be sent if the receiver is a player,
	 * under the c tag, if the receiver is the console, anything without this tags, won't check the receiver.
	 * <p>
	 * Keep in mind that sending multiple messages of the same type <i>(Ignoring chat messages)</i>
	 * isn't supported as it wouldn't make any sense <i>(They would conflict with each other)</i>
	 * 
	 * @param target the {@link CommandSender} that will receive the message.
	 * @param str the message to send, if null or empty, nothing will be done.
	 * 
	 * @return Always true, to make sending messages on commands easier.
	 * 
	 * @throws IllegalArgumentException if <b>target</b> is null.
	 */
	public boolean sendFormattedMessage(@Nonnull CommandSender target, @Nullable String str) {
		if (target == null)
			throw new IllegalArgumentException("target cannot be null.");
		if (str != null && !str.isEmpty()) {
			String toChat = str;
			for (ChatPattern pattern : chatPatterns)
				toChat = pattern.process(target, toChat);
			if (toChat != null && !toChat.isEmpty()) {
				if (target instanceof Player) {
					BaseComponent[] components = processActions(toChat);
					((Player)target).spigot().sendMessage(components != null ? components : TextComponent.fromLegacyText(toChat));
				} else
					target.sendMessage(toChat);
			}
		}
		return true;
	}

	BaseComponent[] processActions(String str) {
		String postProcess = str;
		Matcher matcher = actionPattern.matcher(postProcess);
		ComponentBuilder res = new ComponentBuilder();
		boolean found = false;
		if (matcher.find()) {
			final String pre = matcher.group(1);
			final String[] args = matcher.group(2).split(":");
			final String text = matcher.group(3);
			final String post = matcher.group(4);
			if (args.length == 0 || args.length % 2 != 0)
				return null;
			res.append(TextComponent.fromLegacyText(pre)).append(TextComponent.fromLegacyText(text));
			for (int i = 0; i < args.length; i += 2) {
				HoverEvent.Action hoverAction = getHoverAction(args[i]);
				if (hoverAction != null) {
					Content content = getHoverContent(hoverAction, args[i + 1]);
					res.event(new HoverEvent(content.requiredAction(), content));
				}
				ClickEvent.Action clickAction = getClickAction(args[i]);
				if (clickAction != null)
					res.event(new ClickEvent(clickAction, args[i + 1]));
			}
			res.append(TextComponent.fromLegacyText(post), FormatRetention.FORMATTING);
			found = true;
		}
		return found ? res.create() : null;
	}

	private Content getHoverContent(HoverEvent.Action action, String data) {
		if (action.equals(HoverEvent.Action.SHOW_TEXT))
			return new Text(TextComponent.fromLegacyText(data));
		else if (action.equals(HoverEvent.Action.SHOW_ITEM)) {
			String[] args = data.split(",");
			int amount = args.length >= 2 && isNumeric(args[1]) ? Integer.valueOf(args[1]) : 1;
			return new Item(args[0], amount, null);
		} else if (action.equals(HoverEvent.Action.SHOW_ENTITY)) {
			String[] args = data.split(",");
			if (args.length == 1)
				return new Text(applyColor("&4Error&8: &cMissing UUID of entity."));
			return new Entity(args[0], args[1], (args.length >= 3) ? TextComponent.fromLegacyText(args[2])[0] : null);
		}
		return new Text("");
	}

	private HoverEvent.Action getHoverAction(String action) {
		return switch (action.toLowerCase()) {
		case "text", "show_text" -> HoverEvent.Action.SHOW_TEXT;
		case "item", "show_item" -> HoverEvent.Action.SHOW_ITEM;
		case "entity", "show_entity" -> HoverEvent.Action.SHOW_ENTITY;
		default -> null;
		};
	}

	private ClickEvent.Action getClickAction(String action) {
		return switch (action.toLowerCase()) {
		case "copy", "copy_to_clipboard" -> ClickEvent.Action.COPY_TO_CLIPBOARD;
		case "url", "open_url" -> ClickEvent.Action.OPEN_URL;
		case "run_cmd", "run_command" -> ClickEvent.Action.RUN_COMMAND;
		case "sug_cmd", "suggest_command", "suggest_cmd" -> ClickEvent.Action.SUGGEST_COMMAND;
		default -> null;
		};
	}

	/**
	 * Gets a <b>previously registered</b> {@link ColorPattern} by class, that is,
	 * a {@link ColorPattern} that has been added using either {@link #addColorPattern(ColorPattern)}
	 * or {@link #addColorPatternBefore(ColorPattern, Class)}, if no {@link ColorPattern} matching
	 * <b>pattern</b> has been added, null will be returned.
	 * 
	 * @param <T> must implement {@link ColorPattern}.
	 * @param pattern the class of the {@link ColorPattern} to return.
	 * 
	 * @throws IllegalArgumentException If <b>pattern</b> is null.
	 * 
	 * @return an instance of <b>pattern</b> if registered, null otherwise.
	 */
	public <T extends ColorPattern> ColorPattern getColorPattern(@Nonnull Class<T> pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Pattern cannot be null");
		for (ColorPattern implPattern : colorPatterns)
			if (implPattern.getClass().equals(pattern))
				return implPattern;
		return null;
	}

	/**
	 * Adds a new {@link ColorPattern} to be used on {@link #applyColor(String)}
	 * and {@link #applyColor(List)} before the specified <b>pattern</b>, it's
	 * important to take into account pattern order as patterns might conflict with
	 * one another. For example the {@link Hex} pattern directly conflicts with the
	 * {@link Gradient} pattern overwriting it because {@link Gradient} uses
	 * hexadecimal colors, so {@link Gradient} needs to be added before {@link Hex}.
	 * <p>
	 * Normally, this method won't be needed unless you are adding a pattern that uses
	 * hexadecimal colors on it's syntax as {@link Gradient} does. If that's not the case,
	 * you can just use {@link #addColorPattern(ColorPattern)} to improve performance a bit.
	 * 
	 * @param <T> must implement {@link ColorPattern}
	 * @param pattern the {@link ColorPattern} to add.
	 * @param before the {@link Class} of the {@link ColorPattern} that will
	 * go after <b>pattern</b>, putting <b>pattern</b> before it.
	 * 
	 * @throws IllegalArgumentException If <b>pattern</b> or <b>before</b> are null.
	 * 
	 * @see #addColorPattern(ColorPattern)
	 */
	public <T extends ColorPattern> void addColorPatternBefore(@Nonnull ColorPattern pattern, @Nonnull Class<T> before) {
		if (pattern == null)
			throw new IllegalArgumentException("Added pattern cannot be null");
		if (before == null)
			throw new IllegalArgumentException("Before pattern class cannot be null");
		final LinkedList<ColorPattern> tempPatterns = new LinkedList<>();
		boolean added = false;
		for (ColorPattern implPattern : colorPatterns) {
			if (implPattern.getClass().equals(before))
				added = tempPatterns.add(pattern); // Always true as the Collection changes.
			tempPatterns.add(implPattern);
		}
		if (!added)
			tempPatterns.add(pattern);
		colorPatterns = tempPatterns;
	}

	/**
	 * Adds a new {@link ColorPattern} to be used on {@link #applyColor(String)}
	 * and {@link #applyColor(List)}, it's important to take into account pattern
	 * order as patterns might conflict with one another. For example the {@link Hex}
	 * pattern directly conflicts with the {@link Gradient} pattern overwriting it 
	 * because {@link Gradient} uses hexadecimal colors, so {@link Gradient} needs to
	 * be added first as it doesn't overwrite {@link Hex}.
	 * <p>
	 * Want to add a {@link ColorPattern} that would be overwritten by a MCUtils
	 * {@link ColorPattern} like {@link Hex}? Use {@link #addColorPatternBefore(ColorPattern, Class)}
	 * 
	 * @param pattern the {@link ColorPattern} to add.
	 * 
	 * @throws IllegalArgumentException If <b>pattern</b> is null.
	 * 
	 * @see #addColorPatternBefore(ColorPattern, Class)
	 */
	public void addColorPattern(@Nonnull ColorPattern pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Pattern cannot be null");
		colorPatterns.add(pattern);
	}

	/**
	 * Applies all registered patterns to a <b>string</b> with hexadecimal
	 * color support, then, applies "traditional" colors with char '&' using
	 * {@link ChatColor#translateAlternateColorCodes(char, String)}
	 * 
	 * @param string the string to apply colors.
	 * @return The string, colored.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String applyColor(@Nullable String string) {
		if (string == null)
			return null;
		String res = string;
		for (ColorPattern pattern : colorPatterns)
			res = pattern.process(res);
		return ChatColor.translateAlternateColorCodes('&', res);
	}

	/**
	 * Applies colors to every string of <b>lst</b>
	 * using {@link #applyColor(String)}. If <b>lst</b>
	 * is null, null elements on the list will be kept
	 * as null.
	 * 
	 * @param lst the list to apply colors.
	 * 
	 * @return The list, colored.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> applyColor(@Nullable List<String> lst) {
		if (lst == null)
			return null;
		List<String> res = new ArrayList<>(lst.size());
		for (String str : lst)
			res.add(applyColor(str));
		return res;
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

	/**
	 * Gets a string list as a string with all elements separated by
	 * the specified <b>separator</b>, null elements will be ignored.
	 * If <b>lst</b> is null, null will be returned, if <b>separator</b>
	 * is null, "null" will be used as a separator.
	 * <p>
	 * This method doesn't treat strings without content as elements, meaning
	 * no separator will be added to them (See {@link #hasContent(String)})
	 * <p>
	 * Example:
	 * <p>
	 * Array: [" ", "a", null, "b", "c"]<br>
	 * Separator: ", "<br>
	 * Returns: " a, b, c"
	 * 
	 * @param lst the list to use.
	 * @param separator the separator to use.
	 * 
	 * @return A string list as a string with all elements separated by
	 * the specified <b>separator</b>.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String asString(@Nullable List<String> lst, @Nullable String separator) {
		String res = "";
		if (lst == null)
			return null;
		for (String str : lst) {
			if (str == null)
				continue;
			res += hasContent(res) ? separator+str : str;
		}
		return res;
	}

	/**
	 * Check if <b>str</b> is numeric, meaning that it should
	 * only contain numeric characters between '0' and '9', the
	 * first character of the string can be a sign ('+' or '-').
	 * If {@link #hasContent(String)} returns false, this method
	 * will also return false.
	 * 
	 * @param str the string to check.
	 * 
	 * @return True if <b>str</b> is numeric, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean isNumeric(String str) {
		if (!hasContent(str))
			return false;
		final char sign = str.charAt(0);
		for (int i = (sign == '-' || sign == '+') ? 1 : 0; i < str.length(); i++) {
			final char ch = str.charAt(i);
			if (ch < '0' || ch > '9')
				return false;
		}
		return true;
	}
}
