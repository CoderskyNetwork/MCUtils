package net.codersky.mcutils.crossplatform.server;

import net.codersky.mcutils.MCUtils;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of {@link MCUtils} meant for server
 * platforms such as Spigot or Paper.
 *
 * @param <P> The class that holds an instance of this
 * utils, meant to be the main class of your plugin as
 * it can be accessed via {@link #getPlugin()}.
 *
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public abstract class ServerUtils<P> extends MCUtils<P> {

	public ServerUtils(@NotNull P plugin) {
		super(plugin);
	}
}
