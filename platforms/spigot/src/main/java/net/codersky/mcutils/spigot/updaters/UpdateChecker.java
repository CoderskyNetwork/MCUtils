package net.codersky.mcutils.spigot.updaters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.spigot.updaters.sources.HangarUpdaterSource;
import net.codersky.mcutils.spigot.updaters.sources.SpigetUpdaterSource;
import net.codersky.mcutils.spigot.updaters.sources.SpigotUpdaterSource;
import net.codersky.mcutils.spigot.updaters.sources.VersionInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.checkerframework.checker.index.qual.Positive;

import net.codersky.mcutils.spigot.updaters.sources.GitHubUpdaterSource;
import net.codersky.mcutils.spigot.updaters.sources.GitHubUpdaterSource.GitHubVersionInfo;

/**
 * A class used to easily check for plugin updates from
 * different {@link UpdaterSource sources} at the same time.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #UpdateChecker(Plugin)
 * @see #isNewer(String, String)
 * @see #addSource(UpdaterSource)
 * @see #getLatestVersion()
 * @see #getLatestVersion(boolean)
 */
public class UpdateChecker {

	private final String currentVersion;
	private final List<UpdaterSource> sources = new ArrayList<>();

	/**
	 * Instantiates a new {@link UpdateChecker} for the specified
	 * <b>plugin</b>.
	 * 
	 * @param plugin the {@link Plugin} to use. This will only provide
	 * the plugin version to compare against on all {@link UpdaterSource sources}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public UpdateChecker(@Nonnull Plugin plugin) {
		this.currentVersion = plugin.getDescription().getVersion();
	}

	/**
	 * <b>WARNING:</b> This method does <i>NOT</i> run asynchronously by itself,
	 * it is recommended to do so by using
	 * {@link BukkitScheduler#runTaskAsynchronously(Plugin, Consumer)}
	 * <p><p>
	 * Provides a {@link VersionInfo} object containing the latest known version
	 * of the {@link Plugin} that instantiated this {@link UpdateChecker}. Versions
	 * are provided by any {@link UpdaterSource updater sources} that have been
	 * {@link #addSource(UpdaterSource) added} to this checker.
	 * <p><p>
	 * The returned {@link VersionInfo} may be {@code null}, that means that there isn't
	 * an update available.
	 * 
	 * @param warnSync whether to warn or not about this method being
	 * called on Bukkit's main thread. Only set this to
	 * {@code false} if you are 100% sure that calling this
	 * method synchronously won't affect server performance
	 * (For example, at {@link JavaPlugin#onEnable()}).
	 * 
	 * @return A {@link VersionInfo} object containing the latest known version
	 * of the {@link Plugin} that instantiated this {@link UpdateChecker}. {@code null}
	 * if no newer version has been found.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getLatestVersion()
	 */
	@Nullable
	public final VersionInfo getLatestVersion(boolean warnSync) {
		VersionInfo latest = null;
		for (UpdaterSource source : sources) {
			final VersionInfo sourceVer = source.getLatestVersion(warnSync);
			if (sourceVer == null)
				continue;
			final String current = latest == null ? currentVersion : latest.getVersion();
			if (isNewer(current, sourceVer.getVersion()))
				latest = sourceVer;
		}
		return latest;
	}

	/**
	 * <b>WARNING:</b> This method does <i>NOT</i> run asynchronously by itself,
	 * it is recommended to do so by using
	 * {@link BukkitScheduler#runTaskAsynchronously(Plugin, Consumer)}, in fact
	 * this method will send a warning if you run it synchronously as explained
	 * on {@link UpdaterSource#getLatestVersion()}.
	 * <p><p>
	 * Provides a {@link VersionInfo} object containing the latest known version
	 * of the {@link Plugin} that instantiated this {@link UpdateChecker}. Versions
	 * are provided by any {@link UpdaterSource updater sources} that have been
	 * {@link #addSource(UpdaterSource) added} to this checker.
	 * <p><p>
	 * The returned {@link VersionInfo} may be {@code null}, that means that there isn't
	 * an update available.
	 * 
	 * @return A {@link VersionInfo} object containing the latest known version
	 * of the {@link Plugin} that instantiated this {@link UpdateChecker}. {@code null}
	 * if no newer version has been found.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getLatestVersion(boolean)
	 */
	@Nullable
	public final VersionInfo getLatestVersion() {
		return getLatestVersion(true);
	}

	/**
	 * Checks if <b>source</b> is a newer version than <b>source</b>.
	 * This method is used by {@link #getLatestVersion()} and can be
	 * overridden to implement your own version logic. If you provide
	 * multiple {@link UpdaterSource sources} to this {@link UpdateChecker},
	 * this method will be called once per {@link UpdaterSource source}.
	 * At first <b>version</b> will just be the version of the plugin that
	 * was used to instantiate this {@link UpdateChecker}, but if this method
	 * returns {@code true}, {@link #getLatestVersion()} will use <b>source</b>
	 * as the <b>latest</b> version on the next iteration, this works this
	 * way to ensure that {@link #getLatestVersion()} returns the actual latest
	 * version of the plugin between multiple sources.
	 * 
	 * @param latest the latest known version.
	 * @param source the version returned by a {@link UpdaterSource} to compare
	 * against.
	 * 
	 * @return {@code true} if <b>source</b> is a newer version than <b>latest</b>,
	 * {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean isNewer(@Nonnull String latest, @Nonnull String source) {
		// TODO We should check if source is actually an update to the latest version
		// as some plugins may have outdated sources or different versions on each source.
		return latest.equals(source);
	}

	/*
	 * Source addition
	 */

	/**
	 * Adds a new {@link UpdaterSource} to this {@link UpdateChecker}.
	 * The <b>source</b> to be added cannot be {@code null}.
	 * If an {@link UpdaterSource} from the same {@link Class}
	 * as <b>source</b> has already been added, this operation will be ignored. 
	 * 
	 * @param source the non {@code null} {@link UpdaterSource} to add.
	 * 
	 * @return This {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public UpdateChecker addSource(@Nonnull UpdaterSource source) {
		final Class<? extends UpdaterSource> sourceClass = source.getClass();
		for (UpdaterSource existingSource : sources)
			if (existingSource.getClass().equals(sourceClass))
				return this;
		sources.add(Objects.requireNonNull(source));
		return this;
	}

	/**
	 * Adds a new {@link SpigotUpdaterSource} to this {@link UpdateChecker}.
	 * 
	 * @param resourceId the resource id of the plugin to check on Spigot.
	 * Resource ids must be positive numbers.
	 * 
	 * @throws IllegalArgumentException if <b>resourceId</b> is minor or equal to zero.
	 * 
	 * @return This {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public UpdateChecker addSpigotSource(@Positive long resourceId) {
		return addSource(new SpigotUpdaterSource(resourceId));
	}

	/**
	 * Adds a new {@link SpigetUpdaterSource} to this {@link UpdateChecker}.
	 * 
	 * @param resourceId the resource id of the plugin to check on Spiget.
	 * Resource ids must be positive numbers.
	 * 
	 * @throws IllegalArgumentException if <b>resourceId</b> is minor or equal to zero.
	 * 
	 * @return This {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public UpdateChecker addSpigetSource(@Positive long resourceId) {
		return addSource(new SpigetUpdaterSource(resourceId));
	}

	/**
	 * Adds a new {@link GitHubUpdaterSource} to this {@link UpdateChecker}.
	 * <p><p>
	 * This method will use the tag of the release as a version,
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
	 * @return This {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public UpdateChecker addGitHubSource(@Nonnull String repo) {
		return addSource(new GitHubUpdaterSource(repo));
	}

	/**
	 * Adds a new {@link GitHubUpdaterSource} to this {@link UpdateChecker}.
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
	 * @return This {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public UpdateChecker addGitHubSource(@Nonnull String repo, boolean useName) {
		return addSource(new GitHubUpdaterSource(repo, useName));
	}

	/**
	 * Adds a new {@link HangarUpdaterSource} to this {@link UpdateChecker}.
	 * 
	 * @param project the project to get versions from. For example, let's say you
	 * have a plugin called "Test" and your username is Steve, your url should look
	 * like (https://hangar.papermc.io/Steve/Test), then the project String must be
	 * "Steve/Test".
	 * @param channel the channel that will be used to get updates from, some common
	 * update channels are predefined on {@link HangarUpdaterSource.HangarChannel}.
	 * 
	 * @throws NullPointerException if <b>project</b> or <b>channel</b> are {@code null}.
	 * 
	 * @return This {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addHangarSource(String, HangarUpdaterSource.HangarChannel)
	 */
	@Nonnull
	public UpdateChecker addHangarSource(@Nonnull String project, @Nonnull String channel) {
		return addSource(new HangarUpdaterSource(project, channel));
	}

	/**
	 * Adds a new {@link HangarUpdaterSource} to this {@link UpdateChecker}.
	 * 
	 * @param project the project to get versions from. For example, let's say you
	 * have a plugin called "Test" and your username is Steve, your url should look
	 * like (https://hangar.papermc.io/Steve/Test), then the project String must be
	 * "Steve/Test".
	 * @param channel the {@link HangarUpdaterSource.HangarChannel} that will be used to get updates from.
	 * 
	 * @throws NullPointerException if <b>project</b> or <b>channel</b> are {@code null}.
	 * 
	 * @return This {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addHangarSource(String, String)
	 */
	@Nonnull
	public UpdateChecker addHangarSource(@Nonnull String project, @Nonnull HangarUpdaterSource.HangarChannel channel) {
		return addHangarSource(project, channel.toUrlName());
	}
}
