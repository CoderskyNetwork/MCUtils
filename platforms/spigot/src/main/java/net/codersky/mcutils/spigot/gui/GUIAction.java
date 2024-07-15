package net.codersky.mcutils.spigot.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Represents an action that can be added to a
 * {@link GUIActionMap} to easily handle clicks
 * on a {@link GUI}.
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
