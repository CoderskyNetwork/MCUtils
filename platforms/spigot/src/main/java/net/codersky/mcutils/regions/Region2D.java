package net.codersky.mcutils.regions;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.MCUtils;
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
public class Region2D implements Region {

	final World world;
	final int minX;
	final int maxX;
	final int minZ;
	final int maxZ;

	/*
	 * Constructors
	 */

	/**
	 * Creates a 2D {@link Region} from two locations.
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
	 * @see #Region2D(World, int, int, int, int)
	 */
	public Region2D(@Nonnull Location loc1, @Nonnull Location loc2) {
		if (!Objects.equals(loc1.getWorld(), loc2.getWorld()))
			throw new IllegalArgumentException("Worlds from loc1 and loc2 differ");
		this.world = Objects.requireNonNull(loc1.getWorld(), "Region world cannot be null");
		this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
		this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
		this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
	}

	/**
	 * Creates a 2D {@link Region} by integer coordinates.
	 * 
	 * @param world the {@link  World} this region will be in.
	 * @param x1 the X coordinate of the <b>first</b> location.
	 * @param z1 the Z coordinate of the <b>first</b> location.
	 * @param x2 the X coordinate of the <b>second</b> location.
	 * @param z2 the Z coordinate of the <b>second</b> location.
	 * 
	 * @throws NullPointerException If {@code world} is {code null}.
	 *
	 * @since MCUtils v1.0.0
	 * 
	 * @see #Region2D(Location, Location)
	 */
	public Region2D(@Nonnull World world, int x1, int z1, int x2, int z2) {
		this.world = Objects.requireNonNull(world, "World cannot be null");
		this.maxX = Math.max(x1, x2);
		this.maxZ = Math.max(z1, z2);
		this.minX = Math.min(x1, x2);
		this.minZ = Math.min(z1, z2);
	}

	/*
	 * Location getters
	 */

	@Nonnull
	@Override
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the minimum X coordinate of this {@link Region2D}.
	 * 
	 * @return The minimum X coordinate of this {@link Region2D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinX() {
		return minX;
	}

	/**
	 * Gets the minimum Z coordinate of this {@link Region2D}.
	 * 
	 * @return The minimum Z coordinate of this {@link Region2D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMinZ() {
		return minZ;
	}

	/**
	 * Gets the maximum X coordinate of this {@link Region2D}.
	 * 
	 * @return The maximum X coordinate of this {@link Region2D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxX() {
		return maxX;
	}

	/**
	 * Gets the maximum Z coordinate of this {@link Region2D}.
	 * 
	 * @return The maximum Z coordinate of this {@link Region2D}.
	 * 
	 * @since MCUtils v1.0.0
	 */
	public int getMaxZ() {
		return maxZ;
	}

	/*
	 * Contains
	 */

	/**
	 * Checks if a {@code region} is inside of this {@link Region2D}.
	 * 
	 * @param region the region to check.
	 * 
	 * @return {@code true} only if {@code region} is <b>totally</b>
	 * inside of this {@link Region2D}, {@code false} otherwise.
	 *
	 * @throws NullPointerException if {@code region} is {@code null}.
	 *
	 * @since MCUtils v1.0.0
	 * 
	 * @see #overlaps(Region2D)
	 * @see #contains(Location)
	 * @see #contains(int, int)
	 */
	public boolean contains(@Nonnull Region2D region) {
		return region.getWorld().equals(world) &&
				region.getMinX() >= minX && region.getMaxX() <= maxX &&
				region.getMinZ() >= minZ && region.getMaxZ() <= maxZ;
	}

	@Override
	public boolean contains(int x, int y, int z) {
		return contains(x, z);
	}

	/**
	 * Checks if this {@link Region2D} contains the specified {@code x} and
	 * {@code y} coordinates.
	 * <p>
	 * <b>IMPORTANT</b>: This method ignores {@link World worlds} (Obviously),
	 * so keep that in mind when doing your checks.
	 *
	 * @param x the X coordinate to check.
	 * @param z the Z coordinate to check.
	 *
	 * @return {@code true} if this {@link Region2D} contains the specified
	 * set of coordinates, {@code false} otherwise.
	 *
	 * @since MCUtils v1.0.0
	 *
	 * @see #contains(Region2D)
	 * @see #contains(Location)
	 */
	public boolean contains(int x, int z) {
		return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
	}

	/*
	 * Overlaps
	 */

	/**
	 * Checks if this {@link Region2D} overlaps with {@code region}
	 * 
	 * @param region the region to check.
	 * 
	 * @return True if this region overlaps with {@code region}, false
	 * otherwise
	 * 
	 * @since MCUtils v1.0.0
	 * 
	 * @see #contains(Region2D)
	 */
	public boolean overlaps(@Nonnull Region2D region) {
		return region.getWorld().equals(world) &&
				!(region.getMinX() >= maxX || region.getMaxX() <= minX ||
					region.getMinZ() >= maxZ || region.getMaxZ() <= minZ);
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
		return "Region2D[world:" + world.getName() +
				", minX:" + minX +
				", minZ:" + minZ +
				", maxX:" + maxX +
				", maxZ:" + maxZ + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(world, minX, minZ, maxX, maxZ);
	}
}
