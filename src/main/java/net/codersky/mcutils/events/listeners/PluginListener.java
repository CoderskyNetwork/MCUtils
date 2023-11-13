package net.codersky.mcutils.events.listeners;

import javax.annotation.Nullable;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.codersky.mcutils.MCPlugin;
import net.codersky.mcutils.files.yaml.MessagesFile;
import net.codersky.mcutils.files.yaml.PluginFile;

/**
 * A {@link Listener} implementation that is linked to an {@link MCPlugin},
 * providing fast access to it and some of its methods such as
 * {@link #getConfig()} and {@link #getMessages()}.
 * <p>
 * Note that events need the {@link EventHandler} annotation to work.
 * 
 * @param <P> An {@link MCPlugin} that owns this command listener.
 *
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 * 
 * @see #getPlugin()
 * @see #getConfig()
 * @see #getMessages()
 * @see {@link CommandListener}
 */
public abstract class PluginListener<P extends MCPlugin> implements Listener {

	private final P plugin;

	public PluginListener(P plugin) {
		this.plugin = plugin;
	}

	public P getPlugin() {
		return plugin;
	}

	/*
	 * Shortcuts
	 */

	/**
	 * Shortcut to {@link MCPlugin#getConfig()}.
	 * 
	 * @return The <b>config.yml</b> file being used
	 * by {@link #getPlugin()} as a {@link PluginFile}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public PluginFile getConfig() {
		return plugin.getConfig();
	}

	/**
	 * Shortcut to {@link MCPlugin#getMessages()}.
	 * 
	 * @return The <b>config.yml</b> file being used
	 * by {@link #getPlugin()} as a {@link PluginFile}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public MessagesFile getMessages() {
		return plugin.getMessages();
	}
}
