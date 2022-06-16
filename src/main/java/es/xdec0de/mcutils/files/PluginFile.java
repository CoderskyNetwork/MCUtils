package es.xdec0de.mcutils.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;

import es.xdec0de.mcutils.MCPlugin;
import es.xdec0de.mcutils.general.Replacer;

public class PluginFile extends YmlFile {

	private File file;

	PluginFile(JavaPlugin plugin, String path, String pathIfInvalid, Charset charset) {
		super(plugin, path, pathIfInvalid, charset);
	}

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
	 * the .yml extension is automatically added, if the path is null, empty or blank, "config" will
	 * be used.
	 * 
	 * @see MCPlugin#registerFile(PluginFile)
	 * @see MCPlugin#registerFile(String, Class)
	 */
	protected PluginFile(JavaPlugin plugin, String path) {
		this(plugin, path, "config", Charsets.UTF_8);
	}

	protected PluginFile(JavaPlugin plugin, String path, Charset charset) {
		this(plugin, path, "config", charset);
	}

	/**
	 * Creates this file, the file MUST be present
	 * on the plugin's jar file as a resource.
	 * This is required in order to copy the file from
	 * the plugin source and updating it. If you don't need
	 * this functionality and want an empty file instead, use
	 * {@link YmlFile}<p>
	 * This method is not required when calling
	 * {@link MCPlugin#registerFile(String, Class)}
	 * as it does this automatically.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getFileConfig()
	 */
	@Override
	public void create() {
		if(!plugin.getDataFolder().exists())
			plugin.getDataFolder().mkdir();
		if(!(file = new File(plugin.getDataFolder(), getPath())).exists())
			plugin.saveResource(getPath(), false);
		reload(false);
		update(false, new ArrayList<>(0));
	}

	/**
	 * Reloads this file without updating,
	 * use {@link #reload(boolean)} to update.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload(boolean)
	 */
	@Override
	public void reload() {
		try {
			cfg.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reloads this file.
	 * 
	 * @param update whether to update the files or not.
	 * This is recommended to be true to prevent any
	 * missing path after a file modification, if you
	 * want some paths to be ignored by the updater, use
	 * {@link #reload(List)}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload(List)
	 */
	public void reload(boolean update) {
		if (update)
			update(true, new ArrayList<>(0));
		try {
			cfg.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reloads this file. The file updater ignores the paths present
	 * on <b>ignoredUpdatePaths</b>, this is specially useful
	 * if you want administrators to create their own paths
	 * without them being removed, for example, on a GUI plugin.
	 * 
	 * @param ignoredUpdatePaths a list with the paths to
	 * be ignored by the file updater, update is assumed
	 * to be true with this method, if you want to reload
	 * without updating use {@link #reload(boolean)} with
	 * <b>update</b> as false.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #reload(boolean)
	 */
	public void reload(@Nullable List<String> ignoredUpdatePaths) {
		if (ignoredUpdatePaths == null)
			reload(false);
		else
			update(true, ignoredUpdatePaths);
		CharsetYamlConfiguration chYaml = new CharsetYamlConfiguration(Charsets.UTF_8);
		try {
			chYaml.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}

	private File copyInputStreamToFile(String path, InputStream inputStream) {
		File file = new File(path);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int read;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1)
				outputStream.write(bytes, 0, read);
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean update(boolean reload, List<String> ign) {
		String pluginName = plugin.getName();
		try {
			int changes = 0;
			CharsetYamlConfiguration updated = new CharsetYamlConfiguration(Charsets.UTF_8);
			if(plugin.getResource(getPath()) != null)
				updated.load(copyInputStreamToFile(plugin.getDataFolder()+ "/"+getPath(), plugin.getResource(getPath())));
			else {
				log("&8[&4"+pluginName+"&8] > &cCould not update &6"+getPath());
				return false;
			}
			Set<String> oldKeys = ign.isEmpty() ? cfg.getKeys(true) : cfg.getKeys(true).stream().filter(str -> !ign.contains(str)).collect(Collectors.toSet());
			Set<String> updKeys = ign.isEmpty() ? updated.getKeys(true) : updated.getKeys(true).stream().filter(str -> !ign.contains(str)).collect(Collectors.toSet());
			for(String str : oldKeys)
				if(!updKeys.contains(str)) {
					cfg.set(str, null);
					changes++;
				}
			for(String str : updKeys)
				if(!oldKeys.contains(str)) {
					cfg.set(str, updated.get(str));
					changes++;
				}
			cfg.save(plugin.getDataFolder() + "/"+getPath());
			if(changes != 0) {
				log(" ");
				if(reload)
					log("&8[&6"+pluginName+"&8] > &6"+getPath()+" &7has been reloaded with &b"+changes+" &7changes.");
				else
					log("&8[&6"+pluginName+"&8] > &6"+getPath()+" &7has been updated to &ev"+plugin.getDescription().getVersion()+"&7 with &b"+changes+" &7changes.");
				return true;
			}
			return false;
		} catch(InvalidConfigurationException | IOException ex) {
			log("&8[&4"+pluginName+"&8] > &cCould not update &6"+getPath());
			return false;
		}
	}

	private void log(String str) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', str));
	}
}
