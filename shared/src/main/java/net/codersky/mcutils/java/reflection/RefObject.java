package net.codersky.mcutils.java.reflection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Our equivalent to the reflection methods from {@link Class} with an instance,
 * mostly used to access CraftBukkit or NMS classes reflectively
 * without having to worry about the server version.
 * <p>
 * Even though Java's reflection allows accessing non-accessible constructors,
 * methods and fields, MCUtils will <b>never</b> add support to access them.
 * This practice goes against Java's principle of encapsulation and thus should not be abused.
 * Adding direct support for it would make it very easy to abuse, that's why we won't (Directly) add
 * it, <i>even though you can always use our reflection classes to do it easier than how it would be done
 * with pure Java reflection</i>, however, once again, we <b>DO NOT</b> recommend it, do so at your own risk.
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 *
 * @see #getInstance()
 * @see #getUsedClass()
 * @see #invoke(String)
 * @see #invoke(String, Consumer)
 * @see #invoke(String, Object[])
 * @see #invoke(String, Consumer, Object[])
 */
public class RefObject {

	private final Object instance;

	/**
	 * Creates an instance of a reflection {@link Object}, used
	 * to access methods of its class reflectively. If you pretend
	 * to use this class to get a specific object from it that you
	 * may use quite often, storing it is recommended to avoid using
	 * reflection a lot. Also, please read {@link RefObject}.
	 *
	 * @param instance the instance to use to invoke methods.
	 *
	 * @since MCUtils 1.0.0
	 */
	public RefObject(@NotNull Object instance) {
		this.instance = Objects.requireNonNull(instance, "The instance of RefObject cannot be null.");
	}

	/**
	 * Gets the {@link Object} instance
	 * used to create this {@link RefObject}.
	 *
	 * @return The {@link Object} instance
	 * used to create this {@link RefObject}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public Object getInstance() {
		return instance;
	}

	/**
	 * Gets the class of the {@link Object} instance
	 * used to create this {@link RefObject}.
	 *
	 * @return The class of the {@link Object} instance
	 * used to create this {@link RefObject}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public Class<?> getUsedClass() {
		return instance.getClass();
	}

	/**
	 * Invokes a {@link Method} with the specified <b>method</b>,
	 * returning a {@link RefObject} with the return value of said <b>method</b>.
	 * Note that the returning value will be null if the <b>method</b> doesn't exist,
	 * this {@link RefObject} instance doesn't have access to it or the <b>method</b> itself
	 * returns null. If you want to catch exceptions you can use
	 * {@link #invoke(String, Consumer)}.
	 * <p>
	 * Currently handled exceptions:
	 * <ul>
	 * <li>{@link NoSuchMethodException}</li>
	 * <li>{@link SecurityException}</li>
	 * <li>{@link IllegalAccessException}</li>
	 * <li>{@link IllegalArgumentException}</li>
	 * <li>{@link InvocationTargetException}</li>
	 * </ul>
	 *
	 * @param method the method to invoke.
	 *
	 * @return The return value of the method, null if any error occurs
	 * or the method itself returned null.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public RefObject invoke(String method) {
		return invoke(method, null, new Object[0]);
	}

	/**
	 * Invokes a {@link Method} with the specified <b>method</b>,
	 * returning a {@link RefObject} with the return value of said <b>method</b>.
	 * If any {@link Exception} such as {@link NoSuchMethodException} occurs,
	 * <b>onFailure</b> will accept it, ensuring that an error was the reason
	 * that this method returned false and that it wasn't the invoked method
	 * the one that returned null.
	 * <p>
	 * Currently handled exceptions:
	 * <ul>
	 * <li>{@link NoSuchMethodException}</li>
	 * <li>{@link SecurityException}</li>
	 * <li>{@link IllegalAccessException}</li>
	 * <li>{@link IllegalArgumentException}</li>
	 * <li>{@link InvocationTargetException}</li>
	 * </ul>
	 *
	 * @param method the method to invoke.
	 * @param onFailure a {@link Consumer} that will accept any {@link Exception} thrown
	 * by this method.
	 *
	 * @return The return value of the method, null if any error occurs
	 * or the method itself returned null.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public RefObject invoke(String method, Consumer<Exception> onFailure) {
		return invoke(method, onFailure, new Object[0]);
	}


	/**
	 * Invokes a {@link Method} with the specified <b>method</b> name
	 * and <b>params</b>, returning a {@link RefObject} with the return
	 * value of said <b>method</b>. Note that the returning value will
	 * be null if the <b>method</b> doesn't exist, this {@link RefObject}
	 * instance doesn't have access to it or the <b>method</b> itself
	 * returns null. If you want to catch exceptions you can use
	 * {@link #invoke(String, Consumer, Object[])}.
	 *
	 * @param method the method to invoke.
	 * @param params the parameters to use when invoking the method.
	 *
	 * @return The return value of the method, null if any error occurs
	 * or the method itself returned null.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public RefObject invoke(String method, Object... params) {
		return invoke(method, null, params);
	}

	/**
	 * Invokes a {@link Method} with the specified <b>method</b> name
	 * and <b>params</b>, returning a {@link RefObject} with the return value
	 * of said <b>method</b>. If any {@link Exception} such as
	 * {@link NoSuchMethodException} occurs, <b>onFailure</b> will accept it,
	 * ensuring that an error was the reason that this method returned false
	 * and that it wasn't the invoked method the one that returned null.
	 * <p>
	 * Currently handled exceptions:
	 * <ul>
	 * <li>{@link NoSuchMethodException}</li>
	 * <li>{@link SecurityException}</li>
	 * <li>{@link IllegalAccessException}</li>
	 * <li>{@link IllegalArgumentException}</li>
	 * <li>{@link InvocationTargetException}</li>
	 * </ul>
	 *
	 * @param method the method to invoke.
	 * @param onFailure a {@link Consumer} that will accept any {@link Exception} thrown
	 * by this method.
	 * @param params the parameters to use when invoking the method.
	 *
	 * @return The return value of the method, null if any error occurs
	 * or the method itself returned null.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public RefObject invoke(String method, Consumer<Exception> onFailure, Object... params) {
		try {
			Class<?>[] classes = new Class<?>[params.length];
			for (int i = 0; i < params.length; i++)
				classes[i] = params[i].getClass();
			final Object obj = instance.getClass().getMethod(method, classes).invoke(instance, params);
			return obj == null ? null : new RefObject(obj);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			if (onFailure != null)
				onFailure.accept(e);
			return null;
		}
	}
}
