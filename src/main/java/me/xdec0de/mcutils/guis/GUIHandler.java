package me.xdec0de.mcutils.guis;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.xdec0de.mcutils.MCPlugin;

/**
 * A class intended to handle {@link GUI}s, you
 * can use get an instance in three ways:
 * <p>
 * 1. The recommended one, using {@link MCUtils#getGUIHandler(JavaPlugin)}
 * <p>
 * 2. The manual one, creating a new instance of this class and registering its events.
 * <p>
 * The advantage of using the other method over the second one is that other plugin
 * creators that use your plugin will know how to access it's GUI handler easily, also,
 * you don't have to register the listener and your own, the first method will do that
 * for you the first time you call it.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public class GUIHandler implements Listener {

	private final MCPlugin plugin;
	private final List<GUI> guis = new ArrayList<>();

	public GUIHandler(MCPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Gets the {@link GUI} a {@link Player} is currently viewing, if any.
	 * 
	 * @param playerUUID the {@link UUID} of the player, if null, null will be returned.
	 * 
	 * @return The {@link GUI} a player is currently viewing, null if the player
	 * isn't currently viewing a {@link GUI}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public GUI getOpenedGUI(@Nullable UUID playerUUID) {
		if (playerUUID == null)
			return null;
		for (GUI gui : guis)
			if (gui.getViewersUUID().contains(playerUUID))
				return gui;
		return null;
	}

	/**
	 * Gets the {@link GUI} a {@link Player} is currently viewing, if any.
	 * 
	 * @param playerUUID the player, if null, null will be returned.
	 * 
	 * @return The {@link GUI} a <b>player</b> is currently viewing, null
	 * if the <b>player</b> isn't currently viewing a {@link GUI}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public GUI getOpenedGUI(@Nullable Player player) {
		return player == null ? null : getOpenedGUI(player.getUniqueId());
	}

	/**
	 * Registers the specified {@link GUI} to be handled, if
	 * <b>gui</b> is null or already registered, null will
	 * be returned.
	 * 
	 * @param gui the {@link GUI} to register.
	 * 
	 * @return The <b>gui</b> itself if it was successfully registered,
	 * null otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUIs(GUI...)
	 * @see #unregisterGUIs()
	 * @see #unregisterGUIs(GUI...)
	 */
	public GUI registerGUI(GUI gui) {
		if (gui == null || guis.contains(gui))
			return null;
		guis.add(gui);
		return gui;
	}

	/**
	 * Register any number of {@link GUI GUIs} easily, at the cost
	 * of not returning anything as opposed to {@link #registerGUI(GUI)}.
	 * 
	 * @param guis the {@link GUI GUIs} to be registered, if null, nothing
	 * will be done.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUI(GUI)
	 * @see #unregisterGUIs()
	 * @see #unregisterGUIs(GUI...)
	 */
	public void registerGUIs(GUI... guis) {
		if (guis == null)
			return;
		for (GUI gui : guis)
			registerGUI(gui);
	}

	/**
	 * Unregisters all specified {@link GUI GUIs} from this {@link GUIHandler}.
	 * 
	 * @param guis the {@link GUI GUIs} to be unregistered, the instance doesn't
	 * need to be the same as the registered {@link GUI} as this is checked by class.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUI(GUI)
	 * @see #registerGUIs(GUI...)
	 * @see #unregisterGUIs()
	 */
	public void unregisterGUIs(GUI... guis) {
		if (guis == null)
			return;
		List<Class<? extends GUI>> registered = new ArrayList<>();
		for (GUI gui : this.guis)
			registered.add(gui.getClass());
		for (GUI gui : guis)
			if (registered.contains(gui.getClass()))
				this.guis.remove(gui);
	}

	/**
	 * Unregisters all the {@link GUI GUIs} registered on this {@link GUIHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #registerGUI(GUI)
	 * @see #registerGUIs(GUI...)
	 * @see #unregisterGUIs(GUI...)
	 */
	public void unregisterGUIs() {
		guis.clear();
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		GUI gui = getOpenedGUI(event.getPlayer().getUniqueId());
		if (gui != null)
			gui.close(event.getPlayer());
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		GUI gui = getOpenedGUI(event.getPlayer().getUniqueId());
		if (gui != null)
			gui.close(plugin, (Player) event.getPlayer(), event);
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		GUI gui = getOpenedGUI(event.getWhoClicked().getUniqueId());
		if (gui == null)
			return;
		List<GUIAction> actions = gui.getActions(event.getSlot());
		if (actions != null)
			actions.forEach(action -> action.click((Player)event.getWhoClicked(), event));
		gui.onClick(event);
	}
}
