package net.codersky.mcutils.files;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

import net.codersky.mcutils.files.yaml.MessagesFile;
import net.codersky.mcutils.general.MCCommand;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.replacers.Replacer;

/**
 * An interface that is generally implemented with either
 * {@link FileHolder} or {@link FileUpdater}. Used to be
 * able to send messages to {@link CommandSender CommandSenders}
 * directly from the file. This interface is used by {@link MessagesFile}.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 * 
 * @see #getString(String)
 * @see #getStringList(String)
 * @see #send(CommandSender, String)
 * @see MessagesFile
 */
public interface MessagesFileHolder {

	/*
	 * String getters
	  */

	/**
	 * Gets a {@link String} from this {@link MessagesFileHolder}. This {@link String}
	 * is returned with colors applied via {@link MCStrings#applyColor(String)}.
	 * 
	 * @param path the path of the {@link String} to return.
	 * 
	 * @return The requested {@link String} or {@code null} if no {@link String} was
	 * found under the specified <b>path</b>.
	 * 
	 * @throws NullPointerException if <b>path</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getString(@Nonnull String path);

	/**
	 * Gets a {@link String} from this {@link MessagesFileHolder} by using {@link #getString(String)}, then,
	 * applies the specified <b>replacer</b> to it.
	 * 
	 * @param path the path of the {@link String} to return.
	 * @param replacer the {@link Replacer} to apply to the {@link String}.
	 * 
	 * @return The requested {@link String} or {@code null} if no {@link String} was
	 * found under the specified <b>path</b>.
	 * 
	 * @throws NullPointerException if either <b>path</b> or <b>replacer</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	default public String getString(@Nonnull String path, @Nonnull Replacer replacer) {
		final String str = getString(path);
		return str == null ? null : replacer.replaceAt(str);
	}

	/**
	 * Gets a {@link String} from this {@link MessagesFileHolder} by using {@link #getString(String)}, then,
	 * applies the a new {@link Replacer} made with the specified <b>replacements</b> to it.
	 * <p>
	 * Please read the {@link Replacer#Replacer(Object...)} constructor documentation as exceptions
	 * may be thrown as explained there.
	 * 
	 * @param path the path of the {@link String} to return.
	 * @param replacer the {@link Replacer} to apply to the {@link String}.
	 * 
	 * @return The requested {@link String} or {@code null} if no {@link String} was
	 * found under the specified <b>path</b>.
	 * 
	 * @throws NullPointerException if <b>path</b> is {@code null}.
	 * 
	 * @throws IllegalArgumentException if <b>replacements</b> is {@code null} or the amount of objects is not even, 
	 * more technically, if <b>replacements</b> size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	default public String getString(@Nonnull String path, @Nonnull Object... replacements) {
		return getString(path, new Replacer(replacements));
	}

	/*
	 * String list getters
	 */

	/**
	 * Gets a {@link String} {@link List} from this {@link MessagesFileHolder}. This {@link List}
	 * is returned without any additional processing, just the {@link List} as
	 * it is stored on the {@link File} handled by this {@link MessagesFileHolder}.
	 * 
	 * @param path the path of the {@link List} to return.
	 * 
	 * @return The requested {@link List} or {@code null} if no {@link List} was
	 * found under the specified <b>path</b>.
	 * 
	 * @throws NullPointerException if <b>path</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> getStringList(@Nonnull String path);

	/**
	 * Gets a {@link String} {@link List} from this {@link MessagesFileHolder}, then, applies
	 * the specified <b>replacer</b> to each {@link String} on it.
	 * 
	 * @param path the path of the {@link List} to return.
	 * @param replacer the {@link Replacer} to apply to the {@link List}.
	 * 
	 * @return The requested {@link List} or {@code null} if no {@link String} was
	 * found under the specified <b>path</b>.
	 * 
	 * @throws NullPointerException if either <b>path</b> or <b>replacer</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	default public List<String> getStringList(@Nonnull String path, @Nonnull Replacer replacer) {
		final List<String> lst = getStringList(path);
		return lst == null ? null : replacer.replaceAt(lst);
	}

	/**
	 * Gets a {@link String} {@link List} from this {@link MessagesFileHolder}, then, applies
	 * the specified a new {@link Replacer} made with the specified <b>replacements</b> to
	 * each element of it.
	 * <p>
	 * Please read the {@link Replacer#Replacer(Object...)} constructor documentation as exceptions
	 * may be thrown as explained there.
	 * 
	 * @param path the path of the {@link String} to return.
	 * @param replacer the {@link Replacer} to apply to the {@link String}.
	 * 
	 * @return The requested {@link String} or {@code null} if no {@link String} was
	 * found under the specified <b>path</b>.
	 * 
	 * @throws NullPointerException if <b>path</b> is {@code null}.
	 * 
	 * @throws IllegalArgumentException if <b>replacements</b> is {@code null} or the amount of objects is not even, 
	 * more technically, if <b>replacements</b> size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	default public List<String> getStringList(@Nonnull String path, @Nonnull Object... replacements) {
		return getStringList(path, new Replacer(replacements));
	}

	/*
	 * Message senders
	 */

	private boolean runOn(@Nonnull String path, @Nonnull Function<String, Boolean> action) {
		final List<String> lst = getStringList(path);
		if (lst == null) {
			final String msg = getString(path);
			return msg == null ? true : action.apply(msg);
		}
		for (final String lstMsg : lst)
			action.apply(lstMsg);
		return true;
	}

	/**
	 * Sends a {@link String} or {@link List} of {@link String Strings}, depending on what
	 * is stored on the specified <b>path</b>, to the desired <b>target</b> by using
	 * either {@link #getString(String)} or {@link #getStringList(String)}.
	 * <p>
	 * Note that if no messages are found, nothing will be sent to the <b>target</b>.
	 * 
	 * @param target the {@link CommandSender} that will receive the message or list
	 * of messages stored at the specified <b>path</b>.
	 * @param path the path on which to find the messages to send to the <b>target</b>.
	 * 
	 * @return Always {@code true}. For a better usage on {@link MCCommand MCCommands}.
	 * 
	 * @throws NullPointerException if either <b>target</b> or <b>path</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	default public boolean send(@Nonnull CommandSender target, @Nonnull String path) {
		return runOn(path, msg -> MCStrings.sendMessage(target, msg));
	}

	/**
	 * Sends a {@link String} or {@link List} of {@link String Strings}, depending on what
	 * is stored on the specified <b>path</b>, to the desired <b>target</b> by using
	 * either {@link #getString(String, Replacer)} or {@link #getStringList(String, Replacer)}.
	 * <p>
	 * Note that if no messages are found, nothing will be sent to the <b>target</b>.
	 * 
	 * @param target the {@link CommandSender} that will receive the message or list
	 * of messages stored at the specified <b>path</b>.
	 * @param path the path on which to find the messages to send to the <b>target</b>.
	 * @param replacer the {@link Replacer} to apply to every message before sending it.
	 * 
	 * @return Always {@code true}. For a better usage on {@link MCCommand MCCommands}.
	 * 
	 * @throws NullPointerException if <b>target</b>, <b>path</b> or <b>replacer</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	default public boolean send(@Nonnull CommandSender target, @Nonnull String path, @Nonnull Replacer replacer) {
		return runOn(path, msg -> MCStrings.sendMessage(target, replacer.replaceAt(msg)));
	}

	/**
	 * Sends a {@link String} or {@link List} of {@link String Strings}, depending on what
	 * is stored on the specified <b>path</b>, to the desired <b>target</b> by using
	 * either {@link #getString(String, Object...)} or {@link #getStringList(String, Object...)}.
	 * <p>
	 * Note that if no messages are found, nothing will be sent to the <b>target</b>.
	 * <p>
	 * Please read the {@link Replacer#Replacer(Object...)} constructor documentation as exceptions
	 * may be thrown as explained there.
	 * 
	 * @param target the {@link CommandSender} that will receive the message or list
	 * of messages stored at the specified <b>path</b>.
	 * @param path the path on which to find the messages to send to the <b>target</b>.
	 * @param replacer the <b>objects</b> to build the {@link Replacer} that will be applied
	 * to every message before sending it.
	 * 
	 * @return Always {@code true}. For a better usage on {@link MCCommand MCCommands}.
	 * 
	 * @throws IllegalArgumentException if <b>replacements</b> is {@code null} or the amount of objects is not even, 
	 * more technically, if <b>replacements</b> size % 2 is not equal to 0.
	 * @throws NullPointerException if <b>target</b>, <b>path</b> or <b>replacements</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	default public boolean send(@Nonnull CommandSender target, @Nonnull String path, @Nonnull Object... replacements) {
		return send(target, path, new Replacer(replacements));
	}
}