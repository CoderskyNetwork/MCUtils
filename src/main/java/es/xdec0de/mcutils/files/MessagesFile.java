package es.xdec0de.mcutils.files;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.mcutils.MCPlugin;
import es.xdec0de.mcutils.MCUtils;
import es.xdec0de.mcutils.general.MCStrings;
import es.xdec0de.mcutils.general.Replacer;

/**
 * A class intended to be used to create
 * and use a messages file.
 * 
 * @since 1.0.0
 * 
 * @author xDec0de_
 */
public class MessagesFile extends PluginFile {

	final MCStrings strings = JavaPlugin.getPlugin(MCUtils.class).strings();
	private Replacer defReplacer;

	/**
	 * Creates a message file for the specified plugin and path,
	 * usually the path is just "messages", but you can choose whatever you want.
	 * This constructor doesn't specify a default {@link Replacer},
	 * meaning that the default replacer will be "%prefix%", "Prefix",
	 * with "Prefix" being the string under the path "Prefix" of <b>path</b>.yml
	 * <p><p>
	 * Take this example messages.yml file:
	 * <p>
	 * Prefix: "MCUtils: "
	 * <p><p>
	 * The replacer for %prefix% on every message would be "MCUtils: "
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, two examples are "messages" and "lang/messages",
	 * the .yml extension is automatically added, if the path is null, empty or blank, "messages" will
	 * be used.
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 */
	protected MessagesFile(@Nonnull MCPlugin plugin, @Nullable String path) {
		super(plugin, path, "messages");
		this.defReplacer = new Replacer("%prefix%", "Prefix");
	}

	/**
	 * Creates a message file for the specified plugin
	 * with the specified path, usually the path is just
	 * "messages.yml", but you can choose whatever you want.
	 * 
	 * @param plugin an instance of the plugin creating the file, used to get it's data folder.
	 * @param path the path of the file to create, two examples are "messages.yml" and "lang/messages.yml"
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 */
	protected MessagesFile(@Nonnull MCPlugin plugin, @Nullable String path, @Nullable Replacer defReplacer) {
		super(plugin, path);
		this.defReplacer = defReplacer != null ? defReplacer : new Replacer("%prefix%", "Prefix");
	}

	/**
	 * Gets the default {@link Replacer} being used by this file.
	 * 
	 * @return The default replacer, cannot be null.
	 */
	public Replacer getDefaultReplacer() {
		return defReplacer;
	}

	/**
	 * Gets the requested String by path with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} applied to it.
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * 
	 * @return The requested string, "null" if the path doesn't exist.
	 */
	public String get(String path) {
		return strings.applyColor(getDefaultReplacer().replaceAt(getFileConfig().getString(path)));
	}

	/**
	 * Gets the requested String by path with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} joined with <b>replacer</b> applied to it.
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * @param replacer the replacer to add to the default replacer.
	 * 
	 * @return The requested string, "null" if the path doesn't exist.
	 */
	public String get(String path, Replacer replacer) {
		return strings.applyColor(getDefaultReplacer().add(replacer).replaceAt(getFileConfig().getString(path)));
	}

	/**
	 * Gets the requested String by path with {@link MCStrings#applyColor(String)}
	 * and {@link #getDefaultReplacer()} with added <b>replacements</b> applied to it.
	 * 
	 * @param path the path at {@link #getPath()}.yml
	 * @param replacements the replacements to add to the default replacer.
	 * 
	 * @return The requested string, "null" if the path doesn't exist.
	 */
	public String get(String path, String... replacements) {
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
