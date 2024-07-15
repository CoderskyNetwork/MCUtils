package net.codersky.mcutils.spigot.gui;

import java.util.ArrayList;
import java.util.Objects;

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
 * ConditionalGUIAction.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public class ConditionalGUIAction implements GUIAction {

	private final GUIAction simpleAction;
	private final GUIPosition position;
	private final ArrayList<ClickType> clickTypes;

	/**
	 * Creates a new {@link ConditionalGUIAction} with the specified {@link GUIPosition} and {@link ClickType}s.
	 * 
	 * @param position the position that will trigger this <b>action</b>.
	 * @param action the action to trigger if conditions are met.
	 * @param types the click types that will trigger this <b>action</b>, if no types are specified,
	 * every click type will trigger the <b>action</b>.
	 * 
	 * @throws NullPointerException if <b>position</b>, <b>action</b> or <b>types</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #ConditionalGUIAction(GUIAction, ClickType...)
	 */
	public ConditionalGUIAction(@Nonnull GUIPosition position, @Nonnull GUIAction action, @Nullable ClickType... types) {
		this.position = Objects.requireNonNull(position);
		this.simpleAction = Objects.requireNonNull(action);
		this.clickTypes = new ArrayList<>(types.length);
		if (types != null)
			for (ClickType type : types)
				this.clickTypes.add(type);
	}

	/**
	 * Creates a new {@link ConditionalGUIAction} with the specified {@link ClickType}s.
	 * The {@link GUIPosition} will be {@link GUIPosition#TOP}.
	 * 
	 * @param action the action to trigger if conditions are met.
	 * @param types the click types that will trigger this <b>action</b>, if no types are specified,
	 * every click type will trigger the <b>action</b>.
	 * 
	 * @throws NullPointerException if <b>action</b> or <b>types</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #ConditionalGUIAction(GUIPosition, GUIAction, ClickType...)
	 */
	public ConditionalGUIAction(@Nonnull GUIAction action, @Nullable ClickType... types) {
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
				final Inventory clicked = event.getClickedInventory();
				return clicked == null ? false : clicked.equals(event.getView().getTopInventory());
			}
		},

		/**
		 * Represents the bottom {@link Inventory} of an {@link InventoryView}.
		 * 
		 * @since MCUtils 1.0.0
		 */
		BOTTOM {
			@Override
			boolean matches(InventoryClickEvent event) {
				final Inventory clicked = event.getClickedInventory();
				return clicked == null ? false : clicked.equals(event.getView().getBottomInventory());
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
