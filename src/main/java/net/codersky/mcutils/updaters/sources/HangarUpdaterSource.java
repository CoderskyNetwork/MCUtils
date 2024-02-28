package net.codersky.mcutils.updaters.sources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.updaters.UpdaterSource;

/**
 * {@link UpdaterSource} implementation for
 * <a href="https://hangar.papermc.io/">hangar.papermc.io</a>
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #HangarUpdaterSource(String, HangarChannel)
 * @see #getProject()
 * @see #getChannel()
 * @see HangarChannel
 */
public class HangarUpdaterSource implements UpdaterSource {

	private final String project;
	private final HangarChannel channel;

	/**
	 * Creates a new {@link HangarUpdaterSource} capable of checking
	 * for updates on <a href="https://hangar.papermc.io/">hangar.papermc.io</a>.
	 * 
	 * @param project the project to get versions from. For example, let's say you
	 * have a plugin called "Test" and your username is Steve, your url should look
	 * like (https://hangar.papermc.io/Steve/Test), then the project String must be
	 * "Steve/Test".
	 * @param channel the {@link HangarChannel} that will be used to get updates from.
	 * 
	 * @throws NullPointerException if <b>project</b> or <b>channel</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public HangarUpdaterSource(@Nonnull String project, @Nonnull HangarChannel channel) {
		this.project = Objects.requireNonNull(project);
		this.channel = Objects.requireNonNull(channel);
	}

	/**
	 * Gets the project that will be used to get the
	 * latest version of a plugin.
	 * 
	 * @return The project that will be used to get the
	 * latest version of a plugin.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String getProject() {
		return project;
	}

	/**
	 * Gets the {@link HangarChannel} that is being used to check
	 * for updates.
	 * 
	 * @return The {@link HangarChannel} that is being used to check
	 * for updates.
	 */
	@Nonnull
	public HangarChannel getChannel() {
		return channel;
	}

	/*
	 * UpdaterSource implementation
	 */

	@Nonnull
	public String getName() {
		return "Hangar";
	}

	@Nullable
	@Override
	public String getLatestVersion() {
		try {
			final InputStream inputStream = new URL("https://hangar.papermc.io/api/v1/projects/" + project + "/latest?channel=" + channel.toUrlName()).openStream();
			final Scanner scanner = new Scanner(inputStream);
			String ver = null;
			if (scanner.hasNext())
				ver = scanner.next();
			scanner.close();
			return ver;
		} catch (IOException ex) {
			return null;
		}
	}

	/**
	 * Enum containing all available version channels on Hangar.
	 * 
	 * @author xDec0de_
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #ALPHA
	 * @see #SNAPSHOT
	 * @see #RELEASE
	 */
	public enum HangarChannel {

		/**Alpha channel, for alpha versions of plugins.*/
		ALPHA,
		/**Snapshot channel, for snapshots of plugins.*/
		SNAPSHOT,
		/**Release channel, for full stable relases of plugins.*/
		RELEASE;

		@Nonnull
		public String toUrlName() {
			final StringBuilder name = new StringBuilder(name());
			final int len = name.length();
			for (int i = 1; i < len; i++)
				name.setCharAt(i, Character.toLowerCase(name.charAt(i)));
			return name.toString();
		}
	}
}
