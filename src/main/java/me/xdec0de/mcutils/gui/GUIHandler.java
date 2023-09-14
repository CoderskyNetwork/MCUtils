package me.xdec0de.mcutils.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import me.xdec0de.mcutils.MCPlugin;

public class GUIHandler implements Listener {

	private final MCPlugin plugin;
	private final HashMap<GUI, List<UUID>> guis = new HashMap<>();

	public GUIHandler(MCPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Gets the {@link MCPlugin} that manages this {@link GUIHandler}.
	 * 
	 * @return The {@link MCPlugin} that manages this {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCPlugin getPlugin() {
		return plugin;
	}

	/**
	 * Opens the specified {@link GUI} to a {@link Player}. Note that this calls
	 * {@link GUI#onOpen(Player, Event)}, so if said method returns a {@code null}
	 * {@link Inventory}, nothing will be done.
	 * 
	 * @param gui the {@link GUI} to open.
	 * @param target the {@link Player} that <b>may</b> have the {@link GUI} opened.
	 * @param event the {@link Event} that caused this, can be {@code null}.
	 * 
	 * @return true if an {@link Inventory} was returned by {@link GUI#onOpen(Player, Event)}
	 * so it could be opened to the <b>target</b>, false otherwise.
	 * 
	 * @throws NullPointerException if <b>target</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean openGUI(@Nonnull GUI gui, @Nonnull Player target, @Nullable Event event) {
		final Inventory inv = gui.onOpen(Objects.requireNonNull(target, "target is null"), event);
		if (inv == null)
			return false;
		target.openInventory(inv);
		return true;
	}

	/*
	 * GUI getters
	 */

	public GUI getOpenedGUI(@Nullable Player player) {
		return player == null ? null : getOpenedGUI(player.getUniqueId());
	}

	@Nullable
	public GUI getOpenedGUI(@Nullable UUID playerUUID) {
		if (playerUUID == null)
			return null;
		for (Entry<GUI, List<UUID>> entry : guis.entrySet())
			if (entry.getValue().contains(playerUUID))
				return entry.getKey();
		return null;
	}

	/*
	 * GUI registration
	 */

	public <G extends GUI> G registerGUI(G gui) {
		guis.put(gui, new ArrayList<>());
		return gui;
	}

	public GUIHandler registerGUIs(GUI... guis) {
		if (guis != null)
			for (GUI gui : guis)
				this.guis.put(gui, new ArrayList<>());
		return this;
	}

	public GUIHandler unregisterGUIs() {
		this.guis.clear();
		return this;
	}

	public GUIHandler unregisterGUIs(GUI... guis) {
		if (guis != null)
			for (GUI gui : guis)
				this.guis.remove(gui);
		return this;
	}

	/*
	 * GUI handling
	 */

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		final Player player = (Player)event.getPlayer();
		final GUI gui = getOpenedGUI(player);
		if (gui == null)
			return;
		if (gui.onClose(player, event))
			guis.get(gui).remove(player.getUniqueId());
		else
			Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(event.getInventory()), 1);
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.isCancelled())
			return;
		final Player clicker = (Player) event.getWhoClicked();
		GUI iGui = getOpenedGUI(clicker);
		if (iGui == null)
			return;
		InventoryView view = clicker.getOpenInventory();
		Inventory clicked = event.getClickedInventory();
		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
			clicked = clicked.equals(view.getTopInventory()) ? view.getBottomInventory() : view.getTopInventory();
		if (clicked == null || !iGui.onClick(clicker, clicked, event.getAction(), event.getSlot())) {
			event.setCancelled(true);
			return;
		}
		if (!(iGui instanceof ActionGUI))
			return;
		ActionGUI gui = (ActionGUI)iGui;
		List<GUIAction> actions = gui.getActions(event.getSlot());
		if (actions != null)
			actions.forEach(action -> action.click((Player)event.getWhoClicked(), event));
	}
}
