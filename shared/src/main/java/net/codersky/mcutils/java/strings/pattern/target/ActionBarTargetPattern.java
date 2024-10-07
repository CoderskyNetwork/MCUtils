package net.codersky.mcutils.java.strings.pattern.target;

import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.pattern.TargetPattern;
import org.jetbrains.annotations.NotNull;

public class ActionBarTargetPattern implements TargetPattern {

	@NotNull
	@Override
	public String process(@NotNull MessageReceiver target, @NotNull String string, boolean applyEventPatterns) {
		return MCStrings.match(string, "<ab:", "/ab>", message -> {
			if (target instanceof MCPlayer player)
				player.sendActionBar(message);
		});
	}
}
