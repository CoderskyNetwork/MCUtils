package me.xdec0de.mcutils.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * An interface that represents a GUI that can
 * be opened to {@link Player players}, this is
 * a simple type of GUI, if you want to access
 * features such as GUI actions, use {@link ActionGUI}.
 * 
 * @author xDec0de_
 * 
 * @since MCUtils 1.0.0
 * 
 * @see #open(Player, Event)
 * @see #onOpen(Player, Event)
 * @see #onClick(Player, Inventory, InventoryAction, int)
 * @see #onClose(Player, Event)
 */
@FunctionalInterface
public interface GUI {

	/**
	 * Called whenever this {@link GUI} gets opened to a {@link Player}.
	 * The returned {@link Inventory} will automatically be opened to the
	 * {@link Player}, you don't need to open the {@link Inventory} for the
	 * {@link Player}.
	 * 
	 * @param player the {@link Player} that will view the {@link GUI}.
	 * @param event the {@link Event} that caused this, which may be {@code null}.
	 * 
	 * @return The {@link Inventory} to display to the {@link Player}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public abstract Inventory onOpen(@Nonnull Player player, @Nullable Event event);

	/**
	 * Called whenever a {@link Player} clicks on this {@link GUI}.
	 * Clicks made out of any {@link Inventory} or in other {@link GUI GUIs} won't
	 * call this method.
	 * 
	 * @param clicker the {@link Player} who caused this interaction.
	 * @param inv the clicked {@link Inventory}, the {@link GUI} {@link Inventory}.
	 * @param action the {@link InventoryAction} used by the {@link Player}.
	 * @param slot the clicked {@link Inventory} slot.
	 * 
	 * @return true to allow this interaction, false to cancel it.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public default boolean onClick(@Nonnull Player clicker, @Nonnull Inventory inv, @Nonnull InventoryAction action, int slot) {
		return false;
	}

	/**
	 * Called whenever this {@link GUI} is closed, note that {@link GUI GUIs}
	 * can be closed by a {@link Player}, a {@link JavaPlugin plugin} or an
	 * {@link Event}.
	 * 
	 * @param player the {@link Player} that closed the {@link GUI}.
	 * @param event the {@link Event} that caused this to happen, which
	 * may be {@code null} but generally is an {@link InventoryCloseEvent}.
	 * 
	 * @return true to allow the {@link GUI} to close, false to re-open it if
	 * necessary or to not close it if possible. This method returns true
	 * by default.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public default boolean onClose(@Nonnull Player player, @Nullable Event event) {
		return true;
	}
}
