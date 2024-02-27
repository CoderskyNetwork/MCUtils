package net.codersky.mcutils.gui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
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
 * @see #onClick(Player, Inventory, InventoryClickEvent)
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
	 * @return The {@link Inventory} to display to the {@link Player}, which may
	 * be {@code null} so that {@link GUIHandler#openGUI(GUI, Player, Event)}
	 * does nothing when trying to open this {@link GUI}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Inventory onOpen(@Nonnull Player player, @Nullable Event event);

	/**
	 * Called whenever a {@link Player} clicks on this {@link GUI}.
	 * Clicks made out of any {@link Inventory} or in other {@link GUI GUIs} won't
	 * call this method.
	 * 
	 * @param clicker the {@link Player} who caused this interaction.
	 * @param inv the clicked {@link Inventory}, may be the {@link GUI} {@link Inventory}
	 * or the <b>player</b> {@link Inventory}.
	 * @param event the {@link InventoryClickEvent} that caused this method to be called.
	 * Note that <b>inv</b> may not be the same as {@link InventoryClickEvent#getClickedInventory() the
	 * clicked inventory} from <b>event</b>, as MCUtils provides some additional logic to ensure that
	 * <b>inv</b> is the actual clicked inventory.
	 * 
	 * @return {@code true} to allow this interaction, {@code false} to cancel it.
	 * {@link InventoryClickEvent#setResult(org.bukkit.event.Event.Result) Setting} the
	 * {@link org.bukkit.event.Event.Result Result} of <b>event</b> or directly
	 * {@link InventoryClickEvent#setCancelled(boolean) cancelling} it won't work as the
	 * returning value of this method has a higher priority. This method returns {@code false}
	 * by default.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public default boolean onClick(@Nonnull Player clicker, @Nonnull Inventory inv, @Nonnull InventoryClickEvent event) {
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
	 * @return {@code true} to allow the {@link GUI} to close, {@code false} to re-open it if
	 * necessary or to not close it if possible. This method returns {@code true}
	 * by default.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public default boolean onClose(@Nonnull Player player, @Nullable Event event) {
		return true;
	}
}
