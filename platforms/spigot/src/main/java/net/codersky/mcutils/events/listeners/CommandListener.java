package net.codersky.mcutils.events.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.codersky.mcutils.MCPlugin;
import net.codersky.mcutils.general.MCCommand;

/**
 * A class that joins the functionality of a {@link Listener} and an {@link MCCommand}.
 * Note that this class needs to be registered twice as
 * {@link MCPlugin#registerCommand(MCCommand) command registration} is
 * completely separated from {@link MCPlugin#registerEvents(Listener) event registration}
 * <p>
 * Note that events need the {@link EventHandler} annotation to work.
 * 
 * @param <P> An {@link MCPlugin} that owns this command listener.
 *
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see {@link PluginListener}
 * @see {@link MCCommand}
 * @see {@link Listener}
 */
public abstract class CommandListener<P extends MCPlugin> extends MCCommand<P> implements Listener {

	/**
	 * Creates a new instance of a {@link CommandListener} with the
	 * specified <b>name</b>, owned by <b>plugin</b>.
	 * 
	 * @param plugin the plugin that owns this command.
	 * @param name the name of this command.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public CommandListener(P plugin, String name) {
		super(plugin, name);
	}

	/**
	 * Creates a new instance of a {@link CommandListener} with the
	 * specified <b>name</b> and <b>aliases</b>, owned by <b>plugin</b>.
	 * 
	 * @param plugin the plugin that owns this command.
	 * @param name the name of this command.
	 * @param aliases additional names for the command
	 * 
	 * @since MCUtils 1.0.0
	 */
	public CommandListener(P plugin, String name, String... aliases) {
		super(plugin, name, aliases);
	}
}
