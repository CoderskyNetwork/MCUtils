package net.codersky.mcutils.files;

import java.util.List;

/**
 * An interface for {@link FileHolder FileHolders}
 * that also support updating their files.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public interface FileUpdater extends FileHolder {

	/**
	 * Updates any files handled by this {@link FileUpdater}.
	 * 
	 * @param ignored A list of paths that shall be
	 * ignored when updating files.
	 * 
	 * @return True on success, false on failure.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean update(List<String> ignored);
}
