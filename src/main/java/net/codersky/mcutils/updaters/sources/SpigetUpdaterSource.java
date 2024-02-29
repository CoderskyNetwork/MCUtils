package net.codersky.mcutils.updaters.sources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.checkerframework.checker.index.qual.Positive;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import net.codersky.mcutils.updaters.UpdaterSource;
import net.codersky.mcutils.updaters.sources.HangarUpdaterSource.HangarChannel;

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
public class SpigetUpdaterSource implements UpdaterSource {

	private final long resourceId;

	/**
	 * Creates a new {@link SpigetUpdaterSource} capable of checking
	 * for updates on <a href="https://api.spiget.org/">api.spiget.org</a>
	 * 
	 * @param resourceId the resource id of the plugin to check. This id
	 * can be obtained on the link of your resource.
	 * 
	 * @throws IllegalArgumentException if <b>resourceId</b> is minor or equal to zero.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public SpigetUpdaterSource(@Positive long resourceId) {
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
		return "Spiget";
	}

	@Nullable
	@Override
	public SpigetVersionInfo getLatestVersion() {
		try {
			final String url = "https://api.spiget.org/v2/resources/" + resourceId + "/versions/latest";
			final JsonReader reader = new JsonReader(new InputStreamReader(new URL(url).openStream()));
			final SpigetVersionInfo info = new Gson().fromJson(reader, SpigetVersionInfo.class);
			info.source = this;
			reader.close(); // Should close every stream according to the javadoc.
			return info;
		} catch (IOException e) {
			return null;
		}
	}

	public class SpigetVersionInfo extends VersionInfo {

		String name;
		UUID uuid;
		int downloads;
		RatingInfo rating;
		long releaseDate;
		long resource;
		long id;

		@Nonnull
		@Override
		public String getVersion() {
			return name;
		}

		@Nonnull
		public UUID getUUID() {
			return uuid;
		}

		public int getDownloads() {
			return downloads;
		}

		public int getRatingCount() {
			return rating.count;
		}

		public float getRatingAverage() {
			return rating.average;
		}

		public Date getDate() {
			return Date.from(Instant.ofEpochSecond(releaseDate));
		}

		public long getResourceId() {
			return ((SpigetUpdaterSource) getSource()).getResourceId();
		}

		public long getVersionId() {
			return id;
		}

		private class RatingInfo {
			int count;
			float average;
		}
	}
}
