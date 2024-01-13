package net.codersky.mcutils.events.listeners;

import java.util.Objects;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.projectiles.ProjectileSource;

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

	/**
	 * Instantiates a new {@link PluginListener}, linked to the specified <b>plugin</b>,
	 * which cannot be {@code null}. Note that this {@link PluginListener} can still
	 * be registered by other plugins, even though this is really rare.
	 * 
	 * @param plugin the {@link MCPlugin} that will own this {@link PluginListener}.
	 * 
	 * @throws NullPointerException if <b>plugin</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public PluginListener(@Nonnull P plugin) {
		this.plugin = Objects.requireNonNull(plugin);
	}

	/**
	 * Gets the {@link MCPlugin} used to instantiate this {@link PluginListener}.
	 * This is also normally the {@link MCPlugin plugin} that will register it.
	 * 
	 * @return The {@link MCPlugin} used to instantiate this {@link PluginListener}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
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

	/*
	 * Conversion
	 */

	/**
	 * Casts an {@link Entity} instance to a {@link Player} instance
	 * safely and if possible.
	 * 
	 * @param entity the {@link Entity} to cast to {@link Player}.
	 * 
	 * @return A {@link Player} instance if <b>entity</b> is an
	 * instance of {@link Player}, {@code null} otherwise or if
	 * <b>entity</b> itself is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Player asPlayer(@Nullable Entity entity) {
		if (entity != null && entity instanceof Player)
			return (Player) entity;
		return null;
	}

	/**
	 * Tries to get a related {@link Player} from an {@link Entity}. Here
	 * are the currently supported cases:
	 * <ul>
	 * <li> If {@link Player} is {@link Class#isAssignableFrom(Class) assignable} 
	 * from <b>entity</b>'s class, <b>entity</b> will be casted to {@link Player} and returned. </li>
	 * 
	 * <li> If <b>entity</b> is an instance of {@link Projectile} and the
	 * {@link Projectile#getShooter() shooter} is an instance of
	 * {@link Player}, said shooter will be casted and returned. </li>
	 * 
	 * <li> If <b>entity</b> is an instance of {@link Tameable} and the 
	 * {@link Tameable#getOwner() owner} {@link UUID} corresponds to
	 * an online {@link Player}, said {@link Player} will be returned.
	 * </ul>
	 * 
	 * @param entity the {@link Entity} of which to take a related {@link Player}
	 * instance from.
	 * 
	 * @return A related {@link Player} from an {@link Entity}. {@code null} if
	 * the <b>entity</b> is not related to an <b>ONLINE</b> {@link Player}.
	 * 
	 * @throws NullPointerException if <b>entity</b> is {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Player asRelatedPlayer(@Nonnull Entity entity) {
		if (Player.class.isAssignableFrom(entity.getClass()))
			return (Player) entity;
		else if (entity instanceof Projectile) {
			final ProjectileSource source = ((Projectile)entity).getShooter();
			return source instanceof Player ? (Player) source : null;
		} else if (entity instanceof Tameable) {
			final AnimalTamer tamer = ((Tameable)entity).getOwner();
			return tamer == null ? null : Bukkit.getPlayer(tamer.getUniqueId());
		}
		return null;
	}

	/**
	 * Tries to get a related {@link OfflinePlayer} from an {@link Entity}. Here
	 * are the currently supported cases:
	 * <ul>
	 * <li> If {@link OfflinePlayer} is {@link Class#isAssignableFrom(Class) assignable} 
	 * from <b>entity</b>'s class, <b>entity</b> will be casted to {@link OfflinePlayer} and returned. </li>
	 * 
	 * <li> If <b>entity</b> is an instance of {@link Projectile} and the
	 * {@link Projectile#getShooter() shooter} is an instance of
	 * {@link Player}, said shooter will be casted and returned. </li>
	 * 
	 * <li> If <b>entity</b> is an instance of {@link Tameable} and the 
	 * {@link Tameable#getOwner() owner} {@link UUID} corresponds to
	 * an {@link OfflinePlayer}, said {@link OfflinePlayer} will be returned.
	 * </ul>
	 * 
	 * @param entity the {@link Entity} of which to take a related
	 * {@link OfflinePlayer} instance from.
	 * 
	 * @return A related {@link OfflinePlayer} from an {@link Entity}. {@code null} if
	 * the <b>entity</b> is not related to any {@link OfflinePlayer}.
	 * 
	 * @throws NullPointerException if <b>entity</b> is {@code null}
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public OfflinePlayer asRelatedOfflinePlayer(@Nullable Entity entity) {
		if (OfflinePlayer.class.isAssignableFrom(entity.getClass()))
			return (OfflinePlayer) entity;
		else if (entity instanceof Projectile) {
			final ProjectileSource source = ((Projectile)entity).getShooter();
			return source instanceof Player ? (OfflinePlayer) source : null;
		} else if (entity instanceof Tameable) {
			final AnimalTamer tamer = ((Tameable)entity).getOwner();
			// We assume that the player has played before as the UUID is related to a tamer.
			return tamer == null ? null : Bukkit.getOfflinePlayer(tamer.getUniqueId());
		}
		return null;
	}
}
