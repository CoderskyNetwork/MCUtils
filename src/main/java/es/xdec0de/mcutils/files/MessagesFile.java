package es.xdec0de.mcutils.files;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import es.xdec0de.mcutils.MCPlugin;
import es.xdec0de.mcutils.MCUtils;
import es.xdec0de.mcutils.general.MCStrings;
import es.xdec0de.mcutils.general.Replacer;

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
	private final Replacer defReplacer;

	protected MessagesFile(@Nonnull MCPlugin plugin, @Nullable String path, @Nullable Charset charset, @Nullable Replacer defReplacer) {
		super(plugin, path, charset);
		this.defReplacer = defReplacer != null ? defReplacer : new Replacer("%prefix%", "Prefix");
	}

	/**
	 * Creates a message file for the specified plugin
	 * with the specified path, usually the path is just
	 * "messages.yml", but you can choose whatever you want.
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, two examples are "messages.yml" and "lang/messages.yml"
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 */
	protected MessagesFile(@Nonnull MCPlugin plugin, @Nullable String path, @Nullable Replacer defReplacer) {
		this(plugin, path, Charsets.UTF_8, defReplacer);
	}

	/**
	 * Gets the default {@link Replacer} being used by this file.
	 * 
	 * @return The default replacer, cannot be null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Replacer getDefaultReplacer() {
		return defReplacer;
	}

	/**
	 * Gets the requested String by path using {@link FileConfiguration#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()} to it.
	 * If the path is null or no value exists for it, null will be returned.
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String get(@Nullable String path) {
		return strings.applyColor(getDefaultReplacer().replaceAt(getFileConfig().getString(path)));
	}

	/**
	 * Gets the requested String by path using {@link FileConfiguration#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()}
	 * to it with <b>replaced</b> being added to {@link #getDefaultReplacer()}.
	 * If the path is null or no value exists for it, null will be returned.
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String get(String path, Replacer replacer) {
		return strings.applyColor(getDefaultReplacer().add(replacer).replaceAt(getFileConfig().getString(path)));
	}

	/**
	 * Gets the requested String by path using {@link FileConfiguration#getString(String)}
	 * and then applies {@link MCStrings#applyColor(String)} and {@link #getDefaultReplacer()}
	 * to it with the <b>replacements</b> being added to {@link #getDefaultReplacer()}.
	 * If the path is null or no value exists for it, null will be returned.
	 * <p>
	 * The <b>replacements</b> must not be null or empty and the count must be even as specified on {@link Replacer#add(String...)}
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * 
	 * @return The requested string, null if no value for the path exists or the path is null.
	 * 
	 * @throws IllegalArgumentException if <b>replacents</b> is null, empty or it's size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public String get(@Nullable String path, @Nonnull String... replacements) {
		return strings.applyColor(getDefaultReplacer().add(replacements).replaceAt(getFileConfig().getString(path)));
	}

	/**
	 * Gets the requested list of strings with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} applied to every string on the list.
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * 
	 * @return The requested list of strings, an empty list if the path doesn't exist or the list itself is empty.
	 */
	public List<String> getList(String path) {
		List<String> atCfg = getFileConfig().getStringList(path);
		List<String> res = new ArrayList<>();
		if (atCfg == null || atCfg.isEmpty())
			return res; 
		Replacer rep = getDefaultReplacer();
		for (String str : atCfg)
			res.add(strings.applyColor(rep.replaceAt(str)));
		return res;
	}

	/**
	 * Gets the requested list of strings with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} joined with <b>replacer</b> applied to every
	 * string on the list.
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * @param replacer the replacer to add to the default replacer.
	 * 
	 * @return The requested list of strings, an empty list if the path doesn't exist or the list itself is empty.
	 */
	public List<String> getList(String path, Replacer replacer) {
		List<String> atCfg = getFileConfig().getStringList(path);
		List<String> res = new ArrayList<>();
		if (atCfg == null || atCfg.isEmpty())
			return res; 
		Replacer rep = getDefaultReplacer().add(replacer);
		for (String str : atCfg)
			res.add(strings.applyColor(rep.replaceAt(str)));
		return res;
	}

	/**
	 * Gets the requested list of strings with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} with added <b>replacements</b> applied to every
	 * string on the list.
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * @param replacements the replacements to add to the default replacer.
	 * 
	 * @return The requested list of strings, an empty list if the path doesn't exist or the list itself is empty.
	 */
	public List<String> getList(String path, String... replacements) {
		List<String> atCfg = getFileConfig().getStringList(path);
		List<String> res = new ArrayList<>();
		if (atCfg == null || atCfg.isEmpty())
			return res; 
		Replacer rep = getDefaultReplacer().add(replacements);
		for (String str : atCfg)
			res.add(strings.applyColor(rep.replaceAt(str)));
		return res;
	}

	/**
	 * Uses {@link #get(String)} and then sends the returning
	 * string to the specified <b>target</b>
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * @param target the receiver.
	 */
	public void send(String path, CommandSender target) {
		target.sendMessage(get(path));
	}

	/**
	 * Uses {@link #get(String, Replacer)} and then sends the returning
	 * string to the specified <b>target</b>
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * @param target the receiver.
	 * @param replacer the replacer to add to the default replacer.
	 */
	public void send(String path, CommandSender target, Replacer replacer) {
		target.sendMessage(get(path, replacer));
	}

	/**
	 * Uses {@link #get(String, String...)} and then sends the returning
	 * string to the specified <b>target</b>
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * @param target the receiver.
	 * @param replacements the replacements to add to the default replacer.
	 */
	public void send(String path, CommandSender target, String... replacements) {
		target.sendMessage(get(path, replacements));
	}
}
