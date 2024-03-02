package net.codersky.mcutils.updaters.sources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.checkerframework.checker.index.qual.Positive;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

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
	public SpigotVersionInfo getLatestVersion(boolean warnSync) {
		if (warnSync && Bukkit.isPrimaryThread())
			Bukkit.getLogger().warning("Getting latest version of Spigot resource " + resourceId + " on the main thread, possible performance issue.");
		try {
			final String url = "https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + resourceId;
			final JsonReader reader = new JsonReader(new InputStreamReader(new URL(url).openStream()));
			final SpigotVersionInfo info = new Gson().fromJson(reader, SpigotVersionInfo.class);
			info.source = this;
			reader.close(); // Should close every stream according to the javadoc.
			return info;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Class that provides information about a Spigot plugin version.
	 * 
	 * @author xDec0de_
	 *
	 * @since MCUtils 1.0.0
	 */
	public class SpigotVersionInfo extends VersionInfo {

		String current_version;
		long id;
		String title;
		String tag;
		String native_minecraft_version;
		List<String> supported_minecraft_versions;
		String icon_link;
		Author author;
		Premium premium;
		Stats stats;

		@Override
		public String getVersion() {
			return current_version;
		}

		@Nonnull
		@Override
		public String getVersionUrl() {
			return getResourceUrl() + "/updates";
		}

		/**
		 * Gets the url of the resource that this version
		 * belongs to.
		 * 
		 * @return The url of the resource that this version
		 * belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getResourceUrl() {
			return "https://www.spigotmc.org/resources/" + getResourceId();
		}

		/**
		 * Gets the resource id this version belongs to.
		 * 
		 * @return The resource id this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Positive
		public long getResourceId() {
			return id;
		}

		/**
		 * Gets the title of the resource that this version belongs to.
		 * 
		 * @return The title of the resource that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getResourceTitle() {
			return title;
		}

		/**
		 * Gets the tag of the resource that this version belongs to.
		 * 
		 * @return The tag of the resource that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getResourceTag() {
			return tag;
		}

		/**
		 * Gets the native Minecraft version of the resource that
		 * this version belongs to. This should also be the native
		 * Minecraft version of this version.
		 * 
		 * @return The native Minecraft version of the resource
		 * that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getNativeVersion() {
			return native_minecraft_version;
		}

		/**
		 * Gets the list of supported Minecraft versions of
		 * the resource that this version belongs to. This should
		 * also be the list of supported Minecraft versions of this
		 * version.
		 * 
		 * @return The native Minecraft version of the resource
		 * that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public List<String> getSupportedVersions() {
			return supported_minecraft_versions;
		}

		/**
		 * Gets the url of the icon of the resource
		 * that this version belongs to.
		 * 
		 * @return The url of the icon of the resource
		 * that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getResourceIconUrl() {
			return icon_link;
		}

		/*
		 * Author information
		 */

		private class Author {
			long id;
			String username;
		}

		/**
		 * Gets the id of the Author of the resource
		 * that this version belongs to.
		 * 
		 * @return The id of the Author of the resource
		 * that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Positive
		public long getAuthorId() {
			return author.id;
		}

		/**
		 * Gets the name of the Author of the resource
		 * that this version belongs to.
		 * 
		 * @return The name of the Author of the resource
		 * that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getAuthorName() {
			return author.username;
		}

		/*
		 * Premium information
		 */

		private class Premium {
			float price;
			String currency;
		}

		/**
		 * Checks if the resource that this version belongs
		 * to is premium (Paid) or not.
		 * 
		 * @return {@code true} if the resource is premium,
		 * {@code false} otherwise.
		 * 
		 * @since MCUtils 1.0.0
		 * 
		 * @see #getResourcePrice()
		 * @see #getCurrency()
		 */
		public boolean isPremium() {
			return getResourcePrice() != 0;
		}

		/**
		 * Gets the price of the resource that this version belongs
		 * to. The price will be 0 if the resource isn't premium.
		 * 
		 * @return The price of the resource or 0 if the resource
		 * isn't premium.
		 * 
		 * @since MCUtils 1.0.0
		 * 
		 * @see #isPremium()
		 * @see #getCurrency()
		 */
		@Nonnegative
		public float getResourcePrice() {
			return premium.price;
		}

		/**
		 * Gets the currency of the resource that this version belongs
		 * to. This will be an empty string if the resource isn't premium.
		 * 
		 * @return The currency of the resource or an empty string if the resource
		 * isn't premium.
		 * 
		 * @since MCUtils 1.0.0
		 * 
		 * @see #isPremium()
		 * @see #getResourcePrice()
		 */
		@Nonnull
		public String getCurrency() {
			return premium.currency;
		}

		/*
		 * Stats
		 */

		private class Stats {
			int downloads;
			int updates;
			float rating;
		}

		/**
		 * Gets the amount of downloads of the resource
		 * that this version belongs to.
		 * 
		 * @return The amount of downloads of the resource
		 * that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnegative
		public int getResourceDownloads() {
			return stats.downloads;
		}

		/**
		 * Gets the amount of updates that the resource
		 * that this version belongs to has.
		 * 
		 * @return The amount of updates that the resource
		 * that this version belongs to has.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnegative
		public int getResourceUpdates() {
			return stats.updates;
		}

		/**
		 * Gets the average rating of the resource
		 * that this version belongs to. This number
		 * ranges from 0 to 5.
		 * 
		 * @return The the average rating of the resource
		 * that this version belongs to.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnegative
		public float getResourceRating() {
			return stats.rating;
		}
	}
}
