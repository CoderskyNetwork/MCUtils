package net.codersky.mcutils.java.strings.pattern.target;

import net.codersky.mcutils.crossplatform.MessageReceiver;
import net.codersky.mcutils.crossplatform.player.MCPlayer;
import net.codersky.mcutils.java.strings.MCStrings;
import net.codersky.mcutils.java.strings.pattern.TargetPattern;
import org.jetbrains.annotations.NotNull;

public class PlayerTargetPattern implements TargetPattern {

	@NotNull
	@Override
	public String process(@NotNull MessageReceiver target, @NotNull String string) {
		return MCStrings.match(string, "<p:", "/p>", message -> {
			if (target instanceof MCPlayer<?> player)
				player.sendMessage(message);
		});
	}
}
