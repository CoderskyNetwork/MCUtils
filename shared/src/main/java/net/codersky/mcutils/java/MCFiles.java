package net.codersky.mcutils.java;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Utility class containing methods related to {@link File files}.
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public class MCFiles {

	/**
	 * Ensures a {@link File} is created, creating any necessary
	 * parent directories as well as the file itself if it doesn't
	 * already exist.
	 *
	 * @param file The {@link File} to create.
	 * @param err The {@link Consumer} that will accept any exceptions
	 * thrown by this method. Caught exceptions are only of type
	 * {@link IOException} or {@link SecurityException}.
	 *
	 * @return {@code true} if the {@code file} created successfully
	 * or if it already exists. {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	public static boolean create(@NotNull File file, @NotNull  Consumer<Exception> err) {
		if (file.exists())
			return true;
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
			return true;
		} catch (IOException | SecurityException e) {
			err.accept(e);
			return false;
		}
	}

	/**
	 * Ensures a {@link File} is created, creating any necessary
	 * parent directories as well as the file itself if it doesn't
	 * already exist.
	 * <p>
	 * Any exceptions produced will just be {@link Exception#printStackTrace() printed}.
	 * If you want to override this behaviour use {@link #create(File, Consumer)}.
	 *
	 * @param file The {@link File} to create.
	 *
	 * @return {@code true} if the {@code file} created successfully
	 * or if it already exists. {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	public static boolean create(@NotNull File file) {
		return create(file, Exception::printStackTrace);
	}
}
