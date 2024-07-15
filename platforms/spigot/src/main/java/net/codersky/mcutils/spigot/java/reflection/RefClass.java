package net.codersky.mcutils.spigot.java.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;

/**
 * Our equivalent to the reflection methods from {@link Class} without an instance,
 * mostly used to access CraftBukkit or NMS classes reflectively
 * without having to worry about the server version.
 * <p>
 * If you already have an <b>instance</b> such as {@link Bukkit#getServer()}, please
 * use {@link RefObject}, this class is to create new instances with reflection.
 * <p>
 * Even though Java's reflection allows accessing non-accessible constructors,
 * methods and fields, MCUtils will <b>never</b> add support to access them.
 * This practice goes against Java's principle of encapsulation and thus should not be abused.
 * Adding direct support for it would make it very easy to abuse, that's why we won't (Directly) add
 * it, <i>even though you always can use our reflection classes to do it easier than how it would be done
 * with pure Java reflection</i>, however, once again, we <b>DO NOT</b> recommend it, do so at your own risk.
 * 
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 * 
 * @see #newInstance()
 * @see #newInstance(Class...)
 */
public class RefClass {

	private final Class<?> clazz;

	/**
	 * Creates an instance of a reflection {@link Class}, used
	 * to access constructors of it reflectively. If you pretend
	 * to use this class to get a specific object from it that you
	 * may use quite often, storing it is recommended to avoid using
	 * reflection a lot. Also, please read {@link RefClass}.
	 * 
	 * @param clazz The class to use.
	 * 
	 * @since MCUtils 1.0.0
	 */
	RefClass(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Gets the class used by this {@link RefClass}.
	 * 
	 * @return The class used by this {@link RefClass}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public Class<?> getRealClass() {
		return clazz;
	}

	/**
	 * Creates a new instance of this {@link RefClass} without constructor parameters,
	 * returning a {@link RefObject} if the instantiation was successful.
	 * If any {@link Exception} such as {@link NoSuchMethodException} occurs,
	 * {@code null} will be returned.
	 * <p>
	 * Currently handled exceptions:
	 * <ul>
	 * <li>{@link NoSuchMethodException}</li>
	 * <li>{@link InstantiationException}</li>
	 * <li>{@link IllegalAccessException}</li>
	 * <li>{@link IllegalArgumentException}</li>
	 * <li>{@link InvocationTargetException}</li>
	 * </ul>
	 * 
	 * @return An instance of this {@link RefClass} as a {@link RefObject} if the class
	 * was successfully instantiated, {@code null} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public RefObject newInstance() {
		return newInstance(null, new Object[0]);
	}

	/**
	 * Creates a new instance of this {@link RefClass} without constructor parameters,
	 * returning a {@link RefObject}  if the instantiation was successful.
	 * If any {@link Exception} such as {@link NoSuchMethodException} occurs,
	 * <b>onFailure</b> will accept it and {@code null} will be returned.
	 * <p>
	 * Currently handled exceptions:
	 * <ul>
	 * <li>{@link NoSuchMethodException}</li>
	 * <li>{@link InstantiationException}</li>
	 * <li>{@link IllegalAccessException}</li>
	 * <li>{@link IllegalArgumentException}</li>
	 * <li>{@link InvocationTargetException}</li>
	 * </ul>
	 * 
	 * @param onFailure a {@link Consumer} that will accept any {@link Exception} thrown
	 * by this method.
	 * 
	 * @return An instance of this {@link RefClass} as a {@link RefObject} if the class
	 * was successfully instantiated, {@code null} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public RefObject newInstance(@Nullable Consumer<Exception> onFailure) {
		return newInstance(onFailure, new Object[0]);
	}

	/**
	 * Creates a new instance of this {@link RefClass} with the specified <b>params</b>,
	 * returning a {@link RefObject} if the instantiation was successful.
	 * If any {@link Exception} such as {@link NoSuchMethodException} occurs,
	 * {@code null} will be returned.
	 * <p>
	 * Currently handled exceptions:
	 * <ul>
	 * <li>{@link NoSuchMethodException}</li>
	 * <li>{@link InstantiationException}</li>
	 * <li>{@link IllegalAccessException}</li>
	 * <li>{@link IllegalArgumentException}</li>
	 * <li>{@link InvocationTargetException}</li>
	 * </ul>
	 * 
	 * @param params the parameters to use when instantiating the class.
	 * 
	 * @return An instance of this {@link RefClass} as a {@link RefObject} if the class
	 * was successfully instantiated, {@code null} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public RefObject newInstance(@Nullable Object... params) {
		return newInstance(null, params);
	}

	/**
	 * Creates a new instance of this {@link RefClass} with the specified <b>params</b>,
	 * returning a {@link RefObject} if the instantiation was successful.
	 * If any {@link Exception} such as {@link NoSuchMethodException} occurs,
	 * <b>onFailure</b> will accept it and {@code null} will be returned.
	 * <p>
	 * Currently handled exceptions:
	 * <ul>
	 * <li>{@link NoSuchMethodException}</li>
	 * <li>{@link InstantiationException}</li>
	 * <li>{@link IllegalAccessException}</li>
	 * <li>{@link IllegalArgumentException}</li>
	 * <li>{@link InvocationTargetException}</li>
	 * </ul>
	 * 
	 * @param onFailure a {@link Consumer} that will accept any {@link Exception} thrown
	 * by this method.
	 * @param params the parameters to use when instantiating the class.
	 * 
	 * @return An instance of this {@link RefClass} as a {@link RefObject} if the class
	 * was successfully instantiated, {@code null} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public RefObject newInstance(@Nullable Consumer<Exception> onFailure, @Nullable Object... params) {
		final Object[] parameters = params == null ? new Object[0] : params;
		try {
			Class<?>[] classes = new Class<?>[parameters.length];
			for (int i = 0; i < parameters.length; i++)
				classes[i] = parameters[i].getClass();
			final Constructor<?> constructor = clazz.getConstructor(classes);
			return constructor == null ? null : new RefObject(constructor.newInstance(params));
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException  e) {
			if (onFailure != null)
				onFailure.accept(e);
			return null;
		}
	}
}
