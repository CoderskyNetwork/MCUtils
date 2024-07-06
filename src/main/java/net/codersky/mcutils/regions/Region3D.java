package net.codersky.mcutils.regions;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.MCUtils;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * A class to represent a 3D region.
 * <p>
 * Code has been taken, modified and documented from
 * <a href=https://www.spigotmc.org/threads/between-two-locations-region.325266/>this</a> post.
 * If you like this implementation, please leave a positive rating on BillyGalbreath's reply.
 * 
 * @since MCUtils v1.0.0
 * 
 * @author <a href=https://www.spigotmc.org/members/billygalbreath.29442/>BillyGalbreath</a>
 * @author xDec0de_
 */
public class Region3D extends Region2D {

	private final int maxY;
	private final int minY;

	/**
	 * Creates a 3D {@link Region} from two locations.
	 * 
	 * @param loc1 the first {@link Location}.
	 * @param loc2 the second {@link Location}.
	 * 
	 * @throws NullPointerException If either {@link Location} is {@code null}, {@link Location#getWorld()}
	 * returns {@code null} in either {@link Location} or if said locations aren't from the same {@link World}
	 * @throws IllegalStateException If either location has its world unloaded.
	 * @throws IllegalArgumentException If the world from {@code loc1} isn't equal to the
	 * world of {@code loc2}
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #Region3D(World, int, int, int, int, int, int)
	 */
	public Region3D(@Nonnull Location loc1, @Nonnull Location loc2) {
		super(loc1, loc2);
		this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
	}

	/**
	 * Creates a 3D {@link Region} by integer coordinates.
	 * 
	 * @param world the world this region will be in.
	 * @param x1 the X coordinate of the <b>first</b> location.
	 * @param y1 the Y coordinate of the <b>first</b> location.
	 * @param z1 the Z coordinate of the <b>first</b> location.
	 * @param x2 the X coordinate of the <b>second</b> location.
	 * @param y2 the Y coordinate of the <b>second</b> location.
	 * @param z2 the Z coordinate of the <b>second</b> location.
	 * 
	 * @throws NullPointerException If {@code world} is {code null}.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #Region3D(Location, Location)
	 */
	public Region3D(@Nonnull World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		super(world, x1, z1, x2, z2);
		this.maxY = Math.max(y1, y2);
		this.minY = Math.min(y1, y2);
	}

	/**
	 * Gets the minimum y coordinate of this {@link Region3D}.
	 * 
	 * @return The minimum y coordinate of this {@link Region3D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Gets the maximum Y coordinate of this {@link Region3D}.
	 * 
	 * @return The maximum Y coordinate of this {@link Region3D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Checks if a {@code region} is inside of this {@link Region3D}.
	 *
	 * @param region the region to check.
	 *
	 * @return {@code true} only if {@code region} is <b>totally</b>
	 * inside of this {@link Region3D}, {@code false} otherwise.
	 *
	 * @throws NullPointerException if {@code region} is {@code null}.
	 *
	 * @since MCUtils v1.0.0
	 *
	 * @see #contains(Region3D)
	 * @see #contains(Location)
	 * @see #contains(int, int)
	 * @see #overlaps(Region2D)
	 */
	@Override
	public boolean contains(@Nonnull Region2D region) {
		return region.getWorld().equals(world) &&
				region.getMinX() >= minX && region.getMaxX() <= maxX &&
				world.getMinHeight() >= minY && world.getMaxHeight() <= maxY &&
				region.getMinZ() >= minZ && region.getMaxZ() <= maxZ;
	}

	/**
	 * Checks if a {@code region} is inside of this {@link Region3D}.
	 *
	 * @param region the region to check.
	 *
	 * @return {@code true} only if {@code region} is <b>totally</b>
	 * inside of this {@link Region3D}, {@code false} otherwise.
	 *
	 * @throws NullPointerException if {@code region} is {@code null}.
	 *
	 * @since MCUtils v1.0.0
	 *
	 * @see #contains(Region2D)
	 * @see #contains(Location)
	 * @see #contains(int, int)
	 * @see #overlaps(Region2D)
	 */
	public boolean contains(@Nonnull Region3D region) {
		return region.getWorld().equals(world) &&
				region.getMinX() >= minX && region.getMaxX() <= maxX &&
				region.getMinY() >= minY && region.getMaxY() <= maxY &&
				region.getMinZ() >= minZ && region.getMaxZ() <= maxZ;
	}

	/**
	 * Checks if this {@link Region3D} contains the specified <b>location</b>.
	 * 
	 * @param location the {@link Location} to check.
	 * 
	 * @return True if this {@link Region3D} contains the specified <b>location</b>, if
	 * <b>location</b>'s {@link World} isn't equal to {@link #getWorld()}, false will be
	 * returned even if coordinates match, if you just want to check coordinates then use
	 * {@link #contains(int, int, int)}, if <b>location</b> is null, false will also be returned.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Region3D)
	 * @see #contains(int, int, int)
	 */
	@Override
	public boolean contains(@Nullable Location location) {
		if (location == null || !location.getWorld().equals(world))
			return false;
		return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	/**
	 * Checks if this {@link Region3D} contains the specified coordinates.
	 * 
	 * @param x the x coordinate to check.
	 * @param y the y coordinate to check.
	 * @param z the z coordinate to check.
	 * 
	 * @return True if this {@link Region3D} contains the specified coordinates, false otherwise.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Region3D)
	 * @see #contains(Location)
	 */
	public boolean contains(int x, int y, int z) {
		return x >= minX && x <= maxX &&
				y >= minY && y <= maxY &&
				z >= minZ && z <= maxZ;
	}

	/**
	 * Checks if this {@link Region3D} overlaps with {@code region}.
	 * 
	 * @param region the {@link Region3D} to check.
	 * 
	 * @return {@code true} if this {@link Region3D} overlaps with {@code region},
	 * {@code false} otherwise.
	 *
	 * @throws NullPointerException if {@code region} is {@code null}.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Region2D)
	 * @see #contains(Region3D)
	 * @see #overlaps(Region3D)
	 */
	@Override
	public boolean overlaps(@Nonnull Region2D region) {
		final World otherWorld = region.getWorld();
		return super.overlaps(region) || otherWorld.getMinHeight() >= maxY || otherWorld.getMaxHeight() >= minY;
	}

	/**
	 * Checks if this {@link Region3D} overlaps with {@code region}.
	 *
	 * @param region the {@link Region3D} to check.
	 *
	 * @return {@code true} if this {@link Region3D} overlaps with {@code region},
	 * {@code false} otherwise.
	 *
	 * @throws NullPointerException if {@code region} is {@code null}.
	 *
	 * @since MCUtils v1.0.0
	 *
	 * @see #contains(Region2D)
	 * @see #contains(Region3D)
	 * @see #overlaps(Region2D)
	 */
	public boolean overlaps(@Nonnull Region3D region) {
		return super.overlaps(region) || region.getMinY() >= maxY || region.getMaxY() >= minY;
	}

	/*
	 * Object methods
	 */

	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Region3D))
			return false;
		final Region3D other = (Region3D) obj;
		return world.equals(other.world)
				&& minX == other.minX
				&& minY == other.minY
				&& minZ == other.minZ
				&& maxX == other.maxX
				&& maxY == other.maxY
				&& maxZ == other.maxZ;
	}

	/**
	 * Returns a string representation of this {@link Region3D} following this format:
	 * <p>
	 * "Region3D[world:world_name, minX:X, minY:Y, minZ:Z, maxX:X, maxY:Y, maxZ:Z]";
	 * 
	 * @return A string representation of this {@link Region3D}.
	 */
	@Override
	public String toString() {
		return "Region3D[world:" + world.getName() +
				", minX:" + minX +
				", minY:" + minY +
				", minZ:" + minZ +
				", maxX:" + maxX +
				", maxY:" + maxY +
				", maxZ:" + maxZ + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(world, minX, minY, minZ, maxX, maxY, maxZ);
	}
}
