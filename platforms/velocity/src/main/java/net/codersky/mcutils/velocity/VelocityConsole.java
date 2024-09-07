package net.codersky.mcutils.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public class VelocityConsole implements MCConsole<ConsoleCommandSource> {

	private final ConsoleCommandSource handle;

	VelocityConsole(@NotNull ConsoleCommandSource handle) {
		this.handle = handle;
	}

	@NotNull
	@Override
	public ConsoleCommandSource getHandle() {
		return handle;
	}

	@Override
	public boolean sendMessage(@NotNull String message) {
		return sendMessage(Component.text(message));
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		handle.sendMessage(message);
		return true;
	}
}
