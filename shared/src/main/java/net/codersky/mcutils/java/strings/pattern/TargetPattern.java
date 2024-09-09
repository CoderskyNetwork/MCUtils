package net.codersky.mcutils.java.strings.pattern;

import net.codersky.mcutils.crossplatform.MCConsole;
import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.pattern.target.*;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface used to represent a target pattern that {@link MCStrings}
 * will then use on its {@link MCStrings#applyTargetPatterns(MessageReceiver, String, boolean)}.
 * <p>
 * Target patterns have two purposes. The first one is that they can be used as a filter,
 * meaning that matching parts of the message will only be sent to the {@link MessageReceiver target}
 * if it meets certain criteria. For example, the {@link PlayerTargetPattern} will only send its contents
 * to the {@link MessageReceiver target} if it implements the {@link MCPlayer} interface. The other
 * purpose is to choose where the message is sent, for example, the {@link ActionBarTargetPattern}
 * will send its contents to the ActionBar of the {@link MessageReceiver target}.
 * <p>
 * <b>>> ABOUT EVENT PATTERNS <<</b>
 * <p>
 * You may have noticed that {@link TargetPattern TargetPatterns} have an
 * option to apply event patterns. This is only relevant for those {@link TargetPattern TargetPatterns}
 * that send <b>chat</b> messages to <b>players</b>, as event patterns only apply in that case. This is
 * because the content matched by a {@link TargetPattern} may contain event patterns. For example,
 * you may want to send one message to the console with the {@link ConsoleTargetPattern}, and then another
 * different message to players with the {@link PlayerTargetPattern}, but that message for players may
 * contain a hover event. If event patterns are applied, the hover event will be correctly sent, if not,
 * the hover event won't be built. Here is an example:
 * <p>
 * {@code <p:<show_text;Hover>Only for players/>/p>}
 * <p>
 * This will send "Only for players" with a hover effect that says "Hover" to the {@link MessageReceiver}
 * if it is an instance of {@link MCPlayer}, that is if event patterns are being applied, if not,
 * the literal text {@literal "<show_text:Hover>Only for players/>"} will be sent.
 *
 * @see ActionBarTargetPattern
 * @see ConsoleTargetPattern
 * @see PlayerTargetPattern
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
@FunctionalInterface
public interface TargetPattern {

	/**
	 * Applies this {@link TargetPattern} to the specified {@code string}, removing the
	 * pattern from the resulting {@link String}, if found.
	 *
	 * @param target The {@link MessageReceiver target} that will receive matching elements
	 * of this pattern, if eligible.
	 * @param string The {@link String} to apply the {@link TargetPattern} to.
	 * @param applyEventPatterns Whether to {@link MCStrings#applyEventPatterns(String) apply} event patterns
	 * to the content matched by this pattern. Details about this can be found {@link TargetPattern here},
	 * under the "<b>ABOUT EVENT PATTERNS</b>" section.
	 *
	 * @return By convention all {@link TargetPattern target patterns} will return a clone of
	 * {@code string} with every occurrence of this pattern removed from it if and <b>only</b> if the pattern
	 * is found on the provided {@code string}. If the pattern isn't found, the {@code string} parameter
	 * must be returned as is without creating a new {@link String}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	String process(@NotNull MessageReceiver target, @NotNull String string, boolean applyEventPatterns);
}
