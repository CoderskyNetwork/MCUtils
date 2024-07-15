package net.codersky.mcutils.spigot.updaters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.spigot.updaters.sources.SpigotUpdaterSource;
import net.codersky.mcutils.spigot.updaters.sources.VersionInfo;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Interface used for sources that
 * an {@link UpdateChecker} may use to get the
 * latest version of a plugin.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #getName()
 * @see #getLatestVersion()
 * @see #getLatestVersion(boolean)
 */
public interface UpdaterSource {

	/**
	 * Gets the name of this {@link UpdaterSource source},
	 * such as "Spigot" for {@link SpigotUpdaterSource}.
	 * 
	 * @return The name of this {@link UpdaterSource source},
	 * never {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String getName();

	/**
	 * Gets the latest version available on this source.
	 * The version may be {@code null} to indicate that
	 * an error occurred while trying to get the version.
	 * 
	 * @param warnSync whether to warn or not about this method being
	 * called on Bukkit's main thread. Only set this to
	 * {@code false} if you are 100% sure that calling this
	 * method synchronously won't affect server performance
	 * (For example, at {@link JavaPlugin#onEnable()}).
	 * 
	 * @return The latest version available on this source,
	 * may be {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getLatestVersion()
	 */
	@Nullable
	public VersionInfo getLatestVersion(boolean warnSync);

	/**
	 * Gets the latest version available on this source.
	 * The version may be {@code null} to indicate that
	 * an error occurred while trying to get the version.
	 * <p><p>
	 * Running this method on Bukkit's main thread
	 * (Synchronously) will print a warning message, this
	 * is to help you out with possible performance issues,
	 * however, if you know what you are doing, you can use
	 * {@link #getLatestVersion(boolean)} to disable this
	 * warning feature.
	 * 
	 * @return The latest version available on this source,
	 * may be {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getLatestVersion(boolean)
	 */
	@Nullable
	public default VersionInfo getLatestVersion() {
		return getLatestVersion(true);
	}
}
