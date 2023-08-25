package me.xdec0de.mcutils.java.strings.pattern.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;

import me.xdec0de.mcutils.java.strings.MCStrings;
import me.xdec0de.mcutils.java.strings.pattern.FormatPattern;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar implements FormatPattern {

	@Nullable
	public String process(@Nonnull CommandSender target, @Nullable String message) {
		final Spigot receiver = target instanceof Player ? ((Player)target).spigot() : null;
		return MCStrings.match(message, "<ab:", "/ab>", ab -> {
			if (receiver != null)
				receiver.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ab));
		});
	}
}
