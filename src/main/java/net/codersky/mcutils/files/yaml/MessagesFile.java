package net.codersky.mcutils.files.yaml;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.codersky.mcutils.MCPlugin;
import net.codersky.mcutils.files.MessagesFileHolder;
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
 * {@link MCStrings#sendFormattedMessage(CommandSender, String)}.
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
	 * @param numSupport whether to use numeric support or not (See {@link #setNumSupport(boolean)}).
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getDefaultReplacer()
	 * @see #setNumSupport(boolean)
	 * @see Replacer#setNumSupport(boolean)
	 */
	public void setDefaultReplacer(@Nullable Replacer replacer) {
		this.defReplacer = replacer;
	}

	/**
	 * Gets the default {@link Replacer} being used by this file.
	 * 
	 * @return The default {@link Replacer}, null if no {@link Replacer} has been
	 * specified or if it has been explicitly set to null.
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
		return MCStrings.applyColor(defReplacer == null ? str : defReplacer.replaceAt(str));
	}

	@Nullable
	@Override
	public String getString(@Nonnull String path, @Nonnull Replacer rep) {
		final String str = super.getString(path);
		final Replacer finalRep = defReplacer == null ? rep : defReplacer.add(rep);
		return MCStrings.applyColor(finalRep.replaceAt(str));
	}

	// Lists //

	private List<String> getReplacedList(@Nonnull String path, @Nullable Replacer replacer) {
		final List<String> atCfg = super.getStringList(path);
		if (atCfg == null || atCfg.isEmpty() || replacer == null)
			return atCfg;
		final List<String> replaced = new ArrayList<>(atCfg.size());
		for (String msg : atCfg)
			replaced.add(replacer.replaceAt(MCStrings.applyColor(msg)));
		return replaced;
	}

	@Nullable
	@Override
	public List<String> getStringList(@Nullable String path) {
		return getReplacedList(path, defReplacer);
	}

	@Nullable
	@Override
	public List<String> getStringList(@Nullable String path, @Nullable Replacer replacer) {
		return getReplacedList(path, defReplacer == null ? replacer : defReplacer.add(replacer));
	}
}
