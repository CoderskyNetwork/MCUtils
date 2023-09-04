package me.xdec0de.mcutils.builders;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.xdec0de.mcutils.java.MCNumbers;
import me.xdec0de.mcutils.java.strings.MCStrings;

/**
 * A class made to provide easy an fast
 * creation of {@link Inventory Inventories}.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public class InventoryBuilder implements Cloneable {

	private String title;
	private Inventory inv;

	/**
	 * Tests true if an {@link ItemStack} is null or its type is {@link Material#AIR}
	 * 
	 * @see {@link Predicate#negate()} for not empty items.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public final static Predicate<ItemStack> EMPTY = i -> i == null || i.getType() == Material.AIR;

	/*
	 * Constructors
	 */

	/**
	 * Creates an {@link InventoryBuilder} to modify an existing <b>inventory</b>.
	 * 
	 * @param inventory the {@link Inventory} to use.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public InventoryBuilder(@Nonnull Inventory inventory) {
		this.inv = Objects.requireNonNull(inventory, "inventory cannot be null.");
	}

	/**
	 * Creates an {@link InventoryBuilder} with the type {@link InventoryType#CHEST}
	 * and the specified <b>title</b> and amount of <b>rows</b>.
	 * 
	 * @param title the title of the inventory, will be colored with {@link MCStrings#applyColor(String)},
	 * if null, the inventory will have no custom title.
	 * @param rows the amount of rows from 1 to 6, if a higher or lower number is specified, the minimum
	 * (1) or maximum (6) number of rows will be used instead.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public InventoryBuilder(@Nullable String title, int rows) {
		final int size = getRowsAsSize(rows);
		inv = title == null ? Bukkit.createInventory(null, size) : Bukkit.createInventory(null, size, MCStrings.applyColor(title));
	}

	/**
	 * Creates an {@link InventoryBuilder} with the type {@link InventoryType#CHEST}
	 * and the specified <b>title</b>, <b>holder</b> and amount of <b>rows</b>.
	 * 
	 * @param holder the {@link InventoryHolder} (or owner) of the {@link Inventory}.
	 * @param title the title of the inventory, will be colored with {@link MCStrings#applyColor(String)},
	 * if null, the inventory will have no custom title.
	 * @param rows the amount of rows from 1 to 6, if a higher or lower number is specified, the minimum
	 * (1) or maximum (6) number of rows will be used instead.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public InventoryBuilder(@Nullable InventoryHolder holder,  @Nullable String title, int rows) {
		final int size = getRowsAsSize(rows);
		inv = title == null ? Bukkit.createInventory(holder, size) : Bukkit.createInventory(holder, size, MCStrings.applyColor(title));
	}

	/**
	 * Creates an {@link InventoryBuilder} with the specified <b>type</b> and a custom <b>title</b>.
	 * 
	 * @param title the title of the inventory, will be colored with {@link MCStrings#applyColor(String)},
	 * if null, the inventory will have no custom title.
	 * @param type the {@link InventoryType} of the {@link Inventory}, check {@link InventoryType#isCreatable()}
	 * first as some {@link Inventory inventories} cannot be created and showed to players, see
	 * {@link Bukkit#createInventory(InventoryHolder, InventoryType, String)} for more details.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @throws NullPointerException if <b>type</b> is null.
	 */
	public InventoryBuilder(@Nullable String title, @Nonnull InventoryType type) {
		Objects.requireNonNull(type, "Inventory type cannot be null.");
		inv = title == null ? Bukkit.createInventory(null, type) : Bukkit.createInventory(null, type, MCStrings.applyColor(title));
	}

	/**
	 * Creates an {@link InventoryBuilder} with the specified <b>type</b>, <b>holder</b> and a custom <b>title</b>.
	 * 
	 * @param holder the {@link InventoryHolder} (or owner) of the {@link Inventory}.
	 * @param title the title of the inventory, will be colored with {@link MCStrings#applyColor(String)},
	 * if null, the inventory will have no custom title.
	 * @param type the {@link InventoryType} of the {@link Inventory}, check {@link InventoryType#isCreatable()}
	 * first as some {@link Inventory inventories} cannot be created and showed to players, see
	 * {@link Bukkit#createInventory(InventoryHolder, InventoryType, String)} for more details.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @throws NullPointerException if <b>type</b> is null.
	 */
	public InventoryBuilder(@Nullable InventoryHolder holder,  @Nullable String title, @Nonnull InventoryType type) {
		Objects.requireNonNull(type, "Inventory type cannot be null.");
		inv = title == null ? Bukkit.createInventory(holder, type) : Bukkit.createInventory(holder, type, MCStrings.applyColor(title));
	}

	/*
	 * Utility
	 */

	private int getRowsAsSize(int rows) {
		if (rows < 1)
			return 9;
		else if (rows > 6)
			return 54;
		return rows * 9;
	}

	@Nonnull
	private Inventory edit(@Nullable String title) {
		Inventory changed;
		if (inv.getType() == InventoryType.CHEST)
			changed = title == null ? Bukkit.createInventory(inv.getHolder(), inv.getSize()) : Bukkit.createInventory(inv.getHolder(), inv.getSize(), title);
		else
			changed = title == null ? Bukkit.createInventory(inv.getHolder(), inv.getType()) :Bukkit.createInventory(inv.getHolder(), inv.getType(), title);
		changed.setContents(inv.getContents());
		return changed;
	}

	/*
	 * Build & clone
	 */

	/**
	 * Gets a clone of the {@link Inventory} being handled
	 * by this {@link InventoryBuilder}, meaning that changes
	 * applied to the returned {@link Inventory} won't affect
	 * this {@link InventoryBuilder}.
	 * 
	 * @return A clone of the {@link Inventory} being handled
	 * by this {@link InventoryBuilder}
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #clone()
	 */
	@Nonnull
	public Inventory build() {
		return edit(title);
	}

	/**
	 * Creates a exact clone of this {@link InventoryBuilder},
	 * the {@link Inventory} of the new {@link InventoryBuilder}
	 * will be identical but <b>not</b> the same, meaning that
	 * both {@link InventoryBuilder InventryBuilders} can be
	 * modified separately.
	 * 
	 * @return A exact clone of this {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #build()
	 */
	@Nonnull
	public InventoryBuilder clone() {
		return new InventoryBuilder(build());
	}

	/*
	 * Title
	 */

	/**
	 * Gets the current title of the {@link Inventory}
	 * being handled by this {@link InventoryBuilder}.
	 * 
	 * @return The current title of the {@link Inventory}
	 * being handled by this {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setTitle(String)
	 */
	@Nullable
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the {@link Inventory} being handled
	 * by this {@link InventoryBuilder}, note that this method
	 * doesn't affect already opened inventories but instead
	 * creates a new {@link Inventory} with all the properties
	 * of the current one but with the new title.
	 * 
	 * @param title the new title of the {@link Inventory}, colors
	 * will be applied to it with {@link MCStrings#applyColor(String).
	 * 
	 * @return This {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getTitle()
	 */
	@Nonnull
	public InventoryBuilder setTitle(@Nullable String title) {
		this.inv = edit(title);
		return this;
	}

	/*
	 * Info
	 */

	/**
	 * Gets the size of the {@link Inventory} being handled
	 * by this {@link InventoryBuilder}.
	 * 
	 * @return The size of the {@link Inventory} being handled
	 * by this {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int getSize() {
		return inv.getSize();
	}

	/**
	 * Gets all the slots of the {@link Inventory} being handled
	 * by this {@link InventoryBuilder}. Useful if you want to target
	 * all slots on, for example, {@link #set(ItemStack, int...)}.
	 * 
	 * @return All the slots of the {@link Inventory} being handled
	 * by this {@link InventoryBuilder}
	 * 
	 * @since MCUtils 1.0.0
	 */
	public int[] getSlots() {
		return MCNumbers.range(0, getSize());
	}

	/*
	 * Iteration
	 */

	/**
	 * Applies any <b>action</b> to every {@link ItemStack} of
	 * the {@link Inventory} being handled by this {@link InventoryBuilder}.
	 * Note that items may be {@code null} or {@link Material#AIR air}.
	 * 
	 * @param action the {@link Consumer} that will accept every {@link ItemStack}.
	 * 
	 * @return This {@link InventoryBuilder}.
	 * 
	 * @see #forEachIf(Predicate, Consumer)
	 */
	public InventoryBuilder forEach(@Nullable Consumer<ItemStack> action) {
		if (action != null)
			inv.forEach(action);
		return this;
	}

	/**
	 * Applies any <b>action</b> to every {@link ItemStack} of
	 * the {@link Inventory} being handled by this {@link InventoryBuilder}.
	 * Note that items may be {@code null} or {@link Material#AIR air}, see {@link #EMPTY}.
	 * 
	 * @param action the {@link Consumer} that will accept every {@link ItemStack}.
	 * 
	 * @return This {@link InventoryBuilder}.
	 * 
	 * @see #forEach(Consumer)
	 */
	public InventoryBuilder forEachIf(@Nullable Predicate<ItemStack> condition, @Nullable Consumer<ItemStack> action) {
		if (action == null)
			return this;
		if (condition == null)
			inv.forEach(action);
		else
			inv.forEach(i -> {
				if (condition.test(i))
					action.accept(i);
			});
		return this;
	}

	/*
	 * Slot editing
	 */

	// Setters //

	/**
	 * Sets the specified <b>slots</b> of the {@link Inventory} handled
	 * by this {@link InventoryBuilder} with the specified <b>item</b>.
	 * Out of bounds slots will be ignored and if <b>item</b> is null,
	 * {@link Material#AIR} will be used, leaving the slots empty.
	 * This method will replace existing items on the inventory.
	 * <p>
	 * <b>Tip</b>: Use {@link MCNumbers#range(int, int)} for slot ranges.
	 * 
	 * @param item the item to use, if null {@link Material#AIR} will be used,
	 * note that this item will be cloned for every slot so the {@link ItemStack}
	 * instance won't actually be the same for every slot.
	 * @param slots the slots to set this item to, any out of bounds slot will
	 * just be ignored without throwing any exception.
	 * 
	 * @return This {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setIf(ItemStack, Predicate, int...)
	 */
	@Nonnull
	public InventoryBuilder set(@Nullable ItemStack item, int... slots) {
		return setIf(item, null, slots);
	}

	/**
	 * Sets the specified <b>slots</b> of the {@link Inventory} handled
	 * by this {@link InventoryBuilder} with the specified <b>item</b>.
	 * Out of bounds slots will be ignored and if <b>item</b> is null,
	 * {@link Material#AIR} will be used, leaving the slots empty.
	 * This method will only set the item on a slot if <b>condition</b>
	 * returns true with the item on said slot, see {@link #EMPTY} as items may be null.
	 * <p>
	 * <b>Tip</b>: Use {@link MCNumbers#range(int, int)} for slot ranges.
	 * 
	 * @param item the item to use, if null {@link Material#AIR} will be used,
	 * note that this item will be cloned for every slot so the {@link ItemStack}
	 * instance won't actually be the same for every slot.
	 * @param condition the condition every replaced item should pass in order
	 * for the replacement to be done, note that the {@link ItemStack} passed
	 * to the condition can be null or {@link Material#AIR}, in fact, that is
	 * what {@link #setIfEmpty(ItemStack, int...)} tests for.
	 * @param slots the slots to set this item to, any out of bounds or already
	 * occupied slot will just be ignored without throwing any exception.
	 * 
	 * @return This {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #EMPTY
	 * @see #set(ItemStack, int...)
	 */
	@Nonnull
	public InventoryBuilder setIf(@Nullable ItemStack item, Predicate<ItemStack> condition, int... slots) {
		final ItemStack stack = item == null ? new ItemStack(Material.AIR) : item;
		for (int slot : slots)
			if (slot <= inv.getSize()  && (condition == null || condition.test(inv.getItem(slot))))
				inv.setItem(slot, stack.clone());
		return this;
	}

	// Replacement //

	/**
	 * Replaces all items of the specified <b>material</b> with <b>item</b>
	 * in the {@link Inventory} handled by this {@link InventoryBuilder}.
	 * If <b>material</b> is null, only null items will be replaced, if
	 * <b>item</b> is null, {@link Material#AIR} will be used instead.
	 * 
	 * @param material the {@link Material} to match.
	 * @param item the item to use as a replacement, if null {@link Material#AIR}
	 * will be used, note that this item will be cloned for every slot so the
	 * {@link ItemStack} instance won't actually be the same for every slot.
	 * 
	 * @return This {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #replaceAllIf(Predicate, ItemStack)
	 */
	public InventoryBuilder replaceAll(@Nullable Material material, @Nullable ItemStack item) {
		return replaceAllIf(material == null ? i -> i == null : i -> i != null && i.getType() == material, item);
	}

	/**
	 * Replaces all items of the specified <b>material</b> with <b>item</b>
	 * in the {@link Inventory} handled by this {@link InventoryBuilder}
	 * if a certain <b>condition</b> is met individually per slot, please
	 * take into account that items may be null, see {@link #EMPTY}. Speaking
	 * of that, if <b>item</b> is null, {@link Material#AIR} will be used instead.
	 * 
	 * @param condition the {@link Predicate condition} to {@link Predicate#test(Object) test}
	 * before any {@link ItemStack} gets replaced on the inventory. Remember that items
	 * may be null to avoid exceptions with this method.
	 * @param item the item to use as a replacement, if null {@link Material#AIR}
	 * will be used, note that this item will be cloned for every slot so the
	 * {@link ItemStack} instance won't actually be the same for every slot.
	 * 
	 * @return This {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #EMPTY
	 * @see #replaceAll(Material, ItemStack)
	 */
	public InventoryBuilder replaceAllIf(@Nonnull Predicate<ItemStack> condition, @Nullable ItemStack item) {
		return setIf(item, condition, getSlots());
	}

	/*
	 * Object
	 */

	/**
	 * Checks if this {@link InventoryBuilder} is equal to <b>obj</b>.
	 * An {@link InventoryBuilder} is considered to be equal to another
	 * if both inventories and titles are equal.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof InventoryBuilder))
			return false;
		final InventoryBuilder other = (InventoryBuilder) obj;
		return inv.equals(other.inv) && (title == null ? other.title == null : title.equals(other.title));
	}

	/**
	 * Gets the {@link String} representation of this {@link InventoryBuilder},
	 * including its title (If not null), type, size and contents.
	 * 
	 * @return A {@link String} representation of this {@link InventoryBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public String toString() {
		final StringBuilder sb = new StringBuilder("InventoryBuilder{");
		if (title != null)
			sb.append("title = '").append(title).append("', ");
		return sb.append("type = ").append(inv.getType()).append(", ")
			.append("size = ").append(getSize()).append(", ")
			.append("contents = ").append(Arrays.toString(inv.getContents())).append("}").toString();
	}

	@Override
	public int hashCode() {
		return title == null ? Objects.hashCode(inv) : Objects.hash(inv, title);
	}
}
