package me.xdec0de.mcutils.regions;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	final int minY;
	final int maxY;

	/**
	 * Creates a 3D region from two locations.
	 * 
	 * @param loc1 the first location.
	 * @param loc2 the second location.
	 * 
	 * @throws NullPointerException If either location is null, {@link Location#getWorld()} returns null in either location
	 * or if said locations aren't from the same {@link World}
	 * @throws IllegalStateException If either location has it's world unloaded.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #Region3D(World, int, int, int, int, int, int)
	 */
	public Region3D(@Nonnull Location loc1, @Nonnull Location loc2) {
		super(loc1, loc2);
		this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
	}

	/**
	 * Creates a 3D region from two locations by their coordinates.
	 * 
	 * @param world the world this region will be in.
	 * @param x1 the x coordinate of the first location.
	 * @param y1 the y coordinate of the first location.
	 * @param z1 the z coordinate of the first location.
	 * @param x2 the x coordinate of the second location.
	 * @param y2 the y coordinate of the second location.
	 * @param z2 the z coordinate of the second location.
	 * 
	 * @throws IllegalArgumentException If <b>world</b> is null.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #Region3D(Location, Location)
	 */
	public Region3D(@Nonnull World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		super(world, x1, z1, x2, z2);
		minY = Math.min(y1, y2);
		maxY = Math.max(y1, y2);
	}

	/**
	 * Gets the minimum y coordinate of this region.
	 * 
	 * @return The minimum y coordinate of this region.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Gets the maximum y coordinate of this region.
	 * 
	 * @return The maximum y coordinate of this region.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Checks if <b>region</b> is inside of this {@link Region3D}. If <b>region</b>
	 * is a {@link Region2D}, {@link World#getMinHeight()} will be used as {@link #getMinY()}
	 * and {@link World#getMaxHeight()} as {@link #getMaxX()}.
	 * 
	 * @param region the region to check.
	 * 
	 * @return True only if <b>region</b> is <b>totally</b>
	 * inside of this {@link Region3D}, if <b>region</b> is null, false will be returned.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #overlaps(Region2D)
	 * @see #overlaps(Region3D)
	 * @see #contains(Location)
	 * @see #contains(int, int, int)
	 */
	@Override
	public <T extends Region2D> boolean contains(T region) {
		if (region == null)
			return false;
		int otherMinY = world.getMinHeight(), otherMaxY = world.getMaxHeight();
		if (region instanceof Region3D) {
			Region3D other = (Region3D)region;
			otherMinY = other.getMinY();
			otherMaxY = other.getMaxY();
		}
		return region.getWorld().equals(world) &&
				region.getMinX() >= minX && region.getMaxX() <= maxX &&
				otherMinY >= minY && otherMaxY <= maxY &&
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
	 * Checks if this {@link Region3D} overlaps with <b>region</b>
	 * 
	 * @param region the {@link Region3D} to check.
	 * 
	 * @return True if this {@link Region3D} overlaps with <b>region</b>, false
	 * otherwise or if <b>region</b> is null.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Region2D)
	 * @see #contains(Region3D)
	 */
	@Override
	public <T extends Region2D> boolean overlaps(T region) {
		if (region == null)
			return false;
		int otherMinY = world.getMinHeight(), otherMaxY = world.getMaxHeight();
		if (region instanceof Region3D) {
			Region3D other = (Region3D)region;
			otherMinY = other.getMinY();
			otherMaxY = other.getMaxY();
		}
		return region.getWorld().equals(world) &&
				!(region.getMinX() > maxX || otherMinY > maxY || region.getMinZ() > maxZ ||
						minZ > region.getMaxX() || minY > otherMaxY || minZ > region.getMaxZ());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Region3D))
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
