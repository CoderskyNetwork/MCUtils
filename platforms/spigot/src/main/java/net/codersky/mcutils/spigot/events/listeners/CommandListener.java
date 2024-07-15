package net.codersky.mcutils.spigot.events.listeners;

import net.codersky.mcutils.spigot.SpigotUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.codersky.mcutils.spigot.general.MCCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A class that joins the functionality of a {@link Listener} and an {@link MCCommand}.
 * Note that this class needs to be registered twice as
 * {@link SpigotPlugin#registerCommand(MCCommand) command registration} is
 * completely separated from {@link SpigotPlugin#registerEvents(Listener) event registration}
 * <p>
 * Note that events need the {@link EventHandler} annotation to work.
 * 
 * @param <U> The {@link SpigotUtils} that own this command listener.
 *
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see {@link PluginListener}
 * @see {@link MCCommand}
 * @see {@link Listener}
 */
public abstract class CommandListener<U extends SpigotUtils<?>> extends MCCommand<U> implements Listener {

	/**
	 * Creates a new instance of a {@link CommandListener} with the
	 * specified <b>name</b>, owned by <b>plugin</b>.
	 * 
	 * @param utils the {@link SpigotUtils} that own this command.
	 * @param name the name of this command.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public CommandListener(U utils, String name) {
		super(utils, name);
	}

	/**
	 * Creates a new instance of a {@link CommandListener} with the
	 * specified <b>name</b> and <b>aliases</b>, owned by <b>plugin</b>.
	 * 
	 * @param utils the {@link SpigotUtils} that own this command.
	 * @param name the name of this command.
	 * @param aliases additional names for the command
	 * 
	 * @since MCUtils 1.0.0
	 */
	public CommandListener(U utils, String name, String... aliases) {
		super(utils, name, aliases);
	}
}
