package net.codersky.mcutils.updaters.sources;

import java.io.IOException;
import java.io.InputStreamReader;
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
 * @see #getRepository()
 */
public class GitHubUpdaterSource implements UpdaterSource {

	private final String repo;
	private final boolean useName;

	public GitHubUpdaterSource(@Nonnull String repo, boolean useName) {
		this.repo = Objects.requireNonNull(repo);
		this.useName = useName;
	}

	/**
	 * Creates a new {@link GitHubUpdaterSource} capable of checking
	 * for updates on <a href="https://api.github.com/">api.github.com</a>.
	 * 
	 * @param repo the repository to get releases from. For example, for the
	 * MCUtils repository (https://github.com/CoderskyNetwork/MCUtils), the
	 * String must be "CoderskyNetwork/MCUtils".
	 * 
	 * @throws NullPointerException if <b>repo</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
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

	public BasicReleaseInfo getBasicReleaseInfo() {
		return getData(BasicReleaseInfo.class);
	}

	public class BasicReleaseInfo {

		private String name;
		private String tag_name;

		@Nonnull
		public String getName() {
			return name;
		}

		@Nonnull
		public String getTag() {
			return tag_name;
		}
	}

	@Nullable
	public ReleaseInfo getReleaseInfo() {
		return getData(ReleaseInfo.class);
	}

	public class ReleaseInfo extends BasicReleaseInfo {

		String html_url;
		long id;
		AuthorInfo author;
		Date created_at;
		Date published_at;
		boolean draft;
		boolean prerelease;
		String body;

		@Nonnull
		public String getHtmlUrl() {
			return html_url;
		}

		@Positive
		public long getId() {
			return id;
		}

		public AuthorInfo getAuthor() {
			return author;
		}

		@Nonnull
		public Date getDateCreated() {
			return created_at;
		}

		@Nonnull
		public Date getDatePublished() {
			return published_at;
		}

		public boolean isDraft() {
			return draft;
		}

		public boolean isPreRelease() {
			return prerelease;
		}

		@Nonnull
		public String getBody() {
			return body;
		}

		public class AuthorInfo {

			String login;
			long id;
			String avatar_url;
			String html_url;

			@Nonnull
			public String getLogin() {
				return login;
			}

			@Positive
			public long getId() {
				return id;
			}

			public String getAvatarUrl() {
				return avatar_url;
			}

			@Nonnull
			public String getHtmlUrl() {
				return html_url;
			}
		}
	}
}
