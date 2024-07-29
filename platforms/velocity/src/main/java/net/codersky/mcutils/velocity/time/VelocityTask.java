package net.codersky.mcutils.velocity.time;

import com.velocitypowered.api.scheduler.ScheduledTask;
import net.codersky.mcutils.time.Task;
import org.jetbrains.annotations.NotNull;

public class VelocityTask implements Task {

	private final ScheduledTask task;

	VelocityTask(@NotNull ScheduledTask task) {
		this.task = task;
	}

	@Override
	public void cancel() {
		task.cancel();
	}
}
