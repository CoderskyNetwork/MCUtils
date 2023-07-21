package me.xdec0de.mcutils;

import java.lang.reflect.Field;
import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.xdec0de.mcutils.files.PluginFile;
import me.xdec0de.mcutils.general.PlayerUtils;
import me.xdec0de.mcutils.guis.GUIHandler;
import me.xdec0de.mcutils.strings.ActionBar;
import me.xdec0de.mcutils.strings.Gradient;
import me.xdec0de.mcutils.strings.Hex;
import me.xdec0de.mcutils.strings.MCStrings;
import me.xdec0de.mcutils.strings.TargetPattern;

/**
 * The main class of the MCUtils API, used
 * to access the majority of the features of
 * said API.
 * <p>
 * <b>Getting an instance on a class extending {@link MCPlugin}:</b>
 * <p>
 * <code>MCUtils mcUtils = this.getMCUtils();
 * <p>
 * MCStrings strings = mcUtils.strings();
 * </code>
 * <p>
 * <b>Getting an instance on any class <i>(Not recommended, use constructor parameters):</i></b>
 * <p>
 * <code>MCUtils mcUtils = JavaPlugin.getPlugin(MCUtils.class);
 * <p>
 * MCStrings strings = mcUtils.strings();
 * </code>
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class MCUtils extends MCPlugin {

	private final HashMap<JavaPlugin, GUIHandler> guiHandlers = new HashMap<>();

	private SimpleCommandMap commandMap;

	@Override
	public void onEnable() {
		registerFile("config.yml", PluginFile.class); // Added to #getConfig() by MCPlugin
		MCStrings.addColorPattern("gradient", new Gradient());
		MCStrings.addColorPattern("hex", new Hex());
		MCStrings.addColorPattern("classic", (str, simple) -> MCStrings.applyColorChar('&', str));
		MCStrings.addFormatPattern(new ActionBar());
		MCStrings.addFormatPattern(new TargetPattern());
		logCol("&8[&6MCUtils&8] &aPlugin enabled &8| &bv"+getDescription().getVersion()+" &8| &bMC "+getServerVersion());
	}

	@Override
	public void onDisable() {
		logCol("&8[&6MCUtils&8] &cPlugin disabled &8| &bv"+getDescription().getVersion()+" &8| &bMC "+getServerVersion());
	}

	/**
	 * Gets an instance of {@link PlayerUtils}, this
	 * method exists purely for accessibility, you
	 * can just call {@link PlayerUtils}'s constructor.
	 * 
	 * @return An instance of {@link PlayerUtils}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public PlayerUtils players() {
		return new PlayerUtils();
	}

	/**
	 * Gets the {@link GUIHandler} used by <b>plugin</b>,
	 * if no {@link GUIHandler} exists, one will be created.
	 * 
	 * @param plugin the plugin to get the {@link GUIHandler} from,
	 * if null, null will be returned.
	 * 
	 * @return The {@link GUIHandler} used by <b>plugin</b>.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public GUIHandler getGUIHandler(@Nullable JavaPlugin plugin) {
		if (plugin == null)
			return null;
		if (!guiHandlers.containsKey(plugin))
			guiHandlers.put(plugin, registerEvents(new GUIHandler()));
		return guiHandlers.get(plugin);
	}

	/**
	 * Checks if the version of MCUtils installed on the server supports the
	 * specified <b>version</b>. Supported formats are <i>"X.X.X"</i> and <i>"X.X.XbX"</i>.
	 * <br><br>
	 * <b>Version convention</b>:
	 * <br>
	 * MCUtils versions work this way: (major).(minor).(revision)b(build), so, for example
	 * , <i>"1.0.0b1"</i>, this is highly unlikely to change and you shouldn't be checking
	 * for specific builds but revisions at most as builds are intended for testing, not production.
	 * 
	 * @param version the MCUtils version to check, for example, <i>"1.0.0"</i>.
	 * 
	 * @return true if the installed version of MCUtils on the server
	 * is higher or equal to the specified <b>version</b>, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean supports(@Nonnull String version) {
		if (!MCStrings.hasContent(version))
			return false;
		float[] versions = new float[2]; // Convert to float, so 1.3.1b1 would be 131.1
		for (int v = 0; v <= 1; v++) {
			String ver = v == 0 ? version : getDescription().getVersion();
			boolean decimal = false;
			for (int i = 0; i < ver.length(); i++) {
				char c = ver.charAt(i);
				if (c == 'b')
					decimal = true;
				if (c >= '0' && c <= '9')
					versions[v] = decimal ? versions[v] + ((c - '0') / 10.0f) : (versions[v] * 10) + (c - '0');
			}
		}
		return versions[0] >= versions[1];
	}

	final SimpleCommandMap getCommandMap() {
		if (commandMap != null)
			return commandMap;
		try {
			SimplePluginManager manager = ((SimplePluginManager)getServer().getPluginManager());
			Field map = SimplePluginManager.class.getDeclaredField("commandMap");
			map.setAccessible(true);
			commandMap = (SimpleCommandMap) map.get(manager);
			return commandMap;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			logException(e, "Unable to get command map.");
			Bukkit.getPluginManager().disablePlugin(this);
			return null;
		}
	}
}
