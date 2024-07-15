package net.codersky.mcutils.spigot;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The MCUtils class, this class is not intended to be
 * used as it provides nothing, just an empty {@link #onEnable()}
 * method, this class is here to make testing easier so MCUtils can
 * run as a plugin instead of a shaded library (Used to test MCUtils
 * changes locally as an alternative to maven local).
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public class MCUtilsSpigot extends JavaPlugin {

	/**
	 * Codacy was complaining about this method not
	 * being documented while being empty, so yeah,
	 * here is some documentation to it, this method
	 * is here so MCUtils can be used as a plugin for
	 * testing changes to it without pushing to the
	 * maven repository.
	 */
	@Override
	public void onEnable() {
		// This method is intentionally empty, Codacy, please, stop complaining :(
	}
}
