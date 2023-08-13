package me.xdec0de.mcutils.guis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import me.xdec0de.mcutils.MCPlugin;
import me.xdec0de.mcutils.items.ItemBuilder;

/**
 * Represents an {@link Inventory} that will be handled
 * by {@link MCUtils} to behave as a GUI, in order to handle
 * a GUI, it needs to be registered.
 * 
 * @author xDec0de_
 *
 * @see #open(Player)
 * @see #close(Player)
 * @see #close(Player, Event)
 * @see #addActions(int, GUIAction)
 * @see #onClick(InventoryClickEvent)
 * @see MCPlugin#registerGUI(GUI)
 * @see MCPlugin#registerGUIs(GUI)
 * @see MCUtils#registerGUI(GUI)
 * @see MCUtils#registerGUIs(GUI)
 */
public abstract class GUI {

	private final ArrayList<UUID> viewing = new ArrayList<>();
	private HashMap<Integer, LinkedList<GUIAction>> actions = new HashMap<>();

	/**
	 * Gets the list of players that currently have this {@link GUI} open.
	 * 
	 * @return The list of players that currently have this {@link GUI} open.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public List<Player> getViewers() {
		List<Player> viewers = new ArrayList<>(viewing.size());
		viewing.forEach(uuid -> viewers.add(Bukkit.getPlayer(uuid)));
		return viewers;
	}

	/**
	 * Gets the list of {@link UUID}s of the players that currently have this {@link GUI} open.
	 * 
	 * @return The list of players that currently have this {@link GUI} open.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public List<UUID> getViewersUUID() {
		return new ArrayList<>(viewing);
	}

	/**
	 * Closes this {@link GUI} to the specified <b>player</b>.
	 * 
	 * @param player the player to whom this {@link GUI} will be closed, if null, false will be returned and nothing will be done.
	 * 
	 * @return True if <b>player</b> has this {@link GUI} opened and if {@link #onClose(Player, Event)} returns true, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #close(Player, Event)
	 * 
	 * @apiNote Internal method, don't override it unless you really know what you are doing, implement {@link #onClose(Player, Event)} instead.
	 */
	public boolean close(@Nullable Player player) {
		if (player == null || !viewing.contains(player.getUniqueId()))
			return false;
		if (onClose(player, null)) {
			viewing.remove(player.getUniqueId()); // Remove first so close doesn't get called again...
			player.closeInventory();
			return true;
		}
		return false;
	}

	/**
	 * Closes this {@link GUI} to the specified <b>player</b>, also specifying the <b>event</b> that caused this.
	 * 
	 * @param player the player to whom this {@link GUI} will be closed, if null, false will be returned and nothing will be done.
	 * @param event the event that caused this {@link GUI} to close, if null, {@link #close(Player)} will be called instead.
	 * 
	 * @return True if <b>player</b> has this {@link GUI} opened and if {@link #onClose(Player, Event)} returns true, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #close(Player)
	 * 
	 * @apiNote Internal method, don't override it unless you really know what you are doing, implement {@link #onClose(Player, Event)} instead.
	 */
	public boolean close(MCPlugin plugin, @Nullable Player player, @Nullable Event event) {
		if (event == null)
			return close(player);
		if (player == null || !viewing.contains(player.getUniqueId()))
			return false;
		if (onClose(player, event)) {
			viewing.remove(player.getUniqueId()); // Remove first so close doesn't get called again...
			player.closeInventory();
			return true;
		} else if (event instanceof InventoryCloseEvent)
			Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(((InventoryCloseEvent)event).getInventory()), 1);
		return false;
	}

	/**
	 * Called automatically when either {@link #close(Player)} or {@link #close(Player, Event)}
	 * get called, this method should <b>not</b> be called manually, it is intended to be implemented
	 * by a {@link GUI} to perform any needed action when it gets closed, essentially, it works like an event.
	 * 
	 * @param player the player to whom this {@link GUI} will be closed.
	 * @param event the event that caused this {@link GUI} to close, can be null, {@link MCUtils} only calls this method with
	 * {@link PlayerQuitEvent} and {@link InventoryCloseEvent}.
	 * 
	 * @return True to allow the inventory closing. If false is returned and <b>event</b> is an instance of
	 * {@link InventoryCloseEvent}, the inventory will be re-opened, if not, the inventory just won't be closed.
	 */
	public abstract boolean onClose(@Nonnull Player player, @Nullable Event event);

	/**
	 * Opens this {@link GUI} to the specified <b>player</b>.
	 * 
	 * @param player the player to whom this {@link GUI} will be opened, if null, false will be returned and nothing will be done.
	 * 
	 * @return True if <b>player</b> doesn't have this {@link GUI} opened and if {@link #onOpen(Player, Event)} returns true, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #open(Player, Event)
	 * 
	 * @apiNote Internal method, don't override it unless you really know what you are doing, implement {@link #onOpen(Player, Event)} instead.
	 */
	public boolean open(@Nullable Player player) {
		return player == null || viewing.contains(player.getUniqueId()) || !onOpen(player, null) ? false : viewing.add(player.getUniqueId());
	}

	/**
	 * Opens this {@link GUI} to the specified <b>player</b>, also specifying the <b>event</b> that caused this.
	 * 
	 * @param player the player to whom this {@link GUI} will be opened, if null, false will be returned and nothing will be done.
	 * @param event the event that caused this {@link GUI} to open, if null, {@link #open(Player)} will be called instead.
	 * 
	 * @return True if <b>player</b> doesn't have this {@link GUI} opened and if {@link #onOpen(Player, Event)} returns true, false otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #open(Player)
	 * 
	 * @apiNote Internal method, don't override it unless you really know what you are doing, implement {@link #onOpen(Player, Event)} instead.
	 */
	public boolean open(@Nullable Player player, @Nullable Event event) {
		if (event == null)
			return open(player);
		return player == null || viewing.contains(player.getUniqueId()) || !onOpen(player, event) ? false : viewing.add(player.getUniqueId());
	}

	/**
	 * Called automatically when either {@link #open(Player)} or {@link #open(Player, Event)}
	 * get called, this method should <b>not</b> be called manually, it is intended to be implemented
	 * by a {@link GUI} to perform any needed action when it gets opened, essentially, it works like an event.
	 * <p>
	 * {@link MCUtils} won't open any inventory to the <b>player</b>, you need to create an inventory and open
	 * it to said <b>player</b>, {@link MCUtils} will call {@link #onClick(InventoryClickEvent)} or {@link #onClose(Player, Event)}
	 * when necessary for you, {@link GUI}s aren't intended to make {@link Inventory} creation easier but to remove the hassle of
	 * checking which player clicks or closes which {@link Inventory}, however, {@link ItemBuilder} can help with that.
	 * 
	 * @param player the player to whom this {@link GUI} will be opened.
	 * @param event the event that caused this {@link GUI} to open, can be null, {@link MCUtils} won't call this method by itself.
	 * 
	 * @return True to allow the inventory opening. If false is returned, the inventory won't be handled by {@link MCUtils}, so make
	 * sure to not open one in that case.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addActions(int, GUIAction)
	 * @see {@link GUIAction}
	 * @see ItemBuilder
	 */
	public abstract boolean onOpen(@Nonnull Player player, @Nullable Event event);

	/**
	 * Called automatically when an {@link InventoryClickEvent} is called on this {@link GUI}, this method should
	 * <b>not</b> be called manually, it is intended to be implemented by a {@link GUI} to perform any needed
	 * action when it gets opened, essentially, it works like an event
	 * 
	 * @param event the {@link InventoryClickEvent} that caused this method to be called.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void onClick(InventoryClickEvent event) {}

	/**
	 * Gets the list of {@link GUIActions} applied to
	 * the specified <b>slot</b>.
	 * 
	 * @param slot the slot to get.
	 * 
	 * @return This {@link GUI}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #addActions(int, GUIAction...)
	 * @see #setActions(int, LinkedList)
	 * @see #clearActions()
	 */
	public LinkedList<GUIAction> getActions(int slot) {
		return actions.get(slot);
	}

	/**
	 * Adds any number of {@link GUIAction}s to the specified <b>slot</b>
	 * 
	 * @param slot the slot that will trigger the <b>actions</b>.
	 * @param actions the actions to add.
	 * 
	 * @return This {@link GUI}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getActions(int)
	 * @see #setActions(int, LinkedList)
	 * @see #clearActions()
	 */
	public GUI addActions(int slot, @Nonnull GUIAction... actions) {
		if (actions == null)
			return this;
		LinkedList<GUIAction> actionList = getActions(slot);
		if (actionList == null)
			actionList = new LinkedList<>();
		for (GUIAction action : actions)
			if (action != null)
				actionList.add(action);
		this.actions.put(slot, actionList);
		return this;
	}

	/**
	 * Sets the list of {@link GUIAction}s applied to the specified <b>slot</b>.
	 * 
	 * @param slot the slot that will trigger the <b>actions</b>.
	 * @param actions the actions to set, if null, the list will be removed.
	 * 
	 * @return This {@link GUI}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getActions(int)
	 * @see #addActions(int, GUIAction...)
	 * @see #clearActions()
	 */
	public GUI setActions(int slot, @Nullable LinkedList<GUIAction> actions) {
		if (actions == null)
			this.actions.remove(slot);
		else
			this.actions.put(slot, actions);
		return this;
	}

	/**
	 * Clears all {@link GUIActions} applied to this {@link GUI}.
	 * 
	 * @return This {@link GUI}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getActions(int)
	 * @see #addActions(int, GUIAction...)
	 * @see #setActions(int, LinkedList)
	 */
	public GUI clearActions() {
		actions.clear();
		return this;
	}
}
