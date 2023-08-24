package me.xdec0de.mcutils.java.strings.pattern.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;

import me.xdec0de.mcutils.java.strings.pattern.FormatPattern;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar implements FormatPattern {

	@Nullable
	public String process(@Nonnull CommandSender target, @Nullable String message) {
		final StringBuffer res = new StringBuffer(message);
		final Spigot receiver = target instanceof Player ? ((Player)target).spigot() : null;
		int start = message.indexOf("<ab:", 0);
		while (start != -1) {
			final int end = message.indexOf("/ab>", start);
			if (end != -1) {
				if (receiver != null)
					receiver.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message.substring(start + 4, end)));
				res.replace(start, end + 4, ""); // We still want to remove the pattern for others.
			}
			start = message.indexOf("<ab:", start + 1);
		}
		return res.toString();
	}
}
