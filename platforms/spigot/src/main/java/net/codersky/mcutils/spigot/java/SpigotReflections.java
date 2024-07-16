package net.codersky.mcutils.spigot.java;

import javax.annotation.Nullable;

import net.codersky.mcutils.java.reflection.RefClass;
import net.codersky.mcutils.java.reflection.RefObject;
import org.bukkit.Bukkit;

import net.codersky.mcutils.java.strings.Replacer;

public abstract class SpigotReflections {

	/**
	 * The server version name used on the net.minecraft.server and org.bukkit.craftbukkit packages.
	 */
	public static final String VERSION = Bukkit.getServer().getClass().getName().split("\\.")[3];

	/**
	 * Gets a {@link RefClass} by name, returning {@code null} if no class
	 * is found, note that "{nms}" and "{cb}" can be used on the class name
	 * for net.minecraft.server and org.bukkit.craftbukkit classes respectively.
	 * 
	 * @param className
	 * @return
	 */
	@Nullable
	public static RefClass getClass(@Nullable String className) {
		if (className == null)
			return null;
		final String name = new Replacer("{nms}", ("net.minecraft.server." + VERSION), "{cb}", ("org.bukkit.craftbukkit." + VERSION)).replaceAt(className);
		try {
			return new RefClass(Class.forName(name));
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@Nullable
	public static RefObject getRefObj(@Nullable Object obj) {
		return obj == null ? null : new RefObject(obj);
	}
}
