package net.codersky.mcutils.time.timer;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.sun.source.util.Plugin;
import net.codersky.mcutils.time.Task;
import net.codersky.mcutils.time.TaskScheduler;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	private Task task = null;

	@ApiStatus.Internal
	TimerTask(@NotNull Timer timer) {
		this.timer = timer;
	}

	/**
	 * Gets the {@link Timer} that started this {@link TimerTask}.
	 * 
	 * @return The {@link Timer} that started this {@link TimerTask}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@NotNull
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

	@NotNull
	@ApiStatus.Internal
	TimerTask schedule(@NotNull TaskScheduler scheduler, @NotNull Runnable runnable) {
		task = scheduler.repeatSync(() -> {
			if (paused || !timer.removeOne().hasEnded())
				return;
			runnable.run();
			cancel();
		}, TimeUnit.SECONDS, 1, 1);
		return this;
	}

	@NotNull
	@ApiStatus.Internal
	<T> TimerTask schedule(@NotNull TaskScheduler scheduler, @NotNull Consumer<T> consumer, @Nullable T obj) {
		return schedule(scheduler, () -> consumer.accept(obj));
	}
}
