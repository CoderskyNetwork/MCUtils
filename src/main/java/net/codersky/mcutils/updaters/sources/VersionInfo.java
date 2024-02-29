package net.codersky.mcutils.updaters.sources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.updaters.UpdateChecker;
import net.codersky.mcutils.updaters.UpdaterSource;

/**
 * An interface that represents version information generated
 * by an {@link UpdaterSource}.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 *
 * @see UpdateChecker#getLatestVersion()
 */
public abstract class VersionInfo {

	protected UpdaterSource source;

	VersionInfo() {}

	/**
	 * The {@link UpdaterSource} that provided this info.
	 * This may be {@code null} if no source provided the version,
	 * in that case, {@link #getVersion()} will be the current
	 * version of the plugin. 
	 * 
	 * @return The {@link UpdaterSource} that provided this info.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public final UpdaterSource getSource() {
		return source;
	}

	/**
	 * Returns the never {@code null} version {@link String}.
	 * 
	 * @return The version {@link String}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public abstract String getVersion();
}
