package net.codersky.mcutils.spigot.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.java.MCCollections;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.ApiStatus;

/**
 * A {@link Listener} class that handles {@link GUI}
 * registration and behavior.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 *
 * @see #init()
 */
public class GUIHandler implements Listener {

	private final JavaPlugin plugin;
	private final HashMap<GUI, Set<UUID>> guis = new HashMap<>();

	public GUIHandler(@Nonnull JavaPlugin plugin) {
		this.plugin = Objects.requireNonNull(plugin, "plugin cannot be null.");
	}

	public GUIHandler init() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		return this;
	}

	/**
	 * Gets the {@link JavaPlugin} that manages this {@link GUIHandler}.
	 * 
	 * @return The {@link JavaPlugin} that manages this {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public JavaPlugin getPlugin() {
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
	 * @throws NullPointerException if <b>gui</b> or <b>target</b> are {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean openGUI(@Nonnull GUI gui, @Nonnull Player target, @Nullable Event event) {
		final Inventory inv = gui.onOpen(Objects.requireNonNull(target, "target is null"), event);
		if (inv == null)
			return false;
		if (!guis.containsKey(gui))
			guis.put(gui, MCCollections.asHashSet(target.getUniqueId()));
		else
			guis.get(gui).add(target.getUniqueId());
		target.openInventory(inv);
		return true;
	}

	/*
	 * GUI getters
	 */

	/**
	 * Gets the {@link GUI} that has been opened to a <b>player</b>,
	 * if any. This method will return {@code null} if the
	 * player doesn't have a {@link GUI} opened. Note that
	 * this is only able to track {@link GUI GUIs} opened
	 * by this {@link GUIHandler}.
	 * 
	 * @param player the {@link Player} to check, cannot be {@code null}.
	 * 
	 * @return The {@link GUI} that has been opened to a <b>player</b>
	 * 
	 * @throws NullPointerException if <b>player</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public GUI getOpenedGUI(@Nullable Player player) {
		return getOpenedGUI(player.getUniqueId());
	}

	/**
	 * Gets the {@link GUI} that has been opened to a player,
	 * if any, this method will return {@code null} if the
	 * player doesn't have a {@link GUI} opened. Note that
	 * this is only able to track {@link GUI GUIs} handled
	 * by this {@link GUIHandler}.
	 * 
	 * @param playerUUID the {@link UUID} of the {@link Player}
	 * to check, can be {@code null}, {@code null} will be
	 * returned always in that case.
	 * 
	 * @return The {@link GUI} that has been opened to a player.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public GUI getOpenedGUI(@Nonnull UUID playerUUID) {
		for (Entry<GUI, Set<UUID>> entry : guis.entrySet())
			if (entry.getValue().contains(playerUUID))
				return entry.getKey();
		return null;
	}

	/*
	 * GUI closing
	 */

	private boolean close(@Nonnull Player player, @Nonnull GUI gui, @Nullable Event event, boolean force) {
		if (gui.onClose(player, event) || force) {
			if (event == null || !event.getClass().equals(InventoryCloseEvent.class))
				player.closeInventory();
			// viewers is never null as methods use #getOpenedGUI(Player) first.
			final Set<UUID> viewers = guis.get(gui);
			viewers.remove(player.getUniqueId());
			if (viewers.isEmpty())
				guis.remove(gui);
			return true;
		}
		return false;
	}


	/**
	 * Closes the {@link GUI} a <b>player</b> is currently viewing, if any.
	 * Note that some {@link GUI GUIs} may refuse to close the {@link Inventory}
	 * by returning {@code false} on their {@link GUI#onClose(Player, Event)}
	 * method (Event will be <b>event</b>). However, you can bypass this
	 * restriction and close the {@link Inventory} anyway by setting
	 * <b>force</b> to {@code true}.
	 * 
	 * @param player the {@link Player} that will have its {@link GUI} closed.
	 * @param event the {@link Event} that caused this, can be {@code null}.
	 * @param force whether to ignore the {@link GUI#onClose(Player, Event)} method
	 * and close the {@link Inventory} anyway.
	 * 
	 * @return {@code true} if the {@link GUI} has been closed successfully or the
	 * <b>player</b> wasn't viewing a {@link GUI} to begin with. {@code false} if
	 * the {@link GUI} refused to close and <b>force</b> was set to {@code false}.
	 * Setting <b>force</b> to {@code true} will make this method always return
	 * {@code true}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #closeGUI(Player, boolean)
	 * @see #getOpenedGUI(Player)
	 * @see #closeAll(boolean)
	 * @see #closeAll(Event, boolean)
	 * @see #closeAll(Event, boolean, List)
	 * @see #closeAll(boolean, List)
	 */
	public boolean closeGUI(@Nonnull Player player, @Nullable Event event, boolean force) {
		final GUI gui = getOpenedGUI(player);
		return gui == null ? true : close(player, gui, event, force);
	}

	/**
	 * Closes the {@link GUI} a <b>player</b> is currently viewing, if any.
	 * Note that some {@link GUI GUIs} may refuse to close the {@link Inventory}
	 * by returning {@code false} on their {@link GUI#onClose(Player, Event)}
	 * method (Event will be {@code null}). However, you can bypass this
	 * restriction and close the {@link Inventory} anyway by setting
	 * <b>force</b> to {@code true}.
	 * 
	 * @param player the {@link Player} that will have its {@link GUI} closed.
	 * @param force whether to ignore the {@link GUI#onClose(Player, Event)} method
	 * and close the {@link Inventory} anyway.
	 * 
	 * @return {@code true} if the {@link GUI} has been closed successfully or the
	 * <b>player</b> wasn't viewing a {@link GUI} to begin with. {@code false} if
	 * the {@link GUI} refused to close and <b>force</b> was set to {@code false}.
	 * Setting <b>force</b> to {@code true} will make this method always return
	 * {@code true}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #closeGUI(Player, Event, boolean)
	 * @see #getOpenedGUI(Player)
	 * @see #closeAll(boolean)
	 * @see #closeAll(Event, boolean)
	 * @see #closeAll(Event, boolean, List)
	 * @see #closeAll(boolean, List)
	 */
	public boolean closeGUI(@Nonnull Player player, boolean force) {
		return closeGUI(player, null, force);
	}

	/**
	 * Closes the {@link Inventory} of all online players that are
	 * currently viewing any {@link GUI} that has been opened by this {@link GUIHandler}.
	 * Note that some {@link GUI GUIs} may refuse to close the {@link Inventory}
	 * by returning {@code false} on their {@link GUI#onClose(Player, Event)}
	 * method (Event will be <b>event</b>). However, you can bypass this
	 * restriction and close the {@link Inventory} anyway by setting
	 * <b>force</b> to {@code true}.
	 * 
	 * @param event the {@link Event} that caused this, can be {@code null}.
	 * @param force whether to ignore the {@link GUI#onClose(Player, Event)} method
	 * and close the {@link Inventory} anyway.
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #closeAll(boolean)
	 * @see #closeAll(Event, boolean, List)
	 * @see #closeAll(boolean, List)
	 * @see #closeGUI(Player, boolean)
	 * @see #closeGUI(Player, Event, boolean)
	 */
	@Nonnull
	public GUIHandler closeAll(@Nullable Event event, boolean force) {
		for (Player on : Bukkit.getOnlinePlayers()) {
			final GUI opened = getOpenedGUI(on);
			if (opened != null)
				close(on, opened, event, force);
		}
		return this;
	}

	/**
	 * Closes the {@link Inventory} of all online players that are
	 * currently viewing any {@link GUI} that has been opened by this {@link GUIHandler}.
	 * Note that some {@link GUI GUIs} may refuse to close the {@link Inventory}
	 * by returning {@code false} on their {@link GUI#onClose(Player, Event)}
	 * method (Event will be {@code null}). However, you can bypass this
	 * restriction and close the {@link Inventory} anyway by setting
	 * <b>force</b> to {@code true}.
	 * 
	 * @param force whether to ignore the {@link GUI#onClose(Player, Event)} method
	 * and close the {@link Inventory} anyway.
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #closeAll(Event, boolean)
	 * @see #closeAll(Event, boolean, List)
	 * @see #closeAll(boolean, List)
	 * @see #closeGUI(Player, boolean)
	 * @see #closeGUI(Player, Event, boolean)
	 */
	@Nonnull
	public GUIHandler closeAll(boolean force) {
		return closeAll(null, force);
	}

	/**
	 * Closes the {@link Inventory} of all online players that are
	 * currently viewing any of the specified <b>guis</b>. Note
	 * that some <b>guis</b> may refuse to close the {@link Inventory}
	 * by returning {@code false} on their {@link GUI#onClose(Player, Event)}
	 * method (Event will be <b>event</b>). However, you can bypass this
	 * restriction and close the {@link Inventory} anyway by setting
	 * <b>force</b> to {@code true}.
	 * 
	 * @param event the {@link Event} that caused this, can be {@code null}.
	 * @param force whether to ignore the {@link GUI#onClose(Player, Event)} method
	 * and close the {@link Inventory} anyway.
	 * @param guis the list of {@link GUI GUIs} to close. {@link GUI GUIs} that haven't
	 * been opened by this {@link GUIHandler} will be ignored as {@link #getOpenedGUI(Player)}
	 * will return {@code null}.
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #closeAll(boolean)
	 * @see #closeAll(Event, boolean)
	 * @see #closeAll(boolean, List)
	 * @see #closeGUI(Player, boolean)
	 * @see #closeGUI(Player, Event, boolean)
	 */
	@Nonnull
	public GUIHandler closeAll(@Nullable Event event, boolean force, @Nonnull List<GUI> guis) {
		for (Player on : Bukkit.getOnlinePlayers()) {
			final GUI opened = getOpenedGUI(on);
			if (opened != null && guis.contains(opened))
				close(on, opened, event, force);
		}
		return this;
	}

	/**
	 * Closes the {@link Inventory} of all online players that are
	 * currently viewing any of the specified <b>guis</b>. Note
	 * that some <b>guis</b> may refuse to close the {@link Inventory}
	 * by returning {@code false} on their {@link GUI#onClose(Player, Event)}
	 * method (Event will be {@code null}) However, you can bypass this
	 * restriction and close the {@link Inventory} anyway by setting
	 * <b>force</b> to {@code true}.
	 * 
	 * @param force whether to ignore the {@link GUI#onClose(Player, Event)} method
	 * and close the {@link Inventory} anyway.
	 * @param guis the list of {@link GUI GUIs} to close. {@link GUI GUIs} that haven't
	 * been opened by this {@link GUIHandler} will be ignored as {@link #getOpenedGUI(Player)}
	 * will return {@code null}.
	 * 
	 * @return This {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #closeAll(boolean)
	 * @see #closeAll(Event, boolean)
	 * @see #closeAll(Event, boolean, List)
	 * @see #closeGUI(Player, boolean)
	 * @see #closeGUI(Player, Event, boolean)
	 */
	@Nonnull
	public GUIHandler closeAll(boolean force, @Nonnull List<GUI> guis) {
		return closeAll(null, force, guis);
	}

	/*
	 * GUI handling
	 */

	@ApiStatus.Internal
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClose(InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		final GUI gui = getOpenedGUI(player);
		if (gui != null && !close(player, gui, event, false))
			Bukkit.getScheduler().runTaskLater(plugin, () -> player.openInventory(event.getInventory()), 1);
	}

	@ApiStatus.Internal
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent event) {
		if (event.isCancelled())
			return;
		final Player clicker = (Player) event.getWhoClicked();
		final GUI gui = getOpenedGUI(clicker);
		if (gui == null)
			return;
		final InventoryView view = clicker.getOpenInventory();
		Inventory clicked = event.getClickedInventory();
		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) // Clicked should not be null here.
			clicked = clicked.equals(view.getTopInventory()) ? view.getBottomInventory() : view.getTopInventory();
		if (clicked != null)
			event.setCancelled(!gui.onClick(clicker, clicked, event));
	}
}
