package net.codersky.mcutils.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.codersky.mcutils.crossplatform.MCConsole;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	public boolean sendMessage(@Nullable String message) {
		if (message != null)
			handle.sendMessage(Component.text(message));
		return true;
	}

	@Override
	public boolean sendMessage(@Nullable Component message) {
		if (message != null)
			handle.sendMessage(message);
		return true;
	}
}
