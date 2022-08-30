package es.xdec0de.mcutils.regions;

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
public class Region3D {

	private final World world;
	private final int minX, maxX, minY, maxY, minZ, maxZ;

	/**
	 * Creates a 3D region from two locations.
	 * 
	 * @param loc1 the first location.
	 * @param loc2 the second location.
	 * 
	 * @throws NullPointerException If either location is null or {@link Location#getWorld()} returns null in either location.
	 * @throws IllegalStateException If either location has it's world unloaded.
	 * @throws IllegalArgumentException If locations aren't from the same {@link World}.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #Region3D(World, int, int, int, int, int, int)
	 */
	public Region3D(@Nonnull Location loc1, @Nonnull Location loc2) {
		if (loc1 == null || loc2 == null)
			throw new NullPointerException("Cuboid locations cannot be null.");
		if (loc1.getWorld() == null || loc2.getWorld() == null)
			throw new NullPointerException("Location worlds cannot be null.");
		if (!loc1.isWorldLoaded() || !loc2.isWorldLoaded())
			throw new IllegalStateException("Location worlds must be loaded.");
		if (!loc1.getWorld().equals(loc2.getWorld()))
			throw new IllegalArgumentException("Cuboid locations must be on the same world.");
		this.world = loc1.getWorld();
		this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
		this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
		this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
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
	 * @throws NullPointerException If <b>world</b> is null.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #Region3D(Location, Location)
	 */
	public Region3D(@Nonnull World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		if (world == null)
			throw new NullPointerException("Cuboid world cannot be null.");
		this.world = world;

		minX = Math.min(x1, x2);
		minY = Math.min(y1, y2);
		minZ = Math.min(z1, z2);
		maxX = Math.max(x1, x2);
		maxY = Math.max(y1, y2);
		maxZ = Math.max(z1, z2);
	}

	/**
	 * Gets the {@link World} this {@link Region3D} is in.
	 * 
	 * @return The {@link World} this {@link Region3D} is in.
	 * 
	 * @since MCUtils v1.0.0
	 */
	@Nonnull
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the minimum x coordinate of this {@link Region3D}.
	 * 
	 * @return The minimum x coordinate of this {@link Region3D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinX() {
		return minX;
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
	 * Gets the minimum z coordinate of this {@link Region3D}.
	 * 
	 * @return The minimum z coordinate of this {@link Region3D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinZ() {
		return minZ;
	}

	/**
	 * Gets the maximum x coordinate of this {@link Region3D}.
	 * 
	 * @return The maximum x coordinate of this {@link Region3D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * Gets the maximum y coordinate of this {@link Region3D}.
	 * 
	 * @return The maximum y coordinate of this {@link Region3D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Gets the maximum z coordinate of this {@link Region3D}.
	 * 
	 * @return The maximum z coordinate of this {@link Region3D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxZ() {
		return maxZ;
	}

	/**
	 * Checks if <b>region</b> is inside of this {@link Region3D}.
	 * 
	 * @param region the {@link Region3D} to check.
	 * 
	 * @return True only if <b>region</b> is <b>totally</b>
	 * inside of this {@link Region3D}, if <b>region</b> is null, false will be returned.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #overlaps(Region3D)
	 * @see #contains(Location)
	 * @see #contains(int, int, int)
	 */
	public boolean contains(@Nullable Region3D region) {
		if (region == null)
			return false;
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
	 * @see #overlaps(Region2D)
	 * @see #contains(Region3D)
	 */
	public boolean overlaps(@Nullable Region3D region) {
		if (region == null)
			return false;
		return region.getWorld().equals(world) &&
				!(region.getMinX() > maxX || region.getMinY() > maxY || region.getMinZ() > maxZ ||
						minZ > region.getMaxX() || minY > region.getMaxY() || minZ > region.getMaxZ());
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
	 * @see #overlaps(Region3D)
	 * @see #contains(Region3D)
	 */
	public boolean overlaps(@Nullable Region2D region) {
		if (region == null)
			return false;
		return region.getWorld().equals(world) &&
				!(region.getMinX() > maxX || region.getMinZ() > maxZ ||
						minZ > region.getMaxX() || minZ > region.getMaxZ());
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
}
