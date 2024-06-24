package net.codersky.mcutils.java.strings.pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.codersky.mcutils.java.strings.pattern.target.ActionBarTargetPattern;
import net.codersky.mcutils.java.strings.pattern.target.PlayerConsoleTargetPattern;

/**
 * Represents a color pattern which can used to filter parts of a message that
 * can be sent to different targets, for example, the
 * {@link PlayerConsoleTargetPattern} is able to send one message for
 * {@link Player players} and a different message for the server
 * {@link ConsoleCommandSender console} while using the same string for
 * both cases.
 * 
 * @since MCUtils 1.0.0
 * 
 * @see ActionBarTargetPattern
 * @see PlayerConsoleTargetPattern
 */
@FunctionalInterface
public interface TargetPattern {

	/**
	 * Applies this {@link TargetPattern} to the specified {@code string},
	 * sending any parts of the {@code string} that match this pattern to
	 * the {@code target}, if the {@code target} is actually valid to receive
	 * messages from this pattern.
	 * <p>
	 * Any part of the {@code string} that is handled by this pattern <b>must</b>
	 * be removed from the string being returned by the method, even if no message
	 * is sent to the {@code target}.
	 * 
	 * @param target the target that will receive (or not) the message filtered by
	 * this pattern.
	 * @param string the string to filter.
	 * 
	 * @return The input {@code string} with this pattern removed from it, this
	 * string may be the same as the input if the pattern wasn't present on it.
	 * 
	 *  @since MCUtils 1.0.0
	 */
	@Nullable
	String process(@Nonnull CommandSender target, @Nullable String string);
}
