package es.xdec0de.mcutils.regions;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * A class to represent a 2D region.
 * <p>
 * Code has been taken, modified and documented from
 * <a href=https://www.spigotmc.org/threads/between-two-locations-cuboid.325266/>this</a> post.
 * If you like this implementation, please leave a positive rating on BillyGalbreath's reply.
 * 
 * @since MCUtils v1.0.0
 * 
 * @author <a href=https://www.spigotmc.org/members/billygalbreath.29442/>BillyGalbreath</a>
 * @author xDec0de_
 */
public class Region2D {

	private final World world;
	private final int minX, maxX, minZ, maxZ;

	/**
	 * Creates a 2D region from two locations.
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
	 * @see #Region2D(World, int, int, int, int)
	 */
	public Region2D(@Nonnull Location loc1, @Nonnull Location loc2) {
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
		this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
		this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
	}

	/**
	 * Creates a 2D region from two locations by their coordinates.
	 * 
	 * @param world the world this region will be in.
	 * @param x1 the x coordinate of the first location.
	 * @param z1 the z coordinate of the first location.
	 * @param x2 the x coordinate of the second location.
	 * @param z2 the z coordinate of the second location.
	 * 
	 * @throws NullPointerException If <b>world</b> is null.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #Region2D(Location, Location)
	 */
	public Region2D(@Nonnull World world, int x1, int z1, int x2, int z2) {
		if (world == null)
			throw new NullPointerException("Cuboid world cannot be null.");
		this.world = world;

		minX = Math.min(x1, x2);
		minZ = Math.min(z1, z2);
		maxX = Math.max(x1, x2);
		maxZ = Math.max(z1, z2);
	}

	/**
	 * Gets the {@link World} this {@link Region2D} is in.
	 * 
	 * @return The {@link World} this {@link Region2D} is in.
	 * 
	 * @since MCUtils v1.0.0
	 */
	@Nonnull
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the minimum x coordinate of this {@link Region2D}.
	 * 
	 * @return The minimum x coordinate of this {@link Region2D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinX() {
		return minX;
	}

	/**
	 * Gets the minimum z coordinate of this {@link Region2D}.
	 * 
	 * @return The minimum z coordinate of this {@link Region2D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinZ() {
		return minZ;
	}

	/**
	 * Gets the maximum x coordinate of this {@link Region2D}.
	 * 
	 * @return The maximum x coordinate of this {@link Region2D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * Gets the maximum z coordinate of this {@link Region2D}.
	 * 
	 * @return The maximum z coordinate of this {@link Region2D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxZ() {
		return maxZ;
	}

	/**
	 * Checks if <b>region</b> is inside of this {@link Region2D}.
	 * 
	 * @param region the {@link Region2D} to check.
	 * 
	 * @return True only if <b>region</b> is <b>totally</b>
	 * inside of this {@link Region2D}, if <b>region</b> is null, false will be returned.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #overlaps(Region2D)
	 * @see #contains(Location)
	 * @see #contains(int, int)
	 */
	public boolean contains(Region2D region) {
		return region.getWorld().equals(world) &&
				region.getMinX() >= minX && region.getMaxX() <= maxX &&
				region.getMinZ() >= minZ && region.getMaxZ() <= maxZ;
	}

	/**
	 * Checks if this {@link Region2D} contains the specified <b>location</b>.
	 * 
	 * @param location the {@link Location} to check.
	 * 
	 * @return True if this {@link Region2D} contains the specified <b>location</b>, if
	 * <b>location</b>'s {@link World} isn't equal to {@link #getWorld()}, false will be
	 * returned even if coordinates match, if you just want to check coordinates then use
	 * {@link #contains(int, int)}, if <b>location</b> is null, false will also be returned.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Region2D)
	 * @see #contains(int, int)
	 */
	public boolean contains(Location location) {
		return contains(location.getBlockX(), location.getBlockZ());
	}

	/**
	 * Checks if this {@link Region2D} contains the specified coordinates.
	 * 
	 * @param x the x coordinate to check.
	 * @param z the z coordinate to check.
	 * 
	 * @return True if this {@link Region2D} contains the specified coordinates, false otherwise.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Region2D)
	 * @see #contains(Location)
	 */
	public boolean contains(int x, int z) {
		return x >= minX && x <= maxX &&
				z >= minZ && z <= maxZ;
	}

	public boolean overlaps(Region2D region) {
		return region.getWorld().equals(world) &&
				!(region.getMinX() > maxX || region.getMinZ() > maxZ ||
						minZ > region.getMaxX() || minZ > region.getMaxZ());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Region2D)) {
			return false;
		}
		final Region2D other = (Region2D) obj;
		return world.equals(other.world)
				&& minX == other.minX
				&& minZ == other.minZ
				&& maxX == other.maxX
				&& maxZ == other.maxZ;
	}

	/**
	 * Returns a string representation of this {@link Region2D} following this format:
	 * <p>
	 * "Region2D[world:world_name, minX:X, minZ:Z, maxX:X, maxZ:Z]";
	 * 
	 * @return A string representation of this {@link Region2D}.
	 */
	@Override
	public String toString() {
		return "Region[world:" + world.getName() +
				", minX:" + minX +
				", minZ:" + minZ +
				", maxX:" + maxX +
				", maxZ:" + maxZ + "]";
	}
}
