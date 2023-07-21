package me.xdec0de.mcutils.general.commands;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;

import me.xdec0de.mcutils.MCPlugin;

/**
 * A class designed to register commands easier.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public abstract class MCCommand<P extends MCPlugin> extends BaseMCCommand<P> implements PluginIdentifiableCommand {

	public MCCommand(P plugin, String name) {
		super(plugin, name);
	}

	public MCCommand(P plugin, String name, String... aliases) {
		super(plugin, name);
		setAliases(Arrays.asList(aliases));
	}

	/** @deprecated In favor of {@link #onCommand(CommandSender, String[])}
	 * @hidden */
	@Deprecated
	@Override
	@Nullable
	public final boolean execute(CommandSender sender, String commandLabel, String[] args) {
		return this.onCommand(sender, args); // TODO Add sub-command support.
	}

	/** @deprecated In favor of {@link #onTab(CommandSender, String[])}
	 * @hidden */
	@Deprecated
	@Override
	@Nullable
	public final List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) {
		return null;
	}

	@Nullable
	public List<String> onTab(@Nonnull CommandSender sender, @Nonnull String[] args) {
		return null;
	}
}
