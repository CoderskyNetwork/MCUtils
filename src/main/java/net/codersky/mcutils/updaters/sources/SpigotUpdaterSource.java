package net.codersky.mcutils.updaters.sources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.checkerframework.checker.index.qual.Positive;

import net.codersky.mcutils.updaters.UpdaterSource;

/**
 * {@link UpdaterSource} implementation for
 * <a href="https://api.spigotmc.org/">api.spigot.org</a>
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #SpigotUpdaterSource(long)
 * @see #getResourceId()
 * @see #getLatestVersion()
 */
public class SpigotUpdaterSource implements UpdaterSource {

	private final long resourceId;

	/**
	 * Creates a new {@link SpigotUpdaterSource} capable of checking
	 * for updates on <a href="https://api.spigotmc.org/">api.spigot.org</a>
	 * 
	 * @param resourceId the resource id of the plugin to check. This id
	 * can be obtained on the link of your resource.
	 * 
	 * @throws IllegalArgumentException if <b>resourceId</b> is minor or equal to zero.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public SpigotUpdaterSource(@Positive long resourceId) {
		if (resourceId <= 0)
			throw new IllegalArgumentException("Resource id must be higher than zero, " + resourceId + " was provided.");
		this.resourceId = resourceId;
	}

	/**
	 * Gets the resource id that will be used to get the
	 * latest version of a plugin.
	 * 
	 * @return The resource id that will be used to get the
	 * latest version of a plugin.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Positive
	public long getResourceId() {
		return resourceId;
	}

	/*
	 * UpdaterSource implementation
	 */

	@Nonnull
	public String getName() {
		return "Spigot";
	}

	@Nullable
	@Override
	public SpigotVersionInfo getLatestVersion() {
		try {
			final InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream();
			final Scanner scanner = new Scanner(inputStream);
			String ver = null;
			if (scanner.hasNext())
				ver = scanner.next();
			scanner.close();
			return ver == null ? null : new SpigotVersionInfo(this, ver);
		} catch (IOException ex) {
			return null;
		}
	}

	public class SpigotVersionInfo extends VersionInfo {

		private final String name;

		SpigotVersionInfo(SpigotUpdaterSource source, String name) {
			this.source = source;
			this.name = name;
		}

		@Override
		public String getVersion() {
			return name;
		}
	}
}
