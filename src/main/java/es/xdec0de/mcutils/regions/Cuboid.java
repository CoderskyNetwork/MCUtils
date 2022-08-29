package es.xdec0de.mcutils.regions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * A class to represent a 3D region.
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
public class Cuboid {

	private final World world;
	private final int minX, maxX, minY, maxY, minZ, maxZ;

	/**
	 * Creates a 3D region as a cuboid from two locations.
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
	 * @see #Cuboid(World, int, int, int, int, int, int)
	 */
	public Cuboid(@Nonnull Location loc1, @Nonnull Location loc2) {
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
	 * Creates a 3D region as a cuboid from two locations by their coordinates.
	 * 
	 * @param world the world this cuboid will be in.
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
	 * @see #Cuboid(Location, Location)
	 */
	public Cuboid(@Nonnull World world, int x1, int y1, int z1, int x2, int y2, int z2) {
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
	 * Gets the {@link World} this cuboid is in.
	 * 
	 * @return The {@link World} this cuboid is in.
	 * 
	 * @since MCUtils v1.0.0
	 */
	@Nonnull
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the minimum x coordinate of this {@link Cuboid}.
	 * 
	 * @return The minimum x coordinate of this {@link Cuboid}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinX() {
		return minX;
	}

	/**
	 * Gets the minimum y coordinate of this {@link Cuboid}.
	 * 
	 * @return The minimum y coordinate of this {@link Cuboid}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinY() {
		return minY;
	}

	/**
	 * Gets the minimum z coordinate of this {@link Cuboid}.
	 * 
	 * @return The minimum z coordinate of this {@link Cuboid}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinZ() {
		return minZ;
	}

	/**
	 * Gets the maximum x coordinate of this {@link Cuboid}.
	 * 
	 * @return The maximum x coordinate of this {@link Cuboid}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * Gets the maximum y coordinate of this {@link Cuboid}.
	 * 
	 * @return The maximum y coordinate of this {@link Cuboid}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxY() {
		return maxY;
	}

	/**
	 * Gets the maximum z coordinate of this {@link Cuboid}.
	 * 
	 * @return The maximum z coordinate of this {@link Cuboid}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxZ() {
		return maxZ;
	}

	/**
	 * Checks if <b>cuboid</b> is inside of this {@link Cuboid}.
	 * 
	 * @param cuboid the {@link Cuboid} to check.
	 * 
	 * @return True only if <b>cuboid</b> is <b>totally</b>
	 * inside of this {@link Cuboid}, if <b>cuboid</b> is null, false will be returned.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #overlaps(Cuboid)
	 * @see #contains(Location)
	 * @see #contains(int, int, int)
	 */
	public boolean contains(@Nullable Cuboid cuboid) {
		if (cuboid == null)
			return false;
		return cuboid.getWorld().equals(world) &&
				cuboid.getMinX() >= minX && cuboid.getMaxX() <= maxX &&
				cuboid.getMinY() >= minY && cuboid.getMaxY() <= maxY &&
				cuboid.getMinZ() >= minZ && cuboid.getMaxZ() <= maxZ;
	}

	/**
	 * Checks if this {@link Cuboid} contains the specified <b>location</b>.
	 * 
	 * @param location the {@link Location} to check.
	 * 
	 * @return True if this {@link Cuboid} contains the specified <b>location</b>, if
	 * <b>location</b>'s {@link World} isn't equal to {@link #getWorld()}, false will be
	 * returned even if coordinates match, if you just want to check coordinates then use
	 * {@link #contains(int, int, int)}, if <b>location</b> is null, false will also be returned.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Cuboid)
	 * @see #contains(int, int, int)
	 */
	public boolean contains(@Nullable Location location) {
		if (location == null || !location.getWorld().equals(world))
			return false;
		return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	/**
	 * Checks if this {@link Cuboid} contains the specified coordinates.
	 * 
	 * @param x the x coordinate to check.
	 * @param y the y coordinate to check.
	 * @param z the z coordinate to check.
	 * 
	 * @return True if this {@link Cuboid} contains the specified coordinates, false otherwise.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Cuboid)
	 * @see #contains(Location)
	 */
	public boolean contains(int x, int y, int z) {
		return x >= minX && x <= maxX &&
				y >= minY && y <= maxY &&
				z >= minZ && z <= maxZ;
	}

	/**
	 * Checks if this {@link Cuboid} overlaps with <b>cuboid</b>
	 * 
	 * @param cuboid the {@link Cuboid} to check.
	 * 
	 * @return True if this {@link Cuboid} overlaps with <b>cuboid</b>, false
	 * otherwise or if <b>cuboid</b> is null.
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Cuboid)
	 */
	public boolean overlaps(@Nullable Cuboid cuboid) {
		if (cuboid == null)
			return false;
		return cuboid.getWorld().equals(world) &&
				!(cuboid.getMinX() > maxX || cuboid.getMinY() > maxY || cuboid.getMinZ() > maxZ ||
						minZ > cuboid.getMaxX() || minY > cuboid.getMaxY() || minZ > cuboid.getMaxZ());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Cuboid))
			return false;
		final Cuboid other = (Cuboid) obj;
		return world.equals(other.world)
				&& minX == other.minX
				&& minY == other.minY
				&& minZ == other.minZ
				&& maxX == other.maxX
				&& maxY == other.maxY
				&& maxZ == other.maxZ;
	}

	/**
	 * Returns a string representation of this {@link Cuboid} following this format:
	 * <p>
	 * "Cuboid[world:world_name, minX:X, minY:Y, minZ:Z, maxX:X, maxY:Y, maxZ:Z]";
	 * 
	 * @return A string representation of this {@link Cuboid}.
	 */
	@Override
	public String toString() {
		return "Cuboid[world:" + world.getName() +
				", minX:" + minX +
				", minY:" + minY +
				", minZ:" + minZ +
				", maxX:" + maxX +
				", maxY:" + maxY +
				", maxZ:" + maxZ + "]";
	}
}
