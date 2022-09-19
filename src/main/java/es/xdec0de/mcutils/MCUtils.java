package es.xdec0de.mcutils;

import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.mcutils.general.PlayerUtils;
import es.xdec0de.mcutils.guis.GUIHandler;
import es.xdec0de.mcutils.strings.MCStrings;

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

	private final MCStrings strings = new MCStrings(this);
	final HashMap<JavaPlugin, GUIHandler> guiHandlers = new HashMap<>();

	@Override
	public void onEnable() {
		logCol("&8[&6MCUtils&8] &aPlugin enabled &8| &bv"+getDescription().getVersion()+" &8| &bMC "+getServerVersion().getFormatName());
	}

	@Override
	public void onDisable() {
		logCol("&8[&6MCUtils&8] &cPlugin disabled &8| &bv"+getDescription().getVersion()+" &8| &bMC "+getServerVersion().getFormatName());
	}

	/**
	 * Gets an instance of {@link MCStrings}.
	 * 
	 * @return An instance of {@link MCStrings}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public MCStrings strings() {
		return strings;
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
}
