package net.codersky.mcutils.velocity.time;

import com.velocitypowered.api.proxy.ProxyServer;
import net.codersky.mcutils.time.TaskScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class VelocityTaskScheduler implements TaskScheduler {

	private final ProxyServer server;
	private final Object plugin;

	public VelocityTaskScheduler(@NotNull ProxyServer server, @NotNull Object plugin) {
		this.server = server;
		this.plugin = plugin;
	}

	@Override
	public @NotNull VelocityTask runSync(@NotNull Runnable task) {
		return runAsync(task);
	}

	@Override
	public @NotNull VelocityTask delaySync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay) {
		return delayAsync(task, unit, delay);
	}

	@Override
	public @NotNull VelocityTask repeatSync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat) {
		return repeatAsync(task, unit, delay, repeat);
	}

	@Override
	public @NotNull VelocityTask runAsync(@NotNull Runnable task) {
		return new VelocityTask(server.getScheduler().buildTask(plugin, task).schedule());
	}

	@Override
	public @NotNull VelocityTask delayAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay) {
		return new VelocityTask(server.getScheduler().buildTask(plugin, task).delay(delay, unit).schedule());
	}

	@Override
	public @NotNull VelocityTask repeatAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat) {
		return new VelocityTask(server.getScheduler().buildTask(plugin, task)
				.delay(delay, unit)
				.repeat(repeat, unit)
				.schedule());
	}
}
