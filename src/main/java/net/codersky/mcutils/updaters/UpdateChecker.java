package net.codersky.mcutils.updaters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.checkerframework.checker.index.qual.Positive;

import net.codersky.mcutils.updaters.sources.GitHubUpdaterSource;
import net.codersky.mcutils.updaters.sources.HangarUpdaterSource;
import net.codersky.mcutils.updaters.sources.HangarUpdaterSource.HangarChannel;
import net.codersky.mcutils.updaters.sources.SpigotUpdaterSource;

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
	 * The returned {@link VersionInfo} may return a {@code null} {@link UpdaterSource},
	 * on its {@link VersionInfo#getSource()} method, that means that there is <b>NOT</b>
	 * an available update, {@link VersionInfo#getVersion()} will be the same as the
	 * current version of the plugin.
	 * 
	 * @return A {@link VersionInfo} object containing the latest known version
	 * of the {@link Plugin} that instantiated this {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public final VersionInfo getLatestVersion() {
		VersionInfo latest = new VersionInfo(null, currentVersion);
		for (UpdaterSource source : sources) {
			final String sourceVer = source.getLatestVersion();
			if (sourceVer == null)
				Bukkit.getLogger().log(Level.WARNING, "The " + source.getName() + " updater source returned null, meaning that it failed.");
			else if (isNewer(latest.getVersion(), sourceVer))
				latest = new VersionInfo(source, sourceVer);
		}
		return latest;
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
	 * Adds a new {@link GitHubUpdaterSource} to this {@link UpdateChecker}.
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
	 * Adds a new {@link HangarUpdaterSource} to this {@link UpdateChecker}.
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
	 * @return This {@link UpdateChecker}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addHangarSource(String, HangarChannel)
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
	 * @param channel the {@link HangarChannel} that will be used to get updates from.
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
	public UpdateChecker addHangarSource(@Nonnull String project, @Nonnull HangarChannel channel) {
		return addHangarSource(project, channel.toUrlName());
	}
}
