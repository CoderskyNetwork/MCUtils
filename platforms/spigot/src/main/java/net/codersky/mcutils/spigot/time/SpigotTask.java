package net.codersky.mcutils.spigot.time;

import net.codersky.mcutils.time.Task;
import org.bukkit.scheduler.BukkitTask;

public class SpigotTask implements Task {

	private final BukkitTask task;

	SpigotTask(BukkitTask task) {
		this.task = task;
	}

	@Override
	public void cancel() {
		task.cancel();
	}
}
