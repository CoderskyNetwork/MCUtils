package net.codersky.mcutils.spigot;

import net.codersky.mcutils.crossplatform.MCConsole;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotConsole implements MCConsole<ConsoleCommandSender> {

	private final ConsoleCommandSender handle;

	SpigotConsole(@NotNull ConsoleCommandSender handle) {
		this.handle = handle;
	}

	@NotNull
	@Override
	public ConsoleCommandSender getHandle() {
		return handle;
	}

	@Override
	public boolean sendMessage(@Nullable String message) {
		if (message != null)
			handle.sendMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@Nullable Component message) {
		if (message != null)
			handle.sendMessage(LegacyComponentSerializer.legacySection().serialize(message));
		return true;
	}
}
