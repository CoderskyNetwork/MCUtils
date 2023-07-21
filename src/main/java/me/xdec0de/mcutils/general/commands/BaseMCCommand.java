package me.xdec0de.mcutils.general.commands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.xdec0de.mcutils.MCPlugin;

public abstract class BaseMCCommand<P extends MCPlugin> extends Command {

	private final P plugin;

	protected BaseMCCommand(P plugin, @Nonnull String name) {
		super(name);
		this.plugin = plugin;
	}

	public final P getPlugin() {
		return plugin;
	}

	/** @deprecated In favor of {@link #onCommand(CommandSender, String[])}
	 * @hidden */
	@Deprecated
	@Override
	@Nullable
	public abstract boolean execute(CommandSender sender, String commandLabel, String[] args);

	@Nullable
	public abstract boolean onCommand(@Nonnull CommandSender sender, @Nonnull String[] args);

	/** @deprecated In favor of {@link #onTab(CommandSender, String[])}, do not call this method manually.
	 * @hidden */
	@Deprecated
	@Override
	@Nullable
	public abstract List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args);

	@Nullable
	public abstract List<String> onTab(@Nonnull CommandSender sender, @Nonnull String[] args);
}
