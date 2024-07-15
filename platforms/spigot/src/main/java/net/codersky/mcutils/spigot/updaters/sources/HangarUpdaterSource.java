package net.codersky.mcutils.spigot.updaters.sources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import net.codersky.mcutils.spigot.updaters.UpdaterSource;

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
 * @see #getLatestVersion()
 */
public class HangarUpdaterSource implements UpdaterSource {

	private final String project;
	private final String channel;

	/**
	 * Creates a new {@link HangarUpdaterSource} capable of checking
	 * for updates on <a href="https://hangar.papermc.io/">hangar.papermc.io</a>.
	 * 
	 * @param project the project to get versions from. For example, let's say you
	 * have a plugin called "Test" and your username is Steve, your url should look
	 * like (https://hangar.papermc.io/Steve/Test), then the project String must be
	 * "Steve/Test".
	 * @param channel the channel that will be used to get updates from, some common
	 * update channels are predefined on {@link HangarChannel}.
	 * 
	 * @throws NullPointerException if <b>project</b> or <b>channel</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #HangarUpdaterSource(String, HangarChannel)
	 */
	public HangarUpdaterSource(@Nonnull String project, @Nonnull String channel) {
		this.project = Objects.requireNonNull(project);
		this.channel = Objects.requireNonNull(channel);
	}

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
	 * 
	 * @see #HangarUpdaterSource(String, String)
	 */
	public HangarUpdaterSource(@Nonnull String project, @Nonnull HangarChannel channel) {
		this(project, channel.toUrlName());
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
	 * Gets the channel that is being used to check
	 * for updates.
	 * 
	 * @return The channel that is being used to check
	 * for updates.
	 */
	@Nonnull
	public String getChannel() {
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
	public HangarVersionInfo getLatestVersion(boolean warnSync) {
		if (warnSync && Bukkit.isPrimaryThread())
			Bukkit.getLogger().warning("Getting latest version of Hanger project " + project + " on the main thread, possible performance issue.");
		try {
			final String url = "https://hangar.papermc.io/api/v1/projects/" + project + "/versions/" + getLatestVersionName();
			final JsonReader reader = new JsonReader(new InputStreamReader(new URL(url).openStream()));
			final HangarVersionInfo info = new Gson().fromJson(reader, HangarVersionInfo.class);
			info.source = this;
			reader.close(); // Should close every stream according to the javadoc.
			return info;
		} catch (IOException e) {
			return null;
		}
	}

	@Nullable
	public String getLatestVersionName() {
		try {
			final InputStream inputStream = new URL("https://hangar.papermc.io/api/v1/projects/" + project + "/latest?channel=" + channel).openStream();
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
	 * Enum containing common version channels on Hangar.
	 * 
	 * @author xDec0de_
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #ALPHA
	 * @see #BETA
	 * @see #SNAPSHOT
	 * @see #RELEASE
	 */
	public enum HangarChannel {

		/**Alpha channel, for alpha versions of plugins.*/
		ALPHA,
		/**Beta channel, for beta versions of plugins.*/
		BETA,
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

	/**
	 * Class that provides information about a Hangar plugin version.
	 * 
	 * @author xDec0de_
	 *
	 * @since MCUtils 1.0.0
	 */
	public class HangarVersionInfo extends VersionInfo {

		Date createdAt;
		String name;
		String description;
		String author;
		String reviewState;
		Downloads downloads;
		FormattedVersions platformDependenciesFormatted;

		/**
		 * Gets the project that belongs to this version.
		 * 
		 * @return The project that belongs to this version.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getProject() {
			return ((HangarUpdaterSource) getSource()).getProject();
		}

		@Nonnull
		@Override
		public String getVersion() {
			return name;
		}

		@Nonnull
		@Override
		public String getVersionUrl() {
			return "https://hangar.papermc.io/" + getProject() + "/versions/" + getVersion();
		}

		/**
		 * Gets the release date of this version.
		 * 
		 * @return The release date of this version.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public Date getCreationDate() {
			return createdAt;
		}

		/**
		 * Gets the description of this version.
		 * 
		 * @return The description of this version.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getDescription() {
			return description;
		}

		/**
		 * Gets the author name of this version.
		 * 
		 * @return The author name of this version.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getAuthor() {
			return author;
		}

		/**
		 * Gets the review state of this version.
		 * 
		 * @return The review state of this version.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getReviewState() {
			return reviewState;
		}

		/*
		 * Download URLs
		 */

		// List<DownloadInfo> downloads; didn't work, so...
		private class Downloads {
			DownloadInfo PAPER;
			DownloadInfo WATERFALL;
			DownloadInfo VELOCITY;
		}
		
		private class DownloadInfo {
			String downloadUrl;
			String externalUrl;
		}

		private String getUrl(DownloadInfo info) {
			if (info == null)
				return null;
			return info.downloadUrl != null ? info.downloadUrl : info.externalUrl;
		}

		/**
		 * Gets the paper download url of this version.
		 * <p><p>
		 * Note that some resources may not support paper.
		 * 
		 * @return The paper download url of this version, {@code null}
		 * if the resource doesn't support paper.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nullable
		public String getPaperDownloadUrl() {
			return getUrl(downloads.PAPER);
		}

		/**
		 * Gets the waterfall download url of this version.
		 * <p><p>
		 * Note that some resources may not support waterfall.
		 * 
		 * @return The waterfall download url of this version, {@code null}
		 * if the resource doesn't support waterfall.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nullable
		public String getWaterfallDownloadUrl() {
			return getUrl(downloads.WATERFALL);
		}

		/**
		 * Gets the velocity download url of this version.
		 * <p><p>
		 * Note that some resources may not support velocity.
		 * 
		 * @return The velocity download url of this version, {@code null}
		 * if the resource doesn't support velocity.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nullable
		public String getVelocityDownloadUrl() {
			return getUrl(downloads.VELOCITY);
		}

		/*
		 * Platform versions
		 */

		private class FormattedVersions {
			String PAPER;
			String WATERFALL;
			String VELOCITY;
		}

		/**
		 * Gets the Minecraft versions that are supported
		 * by the paper version of this resource. This is
		 * generally provided as a range, so a resource that supports all
		 * versions from 1.10 to 1.20 could return "1.10-1.20",
		 * while a resource that only supports 1.20 may return
		 * "1.20", but some resources return a more specific version
		 * list separated by commas such as "1.8, 1.9.4, 1.10.2".
		 * This can also be mixed with some versions separated by commas
		 * and other versions provided as ranges.
		 * <p><p>
		 * Note that some resources may not support paper.
		 * 
		 * @return The paper download url of this version, {@code null}
		 * if the resource doesn't support paper.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nullable
		public String getPaperVersions() {
			return platformDependenciesFormatted.PAPER;
		}

		/**
		 * Gets the Minecraft versions that are supported
		 * by the waterfall version of this resource. This is
		 * generally provided as a range, so a resource that supports all
		 * versions from 1.10 to 1.20 could return "1.10-1.20",
		 * while a resource that only supports 1.20 may return
		 * "1.20", but some resources return a more specific version
		 * list separated by commas such as "1.8, 1.9.4, 1.10.2".
		 * This can also be mixed with some versions separated by commas
		 * and other versions provided as ranges.
		 * <p><p>
		 * Note that some resources may not support waterfall.
		 * 
		 * @return The waterfall download url of this version, {@code null}
		 * if the resource doesn't support waterfall.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nullable
		public String getWaterfallVersions() {
			return platformDependenciesFormatted.WATERFALL;
		}

		/**
		 * Gets the Minecraft versions that are supported
		 * by the velocity version of this resource. This is
		 * generally provided as a range, so a resource that supports all
		 * versions from 3.2.0 to 3.3.0 could return "3.2.0-3.3.0",
		 * while a resource that only supports 3.3.0 may return
		 * "3.3.0", but some resources return a more specific version
		 * list separated by commas such as "3.1.1, 3.2.0, 3.3.0".
		 * This can also be mixed with some versions separated by commas
		 * and other versions provided as ranges.
		 * <p><p>
		 * Note that some resources may not support velocity.
		 * 
		 * @return The velocity download url of this version, {@code null}
		 * if the resource doesn't support velocity.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nullable
		public String getVelocityVersions() {
			return platformDependenciesFormatted.VELOCITY;
		}
	}
}
