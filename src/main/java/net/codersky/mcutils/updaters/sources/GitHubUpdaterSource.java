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
		this.repo = Objects.requireNonNull(repo);
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
		try {
			final InputStream inputStream = new URL("https://api.github.com/repos/" + repo + "/releases").openStream();
			final Scanner scanner = new Scanner(inputStream);
			String ver = null;
			if (scanner.hasNext())
				ver = scanner.next();
			scanner.close();
			if (ver == null)
				return null;
			final int start = ver.indexOf("\"name\":\"") + 8;
			return ver.substring(start, ver.indexOf('"', start));
		} catch (IOException ex) {
			return null;
		}
	}
}
