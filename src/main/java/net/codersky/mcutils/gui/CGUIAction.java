package net.codersky.mcutils.gui;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

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
public class CGUIAction implements GUIAction {

	final GUIAction simpleAction;
	final GUIPosition position;
	final ArrayList<ClickType> clickTypes;

	/**
	 * Creates a new {@link CGUIAction} with the specified {@link GUIPosition} and {@link ClickType}s.
	 * 
	 * @param position the position that will trigger this <b>action</b>.
	 * @param action the action to trigger if conditions are met.
	 * @param types the click types that will trigger this <b>action</b>, if no types are specified,
	 * every click type will trigger the <b>action</b>.
	 * 
	 * @throws IllegalArgumentException if <b>position</b> or <b>action</b> are null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #CGUIAction(GUIAction, ClickType...)
	 */
	public CGUIAction(@Nonnull GUIPosition position, @Nonnull GUIAction action, @Nullable ClickType... types) {
		if (position == null)
			throw new IllegalArgumentException("GUI position cannot be null.");
		if (action == null)
			throw new IllegalArgumentException("GUI action cannot be null.");
		this.position = position;
		this.simpleAction = action;
		this.clickTypes = new ArrayList<>(types.length);
		if (types != null)
			for (ClickType type : types)
				this.clickTypes.add(type);
	}

	/**
	 * Creates a new {@link CGUIAction} with the specified {@link ClickType}s.
	 * The {@link GUIPosition} will be {@link GUIPosition#TOP}.
	 * 
	 * @param action the action to trigger if conditions are met.
	 * @param types the click types that will trigger this <b>action</b>, if no types are specified,
	 * every click type will trigger the <b>action</b>.
	 * 
	 * @throws IllegalArgumentException if <b>action</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #CGUIAction(GUIPosition, GUIAction, ClickType...)
	 */
	public CGUIAction(@Nonnull GUIAction action, @Nullable ClickType... types) {
		this(GUIPosition.TOP, action, types);
	}

	@Override
	public void click(Player player, InventoryClickEvent event) {
		if ((clickTypes.isEmpty() || clickTypes.contains(event.getClick())) && position.matches(event))
			simpleAction.click(player, event);
	}

	/**
	 * Identifies an {@link Inventory} by position from an {@link InventoryView}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @author xDec0de_
	 * 
	 * @see #TOP
	 * @see #BOTTOM
	 * @see #BOTH
	 */
	public enum GUIPosition {
		/**
		 * Represents the top {@link Inventory} of an {@link InventoryView}.
		 * 
		 * @since MCUtils 1.0.0
		 */
		TOP {
			@Override
			boolean matches(InventoryClickEvent event) {
				if (event.getClickedInventory() == null)
					return false;
				return event.getClickedInventory().equals(event.getView().getTopInventory());
			}
		},

		/**
		 * Represents the bottom inventory of an {@link InventoryView}.
		 * 
		 * @since MCUtils 1.0.0
		 */
		BOTTOM {
			@Override
			boolean matches(InventoryClickEvent event) {
				if (event.getClickedInventory() == null)
					return false;
				return event.getClickedInventory().equals(event.getView().getBottomInventory());
			}
		},

		/**
		 * Represents both {@link Inventory inventories} of an {@link InventoryView}.
		 * 
		 * @since MCUtils 1.0.0
		 */
		BOTH;

		boolean matches(InventoryClickEvent event) {
			return event.getClickedInventory() != null;
		}
	}
}
