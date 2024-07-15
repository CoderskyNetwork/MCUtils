package net.codersky.mcutils.spigot.java.strings.pattern.target;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.spigot.java.strings.MCStrings;
import net.codersky.mcutils.spigot.java.strings.pattern.TargetPattern;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBarTargetPattern implements TargetPattern {

	@Nullable
	public String process(@Nonnull CommandSender target, @Nullable String message) {
		final Spigot receiver = target instanceof Player ? ((Player)target).spigot() : null;
		return MCStrings.match(message, "<ab:", "/ab>", ab -> {
			if (receiver != null)
				receiver.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ab));
		});
	}
}
