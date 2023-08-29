package me.xdec0de.mcutils.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public interface GUI {

	public default boolean onClose(@Nonnull Player player, @Nullable Event event) {
		return true;
	}

	public default boolean onClick(Player clicker, Inventory inv, int slot) {
		return false;
	}

	public Inventory onOpen(@Nonnull Player player, @Nullable Event event);

}
