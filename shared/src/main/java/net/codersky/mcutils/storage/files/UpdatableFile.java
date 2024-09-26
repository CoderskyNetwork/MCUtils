package net.codersky.mcutils.storage.files;

import net.codersky.mcutils.Reloadable;

import java.util.List;

public interface UpdatableFile extends Reloadable {

	/**
	 * Updates any files handled by this {@link UpdatableFile}.
	 *
	 * @param ignored A list of paths that shall be
	 * ignored when updating files.
	 *
	 * @return True on success, false on failure.
	 *
	 * @since MCUtils 1.0.0
	 */
	boolean update(List<String> ignored);
}
