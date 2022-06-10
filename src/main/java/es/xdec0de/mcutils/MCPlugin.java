package es.xdec0de.mcutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import es.xdec0de.mcutils.files.MCFile;
import es.xdec0de.mcutils.server.MCVersion;

public class MCPlugin extends JavaPlugin {

	private final List<MCFile> files = new ArrayList<>();

	public static <T extends MCPlugin> T getMCPlugin(Class<T> plugin) {
		return JavaPlugin.getPlugin(plugin);
	}

	public <T extends MCFile> boolean registerFile(T file) {
		return files.add(file);
	}

	public <T extends MCFile> T registerFile(String path, Class<T> type) {
		try {
			Constructor<T> constructor = type.getDeclaredConstructor(JavaPlugin.class, String.class, String.class);
			return constructor.newInstance(this, path, "default");
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

	public void reload() {
		files.forEach(file -> file.reload(true));
	}

	public MCVersion getServerVersion() {
		String bukkitVer = Bukkit.getVersion();
		for(MCVersion version : MCVersion.values())
			if(bukkitVer.contains(version.getFormatName()))
				return version;
		return MCVersion.UNKNOWN;
	}
}
