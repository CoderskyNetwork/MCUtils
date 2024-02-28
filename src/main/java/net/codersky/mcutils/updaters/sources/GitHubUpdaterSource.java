package net.codersky.mcutils.updaters.sources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
 * @see #getReleaseInfo()
 * @see #getBasicReleaseInfo()
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
	 * this only affects to {@link #getLatestVersion()}.
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
	 * this only affects to {@link #getLatestVersion()}.
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
	public String getLatestVersion() {
		final BasicReleaseInfo data = getBasicReleaseInfo();
		return useName ? data.getName() : data.getTag();
	}

	/*
	 * Release data (Gson)
	 */

	/**
	 * Gets custom data by using {@link Gson#fromJson(Reader, Type)}. If you don't know how
	 * to use this method, just stick to {@link #getReleaseInfo()} or {@link #getBasicReleaseInfo()},
	 * this method exists for uses that know how to use Gson and want to fetch data that is
	 * provided by GitHub but is not included on the {@link ReleaseInfo} class that MCUtils provides.
	 * 
	 * @param <T> the type of the desired object.
	 * 
	 * @param typeOfT The specific genericized type of src (See {@link Gson#fromJson(Reader, Type)}).
	 * 
	 * @return An object of type {@code T}. {@code null} if any error occurs.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getBasicReleaseInfo()
	 * @see #getReleaseInfo()
	 */
	@Nullable
	public <T> T getData(Type typeOfT) {
		try {
			final JsonReader reader = new JsonReader(new InputStreamReader(new URL("https://api.github.com/repos/" + repo + "/releases").openStream()));
			reader.beginArray();
			final T data = new Gson().fromJson(reader, typeOfT);
			reader.close(); // Should close every stream according to the javadoc.
			return data;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Gets a {@link BasicReleaseInfo} object from this GitHub source.
	 * This method exists to reduce memory usage and only provides the
	 * essential information for version checkers, if you want more
	 * information about the release, use {@link #getReleaseInfo()}.
	 * 
	 * @return A {@link BasicReleaseInfo} object from this GitHub source,
	 * {@code null} if any error occurs.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getReleaseInfo()
	 * @see #getData(Type)
	 */
	@Nullable
	public BasicReleaseInfo getBasicReleaseInfo() {
		return getData(BasicReleaseInfo.class);
	}

	/**
	 * A very basic version of {@link ReleaseInfo} that only
	 * contains the release name and tag, lowering memory
	 * usage on {@link GitHubUpdaterSource#getLatestVersion()}.
	 * 
	 * @author xDec0de_
	 *
	 * @since MCUtils 1.0.0
	 */
	public class BasicReleaseInfo {

		private String name;
		private String tag_name;

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
	}

	/**
	 * Gets a full {@link ReleaseInfo} object from this GitHub source.
	 * This method provides a lot of information about the release that
	 * you may not want to use, if you only want to get the release name
	 * or tag, use {@link GitHubUpdaterSource#getBasicReleaseInfo()}.
	 * 
	 * @return A {@link BasicReleaseInfo} object from this GitHub source,
	 * {@code null} if any error occurs.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getReleaseInfo()
	 * @see #getData(Type)
	 */
	@Nullable
	public ReleaseInfo getReleaseInfo() {
		return getData(ReleaseInfo.class);
	}

	/**
	 * Class that provides full information about a GitHub release.
	 * 
	 * @author xDec0de_
	 *
	 * @since MCUtils 1.0.0
	 */
	public class ReleaseInfo extends BasicReleaseInfo {

		String html_url;
		long id;
		AuthorInfo author;
		Date created_at;
		Date published_at;
		boolean draft;
		boolean prerelease;
		String body;

		/**
		 * Gets the html url String of this release, a link to the release page.
		 * 
		 * @return The html url String of this release.
		 * 
		 * @since MCUtils 1.0.0
		 */
		@Nonnull
		public String getHtmlUrl() {
			return html_url;
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
		 * @see ReleaseInfo
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
