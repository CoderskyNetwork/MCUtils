package es.xdec0de.mcutils.items;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Multimap;

import es.xdec0de.mcutils.MCUtils;
import es.xdec0de.mcutils.general.MCStrings;

@SuppressWarnings("deprecation")
public class ItemBuilder implements Cloneable {

	private final ItemStack item;
	private final ItemMeta meta;
	private final MCStrings strings = JavaPlugin.getPlugin(MCUtils.class).strings();

	public ItemBuilder(@Nonnull Material material) {
		this.item = new ItemStack(material);
		this.meta = Bukkit.getItemFactory().getItemMeta(material);
	}

	public ItemBuilder(@Nonnull ItemStack stack) {
		this.item = stack.clone();
		this.meta = item.hasItemMeta() ? item.getItemMeta().clone() : Bukkit.getItemFactory().getItemMeta(stack.getType());
	}

	@Nonnull
	@Override
	public ItemBuilder clone() {
		return new ItemBuilder(item);
	}

	@Nonnull
	public ItemMeta meta() {
		return meta;
	}

	@Nonnull
	public ItemStack build() {
		item.setItemMeta(meta);
		return item;
	}

	// STACK MODIFIERS //

	// Amount //

	@Nonnull
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}

	@Nonnull
	public ItemBuilder addAmount(int amount) {
		item.setAmount(item.getAmount() + amount);
		return this;
	}

	@Nonnull
	public ItemBuilder removeAmount(int amount) {
		item.setAmount(item.getAmount() - amount);
		return this;
	}

	// Data //

	/**
	 * @deprecated All usage of MaterialData is deprecated and subject to removal. Use {@link BlockData}.
	 * 
	 * @param data
	 * @return
	 */
	@Nonnull
	@Deprecated
	public ItemBuilder setData(@Nullable MaterialData data) {
		item.setData(data);
		return this;
	}

	// Enchantment //

	@Nonnull
	public ItemBuilder addEnchantment(@Nonnull Enchantment ench, int level) {
		item.addEnchantment(ench, level);
		return this;
	}

	@Nonnull
	public ItemBuilder addEnchantments(@Nonnull Map<Enchantment, Integer> enchantments) {
		item.addEnchantments(enchantments);
		return this;
	}

	@Nonnull
	public ItemBuilder addUnsafeEnchantment(@Nonnull Enchantment ench, int level) {
		item.addUnsafeEnchantment(ench, level);
		return this;
	}

	@Nonnull
	public ItemBuilder addUnsafeEnchantments(@Nonnull Map<Enchantment, Integer> enchantments) {
		item.addUnsafeEnchantments(enchantments);
		return this;
	}

	@Nonnull
	public ItemBuilder removeEnchantment(@Nonnull Enchantment ench) {
		item.removeEnchantment(ench);
		return this;
	}

	@Nonnull
	public ItemBuilder clearEnchants() {
		Set<Enchantment> safeSet = new HashSet<>(item.getEnchantments().keySet());
		safeSet.forEach(item::removeEnchantment);
		return this;
	}

	// META MODIFIERS //

	// Names //

	@Nonnull
	public ItemBuilder setLocalizedName(@Nullable String name) {
		meta.setLocalizedName(name);
		return this;
	}

	@Nonnull
	public ItemBuilder setDisplayName(@Nullable String name) {
		meta.setDisplayName(strings.applyColor(name));
		return this;
	}

	// Lore //

	@Nonnull
	public ItemBuilder setLore(@Nullable List<String> lore) {
		meta.setLore(strings.applyColor(lore));
		return this;
	}

	@Nonnull
	public ItemBuilder setLore(@Nullable String... lore) {
		return setLore(Arrays.asList(lore));
	}

	@Nonnull
	public ItemBuilder addLore(@Nullable String str) {
		List<String> lore = meta.getLore();
		lore.add(strings.applyColor(str));
		return setLore(lore);
	}

	// Durability //

	@Nonnull
	public ItemBuilder setDamage(short damage) {
		if (meta instanceof Damageable)
			((Damageable)meta).setDamage(damage);
		return this;
	}

	@Nonnull
	public ItemBuilder setDurability(short durability) {
		if (meta instanceof Damageable)
			((Damageable)meta).setDamage(item.getType().getMaxDurability() - durability);
		return this;
	}

	@Nonnull
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		meta.setUnbreakable(unbreakable);
		return this;
	}

	// Persistent data //

	@Nonnull
	public ItemBuilder setPersistentData(@Nonnull NamespacedKey key, @Nonnull PersistentDataType<Object, Object> type, @Nonnull Object value) {
		meta.getPersistentDataContainer().set(key, type, value);
		return this;
	}

	@Nonnull
	public ItemBuilder removePersistentData(@Nonnull NamespacedKey key) {
		meta.getPersistentDataContainer().remove(key);
		return this;
	}

	// Attribute modifiers //

	@Nonnull
	public ItemBuilder addAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
		meta.addAttributeModifier(attribute, modifier);
		return this;
	}

	@Nonnull
	public ItemBuilder setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
		meta.setAttributeModifiers(attributeModifiers);
		return this;
	}

	@Nonnull
	public ItemBuilder removeAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
		meta.removeAttributeModifier(attribute, modifier);
		return this;
	}

	@Nonnull
	public ItemBuilder removeAttributeModifier(@Nonnull Attribute attribute) {
		meta.removeAttributeModifier(attribute);
		return this;
	}

	@Nonnull
	public ItemBuilder removeAttributeModifier(@Nonnull EquipmentSlot slot) {
		meta.removeAttributeModifier(slot);
		return this;
	}

	// Item flags //

	@Nonnull
	public ItemBuilder addItemFlags(@Nonnull ItemFlag... flags) {
		meta.addItemFlags(flags);
		return this;
	}

	@Nonnull
	public ItemBuilder removeitemFlags(@Nonnull ItemFlag... flags) {
		meta.removeItemFlags(flags);
		return this;
	}

	// Model data //

	@Nonnull
	public ItemBuilder setCustomModelData(@Nullable Integer data) {
		meta.setCustomModelData(data);
		return this;
	}
}
