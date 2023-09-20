package me.xdec0de.mcutils.gui;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.bukkit.scheduler.BukkitTask;

import me.xdec0de.mcutils.MCPlugin;
import me.xdec0de.mcutils.java.annotations.Internal;

/**
 * A {@link Listener} class that handles {@link GUI}
 * registration and behavior. This class is linked
 * to the {@link MCPlugin} that created it.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 * 
 * @see MCPlugin#getGUIHandler()
 * @see #registerGUI(GUI)
 * @see #registerGUIs(GUI...)
 * @see #unregisterGUIs()
 * @see #unregisterGUIs(GUI...)
 */
public class GUIHandler implements Listener {

	private final MCPlugin plugin;
	private final HashMap<GUI, List<UUID>> guis = new HashMap<>();

	/**
	 * Creates a new {@link GUIHandler} for the specified
	 * <b>plugin</b>. Using this constructor should generally
	 * be avoided as {@link MCPlugin#getGUIHandler()} already
	 * creates and register a new {@link GUIHandler} for you.
	 * However, if you want to create some sort of global
	 * {@link GUIHandler} for multiple plugins, then you may
	 * find this constructor useful.
	 * 
	 * @param plugin the plugin that handles this {@link GUIHandler},
	 * this is the one returned by {@link #getPlugin()} and the one
	 * that will schedule any necessary {@link BukkitTask} (For example,
	 * a 1 tick delayed task is created when a {@link GUI} has been closed
	 * by a player but the {@link GUI} doesn't allow this).
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @throws NullPointerException if <b>plugin</b> is {@code null}.
	 */
	public GUIHandler(@Nonnull MCPlugin plugin) {
		this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null.");
	}

	/**
	 * Gets the {@link MCPlugin} that manages this {@link GUIHandler}.
	 * 
	 * @return The {@link MCPlugin} that manages this {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
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

	/**
	 * Gets the {@link GUI} that has been opened to a <b>player</b>,
	 * if any, this method will return {@code null} if the
	 * player doesn't have a {@link GUI} opened. Note that
	 * this is only able to track {@link GUI GUIs} handled
	 * by this {@link GUIHandler}.
	 * 
	 * @param player the {@link Player} to check, can be {@code null},
	 * {@code null} will be returned always in that case.
	 * 
	 * @return The {@link GUI} that has been opened to a <b>player</b>
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public GUI getOpenedGUI(@Nullable Player player) {
		return player == null ? null : getOpenedGUI(player.getUniqueId());
	}

	/**
	 * Gets the {@link GUI} that has been opened to a player,
	 * if any, this method will return {@code null} if the
	 * player doesn't have a {@link GUI} opened. Note that
	 * this is only able to track {@link GUI GUIs} handled
	 * by this {@link GUIHandler}.
	 * 
	 * @param player the {@link UUID} of the {@link Player}
	 * to check, can be {@code null}, {@code null} will be
	 * returned always in that case.
	 * 
	 * @return The {@link GUI} that has been opened to a player.
	 * 
	 * @since MCUtils 1.0.0
	 */
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

	/**
	 * Registers a {@link GUI} to this {@link GUIHandler},
	 * returning the registered <b>gui</b>.
	 * If <b>gui</b> is {@code null}, nothing will be
	 * done and {@code null} will be returned.
	 * 
	 * @param <G> Must implement {@link GUI}.
	 * @param gui the {@link GUI} to register.
	 * 
	 * @return The registered {@link GUI} or {@code null} if
	 * <b>gui</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUIs(GUI...)
	 */
	@Nullable
	public <G extends GUI> G registerGUI(@Nullable G gui) {
		if (gui != null)
			guis.put(gui, new ArrayList<>());
		return gui;
	}

	/**
	 * Registers any number of {@link GUI GUIs} to this
	 * {@link GUIHandler} at the cost of returning this
	 * {@link GUIHandler} instead of a {@link GUI} as
	 * {@link #registerGUI(GUI)} does.
	 * 
	 * @param guis the {@link GUI GUIs} to register, if
	 * {@code null} or empty, nothing will be done.
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUI(GUI)
	 */
	@Nonnull
	public GUIHandler registerGUIs(@Nullable GUI... guis) {
		if (guis != null)
			for (GUI gui : guis)
				this.guis.put(gui, new ArrayList<>());
		return this;
	}

	/**
	 * Unregisters <b>ALL</b> {@link GUI GUIs} that have
	 * been previously registered to this {@link GUIHandler}.
	 * Note that this method doesn't close any inventory if
	 * a {@link Player} has a {@link GUI} opened but the
	 * {@link Player} will be able to interact with the {@link GUI}
	 * as if it was a regular {@link Inventory}, so be careful
	 * about when and how you use this method. You can of course
	 * avoid this problem with {@link #closeAll()}.
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #unregisterGUIs(GUI...)
	 * @see #closeAll()
	 */
	@Nonnull
	public GUIHandler unregisterGUIs() {
		this.guis.clear();
		return this;
	}

	/**
	 * Unregisters all of the specified <b>guis</b> from
	 * this {@link GUIHandler}, if any {@link GUI} is {@code null}
	 * or not present on this {@link GUIHandler}, it will be skipped.
	 * For more details please read {@link #unregisterGUIs()}.
	 * 
	 * @param guis the list of {@link GUI GUIs} to unregister, if
	 * {@code null}, nothing will be done.
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #unregisterGUIs()
	 * @see #closeAll(GUI...)
	 */
	@Nonnull
	public GUIHandler unregisterGUIs(GUI... guis) {
		if (guis != null)
			for (GUI gui : guis)
				this.guis.remove(gui);
		return this;
	}

	/*
	 * GUI handling
	 */

	/**
	 * Convenience method to close the {@link Inventory} of
	 * all online {@link Player players} that are currently viewing
	 * any {@link GUI} that is handled by this {@link GUIHandler}.
	 * This can be used before unregistering {@link GUI GUIs} with
	 * {@link #unregisterGUIs()} to avoid problems. Be aware that this may be cancelled
	 * by any {@link GUI} if {@link GUI#onClose(Player, Event)} returns false.
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #closeAll(GUI...)
	 * @see #unregisterGUIs()
	 */
	@Nonnull
	public GUIHandler closeAll() {
		for (Player on : Bukkit.getOnlinePlayers())
			if (getOpenedGUI(on) != null)
				on.closeInventory();
		return this;
	}

	/**
	 * Convenience method to close the {@link Inventory} of
	 * all online {@link Player players} that are currently viewing
	 * any of the specified <b>guis</b>. Note that only the {@link GUI GUIs}
	 * handled by this {@link GUIHandler} will be closed as {@link #getOpenedGUI(Player)}
	 * is used to check if a {@link Player} is currently viewing a {@link GUI}.
	 * This can be used before unregistering {@link GUI GUIs} with
	 * {@link #unregisterGUIs(GUI...)} to avoid problems. Be aware that this may be cancelled
	 * by any {@link GUI} if {@link GUI#onClose(Player, Event)} returns false.
	 * 
	 * @param guis
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #closeAll()
	 * @see #unregisterGUIs(GUI...)
	 */
	@Nonnull
	public GUIHandler closeAll(@Nullable GUI... guis) {
		if (guis == null || guis.length == 0)
			return this;
		final List<GUI> toClose = Arrays.asList(guis);
		for (Player on : Bukkit.getOnlinePlayers()) {
			final GUI opened = getOpenedGUI(on);
			if (opened != null && toClose.contains(opened))
				on.closeInventory();
		}
		return this;
	}

	@Internal
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

	@Internal
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
