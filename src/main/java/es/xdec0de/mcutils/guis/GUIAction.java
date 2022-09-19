package es.xdec0de.mcutils.guis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * An implementation of {@link GUIAction} that adds
 * conditions in order to be executed, hence the name
 * C(onditional)GUIAction, even though the class name isn't
 * very descriptive, it's shorted in favor of a shorter implementation.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public interface GUIAction {

	/**
	 * A method that will run whenever this {@link GUIAction} gets triggered.
	 * 
	 * @param player the player that caused the action.
	 * @param event the event that caused the action.
	 */
	public void click(@Nonnull Player player, @Nullable InventoryClickEvent event);
}
