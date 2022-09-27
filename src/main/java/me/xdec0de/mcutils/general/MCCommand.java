package me.xdec0de.mcutils.general;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import me.xdec0de.mcutils.MCUtils;
import me.xdec0de.mcutils.strings.MCStrings;

/**
 * A class designed to register commands easier.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class MCCommand {

	private final PluginCommand cmd;

	/**
	 * Creates and registers a new {@link PluginCommand} to be further modified at runtime.
	 * 
	 * @param plugin the plugin that will register this command.
	 * @param label name or alias of the command
	 * @param executor the {@link CommandExecutor} for this command.
	 * 
	 * @throws NullPointerException if <b>plugin</b>, <b>label</b> or <b>executor</b> are null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public MCCommand(@Nonnull JavaPlugin plugin, @Nonnull String label, @Nonnull CommandExecutor executor) {
		if (plugin == null)
			throw new NullPointerException("Plugin cannot be null.");
		if (label == null)
			throw new NullPointerException("Label cannot be null.");
		if (executor == null)
			throw new NullPointerException("Executor cannot be null.");
		cmd = plugin.getCommand(label);
		cmd.setExecutor(executor);
	}

	/**
	 * Sets the list of aliases to request on registration for this command.
	 * This can and should be defined on your <b>plugin.yml</b>,
	 * so there is no reason to use this method, but it's here for accessibility.
	 * 
	 * @param aliases aliases to register to this command
	 * 
	 * @return This {@link MCCommand} object, for chaining
	 * 
	 * @throws NullPointerException if <b>aliases</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public MCCommand aliases(@Nonnull List<String> aliases) {
		cmd.setAliases(aliases);
		return this;
	}

	/**
	 * Sets the list of aliases to request on registration for this command.
	 * This can and should be defined on your <b>plugin.yml</b>,
	 * so there is no reason to use this method, but it's here for accessibility.
	 * 
	 * @param aliases aliases to register to this command
	 * 
	 * @return This {@link MCCommand} object, for chaining
	 * 
	 * @throws NullPointerException if <b>aliases</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public MCCommand aliases(@Nonnull String... aliases) {
		cmd.setAliases(Arrays.asList(aliases));
		return this;
	}

	/**
	 * Sets the command description, this can and should be defined on your <b>plugin.yml</b>,
	 * so there is no reason to use this method, but it's here for accessibility. If <b>description</b>
	 * is null, the command description will be empty.
	 * 
	 * @param description new command description.
	 * 
	 * @return This {@link MCCommand} object, for chaining
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public MCCommand description(@Nullable String description) {
		cmd.setDescription(description);
		return this;
	}

	/**
	 * Sets the permission required by users to be able to perform this command,
	 * this can be defined on your <b>plugin.yml</b>, use this only if you want
	 * to let server owners choose their own permissions.
	 * 
	 * @param permission permission name or null.
	 * 
	 * @return This {@link MCCommand} object, for chaining.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public MCCommand permission(@Nullable String permission) {
		cmd.setPermission(permission);
		return this;
	}

	/**
	 * <b>Important:</b> Server owners already can define their own "insufficient permissions"
	 * message without the need of any plugin, only use this method if you really want
	 * to overwrite it for some reason...
	 * 
	 * Sets the permission message of this command with applied colors using
	 * {@link MCStrings#applyColor(String)}, this message will be sent whenever
	 * a {@link CommandExecutor} tries to execute a command but doesn't have
	 * permission to do so.
	 * 
	 * @param usage new permission message, null to indicate default message,
	 * or an empty string to indicate no message
	 * 
	 * @return This {@link MCCommand} object, for chaining.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public MCCommand permissionMessage(@Nullable String permissionMessage) {
		MCStrings strings = JavaPlugin.getPlugin(MCUtils.class).strings();
		cmd.setPermissionMessage(strings.applyColor(permissionMessage));
		return this;
	}

	/**
	 * Sets the example usage of this command with applied colors using
	 * {@link MCStrings#applyColor(String)}. If <b>usage</b>
	 * is null, the command description will be empty.
	 * 
	 * @param usage new example usage.
	 * 
	 * @return This {@link MCCommand} object, for chaining.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public MCCommand usage(@Nullable String usage) {
		MCStrings strings = JavaPlugin.getPlugin(MCUtils.class).strings();
		cmd.setUsage(strings.applyColor(usage));
		return this;
	}

	/**
	 * Returns the {@link PluginCommand} object being modified
	 * by this {@link MCCommand}.
	 * 
	 * @return The {@link PluginCommand} object being modified
	 * by this {@link MCCommand}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public PluginCommand command() {
		return cmd;
	}
}
