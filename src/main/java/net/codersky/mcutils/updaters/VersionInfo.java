package net.codersky.mcutils.updaters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A class that represents version information generated
 * by an {@link UpdaterChecker}. 
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 *
 * @see UpdateChecker#getLatestVersion()
 */
public class VersionInfo {

	private final UpdaterSource source;
	private final String version;

	VersionInfo(@Nullable  UpdaterSource source, @Nonnull String version) {
		this.source = source;
		this.version = version;
	}

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
	public UpdaterSource getSource() {
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
	public String getVersion() {
		return version;
	}
}
