package me.xdec0de.mcutils.java.strings.builders;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Entity;
import net.md_5.bungee.api.chat.hover.content.Item;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Hover {

	private HoverEvent event;

	public Hover(@Nonnull Action type, @Nullable String hoverData) {
		this.event = switch (type) {
		case SHOW_TEXT -> new HoverEvent(Action.SHOW_TEXT, new Text(TextComponent.fromLegacyText(hoverData)));
		case SHOW_ITEM -> buildItem(hoverData);
		case SHOW_ENTITY -> buildEntity(hoverData);
		default -> throw new IllegalArgumentException("Invalid Hover Action type.");
		};
	}

	public BaseComponent[] apply(BaseComponent[] components) {
		BaseComponent[] clone = new BaseComponent[components.length];
		for (BaseComponent component : clone)
			component.setHoverEvent(event);
		return clone;
	}

	public BaseComponent[] apply(String str) {
		return apply(TextComponent.fromLegacyText(str));
	}

	// minecraft:diamond
	// minecraft:diamond 64
	// minecraft:diamond 64 {display:{Name:'[{"text":"Name"}]'}}
	private HoverEvent buildItem(String hoverData) {
		String[] itemData = hoverData.split(" ");
		final String item = itemData[0];
		int amount = 1;
		if (itemData.length >= 2)
			amount = isNumeric(itemData[1]) ? Integer.valueOf(itemData[1]) : 1;
		String nbt = itemData.length >= 3 ? itemData[2] : null;
		if (itemData.length > 3)
			for (int i = 3; i < itemData.length; i++)
				nbt += " " + itemData[i];
		return new HoverEvent(Action.SHOW_ITEM, new Item(item, amount, nbt == null ? null : ItemTag.ofNbt(nbt)));
	}

	// minecraft:pig uuid name
	private HoverEvent buildEntity(String hoverData) {
		String[] entityData = hoverData.split(" ");
		final String type = entityData[0];
		final String uuid = entityData.length >= 2 ? entityData[1] : "";
		String name = entityData.length >= 3 ? entityData[2] : null;
		if (entityData.length > 3)
			for (int i = 3; i < entityData.length; i++)
				name += " " + entityData[i];
		return new HoverEvent(Action.SHOW_ENTITY, new Entity(type, uuid, TextComponent.fromLegacyText(name)[0]));
	}

	private boolean isNumeric(String str) {
		if (str == null)
			return false;
		for (int i = 0; i < str.length(); i++) {
			final char ch = str.charAt(i);
			if (ch < '0' || ch > '9')
				return false;
		}
		return true;
	}
}
