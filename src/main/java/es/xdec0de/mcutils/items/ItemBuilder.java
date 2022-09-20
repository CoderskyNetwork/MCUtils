package es.xdec0de.mcutils.items;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Multimap;

import es.xdec0de.mcutils.MCUtils;
import es.xdec0de.mcutils.strings.MCStrings;

/**
 * A class made to provide easy and fast
 * creation of {@link ItemStack ItemStaks}.
 * 
 * @author xDec0de_
 * 
 * @since MCUtils 1.0.0
 */
@SuppressWarnings("deprecation")
public class ItemBuilder implements Cloneable {

	private final ItemStack item;
	private final ItemMeta meta;
	private final MCStrings strings = JavaPlugin.getPlugin(MCUtils.class).strings();

	/**
	 * Creates a new {@link ItemBuilder} with the specified {@link Material}.
	 * 
	 * @param material the {@link Material} of the new {@link ItemStack} to create.
	 * 
	 * @throws NullPointerException if <b>material</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public ItemBuilder(@Nonnull Material material) {
		if (material == null)
			throw new NullPointerException("Material cannot be null.");
		this.item = new ItemStack(material);
		this.meta = Bukkit.getItemFactory().getItemMeta(material);
	}

	/**
	 * Creates a new {@link ItemBuilder} with a clone of the specified {@link ItemStack}.
	 * 
	 * @param stack the {@link ItemStack} to clone.
	 * 
	 * @throws NullPointerException if <b>stack</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public ItemBuilder(@Nonnull ItemStack stack) {
		if (stack == null)
			throw new NullPointerException("Stack cannot be null.");
		this.item = stack.clone();
		this.meta = item.hasItemMeta() ? item.getItemMeta().clone() : Bukkit.getItemFactory().getItemMeta(stack.getType());
	}

	@Nonnull
	@Override
	public ItemBuilder clone() {
		return new ItemBuilder(item);
	}

	/**
	 * Gets the {@link ItemMeta} being used by this {@link ItemBuilder}.
	 * 
	 * @return The {@link ItemMeta} being used, can be modified.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemMeta meta() {
		return meta;
	}

	/**
	 * Applies {@link #meta()} to the {@link ItemStack} being
	 * used by this {@link ItemBuilder} and returns it.
	 * 
	 * @return An {@link ItemStack} with {@link #meta()} applied to it.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemStack build() {
		item.setItemMeta(meta);
		return item;
	}

	// STACK MODIFIERS //

	// Amount //

	/**
	 * Sets the amount of items on the {@link ItemStack} being
	 * used by this {@link ItemBuilder}. If the amount is higher
	 * than the max stack size, the max stack size will be used,
	 * setting the amount on zero or any negative number will result
	 * in the item being removed (Not the {@link ItemBuilder}).
	 * 
	 * @param amount the new amount of items on the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder setAmount(int amount) {
		if (amount > item.getMaxStackSize())
			item.setAmount(item.getMaxStackSize());
		else
			item.setAmount(amount);
		return this;
	}

	/**
	 * Adds <b>amount</b> to the current amount the {@link ItemStack} being
	 * used by this {@link ItemBuilder} has and calls {@link #setAmount(int)}
	 * with said amount.
	 * 
	 * @param amount the amount of items to add to the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder addAmount(int amount) {
		setAmount(item.getAmount() + amount);
		return this;
	}

	/**
	 * Removes <b>amount</b> from the current amount the {@link ItemStack} being
	 * used by this {@link ItemBuilder} has and calls {@link #setAmount(int)}
	 * with said amount.
	 * 
	 * @param amount the amount of items to remove from the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder removeAmount(int amount) {
		setAmount(item.getAmount() - amount);
		return this;
	}

	// Data //

	/**
	 * @deprecated All usage of {@link MaterialData} is deprecated and subject to removal. Use {@link BlockData}.
	 * 
	 * @param data the new material data for this item
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@Deprecated
	public ItemBuilder setData(@Nullable MaterialData data) {
		item.setData(data);
		return this;
	}

	// Enchantment //

	/**
	 * Adds the specified {@link Enchantment} to the {@link ItemStack} being
	 * used by this {@link ItemBuilder}
	 * <p>
	 * If this item stack already contained the given enchantment (at any level), it will be replaced.
	 *
	 * @param ench the {@link Enchantment} to add.
	 * @param level the level of the {@link Enchantment} to add.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws IllegalArgumentException if <b>ench</b> is null, or not applicable.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addEnchantments(Map)
	 * @see #addUnsafeEnchantment(Enchantment, int)
	 * @see #addUnsafeEnchantments(Map)
	 */
	@Nonnull
	public ItemBuilder addEnchantment(@Nonnull Enchantment ench, int level) {
		item.addEnchantment(ench, level);
		return this;
	}

	/**
	 * Adds the specified {@link Enchantment Enchantments} to the {@link ItemStack} being
	 * used by this {@link ItemBuilder}
	 * <p>
	 * This method is the same as calling {@link #addEnchantment(Enchantment, int)} for each element of the map.
	 * 
	 * @param enchantments
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addEnchantment(Enchantment, int)
	 * @see #addUnsafeEnchantment(Enchantment, int)
	 * @see #addUnsafeEnchantments(Map)
	 */
	@Nonnull
	public ItemBuilder addEnchantments(@Nonnull Map<Enchantment, Integer> enchantments) {
		item.addEnchantments(enchantments);
		return this;
	}

	/**
	 * Adds the specified {@link Enchantment} to the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * <p>
	 * If this item stack already contained the given enchantment (at any level), it will be replaced.
	 * <p>
	 * This method is unsafe and will ignore level restrictions or item type. Use at your own discretion.
	 * 
	 * @param ench the {@link Enchantment} to add.
	 * @param level the level of the {@link Enchantment} to add.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addEnchantment(Enchantment, int)
	 * @see #addEnchantments(Map)
	 * @see #addUnsafeEnchantments(Map)
	 */
	@Nonnull
	public ItemBuilder addUnsafeEnchantment(@Nonnull Enchantment ench, int level) {
		item.addUnsafeEnchantment(ench, level);
		return this;
	}

	/**
	 * Adds the specified {@link Enchantment Enchantments} to the {@link ItemStack} being
	 * used by this {@link ItemBuilder} in an unsafe manner. 
	 * <p>
	 * This method is the same as calling {@link #addEnchantment(Enchantment, int)} for each element of the map.
	 * <p>
	 * This method is unsafe and will ignore level restrictions or item type. Use at your own discretion.
	 * 
	 * @param enchantments the {@link Enchantment Enchantments} to add.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addEnchantment(Enchantment, int)
	 * @see #addEnchantments(Map)
	 * @see #addUnsafeEnchantment(Enchantment, int)
	 */
	@Nonnull
	public ItemBuilder addUnsafeEnchantments(@Nonnull Map<Enchantment, Integer> enchantments) {
		item.addUnsafeEnchantments(enchantments);
		return this;
	}

	/**
	 * Removes the specified {@link Enchantment} to the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @param enchantments the {@link Enchantment Enchantments} to remove, if null, nothing will be done.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #clearEnchants()
	 */
	@Nonnull
	public ItemBuilder removeEnchantment(@Nonnull Enchantment... enchantments) {
		if (enchantments == null)
			return this;
		for (Enchantment ench : enchantments)
			item.removeEnchantment(ench);
		return this;
	}

	/**
	 * Clears the {@link Enchantment Enchantments} of the {@link ItemStack} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #removeEnchantment(Enchantment)
	 */
	@Nonnull
	public ItemBuilder clearEnchants() {
		Set<Enchantment> safeSet = new HashSet<>(item.getEnchantments().keySet());
		safeSet.forEach(item::removeEnchantment);
		return this;
	}

	// META MODIFIERS //

	// Names //

	/**
	 * Sets the localized name of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @param name the name to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder setLocalizedName(@Nullable String name) {
		meta.setLocalizedName(name);
		return this;
	}

	/**
	 * Sets the display name of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @param name the name to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setColoredDisplayName(String)
	 */
	@Nonnull
	public ItemBuilder setDisplayName(@Nullable String name) {
		meta.setDisplayName(name);
		return this;
	}

	/**
	 * Sets the display name of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}, applying colors to
	 * it by using {@link MCStrings#applyColor(String)}.
	 * 
	 * @param name the name to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setDisplayName(String)
	 */
	@Nonnull
	public ItemBuilder setColoredDisplayName(@Nullable String name) {
		meta.setDisplayName(strings.applyColor(name));
		return this;
	}

	// Lore //

	/**
	 * Sets the lore of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @param lore the lore to set, if null, the lore will be removed.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setColoredLore(List)
	 */
	@Nonnull
	public ItemBuilder setLore(@Nullable List<String> lore) {
		meta.setLore(strings.applyColor(lore));
		return this;
	}

	/**
	 * Sets the lore of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}.
	 * 
	 * @param lore the lore to set, if null, the lore will be removed.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setColoredLore(List)
	 */
	@Nonnull
	public ItemBuilder setLore(@Nullable String... lore) {
		meta.setLore(Arrays.asList(lore));
		return this;
	}

	/**
	 * Sets the lore of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}, applying colors to
	 * it by using {@link MCStrings#applyColor(List)}.
	 * 
	 * @param lore the lore to set, if null, the lore will be removed.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setLore(List)
	 */
	@Nonnull
	public ItemBuilder setColoredLore(@Nullable List<String> lore) {
		meta.setLore(strings.applyColor(lore));
		return this;
	}

	/**
	 * Sets the lore of the {@link ItemMeta} being
	 * used by this {@link ItemBuilder}, applying colors to
	 * it by using {@link MCStrings#applyColor(List)}.
	 * 
	 * @param lore the lore to set, if null, the lore will be removed.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setLore(List)
	 */
	@Nonnull
	public ItemBuilder setColoredLore(@Nullable String... lore) {
		return setColoredLore(Arrays.asList(lore));
	}

	/**
	 * Adds the specified <b>lines</b> to the lore of the
	 * {@link ItemMeta} being used by this {@link ItemBuilder}.
	 * 
	 * @param lines the lines to add, if null, nothing will be done.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder addLore(@Nullable String... lines) {
		if (lines == null)
			return this;
		List<String> lore = meta.getLore();
		for (String line : lines)
			lore.add(line);
		return setLore(lore);
	}

	/**
	 * Adds the specified <b>lines</b> to the lore of the
	 * {@link ItemMeta} being used by this {@link ItemBuilder},
	 * applying colors to them by using {@link MCStrings#applyColor(String)}.
	 * 
	 * @param lines the lines to add, if null, nothing will be done.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder addColoredLore(@Nullable String... lines) {
		if (lines == null)
			return this;
		List<String> lore = meta.getLore();
		for (String line : lines)
			lore.add(strings.applyColor(line));
		return setLore(lore);
	}

	// Durability //

	/**
	 * Sets the damage of the {@link ItemMeta} being used by
	 * this {@link ItemBuilder}, this method does nothing if
	 * said {@link ItemMeta} isn't an instance of {@link Damageable}.
	 * 
	 * @param damage the item damage to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder setDamage(short damage) {
		if (meta instanceof Damageable)
			((Damageable)meta).setDamage(damage);
		return this;
	}

	/**
	 * Sets the durability of the {@link ItemMeta} being used by
	 * this {@link ItemBuilder}, this method does nothing if
	 * said {@link ItemMeta} isn't an instance of {@link Damageable}.
	 * 
	 * @param durability the item durability to set.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder setDurability(short durability) {
		if (meta instanceof Damageable)
			((Damageable)meta).setDamage(item.getType().getMaxDurability() - durability);
		return this;
	}

	/**
	 * Sets the unbreakable tag of the {@link ItemMeta} being used by
	 * this {@link ItemBuilder}. An unbreakable item will not lose durability.
	 * 
	 * @param unbreakable true set the item unbreakable, false to disable said property.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		meta.setUnbreakable(unbreakable);
		return this;
	}

	// Persistent data //

	/**
	 * Stores a metadata value on the {@link PersistentDataHolder} instance
	 * of the {@link ItemMeta} being used by this {@link ItemBuilder}.
	 * <p>
	 * This API cannot be used to manipulate Minecraft data,
	 * as the values will be stored using your {@link NamespacedKey}.
	 * This method will override any existing value the {@link PersistentDataHolder}
	 * may have stored under the provided key.
	 * 
	 * @param <T> the generic java type of the tag value.
	 * @param <Z> the generic type of the object to store.
	 * @param key the key this value will be stored under.
     * @param type the type this tag uses.
     * @param value the value stored in the tag.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if the <b>key</b> is null.
     * @throws NullPointerException if the <b>type</b> is null.
     * @throws NullPointerException if the <b>value</b> is null. Removing a tag should
     * be done using {@link #removePersistentData(NamespacedKey)}.
     * @throws IllegalArgumentException if no suitable adapter is found for
     * the {@link PersistentDataType#getPrimitiveType()}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder setPersistentData(@Nonnull NamespacedKey key, @Nonnull PersistentDataType<Object, Object> type, @Nonnull Object value) {
		meta.getPersistentDataContainer().set(key, type, value);
		return this;
	}

	/**
	 * Removes a custom <b>key</b> from the {@link PersistentDataHolder} instance
	 * of the {@link ItemMeta} being used by this {@link ItemBuilder}.
	 * 
	 * @param key the key to remove.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if the provided <b>key</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder removePersistentData(@Nonnull NamespacedKey key) {
		meta.getPersistentDataContainer().remove(key);
		return this;
	}

	// Attribute modifiers //

	/**
	 * Add an Attribute and it's Modifier.
	 * <p>
     * {@link AttributeModifier AttributeModifiers} support {@link EquipmentSlot EquipmentSlots}.
     * If not set, the {@link AttributeModifier} will be active in ALL slots.
     * <p>
     * Two {@link AttributeModifier AttributeModifiers} that have the same {@link UUID}
     * cannot exist on the same Attribute.
     *
     * @param attribute the {@link Attribute} to modify
     * @param modifier the {@link AttributeModifier} specifying the modification
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if either <b>attribute</b> or <b>modifier</b> are null.
     * @throws IllegalArgumentException if <b>modifier</b> already exists.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder addAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
		meta.addAttributeModifier(attribute, modifier);
		return this;
	}

	/**
	 * Set all {@link AttributeModifier AttributeModifiers} and their
	 * {@link AttributeModifier AttributeModifiers}. To clear all currently
	 * set Attributes and AttributeModifiers use null or an empty {@link Multimap}.
     * If not null nor empty, this will filter all entries that are not-null
     * and add them to the ItemStack.
     *
     * @param attributeModifiers the new {@link Multimap} containing the {@link Attribute Attributes}
     * and their {@link AttributeModifier AttributeModifiers}.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if either <b>attribute</b> or <b>modifier</b> are null.
     * @throws IllegalArgumentException if <b>modifier</b> already exists.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
		meta.setAttributeModifiers(attributeModifiers);
		return this;
	}

	/**
	 * Remove a specific {@link Attribute} and {@link AttributeModifier}.
     * AttributeModifiers are matched according to their {@link UUID}.
	 * 
	 * @param attribute the {@link Attribute} to remove.
     * @param modifier the {@link AttributeModifier} to remove.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @throws NullPointerException if either <b>attribute</b> or <b>modifier</b> are null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder removeAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
		meta.removeAttributeModifier(attribute, modifier);
		return this;
	}

	/**
	 * Remove all {@link Attribute Attributes} associated with the given
	 * {@link AttributeModifier AttributeModifiers}.
	 * 
	 * @param attribute the {@link Attribute} to remove.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder removeAttributeModifier(@Nonnull Attribute attribute) {
		meta.removeAttributeModifier(attribute);
		return this;
	}

	/**
	 * Remove all {@link Attribute Attributes} and {@link AttributeModifier AttributeModifiers}
	 * for a given {@link EquipmentSlot}.
     * <p>
     * If the given {@link EquipmentSlot} is null, this will remove all
     * {@link AttributeModifier AttributeModifiers} that do not have an EquipmentSlot set.
	 * 
	 * @param slot the {@link EquipmentSlot} to clear all Attributes and
	 * their modifiers for.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder removeAttributeModifier(@Nonnull EquipmentSlot slot) {
		meta.removeAttributeModifier(slot);
		return this;
	}

	// Item flags //

	/**
	 * Adds a specific set of {@link ItemFlag ItemFlags} which should be ignored by the client when
	 * rendering an {@link ItemStack}. This Method does silently ignore double removed {@link ItemFlag ItemFlags}.
	 * 
	 * @param flags {@link ItemFlag ItemFlags} which should be removed
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder addItemFlags(@Nonnull ItemFlag... flags) {
		meta.addItemFlags(flags);
		return this;
	}

	/**
	 * Remove a specific set of {@link ItemFlag ItemFlags}. This tells the client it
	 * should render the item again. This Method does silently ignore double removed {@link ItemFlag ItemFlags}.
	 * 
	 * @param flags {@link ItemFlag ItemFlags} which should be removed
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder removeitemFlags(@Nonnull ItemFlag... flags) {
		meta.removeItemFlags(flags);
		return this;
	}

	// Model data //

	/**
	 * Sets the custom model data.
	 * <p>
	 * CustomModelData is an integer that may be associated client side with a
	 * custom item model.
	 * 
	 * @param data the data to set, or null to clear.
	 * 
	 * @return This {@link ItemBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ItemBuilder setCustomModelData(@Nullable Integer data) {
		meta.setCustomModelData(data);
		return this;
	}
}
