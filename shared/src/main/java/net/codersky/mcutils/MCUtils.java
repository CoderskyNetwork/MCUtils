package net.codersky.mcutils;

import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.cmd.MCCommandSender;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Platform independent class that provides access to
 * most of the features that MCUtils has to offer, just
 * check the "See Also" section here.
 *
 * @since MCUtils 1.0.0
 *
 * @see #getPlugin()
 * @see #getPlayer(UUID)
 * @see #getConsole()
 *
 * @author xDec0de_
 *
 * @param <P> The class that holds an instance of this
 * utils, meant to be the main class of your plugin as
 * it can be accessed via {@link #getPlugin()}.
 */
public abstract class MCUtils<P> {

	private final P plugin;
	protected final Set<Reloadable> reloadables = new HashSet<>();

	public MCUtils(@NotNull P plugin) {
		this.plugin = Objects.requireNonNull(plugin);
	}

	@NotNull
	public final P getPlugin() {
		return plugin;
	}

	@Nullable
	public abstract MCPlayer getPlayer(@NotNull UUID uuid);

	/**
	 * Provides a cross-platform {@link MCConsole} instance.
	 *
	 * @return A cross-platform {@link MCConsole} instance.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public abstract MCConsole getConsole();

	/**
	 * Gets the {@link MCPlatform} this {@link MCUtils} class
	 * is designed for. This can be useful when you are using
	 * a generic {@link MCUtils} instance, to let you know which
	 * platform you are in without casting.
	 *
	 * @return The {@link MCPlatform} this {@link MCUtils} class
	 * is designed for.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public abstract MCPlatform getPlatform();

	/**
	 * Gets the version of MCUtils being used by this utility
	 * class.
	 *
	 * @return The version of MCUtils being used by this utility
	 * class.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public final String getMCUtilsVersion() {
		return "1.0.0"; // Keep it the same as the project version from pom.xml
	}

	/*
	 * Commands
	 */

	public abstract void registerCommands(MCCommand<P, MCCommandSender>... commands);

	/*
	 * Reloadables
	 */

	@NotNull
	public MCUtils<P> registerReloadable(@NotNull Reloadable reloadable) {
		reloadables.add(Objects.requireNonNull(reloadable));
		return this;
	}

	public int reload() {
		int failures = 0;
		for (Reloadable reloadable : reloadables)
			if (!reloadable.reload())
				failures++;
		return failures;
	}
}
