package net.codersky.mcutils.regions;

import org.bukkit.Location;
import org.bukkit.World;

import javax.annotation.Nonnull;

/**
 * Interface that represents a region that may
 * or may not contain certain coordinates or
 * {@link Location locations}
 *
 * @since MCUtils v1.0.0
 *
 * @author xDec0de_
 */
public interface Region {

	/**
	 * Gets the {@link World} that this {@link Region} is in.
	 *
	 * @return The {@link World} that this {@link Region} is in,
	 * never {@code null}
	 *
	 * @since MCUtils v1.0.0
	 */
	@Nonnull
	public World getWorld();

	/**
	 * Checks if this {@link Region} contains the specified
	 * {@code x}, {@code y} and {@code z} coordinates. Keep in
	 * mind that some {@link Region} types may ignore certain
	 * values, like {@link Region2D}, that ignores {@code y} coordinates.
	 * <p>
	 * <b>IMPORTANT</b>: This method ignores {@link World worlds} (Obviously),
	 * so keep that in mind when doing your checks.
	 *
	 * @param x the X coordinate to check.
	 * @param y the Y coordinate to check.
	 * @param z the Z coordinate to check.
	 *
	 * @return {@code true} if this {@link Region} contains the specified
	 * set of coordinates, {@code false} otherwise.
	 *
	 * @since MCUtils v1.0.0
	 */
	public boolean contains(int x, int y, int z);

	/**
	 * Checks if this {@link Region} contains the specified
	 * {@code location}. By default, this method checks that the
	 * {@code location} {@link World} corresponds with {@link #getWorld()},
	 * then tests {@link #contains(int, int, int)}. Keep in
	 * mind that some {@link Region} types may ignore certain
	 * values, like {@link Region2D}, that ignores {@code y} coordinates.
	 *
	 * @param location the {@link Location} to check.
	 *
	 * @return {@code true} if this {@link Region} contains the specified
	 * {@code location}, {@code false} otherwise.
	 *
	 * @throws NullPointerException if {@code location} is {@code null}
	 *
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(int, int, int)
	 */
	public default boolean contains(@Nonnull Location location) {
		return getWorld().equals(location.getWorld()) &&
				contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
}
