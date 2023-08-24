package me.xdec0de.mcutils.java.strings.pattern.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.xdec0de.mcutils.java.strings.pattern.FormatPattern;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar implements FormatPattern {

	private final Pattern pattern = Pattern.compile("<ab:(.*?)[/]ab>");

	@Nullable
	public String process(@Nonnull CommandSender target, @Nullable String string) {
		String postProcess = string;
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) {
			final String toAction = matcher.group(1);
			if (target instanceof Player)
				((Player)target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(toAction));
			postProcess = matcher.replaceFirst("");
		}
		return postProcess;
	}
}
