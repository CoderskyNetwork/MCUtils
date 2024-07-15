package net.codersky.mcutils.spigot.time;

import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a task created and linked to a {@link Timer}, which can be obtained
 * via {@link #getTimer()} and used to get the time left for this task to be executed.
 * <p>
 * This class is not intended to be instantiated manually but rather via
 * {@link Timer#schedule(Plugin, Runnable)} or {@link Timer#schedule(Plugin, Consumer, Object)}.
 * 
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public class TimerTask {

	private final Timer timer;
	private boolean paused = false;
	private BukkitTask task = null;

	@ApiStatus.Internal
	TimerTask(@Nonnull Timer timer) {
		this.timer = timer;
	}

	/**
	 * Gets the {@link Timer} that started this {@link TimerTask}.
	 * 
	 * @return The {@link Timer} that started this {@link TimerTask}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Cancels this {@link TimerTask}, meaning that it won't be executed.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void cancel() {
		if (task == null)
			return;
		task.cancel();
		task = null;
	}

	/**
	 * Checks if this {@link TimerTask} is paused or not.
	 * 
	 * @return {@code true} if this {@link TimerTask} is
	 * paused, {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Pauses or unpauses this {@link TimerTask}. Paused
	 * tasks won't {@link Timer#removeOne() tick} their
	 * {@link #getTimer() timer}.
	 * 
	 * @param paused Whether to pause or unpause this
	 * {@link TimerTask}.
	 * 
	 * @return This {@link TimerTask}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public TimerTask setPaused(boolean paused) {
		this.paused = paused;
		return this;
	}

	/*
	 * Scheduler internals
	 */

	@Nonnull
	@ApiStatus.Internal
	TimerTask schedule(@Nonnull Plugin plugin, @Nonnull Runnable runnable) {
		task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			if (paused || !timer.removeOne().hasEnded())
				return;
			runnable.run();
			cancel();
		}, 20, 20);
		return this;
	}

	@Nonnull
	@ApiStatus.Internal
	<T> TimerTask schedule(@Nonnull Plugin plugin, @Nonnull Consumer<T> consumer, @Nullable T obj) {
		return schedule(plugin, () -> consumer.accept(obj));
	}
}
