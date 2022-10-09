package me.xdec0de.mcutils.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class TargetPattern implements ChatPattern {

	private final MCStrings strings;
	private final Pattern player = Pattern.compile("<p:(.*?)[/]p>");
	private final Pattern console = Pattern.compile("<c:(.*?)[/]c>");

	TargetPattern(MCStrings strings) {
		this.strings = strings;
	}

	@Nullable
	public String process(@Nonnull CommandSender target, @Nullable String string) {
		String postProcess = string;
		final boolean isPlayer = target instanceof Player;
		Matcher matcher = isPlayer ? player.matcher(postProcess) : console.matcher(postProcess);
		while (matcher.find()) {
			if (isPlayer) {
				BaseComponent[] components = strings.applyEvents(matcher.group(1));
				((Player)target).spigot().sendMessage(components != null ? components : TextComponent.fromLegacyText(matcher.group(1)));
			} else
				target.sendMessage(matcher.group(1));
			matcher.reset((postProcess = matcher.replaceFirst("")));
		}
		matcher.usePattern(isPlayer ? console : player);
		while (matcher.find())
			matcher.reset((postProcess = matcher.replaceFirst("")));
		return postProcess;
	}
}
