package me.xdec0de.mcutils.files;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import me.xdec0de.mcutils.MCPlugin;
import me.xdec0de.mcutils.MCUtils;
import me.xdec0de.mcutils.general.Replacer;
import me.xdec0de.mcutils.strings.MCStrings;

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
public class MessagesFile extends PluginFile {

	/** An instance of {@link MCStrings} used for the messages methods */
	final MCStrings strings = JavaPlugin.getPlugin(MCUtils.class).strings();
	private Replacer defReplacer;

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
	 * 
	 * Replacer handling
	 * 
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
	public void setDefaultReplacer(@Nullable Replacer replacer, boolean numSupport) {
		(this.defReplacer = replacer).setNumSupport(numSupport);
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

	/**
	 * This method toggles a feature from the {@link Replacer} class, and it is essentially
	 * a shortcut to {@link Replacer#setNumSupport(boolean)} on {@link #getDefaultReplacer()},
	 * you can see a detailed explanation on said method.
	 * 
	 * @param support true to enable, false to disable.
	 */
	public void setNumSupport(boolean support) {
		this.defReplacer.setNumSupport(support);
	}

	/*
	 * 
	 * Message getters
	 * 
	 */

	// Strings //

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)},
	 * then applies {@link #getDefaultReplacer()} to it.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * 
	 * @return The requested string, null if no value for the path exists and no
	 * default value was specified or the path is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Override
	public String getString(@Nullable String path) {
		if (path == null)
			return null;
		String str = super.getString(path);
		return getDefaultReplacer() == null ? str : getDefaultReplacer().replaceAt(str);
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)},
	 * then applies {@link #getDefaultReplacer()} to it with <b>replacer</b> being added to {@link #getDefaultReplacer()}.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * @param replacer the {@link Replacer} to apply.
	 * 
	 * @return The requested string, null if no value for the path exists and no
	 * default value was specified or the path is null.
	 * 
	 * @throws IllegalArgumentException if <b>replacer</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String getString(@Nullable String path, @Nonnull Replacer replacer) {
		if (path == null)
			return null;
		if (replacer == null)
			throw new IllegalArgumentException("Replacer cannot be null.");
		String str = super.getString(path);
		return getDefaultReplacer() == null ? replacer.replaceAt(str) : getDefaultReplacer().add(replacer).replaceAt(str);
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)},
	 * then applies {@link #getDefaultReplacer()} to it with <b>replacements</b> being added to {@link #getDefaultReplacer()}.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * <p>
	 * <b>replacements</b> must not be null and it's size must be even as specified on {@link Replacer#add(String...)}
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * @param replacements the replacements to apply (See {@link Replacer} for more information).
	 * 
	 * @return The requested string, null if no value for the path exists and no
	 * default value was specified or the path is null.
	 * 
	 * @throws IllegalArgumentException if <b>replacements</b> null or it's size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String getString(@Nullable String path, @Nonnull String... replacements) {
		if (path == null)
			return null;
		if (replacements == null)
			throw new IllegalArgumentException("Replacements cannot be null.");
		String str = super.getString(path);
		return getDefaultReplacer() == null ? new Replacer(replacements).replaceAt(str) : getDefaultReplacer().add(replacements).replaceAt(str);
	}

	// Colored strings //

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()} to it.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * 
	 * @return The requested colored string, null if no value for the path exists and no
	 * default value was specified or the path is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getColoredString(@Nullable String path) {
		if (path == null)
			return null;
		String str = super.getString(path);
		return getDefaultReplacer() == null ? strings.applyColor(str) : strings.applyColor(getDefaultReplacer().replaceAt(str));
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()}
	 * to it with <b>replacer</b> being added to {@link #getDefaultReplacer()}.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * @param replacer the {@link Replacer} to apply.
	 * 
	 * @return The requested colored string, null if no value for the path exists and no
	 * default value was specified or the path is null.
	 * 
	 * @throws IllegalArgumentException if <b>replacer</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getColoredString(@Nullable String path, @Nonnull Replacer replacer) {
		if (path == null)
			return null;
		if (replacer == null)
			throw new IllegalArgumentException("Replacer cannot be null.");
		String str = super.getString(path);
		return getDefaultReplacer() == null ? strings.applyColor(replacer.replaceAt(str)) : strings.applyColor(getDefaultReplacer().add(replacer).replaceAt(str));
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()}
	 * to it with the <b>replacements</b> being added to {@link #getDefaultReplacer()}.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * <p>
	 * The <b>replacements</b> must not be null and it's size must be even as specified on {@link Replacer#add(String...)}
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * @param replacements the replacements to apply (See {@link Replacer} for more information).
	 * 
	 * @return The requested colored string, null if no value for the path exists and no
	 * default value was specified or the path is null.
	 * 
	 * @throws IllegalArgumentException if <b>replacements</b> is null or it's size size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getColoredString(@Nullable String path, @Nonnull String... replacements) {
		if (path == null)
			return null;
		if (replacements == null)
			throw new IllegalArgumentException("Replacements cannot be null.");
		String str = super.getString(path);
		return getDefaultReplacer() == null ?
				strings.applyColor(new Replacer(replacements).replaceAt(str)) : strings.applyColor(getDefaultReplacer().add(replacements).replaceAt(str));
	}

	// Lists //

	// Colored Lists //

	/**
	 * Gets the requested list of strings with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} applied to every string on the list.
	 * 
	 * @param path the path of the List to get from {@link #getPath()}.
	 * 
	 * @return The requested colored list of strings, an empty list if the path doesn't
	 * exist or the list itself is empty, null if <b>path</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> getColoredList(@Nullable String path) {
		if (path == null)
			return null;
		List<String> atCfg = super.getStringList(path);
		List<String> res = new ArrayList<>();
		if (atCfg == null || atCfg.isEmpty())
			return res; 
		Replacer rep = getDefaultReplacer();
		if (rep != null)
			for (String str : atCfg)
				res.add(strings.applyColor(rep.replaceAt(str)));
		else
			for (String str : atCfg)
				res.add(strings.applyColor(str));
		return res;
	}

	/**
	 * Gets the requested list of strings with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} joined with <b>replacer</b> applied to every
	 * string on the list.
	 * 
	 * @param path the path of the List to get from {@link #getPath()}.
	 * @param replacer the {@link Replacer} to apply.
	 * 
	 * @return The requested colored list of strings, an empty list if the path doesn't
	 * exist or the list itself is empty, null if <b>path</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> getColoredList(@Nullable String path, @Nullable Replacer replacer) {
		if (path == null)
			return null;
		if (replacer == null)
			throw new IllegalArgumentException("Replacer cannot be null.");
		List<String> atCfg = super.getStringList(path);
		List<String> res = new ArrayList<>();
		if (atCfg == null || atCfg.isEmpty())
			return res; 
		Replacer rep = getDefaultReplacer() != null ? getDefaultReplacer().add(replacer) : replacer;
		for (String str : atCfg)
			res.add(strings.applyColor(rep.replaceAt(str)));
		return res;
	}

	/**
	 * Gets the requested list of strings with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} with added <b>replacements</b> applied to every
	 * string on the list.
	 * 
	 * @param path the path of the List to get from {@link #getPath()}.
	 * @param replacements the replacements to apply (See {@link Replacer} for more information).
	 * 
	 * @return The requested colored list of strings, an empty list if the path doesn't
	 * exist or the list itself is empty, null if <b>path</b> is null.
	 * 
	 * @throws IllegalArgumentException if <b>replacements</b> is null or it's size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> getColoredList(@Nullable String path, @Nullable String... replacements) {
		if (path == null)
			return null;
		if (replacements == null)
			throw new IllegalArgumentException("Replacements cannot be null.");
		List<String> atCfg = super.getStringList(path);
		List<String> res = new ArrayList<>();
		if (atCfg == null || atCfg.isEmpty())
			return res; 
		Replacer rep = getDefaultReplacer() != null ? getDefaultReplacer().add(replacements) : new Replacer(replacements);
		for (String str : atCfg)
			res.add(strings.applyColor(rep.replaceAt(str)));
		return res;
	}

	/*
	 *
	 * Message sending
	 * 
	 */

	/**
	 * Uses {@link #getString(String)} and then sends the returning
	 * string to the specified <b>target</b>.
	 * <p>
	 * <b>Formatted messages</b> are supported by this method <i>(See {@link MCStrings#sendFormattedMessage(CommandSender, String)})</i>
	 * 
	 * @param target the receiver.
	 * @param path the path of the message to get from {@link #getPath()}.
	 * 
	 * @return Always true, to make sending messages on commands easier. If you want to check if
	 * the message will be sent, use {@link #getString(String)} with the message <b>path</b>, the returned
	 * string must not be null and {@link String#isEmpty()} must return false with it in order to be sent.
	 * 
	 * @throws IllegalArgumentException if <b>target</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean send(@Nonnull CommandSender target, @Nullable String path) {
		return strings.sendFormattedMessage(target, getString(path));
	}

	/**
	 * Uses {@link #getString(String, Replacer)} and then sends the returning
	 * string to the specified <b>target</b>.
	 * <p>
	 * <b>Formatted messages</b> are supported by this method <i>(See {@link MCStrings#sendFormattedMessage(CommandSender, String)})</i>
	 * 
	 * @param target the receiver.
	 * @param path the path of the message to get from {@link #getPath()}.
	 * @param replacer the {@link Replacer} to apply.
	 * 
	 * @return Always true, to make sending messages on commands easier. If you want to check if
	 * the message will be sent, use {@link #getString(String)} with the message <b>path</b>, the returned
	 * string must not be null and {@link String#isEmpty()} must return false with it in order to be sent.
	 * 
	 * @throws IllegalArgumentException if <b>target</b> or <b>replacer</b> are null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean send(@Nonnull CommandSender target, @Nullable String path, @Nonnull Replacer replacer) {
		return strings.sendFormattedMessage(target, getString(path, replacer));
	}

	/**
	 * Uses {@link #getString(String, String...)} and then sends the returning
	 * string to the specified <b>target</b>.
	 * <p>
	 * <b>Formatted messages</b> are supported by this method <i>(See {@link MCStrings#sendFormattedMessage(CommandSender, String)})</i>
	 * 
	 * @param target the receiver.
	 * @param path the path of the message to get from {@link #getPath()}.
	 * @param replacements the replacements to apply (See {@link Replacer} for more information).
	 * 
	 * @return Always true, to make sending messages on commands easier. If you want to check if
	 * the message will be sent, use {@link #getString(String)} with the message <b>path</b>, the returned
	 * string must not be null and {@link String#isEmpty()} must return false with it in order to be sent.
	 * 
	 * @throws IllegalArgumentException if <b>target</b> or <b>replacements</b> are null or if <b>replacements</b> size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean send(@Nonnull CommandSender target, @Nullable String path, @Nonnull String... replacements) {
		return strings.sendFormattedMessage(target, getString(path, replacements));
	}

	/**
	 * Uses {@link #getColoredString(String)} and then sends the returning
	 * string to the specified <b>target</b>.
	 * <p>
	 * <b>Formatted messages</b> are supported by this method <i>(See {@link MCStrings#sendFormattedMessage(CommandSender, String)})</i>
	 * 
	 * @param target the receiver.
	 * @param path the path of the message to get from {@link #getPath()}.
	 * 
	 * @return Always true, to make sending messages on commands easier. If you want to check if
	 * the message will be sent, use {@link #getString(String)} with the message <b>path</b>, the returned
	 * string must not be null and {@link String#isEmpty()} must return false with it in order to be sent.
	 * 
	 * @throws IllegalArgumentException if <b>target</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean sendColored(@Nonnull CommandSender target, @Nullable String path) {
		return strings.sendFormattedMessage(target, getColoredString(path));
	}

	/**
	 * Uses {@link #getColoredString(String, Replacer)} and then sends the returning
	 * string to the specified <b>target</b>.
	 * <p>
	 * <b>Formatted messages</b> are supported by this method <i>(See {@link MCStrings#sendFormattedMessage(CommandSender, String)})</i>
	 * 
	 * @param target the receiver.
	 * @param path the path of the message to get from {@link #getPath()}.
	 * @param replacer the {@link Replacer} to apply.
	 * 
	 * @return Always true, to make sending messages on commands easier. If you want to check if
	 * the message will be sent, use {@link #getString(String)} with the message <b>path</b>, the returned
	 * string must not be null and {@link String#isEmpty()} must return false with it in order to be sent.
	 * 
	 * @throws IllegalArgumentException if <b>target</b> or <b>replacer</b> are null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean sendColored(@Nonnull CommandSender target, @Nullable String path, @Nonnull Replacer replacer) {
		return strings.sendFormattedMessage(target, getColoredString(path, replacer));
	}

	/**
	 * Uses {@link #getColoredString(String, String...)} and then sends the returning
	 * string to the specified <b>target</b>.
	 * <p>
	 * <b>Formatted messages</b> are supported by this method <i>(See {@link MCStrings#sendFormattedMessage(CommandSender, String)})</i>
	 * 
	 * @param target the receiver.
	 * @param path the path of the message to get from {@link #getPath()}.
	 * @param replacements the replacements to apply (See {@link Replacer} for more information).
	 * 
	 * @return Always true, to make sending messages on commands easier. If you want to check if
	 * the message will be sent, use {@link #getString(String)} with the message <b>path</b>, the returned
	 * string must not be null and {@link String#isEmpty()} must return false with it in order to be sent.
	 * 
	 * @throws IllegalArgumentException if <b>target</b> or <b>replacements</b> are null or if <b>replacements</b> size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean sendColored(@Nonnull CommandSender target, @Nullable String path, @Nonnull String... replacements) {
		return strings.sendFormattedMessage(target, getColoredString(path, replacements));
	}
}
