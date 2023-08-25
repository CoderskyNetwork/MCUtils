package me.xdec0de.mcutils.java.strings.pattern.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.xdec0de.mcutils.java.strings.MCStrings;
import me.xdec0de.mcutils.java.strings.pattern.FormatPattern;

public class TargetPattern implements FormatPattern {

	@Nullable
	public String process(@Nonnull CommandSender target, @Nullable String message) {
		final Player asPlayer = target instanceof Player ? (Player)target : null;
		// Process for players
		String processed = MCStrings.matchAndAccept(message, "<p:", "/p>", msg -> {
			if (asPlayer != null)
				asPlayer.sendMessage(msg);
		});
		// Process for the console
		processed = MCStrings.matchAndAccept(processed, "<c:", "/c>", msg -> {
			if (asPlayer == null)
				target.sendMessage(msg);
		});
		// Send any message left for both players and the console.
		if (MCStrings.hasContent(processed))
			target.sendMessage(processed);
		return processed;
	}
}
