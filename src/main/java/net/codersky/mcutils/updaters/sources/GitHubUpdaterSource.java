package net.codersky.mcutils.updaters.sources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.checkerframework.checker.index.qual.Positive;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import net.codersky.mcutils.updaters.UpdaterSource;

/**
 * {@link UpdaterSource} implementation for
 * <a href="https://api.github.com/">api.github.com</a>
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #GitHubUpdaterSource(String)
 * @see #GitHubUpdaterSource(String, boolean)
 * @see #getRepository()
 * @see #getLatestVersion()
 */
public class GitHubUpdaterSource implements UpdaterSource {

	private final String repo;
	private final boolean useName;

	/**
	 * Creates a new {@link GitHubUpdaterSource} capable of checking
	 * for updates on <a href="https://api.github.com/">api.github.com</a>.
	 * 
	 * @param repo the repository to get releases from. For example, for the
	 * MCUtils repository (https://github.com/CoderskyNetwork/MCUtils), the
	 * String must be "CoderskyNetwork/MCUtils".
	 * @param useName {@link GitHubUpdaterSource} uses the release tag
	 * by default. Setting this to {@code true} will use the release name,
	 * this only affects to {@link #getLatestVersion()} as 
	 * {@link GitHubVersionInfo#getVersion()} will return either the version
	 * tag or name depending on this.
	 * 
	 * @throws NullPointerException if <b>repo</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #GitHubUpdaterSource(String)
	 */
	public GitHubUpdaterSource(@Nonnull String repo, boolean useName) {
		this.repo = Objects.requireNonNull(repo);
		this.useName = useName;
	}

	/**
	 * Creates a new {@link GitHubUpdaterSource} capable of checking
	 * for updates on <a href="https://api.github.com/">api.github.com</a>.
	 * <p><p>
	 * This constructor will use the tag of the release as a version,
	 * this only affects to {@link #getLatestVersion()} as 
	 * {@link GitHubVersionInfo#getVersion()} will return either the version
	 * tag or name depending on this.
	 * 
	 * @param repo the repository to get releases from. For example, for the
	 * MCUtils repository (https://github.com/CoderskyNetwork/MCUtils), the
	 * String must be "CoderskyNetwork/MCUtils".
	 * 
	 * @throws NullPointerException if <b>repo</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #GitHubUpdaterSource(String, boolean)
	 */
	public GitHubUpdaterSource(@Nonnull String repo) {
		this(repo, false);
	}

	/**
	 * Gets the repository that will be used to get the
	 * latest version of a plugin.
	 * 
	 * @return The repository that will be used to get the
	 * latest version of a plugin.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String getRepository() {
		return repo;
	}

	/*
	 * UpdaterSource implementation
	 */

	@Nonnull
	public String getName() {
		return "GitHub";
	}

	@Nullable
	@Override
	public GitHubVersionInfo getLatestVersion(boolean warnSync) {
		if (warnSync && Bukkit.isPrimaryThread())
			Bukkit.getLogger().warning("Getting latest version of GitHub repository " + repo + " on the main thread, possible performance issue.");
		try {
			final JsonReader reader = new JsonReader(new InputStreamReader(new URL("https://api.github.com/repos/" + repo + "/releases").openStream()));
			reader.beginArray();
			final GitHubVersionInfo info = new Gson().fromJson(reader, GitHubVersionInfo.class);
			info.source = this;
			reader.close(); // Should close every stream according to the javadoc.
			return info;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Class that provides information about a GitHub release.
	 * 
	 * @author xDec0de_
	 *
	 * @since MCUtils 1.0.0
	 */
	public class GitHubVersionInfo extends VersionInfo {

		private String name;
		private String tag_name;
		String html_url;
		long id;
		AuthorInfo author;
		Date created_at;
		Date published_at;
		boolean draft;
		boolean prerelease;
		String body;

		@Nonnull
		@Override
		public String getVersion() {
			final GitHubUpdaterSource source = (GitHubUpdaterSource) getSource();
			return source.useName ? name : tag_name;
		}

		@Nonnull
		@Override
		public String getVersionUrl() {
			return html_url;
		}

		/**
		 * Gets the name of this release.
		 * 
		 * @return The name of this release.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getName() {
			return name;
		}

		/**
		 * Gets the tag of this release.
		 * 
		 * @return The tag of this release.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getTag() {
			return tag_name;
		}

		/**
		 * Gets the id of this release.
		 * 
		 * @return The id of this release.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Positive
		public long getId() {
			return id;
		}

		/**
		 * Gets the {@link AuthorInfo} of this release.
		 * 
		 * @return The {@link AuthorInfo} of this release.
		 */
		public AuthorInfo getAuthor() {
			return author;
		}

		/**
		 * Gets the creation {@link Date} of this release.
		 * 
		 * @return The creation {@link Date} of this release.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public Date getDateCreated() {
			return created_at;
		}

		/**
		 * Gets the {@link Date} this release has been published.
		 * 
		 * @return The {@link Date} this release has been published.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public Date getDatePublished() {
			return published_at;
		}

		/**
		 * Checks if this release is a draft.
		 * 
		 * @return {@code true} if this release is a draft,
		 * {@code false} otherwise.
		 * 
		 * @since MCUtils 1.0.0
		 */
		public boolean isDraft() {
			return draft;
		}

		/**
		 * Checks if this release is a pre-release.
		 * 
		 * @return {@code true} if this release is a pre-release,
		 * {@code false} otherwise.
		 * 
		 * @since MCUtils 1.0.0
		 */
		public boolean isPreRelease() {
			return prerelease;
		}

		/**
		 * Gets the body of this release. This generally contains
		 * a description of the release and may be formatted with
		 * Markdown.
		 * 
		 * @return The body of this release.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getBody() {
			return body;
		}

		/**
		 * Class that contains author information retrieved
		 * from a GitHub release.
		 * 
		 * @author xDec0de_
		 * 
		 * @see GitHubUpdaterSource
		 * @see GitHubVersionInfo
		 */
		public class AuthorInfo {

			String login;
			long id;
			String avatar_url;
			String html_url;

			/**
			 * Gets the login of the author (user name).
			 * 
			 * @return The login of the author.
			 * 
			 * @since MCUtils 1.0.0
			 */
			@Nonnull
			public String getLogin() {
				return login;
			}

			/**
			 * Gets the id of the author.
			 * 
			 * @return The id of the author.
			 * 
			 * @since MCUtils 1.0.0
			 */
			@Positive
			public long getId() {
				return id;
			}

			/**
			 * Gets the avatar url of the author. If the author doesn't have a custom
			 * avatar, this won't be {@code null} and will point to the default
			 * avatar url that GitHub provided to this author.
			 * 
			 * @return The avatar url of the author.
			 * 
			 * @since MCUtils 1.0.0
			 */
			@Nonnull
			public String getAvatarUrl() {
				return avatar_url;
			}

			/**
			 * Gets the html url of this author, a link to the author page.
			 * 
			 * @return The html url of this author
			 * 
			 * @since MCUtils 1.0.0
			 */
			@Nonnull
			public String getHtmlUrl() {
				return html_url;
			}
		}
	}
}
