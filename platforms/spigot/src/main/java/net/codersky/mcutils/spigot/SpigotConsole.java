package net.codersky.mcutils.spigot;

import net.codersky.mcutils.crossplatform.MCConsole;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public class SpigotConsole implements MCConsole {

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
	public boolean sendMessage(@NotNull String message) {
		handle.sendMessage(message);
		return true;
	}

	@Override
	public boolean sendMessage(@NotNull Component message) {
		handle.sendMessage(LegacyComponentSerializer.legacySection().serialize(message));
		return true;
	}
}
