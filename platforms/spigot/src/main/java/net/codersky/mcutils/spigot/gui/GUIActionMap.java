package net.codersky.mcutils.spigot.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIActionMap {

	private final HashMap<Integer, LinkedList<GUIAction>> actions = new HashMap<>();

	public final void execute(@Nonnull InventoryClickEvent event) {
		final List<GUIAction> actions = getActions(event.getSlot());
		if (actions != null)
			actions.forEach(action -> action.click( (Player) event.getWhoClicked(), event));
	}

	@Nullable
	public final LinkedList<GUIAction> getActions(int slot) {
		return this.actions.get(slot);
	}

	@Nonnull
	public final GUIActionMap add(int slot, @Nonnull GUIAction... actions) {
		if (actions == null || actions.length == 0 || slot <= 0)
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

	@Nonnull
	public final GUIActionMap set(int slot, @Nullable LinkedList<GUIAction> actions) {
		if (actions == null || actions.isEmpty())
			return clear(slot);
		this.actions.put(slot, actions);
		return this;
	}

	@Nonnull
	public final GUIActionMap clear(int slot) {
		this.actions.remove(slot);
		return this;
	}

	@Nonnull
	public final GUIActionMap clear() {
		this.actions.clear();
		return this;
	}
}
