package es.xdec0de.mcutils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

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

	/**
	 * <b>WARNING:</b> This method does <i>NOT</i> run asynchronously by itself, it's recommended to do so by using:
	 * {@link BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Consumer)}
	 * <p><p>
	 * Gets the latest version of a plugin on <b>consumer</b> using spigotmc's API.
	 * Version can take a time to update after a new resource update.
	 * <p><p>
	 * Example usage from a class extending {@link MCPlugin}:
	 * <p>
	 * <code>Bukkit.getScheduler().runTaskAsynchronously(this, () -> getLatestVersion(42, ver -> { } ));</code>
	 * 
	 * @param resourceId the ID of the resource to retrieve, the ID can be found at your resource url:
	 * <i>{@literal https://www.spigotmc.org/resources/<name>.<ID IS HERE>/}</i>
	 * @param consumer the consumer that will accept the version string, note that this string can be null if
	 * any error occurred while retrieving the latest version.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Bukkit#getScheduler()
	 * @see BukkitScheduler#runTaskAsynchronously(org.bukkit.plugin.Plugin, Consumer)
	 */
	public void getLatestVersion(int resourceId, Consumer<String> consumer) {
		try {
			InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
			Scanner scanner = new Scanner(inputStream);
			if (scanner.hasNext())
				consumer.accept(scanner.next());
			scanner.close();
		} catch (IOException ex) {
			consumer.accept(null);
		}
	}
}
