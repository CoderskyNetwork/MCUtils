package net.codersky.mcutils.spigot.time;

import net.codersky.mcutils.time.Task;
import net.codersky.mcutils.time.TaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * The Spigot platform {@link TaskScheduler}.
 *
 * @since MCUtils 1.0.0
 */
public class SpigotTaskScheduler implements TaskScheduler {

	private final JavaPlugin plugin;

	public SpigotTaskScheduler(@NotNull JavaPlugin plugin) {
		this.plugin = plugin;
	}

	private long toTicks(@NotNull TimeUnit unit, int amount) {
		return unit.toMillis(amount) * 50;
	}

	@Override
	public @NotNull Task runSync(@NotNull Runnable task) {
		return new SpigotTask(Bukkit.getScheduler().runTask(plugin, task));
	}

	@Override
	public @NotNull Task delaySync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay) {
		return new SpigotTask(Bukkit.getScheduler().runTaskLater(plugin, task, toTicks(unit, delay)));
	}

	@Override
	public @NotNull Task repeatSync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat) {
		return new SpigotTask(Bukkit.getScheduler().runTaskTimer(plugin, task, toTicks(unit, delay), toTicks(unit, repeat)));
	}

	@Override
	public @NotNull Task runAsync(@NotNull Runnable task) {
		return new SpigotTask(Bukkit.getScheduler().runTaskAsynchronously(plugin, task));
	}

	@Override
	public @NotNull Task delayAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay) {
		return new SpigotTask(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, unit.toMillis(delay) * 50));
	}

	@Override
	public @NotNull Task repeatAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat) {
		return new SpigotTask(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, toTicks(unit, delay), toTicks(unit, repeat)));
	}
}
