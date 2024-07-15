package net.codersky.mcutils.spigot.general;

import net.codersky.mcutils.spigot.SpigotUtils;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public class Items {

	/*
	 * Tools
	 */

	public static boolean isWoodenTool(@Nonnull Material material, boolean includeSword) {
		return material == Material.WOODEN_AXE || material == Material.WOODEN_HOE ||
				material == Material.WOODEN_PICKAXE || material == Material.WOODEN_SHOVEL ||
				(includeSword && material == Material.WOODEN_SWORD);
	}

	public static boolean isStoneTool(@Nonnull Material material, boolean includeSword) {
		return material == Material.STONE_AXE || material == Material.STONE_HOE ||
				material == Material.STONE_PICKAXE || material == Material.STONE_SHOVEL ||
				(includeSword && material == Material.STONE_SWORD);
	}

	public static boolean isGoldenTool(@Nonnull Material material, boolean includeSword) {
		return material == Material.GOLDEN_AXE || material == Material.GOLDEN_HOE ||
				material == Material.GOLDEN_PICKAXE || material == Material.GOLDEN_SHOVEL ||
				(includeSword && material == Material.GOLDEN_SWORD);
	}

	public static boolean isIronTool(@Nonnull Material material, boolean includeSword) {
		return material == Material.IRON_AXE || material == Material.IRON_HOE ||
				material == Material.IRON_PICKAXE || material == Material.IRON_SHOVEL ||
				(includeSword && material == Material.IRON_SWORD);
	}

	public static boolean isDiamondTool(@Nonnull Material material, boolean includeSword) {
		return material == Material.DIAMOND_AXE || material == Material.DIAMOND_HOE ||
				material == Material.DIAMOND_PICKAXE || material == Material.DIAMOND_SHOVEL ||
				(includeSword && material == Material.DIAMOND_SWORD);
	}

	public static boolean isNetheriteTool(@Nonnull Material material, boolean includeSword) {
		if (!SpigotPlugin.serverSupports("1.16"))
			return false;
		return material == Material.NETHERITE_AXE || material == Material.NETHERITE_HOE ||
				material == Material.NETHERITE_PICKAXE || material == Material.NETHERITE_SHOVEL ||
				(includeSword && material == Material.NETHERITE_SWORD);
	}

	public static boolean isTool(@Nonnull Material material, boolean includeSword) {
		final String name = material.name();
		return name.endsWith("AXE") || name.endsWith("HOE") || name.endsWith("PICKAXE") ||
				name.endsWith("SHOVEL") || (includeSword && name.endsWith("SWORD"));
	}

	/*
	 * Armor
	 */

	public static boolean isLeatherArmor(@Nonnull Material material) {
		return material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE ||
				material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS;
	}

	public static boolean isGoldenArmor(@Nonnull Material material) {
		return material == Material.GOLDEN_HELMET || material == Material.GOLDEN_CHESTPLATE ||
				material == Material.GOLDEN_LEGGINGS || material == Material.GOLDEN_BOOTS;
	}

	public static boolean isChainmailArmor(@Nonnull Material material) {
		return material == Material.CHAINMAIL_HELMET || material == Material.CHAINMAIL_CHESTPLATE ||
				material == Material.CHAINMAIL_LEGGINGS || material == Material.CHAINMAIL_BOOTS;
	}

	public static boolean isIronArmor(@Nonnull Material material) {
		return material == Material.IRON_HELMET || material == Material.IRON_CHESTPLATE ||
				material == Material.IRON_LEGGINGS || material == Material.IRON_BOOTS;
	}

	public static boolean isDiamondArmor(@Nonnull Material material) {
		return material == Material.DIAMOND_HELMET || material == Material.DIAMOND_CHESTPLATE ||
				material == Material.DIAMOND_LEGGINGS || material == Material.DIAMOND_BOOTS;
	}

	public static boolean isNetheriteArmor(@Nonnull Material material) {
		if (!SpigotUtils.serverSupports("1.16"))
			return false;
		return material == Material.NETHERITE_HELMET || material == Material.NETHERITE_CHESTPLATE ||
				material == Material.NETHERITE_LEGGINGS || material == Material.NETHERITE_BOOTS;
	}

	public static boolean isArmor(@Nonnull Material material) {
		final String name = material.name();
		return name.endsWith("HELMET") || name.endsWith("CHESTPLATE") ||
				name.endsWith("LEGGINGS") || name.endsWith("BOOTS");
	}
}
