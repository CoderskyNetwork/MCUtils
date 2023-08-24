package me.xdec0de.mcutils.java.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TargetPattern implements FormatPattern {

	private final Pattern player = Pattern.compile("<p:(.*?)[/]p>");
	private final Pattern console = Pattern.compile("<c:(.*?)[/]c>");

	@Nullable
	public String process(@Nonnull CommandSender target, @Nullable String string) {
		String postProcess = string;
		final boolean isPlayer = target instanceof Player;
		Matcher matcher = isPlayer ? player.matcher(postProcess) : console.matcher(postProcess);
		while (matcher.find()) {
			if (isPlayer)
				((Player)target).spigot().sendMessage(MCStrings.applyEventPatterns(matcher.group(1)));
			else
				target.sendMessage(matcher.group(1));
			matcher.reset((postProcess = matcher.replaceFirst("")));
		}
		matcher.usePattern(isPlayer ? console : player);
		while (matcher.find())
			matcher.reset((postProcess = matcher.replaceFirst("")));
		return postProcess;
	}
}
