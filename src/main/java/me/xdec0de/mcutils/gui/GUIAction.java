package me.xdec0de.mcutils.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Represents a registrable action to be handled
 * when an {@link InventoryClickEvent} gets triggered
 * on a {@link ActionGUI}.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
@FunctionalInterface
public interface GUIAction {

	/**
	 * A method that will run whenever this {@link GUIAction} gets triggered.
	 * 
	 * @param player the player that caused the action.
	 * @param event the event that caused the action.
	 */
	public void click(@Nonnull Player player, @Nullable InventoryClickEvent event);
}
