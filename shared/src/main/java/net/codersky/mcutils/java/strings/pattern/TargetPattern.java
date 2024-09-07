package net.codersky.mcutils.java.strings.pattern;

import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.pattern.target.*;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface used to represent a target pattern that {@link MCStrings}
 * will then use on its {@link MCStrings#applyTargetPatterns(MessageReceiver, String)}.
 * <p>
 * Target patterns have two purposes. The first one is that they can be used as a filter,
 * meaning that matching parts of the message will only be sent to the {@link MessageReceiver target}
 * if it meets certain criteria. For example, the {@link PlayerTargetPattern} will only send its contents
 * to the {@link MessageReceiver target} if it implements the {@link MCPlayer} interface. The other
 * purpose is to choose where the message is sent, for example, the {@link ActionBarTargetPattern}
 * will send its contents to the ActionBar of the {@link MessageReceiver target}.
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
	 * @param target the {@link MessageReceiver target} that will receive matching elements
	 * of this pattern, if eligible.
	 * @param string the {@link String} to apply the {@link TargetPattern} to.
	 *
	 * @return By convention all {@link TargetPattern target patterns} will return a clone of
	 * {@code string} with every occurrence of this pattern removed from it if and <b>only</b> if the pattern
	 * is found on the provided {@code string}. If the pattern isn't found, the {@code string} parameter
	 * must be returned as is without creating a new {@link String}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	String process(@NotNull MessageReceiver target, @NotNull String string);
}
