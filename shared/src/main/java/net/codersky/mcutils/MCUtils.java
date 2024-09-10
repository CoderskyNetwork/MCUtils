package net.codersky.mcutils;

import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.cmd.MCCommandSender;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.codersky.mcutils.crossplatform.player.PlayerProvider;
import net.codersky.mcutils.files.ConfigFileHolder;
import net.codersky.mcutils.files.FileHolder;
import net.codersky.mcutils.files.FileUpdater;
import net.codersky.mcutils.files.MessagesFileHolder;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class MCUtils<P> {

	private final P plugin;
	protected final LinkedList<FileHolder> files = new LinkedList<>();

	public MCUtils(@NotNull P plugin) {
		this.plugin = Objects.requireNonNull(plugin);
	}

	@NotNull
	public final P getPlugin() {
		return plugin;
	}

	@Nullable
	public abstract MCPlayer<?> getPlayer(@NotNull UUID uuid);

	/**
	 * Provides a cross-platform {@link MCConsole} instance.
	 *
	 * @return A cross-platform {@link MCConsole} instance.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public abstract MCConsole<?> getConsole();

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

	/**
	 * Registers a new {@link FileHolder}, if the file doesn't exist it will be created.
	 *
	 * @param <T> must implement {@link FileHolder}
	 * @param file the file to be registered, {@link T#create()} will be called to ensure that the file exists.
	 *
	 * @return The registered file, the {@code file} parameter.
	 *
	 * @throws NullPointerException if {@code file} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public <T extends FileHolder> T registerFile(@NotNull T file) {
		file.create();
		files.add(file);
		return file;
	}

	public abstract void registerCommands(MCCommand<P, MCCommandSender<?, ?>>... commands);

	/**
	 * Gets the files currently being handled by this {@link MCUtils} instance. The list returned
	 * is a new list, not the actual list used by the plugin, meaning that you can change
	 * it but the changes won't be applied to the original list.
	 *
	 * @return The list files currently being handled by this {@link MCUtils} instance, this list will
	 * never be {@code null} but may be empty, this list is ordered so that the most recent
	 * element is always the last element on the list.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public LinkedList<FileHolder> getFiles() {
		return new LinkedList<>(files);
	}

	/**
	 * Gets the first registered {@link ConfigFileHolder}.
	 * Plugins may override this method to return their own
	 * {@link ConfigFileHolder} type.
	 * <p>
	 * <b>Performance note</b>: Even though this is <b>very</b> insignificant, this
	 * method does iterate through all the registered {@link FileHolder FileHolders}
	 * and checks if they implement the {@link ConfigFileHolder} interface, so you may
	 * want to store the file instead of calling this method more than once on a listener
	 * or something like that. This would also allow you to return directly whatever
	 * implementation you are using.
	 *
	 * @return The first registered {@link ConfigFileHolder}, may be
	 * {@code null} if no {@link ConfigFileHolder} has been registered yet.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public ConfigFileHolder getConfig() {
		for (FileHolder holder : files)
			if (holder instanceof final ConfigFileHolder config)
				return config;
		return null;
	}

	/**
	 * Gets the first registered {@link MessagesFileHolder}.
	 * Plugins may override this method to return their own
	 * {@link MessagesFileHolder} type.
	 * <p>
	 * <b>Performance note</b>: Even though this is <b>very</b> insignificant, this
	 * method does iterate through all the registered {@link FileHolder FileHolders}
	 * and checks if they implement the {@link MessagesFileHolder} interface, so you may
	 * want to store the file instead of calling this method more than once on a listener
	 * or something like that. This would also allow you to return directly whatever
	 * implementation you are using.
	 *
	 * @return The first registered {@link MessagesFileHolder}, may be
	 * {@code null} if no {@link MessagesFileHolder} has been registered yet.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public MessagesFileHolder getMessages() {
		for (FileHolder holder : files)
			if (holder instanceof final MessagesFileHolder messages)
				return messages;
		return null;
	}

	/**
	 * This method is intended to be a future-proof solution to reload
	 * utility classes with just one method as new features that may
	 * require reloading can be added. Currently, this method reloads:
	 * <ul>
	 *     <li>{@link #reloadFiles() Registered files}</li>
	 * </ul>
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #reloadFiles()
	 *
	 * @return The amount of errors that occurred while
	 * reloading, 0 means no errors occurred.
	 */
	public int reload() {
		return reloadFiles();
	}

	/**
	 * Reloads all registered {@link FileHolder files}.
	 *
	 * @return The amount of files that failed when
	 * {@link FileHolder#reload() reloading}.
	 *
	 * @since MCUtils 1.0.0
	 */
	public int reloadFiles() {
		int failures = 0;
		for (FileHolder file : files)
			if (!file.reload())
				failures++;
		return failures;
	}

	/**
	 * Reloads all registered {@link FileUpdater updaters}
	 * without ignoring any path.
	 *
	 * @return The amount of files that failed when
	 * {@link FileUpdater#update(List<String>) updating}.
	 *
	 * @since MCUtils 1.0.0
	 */
	public int update() {
		return update(null);
	}

	/**
	 * Reloads all registered {@link FileUpdater updaters}
	 * without ignoring the paths specified at {@code updateIgnored}.
	 *
	 * @param ignored the file paths to ignore while updating
	 * all files, this paths won't be affected by the file updater,
	 * see {@link FileUpdater#update(List)} for more details.
	 *
	 * @return The amount of files that failed when
	 * {@link FileUpdater#update(List<String>) updating}.
	 *
	 * @since MCUtils 1.0.0
	 */
	public int update(@Nullable List<String> ignored) {
		int failures = 0;
		for (FileHolder file : files)
			if (file instanceof final FileUpdater updater && !updater.update(ignored))
				failures++;
		return failures;
	}
}
