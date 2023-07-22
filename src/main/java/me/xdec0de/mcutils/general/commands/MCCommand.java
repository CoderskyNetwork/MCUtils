package me.xdec0de.mcutils.general.commands;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;

import me.xdec0de.mcutils.MCPlugin;

/**
 * A class used to represent a command that can
 * be registered by any {@link MCPlugin}.
 * 
 * @param <P> An {@link MCPlugin} that owns this command.
 *
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public abstract class MCCommand<P extends MCPlugin> extends BaseMCCommand<P> implements PluginIdentifiableCommand {

	/**
	 * Creates a new instance of an {@link MCCommand} with the
	 * specified <b>name</b>, owned by <b>plugin</b>.
	 * 
	 * @param plugin the plugin that owns (And may register) this command.
	 * @param name the name of this command.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCCommand(P plugin, String name) {
		super(plugin, name);
	}

	/**
	 * Creates a new instance of an {@link MCCommand} with the
	 * specified <b>name</b> and <b>aliases</b>, owned by <b>plugin</b>.
	 * 
	 * @param plugin the plugin that owns (And may register) this command.
	 * @param name the name of this command.
	 * @param aliases additional names for the command
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCCommand(P plugin, String name, String... aliases) {
		super(plugin, name, aliases);
	}

	/**
	 * @since MCUtils 1.0.0
	 * 
	 * @see #onCommand(CommandSender, String[])
	 * @see #asString(int, String[])
	 * @see #asInt(int, String[])
	 * @see #asPlayer(int, String[])
	 * @see #asOfflinePlayer(int, String[])
	 * @see #asEnum(int, String[], Class)
	 */
	@Nullable
	public List<String> onTab(@Nonnull CommandSender sender, @Nonnull String[] args) {
		return null; // Using this by default, as some commands may not require any arguments.
	}

	/*
	 * Method override for better documentation & return types.
	 */

	/** @return This {@link MCCommand} */
	@Override
	public final MCCommand<P> inject(int position, @Nullable MCSubCommand<?>... commands) {
		super.inject(position, commands);
		return this;
	}
}
