package net.codersky.mcutils.spigot;

import net.codersky.mcutils.spigot.general.MCCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestCommand extends MCCommand<MCUtilsSpigot, SpigotUtils<MCUtilsSpigot>> {

	public TestCommand(SpigotUtils utils, @NotNull String name) {
		super(utils, name);
	}

	@Nullable
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
		return false;
	}

	@Nullable
	@Override
	public List<String> onTab(@NotNull CommandSender sender, @NotNull String[] args) {
		return List.of();
	}
}
