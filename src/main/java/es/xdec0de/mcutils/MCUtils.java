package es.xdec0de.mcutils;

import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.mcutils.general.MCStrings;

public class MCUtils extends JavaPlugin {

	private final MCStrings strings = new MCStrings();

	@Override
	public void onEnable() {
		
	}

	@Override
	public void onDisable() {
		
	}

	public MCStrings strings() {
		return strings;
	}
}
