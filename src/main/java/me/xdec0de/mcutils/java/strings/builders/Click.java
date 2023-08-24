package me.xdec0de.mcutils.java.strings.builders;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class Click {

	private ClickEvent event;

	public Click(@Nonnull Action type, @Nullable String clickData) {
		this.event = new ClickEvent(type, clickData);
	}

	public BaseComponent[] apply(BaseComponent[] components) {
		BaseComponent[] clone = new BaseComponent[components.length];
		for (BaseComponent component : clone)
			component.setClickEvent(event);
		return clone;
	}

	public BaseComponent[] apply(String str) {
		return apply(TextComponent.fromLegacyText(str));
	}
}
