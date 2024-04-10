package net.codersky.mcutils.files.yaml;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.codersky.mcutils.MCPlugin;
import net.codersky.mcutils.files.MessagesFileHolder;
import net.codersky.mcutils.java.MCLists;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.replacers.Replacer;

/**
 * A class intended to be used to create
 * and use a messages file. The most notorious features
 * of this file type include:
 * <p>
 * <b>Default replacements</b>: Defining a "%prefix%" replacement on every message
 * is tedious, that's why you can define a default {@link Replacer} by using
 * {@link #setDefaultReplacer(Replacer, boolean)}, this replacer will be applied on every message.
 * <p>
 * <b>Numeric support</b>: Disabled by default, can
 * be toggled by calling {@link #setNumSupport(boolean)}.
 * An explanation of this feature is provided at {@link Replacer#setNumSupport(boolean)}
 * <p>
 * <b>Formatted messages</b>: This feature is always enabled and you
 * don't need to do anything for it to work, all messages will be sent using
 * {@link MCStrings#sendMessage(CommandSender, String)}.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class MessagesFile extends PluginFile implements MessagesFileHolder {

	@Nullable
	private Replacer defReplacer = null;

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link MessagesFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link MCPlugin#registerFile(String, Class)}.
	 * <p>
	 * {@link MessagesFile}s are required to be on <b>plugin</b>'s jar file as a resource. For this
	 * exact reason they can be easily updated to newer versions. This files are intended to be used as
	 * as message files that require certain content to be present on them and are likely to be
	 * updated on future versions, as an extension of {@link PluginFile}.
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, the ".yml" extension is automatically added if missing,
	 * if the path is null, empty or blank, "file" will be used.
	 * @param charset the charset to use, if null, {@link StandardCharsets#UTF_8} will be used.
	 * 
	 * @throws IllegalArgumentException if <b>plugin</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(MessagesFile)
	 * @see MCPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public MessagesFile(@Nonnull JavaPlugin plugin, @Nullable String path, @Nullable Charset charset) {
		super(plugin, path, charset);
		if (!contains("commands.noPlayer"))
			addDefault("commands.noPlayer", "&8&l[&4&l!&8&l] &cThis command cannot be executed by players&8.");
		if (!contains("commands.noConsole"))
			addDefault("commands.noConsole", "&8&l[&4&l!&8&l] &cThis command cannot be executed by the console&8.");
	}

	/**
	 * Creates an instance, <b>NOT</b> a file, of a {@link MessagesFile} for the specified <b>plugin</b> and <b>path</b>.
	 * Fast access for file creation is provided by {@link MCPlugin#registerFile(String, Class)}.
	 * <p>
	 * {@link MessagesFile}s are required to be on <b>plugin</b>'s jar file as a resource. For this
	 * exact reason they can be easily updated to newer versions. This files are intended to be used as
	 * as message files that require certain content to be present on them and are likely to be
	 * updated on future versions, as an extension of {@link PluginFile}.
	 * <p>
	 * This constructor uses {@link StandardCharsets#UTF_8}, to specify use {@link #PluginFile(JavaPlugin, String, Charset)}
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, the ".yml" extension is automatically added if missing,
	 * if the path is null, empty or blank, "file" will be used.
	 * 
	 * @throws IllegalArgumentException if <b>plugin</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(MessagesFile)
	 * @see MCPlugin#registerFile(String, Class)
	 * @see #create()
	 */
	public MessagesFile(@Nonnull JavaPlugin plugin, @Nullable String path) {
		this(plugin, path, StandardCharsets.UTF_8);
	}

	/*
	 * Replacer handling
	 */

	/**
	 * Sets the default {@link Replacer} to be used on this {@link MessagesFile}.
	 * If the default {@link Replacer} is null, no default {@link Replacer} will be used on any
	 * message, the default {@link Replacer} is null by default.
	 * 
	 * @param replacer the default {@link Replacer} to be used on this {@link MessagesFile}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getDefaultReplacer()
	 * @see #setDefaultObjReplacer(Object...)
	 * @see #setDefaultPathReplacer(String...)
	 */
	public void setDefaultReplacer(@Nullable Replacer replacer) {
		this.defReplacer = replacer;
	}

	/**
	 * Creates a new {@link Replacer} with the specified {@code replacements} and
	 * sets it as the default {@link Replacer} to use on this {@link MessagesFile}.
	 * 
	 * @param replacements the replacements to use for the new {@link Replacer}.
	 * 
	 * @throws IllegalArgumentException if {@code replacements} is {@code null} or the amount of
	 * objects is not even, more technically, if {@code replacements}
	 * size % 2 is not equal to 0 as specified by {@link Replacer#Replacer(Object...)}
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getDefaultReplacer()
	 * @see #setDefaultReplacer(Replacer)
	 * @see #setDefaultPathReplacer(String...)
	 */
	public void setDefaultObjReplacer(@Nonnull Object... replacements) {
		setDefaultReplacer(new Replacer(replacements));
	}

	/**
	 * Creates a new {@link Replacer} with the specified {@code replacements} and
	 * sets it as the default {@link Replacer} to use on this {@link MessagesFile}.
	 * <p>
	 * The difference between this method and {@link #setDefaultObjReplacer(Object...)}
	 * is that this method will get strings from this file for the replacements, so the
	 * replacements must be paths, take the following code as an example:
	 * <p>
	 * {@code setDefaultPathReplacer("%err%", "error");}
	 * <p>
	 * This default {@link Replacer} will replace every occurrence of "%error%" on the file
	 * with the string found at the "error" path of the file, not the literal string "error".
	 * 
	 * @param replacements the replacements to use for the new {@link Replacer}.
	 * 
	 * @throws IllegalArgumentException if {@code replacements} is {@code null} or the amount of
	 * objects is not even, more technically, if {@code replacements}
	 * size % 2 is not equal to 0 as specified by {@link Replacer#Replacer(Object...)}
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getDefaultReplacer()
	 * @see #setDefaultReplacer(Replacer)
	 * @see #setDefaultObjReplacer(Object...)
	 */
	public void setDefaultPathReplacer(@Nonnull String... replacements) {
		final String[] rep = new String[replacements.length];
		for (int i = 1; i < rep.length; i +=2)
			rep[i] = getString(rep[i]);
		this.defReplacer = new Replacer((Object[]) rep);
	}

	/**
	 * Gets a {@link Replacer#clone() clone} of the default {@link Replacer}
	 * being used by this file.
	 * 
	 * @return A {@link Replacer#clone() clone} of The default {@link Replacer},
	 * {@code null} if no {@link Replacer} has been specified or if it has been
	 * explicitly set to {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setDefaultReplacer(Replacer)
	 */
	@Nullable
	public Replacer getDefaultReplacer() {
		return defReplacer == null ? null : defReplacer.clone();
	}

	/*
	 * Message getters
	 */

	// Strings //

	@Nullable
	@Override
	public String getString(@Nonnull String path) {
		final String str = super.getString(path);
		return MCStrings.applyColor(defReplacer == null ? str : getDefaultReplacer().replaceAt(str));
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path, @Nonnull Replacer rep) {
		final String str = super.getString(path);
		final Replacer finalRep = defReplacer == null ? rep : getDefaultReplacer().add(rep);
		return MCStrings.applyColor(finalRep.replaceAt(str));
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path, @Nonnull Object... replacements) {
		final String str = super.getString(path);
		final Replacer finalRep = defReplacer == null ? new Replacer(replacements) : getDefaultReplacer().add(replacements);
		return MCStrings.applyColor(finalRep.replaceAt(str));
	}

	// Lists //

	private List<String> getReplacedList(@Nonnull String path, @Nullable Replacer replacer) {
		final List<String> lst = super.getStringList(path);
		if (lst.isEmpty())
			return null;
		if (replacer == null)
			return MCStrings.applyColor(lst);
		return MCLists.map(msg -> replacer.replaceAt(MCStrings.applyColor(msg)), lst);
	}

	@Nullable
	@Override
	public List<String> getStringList(@Nullable String path) {
		return getReplacedList(path, defReplacer);
	}

	@Nullable
	@Override
	public List<String> getStringList(@Nullable String path, @Nullable Replacer replacer) {
		return getReplacedList(path, defReplacer == null ? replacer : defReplacer.clone().add(replacer));
	}

	@Nullable
	@Override
	public List<String> getStringList(@Nullable String path, @Nonnull Object... replacements) {
		return getReplacedList(path, new Replacer(replacements));
	}
}
