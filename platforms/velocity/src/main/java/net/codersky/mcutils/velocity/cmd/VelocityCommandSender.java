package net.codersky.mcutils.velocity.cmd;

import com.velocitypowered.api.command.CommandSource;
import net.codersky.mcutils.cmd.MCCommandSender;
import org.jetbrains.annotations.NotNull;

public class VelocityCommandSender extends MCCommandSender<CommandSource> {

	public VelocityCommandSender(@NotNull CommandSource original) {
		super(original);
	}

	@Override
	public boolean sendMessage(@NotNull String message) {
		getOriginal().sendPlainMessage(message);
		return true;
	}

	@Override
	public boolean hasPermission(@NotNull String permission) {
		return getOriginal().hasPermission(permission);
	}
}
