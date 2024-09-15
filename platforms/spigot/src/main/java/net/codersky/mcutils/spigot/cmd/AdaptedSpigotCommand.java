package net.codersky.mcutils.spigot.cmd;

import net.codersky.mcutils.cmd.MCCommand;
import net.codersky.mcutils.cmd.MCCommandSender;
import net.codersky.mcutils.spigot.SpigotUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdaptedSpigotCommand<P extends JavaPlugin> extends SpigotCommand<P> {

	private final MCCommand<?, MCCommandSender> command;

	public AdaptedSpigotCommand(@NotNull SpigotUtils<P> utils, @NotNull MCCommand<?, MCCommandSender> command) {
		super(utils, command.getName(), command.getAliases().toArray(new String[0]));
		this.command = command;
	}

	@Override
	public boolean onCommand(@NotNull SpigotCommandSender sender, @NotNull String[] args) {
		return command.onCommand(sender, args);
	}

	@NotNull
	@Override
	public List<String> onTab(@NotNull SpigotCommandSender sender, @NotNull String[] args) {
		return command.onTab(sender, args);
	}

	@Override
	public boolean hasAccess(@NotNull SpigotCommandSender sender, boolean message) {
		return command.hasAccess(sender, message);
	}

	@NotNull
	@Override
	public MCCommand<P, SpigotCommandSender> inject(@NotNull MCCommand<P, SpigotCommandSender>... commands) {
		throw new UnsupportedOperationException("Adapted commands cannot inject new commands.");
	}
}
