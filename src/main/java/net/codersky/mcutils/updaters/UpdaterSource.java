package net.codersky.mcutils.updaters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.updaters.sources.SpigotUpdaterSource;

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
	 * @return The latest version available on this source,
	 * may be {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getLatestVersion();
}
