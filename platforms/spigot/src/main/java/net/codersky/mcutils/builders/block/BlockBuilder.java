package net.codersky.mcutils.builders.block;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.sign.Side;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

import net.codersky.mcutils.MCPlugin;
import net.codersky.mcutils.java.MCLists;

public class BlockBuilder {

	private final Block block;

	public BlockBuilder(@Nonnull Block bukkitBlock) {
		this.block = Objects.requireNonNull(bukkitBlock, "bukkitBlock cannot be null");;
	}

	public BlockBuilder(@Nonnull Location blockLocation) {
		Objects.requireNonNull(blockLocation, "blockLocation cannot be null");
		this.block = blockLocation.getWorld().getBlockAt(blockLocation);
	}

	public BlockBuilder(@Nonnull World world, int x, int y, int z) {
		this.block = Objects.requireNonNull(world, "world cannot be null").getBlockAt(x, y, z);
	}

	@Nonnull
	public Block bukkit() {
		return block;
	}

	@Nonnull
	public Material getType() {
		return block.getType();
	}

	@Nonnull
	public BlockBuilder setType(Material material) {
		block.setType(material);
		return this;
	}

	@Nonnull
	public BlockBuilder setType(Material material, boolean applyPhysics) {
		block.setType(material, applyPhysics);
		return this;
	}

	/**
	 * Checks if this block's material matches the <b>material</b> string, this
	 * string may start with the '*' character to check if this block's material
	 * ends with the rest of string, here are some examples:
	 * <ul>
	 *   <li>"diamond_sword" will match only {@link Material#DIAMOND_SWORD}</li>
	 *   <li>"*sword" materials ending with "SWORD" such as {@link Material#DIAMOND_SWORD}</li>
	 *   <li>"iron*" materials starting with "IRON" such as {@link Material#IRON_AXE}</li>
	 *   <li>"disc*" materials containing "DISC" such as {@link Material#DISC_FRAGMENT_5} or {@link Material#MUSIC_DISC_5}</li>
	 * </ul>
	 * This check is case insensitive as the <b>material</b> string will be converted to upper case.
	 * 
	 * @param material the material string to check.
	 * 
	 * @return True if this block's material matches <b>material</b>, false otherwise.
	 * 
	 * @since CSKCore 1.0.0
	 */
	public boolean matches(@Nonnull String material) {
		final int len = material.length() - 1;
		final String mat = material.toUpperCase();
		if (mat.charAt(0) == '*' && mat.charAt(len) == '*')
			return block.getType().name().contains(mat.substring(1, len - 1));
		else if (mat.charAt(0) == '*') 
			return block.getType().name().endsWith(mat.substring(1));
		else if (mat.charAt(len) == '*') 
			return block.getType().name().startsWith(mat.substring(0, len - 1));
		return block.getType().name().equals(mat);
	}

	/*
	 * Location
	 */

	public World getWorld() {
		return block.getWorld();
	}

	public Chunk getChunk() {
		return block.getChunk();
	}

	public Location getLocation() {
		return block.getLocation();
	}

	public boolean isAt(World world) {
		return block.getWorld().equals(world);
	}

	public boolean isAt(Chunk chunk) {
		return block.getChunk().equals(chunk);
	}

	public boolean isAt(Location location) {
		return block.getLocation().equals(location);
	}

	public boolean isAt(Block block) {
		return isAt(block.getLocation());
	}

	public boolean isAt(BlockBuilder block) {
		return isAt(block.getLocation());
	}

	/*
	 * Relatives
	 */

	@Nonnull
	public BlockBuilder getRelative(@Nonnull BlockFace face) {
		return new BlockBuilder(block.getRelative(face));
	}

	@Nonnull
	public BlockBuilder getRelative(@Nonnull BlockFace face, int distance) {
		return new BlockBuilder(block.getRelative(face, distance));
	}

	@Nonnull
	public BlockBuilder getRelative(int modX, int modY, int modZ) {
		return new BlockBuilder(block.getRelative(modX, modY, modZ));
	}

	@Nonnull
	public Block getRelativeBlock(@Nonnull BlockFace face) {
		return block.getRelative(face);
	}

	@Nonnull
	public Block getRelativeBlock(@Nonnull BlockFace face, int distance) {
		return block.getRelative(face, distance);
	}

	@Nonnull
	public Block getRelativeBlock(int modX, int modY, int modZ) {
		return block.getRelative(modX, modY, modZ);
	}

	/*
	 * BlockState
	 */

	public BlockState getState() {
		return block.getState();
	}

	public BlockBuilder update() {
		getState().update();
		return this;
	}

	public BlockBuilder update(boolean force) {
		getState().update(force);
		return this;
	}

	public boolean isPlaced() {
		return block.getState().isPlaced();
	}

	/*
	 * Containers
	 */

	/**
	 * Checks if this block is a container.
	 * 
	 * @return True if this block is a container, false otherwise.
	 */
	public boolean isContainer() {
		return block.getState() instanceof Container;
	}

	/**
	 * <b>Note</b>: This is a Container specific method, it requires {@link #isChest()} to return true.
	 * <p>
	 * Gets the inventory of the block represented by this container.
	 * <p>
	 * If the block was changed to a different type in the meantime, thereturned inventory might no longer be valid.
	 * <p>
	 * If this block state is not placed this will return the captured inventory snapshot instead.
	 * 
	 * @return The inventory of the block represented by this container.
	 * 
	 * @see #getSnapshotInventory()
	 */
	public Inventory getInventory() {
		return isContainer() ? ((Container)block.getState()).getInventory() : null;
	}

	/**
	 * <b>Note</b>: This is a Container specific method, it requires {@link #isChest()} to return true.
	 * <p>
	 * Gets the captured inventory snapshot of this container.
	 * <p>
	 * The returned inventory is not linked to any block. Any modifications to
	 * the returned inventory will not be applied to the block represented by
	 * this block state up until update(boolean, boolean) has been called.
	 * 
	 * @return The captured inventory snapshot of this container.
	 * 
	 * @see #getInventory()
	 */
	public Inventory getSnapshotInventory() {
		return isContainer() ? ((Container)block.getState()).getSnapshotInventory() : null;
	}

	/*
	 * Chests
	 */

	/**
	 * Checks if this block is a chest, double and regular chests are valid for this method.
	 * 
	 * @return True if this block is a chest, false otherwise.
	 */
	public boolean isChest() {
		return block.getState() instanceof Chest;
	}

	/**
	 * Checks if this block is a double chest.
	 * 
	 * @return True if this block is a double chest, false otherwise.
	 */
	public boolean isDoubleChest() {
		return isContainer() ? getInventory() instanceof DoubleChestInventory : false;
	}

	/**
	 * <b>Note</b>: This is a Chest specific method, it requires {@link #isChest()} to return true.
	 * <p>
	 * Gets the inventories linked to this chest, the array size may differ between
	 * one and two if this block is a single chest or a double chest, this method
	 * will return null if this block isn't a chest.
	 * 
	 * @return An array with all inventories linked to this chest, null if {@link #isChest()} returns false.
	 */
	public Inventory[] getChestInventories() {
		if (!isChest())
			return null;
		if (!isDoubleChest())
			return new Inventory[] { ((Chest)block.getState()).getInventory() };
		DoubleChestInventory doubleChest = (DoubleChestInventory)((Chest)block.getState()).getInventory();
		return new Inventory[] { doubleChest.getLeftSide(), doubleChest.getRightSide() };
	}

	/**
	 * <b>Note</b>: This is a DoubleChest specific method, it requires {@link #isChest()} to return true.
	 * <p>
	 * Gets the chest attached to this chest, this is a simplification of getLeftSide and getRightSide
	 * from Bukkit, making it so it always returns the other chest as you already have this chest object. If you
	 * want the old left & right methods, they also exist on {@link BlockBuilder} as {@link #getLeftChest()} and {@link #getRightChest()}
	 * 
	 * @return The chest attached to this chest, null if {@link #isDoubleChest()} returns false.
	 */
	public BlockBuilder getOtherChest() {
		if (!isDoubleChest())
			return null;
		DoubleChestInventory doubleChest = (DoubleChestInventory)((Chest)block.getState()).getInventory();
		return new BlockBuilder(isAt(doubleChest.getLeftSide().getLocation()) ? doubleChest.getRightSide().getLocation() : doubleChest.getLeftSide().getLocation());
	}

	/**
	 * <b>Note</b>: This is a DoubleChest specific method, it requires {@link #isChest()} to return true.
	 * <p>
	 * Gets the left half of this double chest.
	 * 
	 * @return The left half of this double chest, null if {@link #isDoubleChest()} returns false.
	 */
	public BlockBuilder getLeftChest() {
		if (!isDoubleChest())
			return null;
		return new BlockBuilder( ((DoubleChestInventory) ( (Chest)block.getState() ).getInventory() ).getLeftSide().getLocation() );
	}

	/**
	 * <b>Note</b>: This is a DoubleChest specific method, it requires {@link #isChest()} to return true.
	 * <p>
	 * Gets the right half of this double chest.
	 * 
	 * @return The right half of this double chest, null if {@link #isDoubleChest()} returns false.
	 */
	public BlockBuilder getRightChest() {
		if (!isDoubleChest())
			return null;
		return new BlockBuilder( ((DoubleChestInventory) ( (Chest)block.getState() ).getInventory() ).getRightSide().getLocation() );
	}

	/*
	 * Bisected
	 */

	public boolean isBisected() {
		return block.getBlockData() instanceof Bisected;
	}

	public Half getHalf() {
		return isBisected() ? ((Bisected)block.getBlockData()).getHalf() : null;
	}

	public BlockBuilder getOtherHalf() {
		Half half = getHalf();
		if (half == null)
			return null;
		return half == Half.TOP ? getRelative(BlockFace.DOWN) : getRelative(BlockFace.UP);
	}

	/*
	 * Directional
	 */

	/**
	 * Checks if this block is directional.
	 * 
	 * @return True if this block is directional, false otherwise.
	 */
	public boolean isDirectional() {
		return block.getBlockData() instanceof org.bukkit.block.data.Directional;
	}

	/**
	 * <b>Note</b>: This is a Directional specific method, it requires {@link #isDirectional()} to return true.
	 * <p>
	 * Gets the 'facing' property of this block, will be null if this block isn't Directional.
	 * 
	 * @return The value of the 'facing' property of this block, null if {@link #isDirectional()} returns false.
	 */
	@Nullable
	public BlockFace getFacing() {
		return isDirectional() ? ((org.bukkit.block.data.Directional)block.getBlockData()).getFacing() : null;
	}

	/**
	 * <b>Note</b>: This is a Directional specific method, it requires {@link #isDirectional()} to return true.
	 * <p>
	 * Sets the 'facing' property of this block to <b>face</b>, will do nothing if the block isn't Directional.
	 * 
	 * @return This {@link BlockBuilder}
	 */
	@Nonnull
	public BlockBuilder setFacing(BlockFace face) {
		if (!isDirectional())
			return this;
		Directional dir = ((Directional)block.getBlockData());
		dir.setFacing(face);
		block.setBlockData(dir);
		block.getState().update();
		return this;
	}

	/**
	 * <b>Note</b>: This is a Directional specific method, it requires {@link #isDirectional()} to return true.
	 * <p>
	 * Gets the faces which are applicable to this block, will be null if this block isn't Directional.
	 * 
	 * @return The allowed 'facing' values, null if {@link #isDirectional()} returns false.
	 */
	@Nullable
	public Set<BlockFace> getFaces() {
		return isDirectional() ? ((org.bukkit.block.data.Directional)block.getBlockData()).getFaces() : null;
	}

	/*
	 * Rotatable
	 */

	public boolean isRotatable() {
		return block.getState() instanceof Rotatable;
	}

	public BlockFace getRotation() {
		return isRotatable() ? ((Rotatable)block.getState()).getRotation() : null;
	}

	public BlockBuilder setRotation(BlockFace face) {
		if (!isRotatable())
			return this;
		Rotatable rotatable = ((Rotatable)block.getState());
		rotatable.setRotation(face);
		block.setBlockData(rotatable);
		return this;
	}
