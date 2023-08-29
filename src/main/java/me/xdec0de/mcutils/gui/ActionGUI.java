package me.xdec0de.mcutils.gui;

import java.util.HashMap;
import java.util.LinkedList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ActionGUI implements GUI {

	private HashMap<Integer, LinkedList<GUIAction>> actions = new HashMap<>();

	public LinkedList<GUIAction> getActions(int slot) {
		return actions.get(slot);
	}

	public ActionGUI addActions(int slot, @Nonnull GUIAction... actions) {
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

	public ActionGUI setActions(int slot, @Nullable LinkedList<GUIAction> actions) {
		if (actions == null)
			this.actions.remove(slot);
		else
			this.actions.put(slot, actions);
		return this;
	}

	public ActionGUI clearActions() {
		actions.clear();
		return this;
	}
}
