package net.codersky.mcutils.time;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public interface TaskScheduler {

	/*
	 * Synchronous tasks
	 */

	@NotNull Task runSync(@NotNull Runnable task);

	@NotNull Task delaySync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay);

	default @NotNull Task delaySync(@NotNull Runnable task, int tickDelay) {
		return delaySync(task, TimeUnit.MILLISECONDS, tickDelay * 50); // 1 tick = 50 milliseconds
	}

	@NotNull Task repeatSync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat);

	/*
	 * Asynchronous tasks
	 */

	@NotNull Task runAsync(@NotNull Runnable task);

	@NotNull Task delayAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay);

	default @NotNull Task delayAsync(@NotNull Runnable task, int tickDelay) {
		return delayAsync(task, TimeUnit.MILLISECONDS, tickDelay * 50); // 1 tick = 50 milliseconds
	}

	@NotNull Task repeatAsync(@NotNull Runnable task, @NotNull TimeUnit unit, int delay, int repeat);
}
