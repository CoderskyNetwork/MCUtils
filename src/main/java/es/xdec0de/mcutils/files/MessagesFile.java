package es.xdec0de.mcutils.files;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import es.xdec0de.mcutils.MCPlugin;
import es.xdec0de.mcutils.MCUtils;
import es.xdec0de.mcutils.general.Replacer;
import es.xdec0de.mcutils.strings.MCStrings;

/**
 * A class intended to be used to create
 * and use a messages file.
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
	 * Creates a message file for the specified plugin
	 * with the specified path, usually the path is just
	 * "messages", but you can choose whatever you want.
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, two examples are "messages.yml" and "lang/messages.yml"
	 * @param charset the {@link Charset} to use, if null, {@link Charsets#UTF_8} will be used. 
	 * @param defReplacer the default {@link Replacer} to use on every message.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 */
	protected MessagesFile(@Nonnull JavaPlugin plugin, @Nullable String path, @Nullable Charset charset) {
		super(plugin, path, charset);
	}

	/**
	 * Creates a message file for the specified plugin
	 * with the specified path, usually the path is just
	 * "messages", but you can choose whatever you want.
	 * {@link Charsets#UTF_8} will be used.
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, two examples are "messages.yml" and "lang/messages.yml"
	 * @param defReplacer the default {@link Replacer} to use on every message.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 */
	protected MessagesFile(@Nonnull JavaPlugin plugin, @Nullable String path) {
		this(plugin, path, Charsets.UTF_8);
	}

	/**
	 * Sets the default {@link Replacer} to be used on this {@link MessagesFile}.
	 * 
	 * @param replacer the default {@link Replacer} to be used on this {@link MessagesFile}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void setDefaultReplacer(Replacer replacer) {
		this.defReplacer = replacer;
	}

	/**
	 * Gets the default {@link Replacer} being used by this file.
	 * 
	 * @return The default replacer.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Replacer getDefaultReplacer() {
		return defReplacer == null ? null : defReplacer.clone();
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)},
	 * then applies {@link #getDefaultReplacer()} to it.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
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
	 * then applies {@link #getDefaultReplacer()} and <b>replacer</b> to it.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * @param replacer the {@link Replacer} to apply.
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String getString(@Nullable String path, Replacer replacer) {
		if (path == null)
			return null;
		String str = super.getString(path);
		return getDefaultReplacer() == null ? replacer.replaceAt(str) : getDefaultReplacer().add(replacer).replaceAt(str);
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)},
	 * then applies {@link #getDefaultReplacer()} and <b>replacements</b> to it.
	 * <p>
	 * If the path is null, null will always be returned,
	 * if no value exists for it and no default value was specified null will also be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * @param replacements the replacements to apply (See {@link Replacer} for more information).
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String getString(@Nullable String path, String... replacements) {
		if (path == null)
			return null;
		String str = super.getString(path);
		return defReplacer == null ? new Replacer(replacements).replaceAt(str) : defReplacer.add(replacements).replaceAt(str);
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()} to it.
	 * If the path is null or no value exists for it, null will be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getColoredString(@Nullable String path) {
		if (path == null)
			return null;
		String str = super.getString(path);
		return defReplacer == null ? strings.applyColor(str) : strings.applyColor(defReplacer.replaceAt(str));
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()}
	 * to it with <b>replaced</b> being added to {@link #getDefaultReplacer()}.
	 * If the path is null or no value exists for it, null will be returned.
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * @param replacer the {@link Replacer} to apply.
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getColoredString(@Nullable String path, @Nullable Replacer replacer) {
		if (path == null)
			return null;
		String str = super.getString(path);
		return defReplacer == null ? strings.applyColor(replacer.replaceAt(str)) : strings.applyColor(defReplacer.add(replacer).replaceAt(str));
	}

	/**
	 * Gets the requested String by path using {@link ConfigurationSection#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()}
	 * to it with the <b>replacements</b> being added to {@link #getDefaultReplacer()}.
	 * If the path is null or no value exists for it, null will be returned.
	 * <p>
	 * The <b>replacements</b> must not be null or empty and the count must be even as specified on {@link Replacer#add(String...)}
	 * 
	 * @param path the path of the String to get from {@link #getPath()}.
	 * @param replacements the replacements to apply (See {@link Replacer} for more information).
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
	 * 
	 * @throws IllegalArgumentException if <b>replacents</b> is null, empty or it's size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getColoredString(@Nullable String path, @Nonnull String... replacements) {
		if (path == null)
			return null;
		String str = super.getString(path);
		return defReplacer == null ? strings.applyColor(new Replacer(replacements).replaceAt(str)) : strings.applyColor(defReplacer.add(replacements).replaceAt(str));
	}

	/**
	 * Gets the requested list of strings with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} applied to every string on the list.
	 * 
	 * @param path the path at {@link #getPath()}
	 * 
	 * @return The requested list of strings, an empty list if the path doesn't exist or the list itself is empty.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> getColoredList(@Nullable String path) {
		if (path == null)
			return null;
		List<String> atCfg = getStringList(path);
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
	 * @param path the path at {@link #getPath()}
	 * @param replacer the replacer to add to the default replacer.
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
		List<String> atCfg = getStringList(path);
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
	 * @param path the path at {@link #getPath()}
	 * @param replacements the replacements to add to the default replacer.
	 * 
	 * @return The requested list of strings, an empty list if the path doesn't exist or the list itself is empty.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> getColoredList(@Nullable String path, @Nullable String... replacements) {
		if (path == null)
			return null;
		List<String> atCfg = getStringList(path);
		List<String> res = new ArrayList<>();
		if (atCfg == null || atCfg.isEmpty())
			return res; 
		Replacer rep = getDefaultReplacer() != null ? getDefaultReplacer().add(replacements) : new Replacer(replacements);
		for (String str : atCfg)
			res.add(strings.applyColor(rep.replaceAt(str)));
		return res;
	}

	/**
	 * Uses {@link #getColoredString(String)} and then sends the returning
	 * string to the specified <b>target</b>
	 * 
	 * @param path the path at {@link #getPath()}
	 * @param target the receiver.
	 * 
	 * @throws NullPointerException if <b>target</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void send(@Nullable String path, @Nonnull CommandSender target) {
		target.sendMessage(getColoredString(path));
	}

	/**
	 * Uses {@link #getColoredString(String, Replacer)} and then sends the returning
	 * string to the specified <b>target</b>
	 * 
	 * @param path the path at {@link #getPath()}
	 * @param target the receiver.
	 * @param replacer the replacer to add to the default replacer.
	 * 
	 * @throws NullPointerException if <b>target</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void send(@Nullable String path, @Nonnull CommandSender target, @Nullable Replacer replacer) {
		target.sendMessage(getColoredString(path, replacer));
	}

	/**
	 * Uses {@link #getColoredString(String, String...)} and then sends the returning
	 * string to the specified <b>target</b>
	 * 
	 * @param path the path at {@link #getPath()}
	 * @param target the receiver.
	 * @param replacements the replacements to add to the default replacer.
	 * 
	 * @throws NullPointerException if <b>target</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void send(@Nullable String path, @Nonnull CommandSender target, @Nullable String... replacements) {
		target.sendMessage(getColoredString(path, replacements));
	}
}
