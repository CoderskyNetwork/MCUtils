package net.codersky.mcutils.files;

/**
 * An interface that represents any class
 * that can hold any number of files, implementations
 * must be able to create, reload and save said files.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public interface FileHolder {

	/**
	 * Creates any files handled by this {@link FileHolder}.
	 * 
	 * @return True on success, false on failure.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean create();

	/**
	 * Reloads any files handled by this {@link FileHolder}.
	 * 
	 * @return True on success, false on failure.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean reload();

	/**
	 * Saves any files handled by this {@link FileHolder}.
	 * 
	 * @return True on success, false on failure.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean save();

}
