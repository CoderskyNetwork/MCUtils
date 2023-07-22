package me.xdec0de.mcutils.general.commands;

import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

import me.xdec0de.mcutils.MCPlugin;

/**
 * A class used to represent a sub command that can
 * be added to an {@link MCCommand}, this class mostly
 * exist to differenciate between sub commands and commands,
 * not allowing sub commands to be directly registered by
 * a plugin, only injected to an {@link MCCommand}.
 * 
 * @param <P> An {@link MCPlugin} that owns this sub command.
 *
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see MCCommand#inject(int, MCSubCommand...)
 */
public abstract class MCSubCommand<P extends MCPlugin> extends BaseMCCommand<P> {

	/**
	 * Creates a new instance of a {@link MCSubCommand} with the
	 * specified <b>name</b>, owned by <b>plugin</b>.
	 * 
	 * @param plugin the plugin that owns (And may register) this sub command.
	 * @param name the name of this sub command.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public MCSubCommand(P plugin, String name) {
		super(plugin, name);
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
	@Override
	public List<String> onTab(CommandSender sender, String[] args) {
		return null; // Using this by default, as some sub commands may not require any arguments.
	}

	/*
	 * Method override for better documentation & return types.
	 */

	/** @return This {@link MCSubCommand} */
	@Override
	public final MCSubCommand<P> inject(int position, @Nullable MCSubCommand<?>... commands) {
		super.inject(position, commands);
		return this;
	}
}
