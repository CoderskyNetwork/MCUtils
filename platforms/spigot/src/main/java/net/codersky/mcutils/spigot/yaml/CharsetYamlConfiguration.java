package net.codersky.mcutils.spigot.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;

/**
 * A {@link YamlConfiguration} class with {@link Charset} support.
 * 
 * @author xDec0de_
 * 
 * @since MCUtils 1.0.0
 */
public class CharsetYamlConfiguration extends YamlConfiguration {

	private final Charset charset;

	/**
	 * Creates a new {@link YamlConfiguration} with {@link Charset} support.
	 * If <b>charset</b> is null {@link StandardCharsets#UTF_8} will be used.
	 * 
	 * @param charset the charset to use.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public CharsetYamlConfiguration(@Nonnull Charset charset) {
		this.charset = charset == null ? StandardCharsets.UTF_8 : charset;
	}

	/**
	 * Gets the {@link Charset} being used by this {@link CharsetYamlConfiguration}.
	 * 
	 * @return the charset being used.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public Charset getCharset() {
		return charset;
	}

	/**
	 * Saves this {@link CharsetYamlConfiguration} to the specified location.
	 * <p>
	 * If the file does not exist, it will be created. If already exists, it
	 * will be overwritten. If it cannot be overwritten or created, an
	 * exception will be thrown.
	 * <p>
	 * This method will save using {@link #getCharset()}.
	 *
	 * @param file File to save to, if null, nothing will be done.
	 * 
	 * @throws IOException Thrown when the given file cannot be written to for any reason.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Override
	public void save(@Nullable File file) throws IOException {
		if (file == null)
			return;
		Files.createParentDirs(file);
		String data = this.saveToString();
		Writer writer = new OutputStreamWriter(new FileOutputStream(file), charset);
		try {
			writer.write(data);
		} finally {
			writer.close();
		}
	}

	/**
	 * Loads this {@link CharsetYamlConfiguration} from the specified location.
	 * <p>
	 * All the values contained within this configuration will be removed,
	 * leaving only settings and defaults, and the new values will be loaded
	 * from the given file.
	 * <p>
	 * If the file cannot be loaded for any reason, an exception will be
	 * thrown.
	 * <p>
	 * This method will load using {@link #getCharset()}.
	 *
	 * @param file File to load from.
	 * @throws FileNotFoundException Thrown when the given file cannot be opened or <b>file</b> is null.
	 * @throws IOException Thrown when the given file cannot be read.
	 * @throws InvalidConfigurationException Thrown when the given file is not a valid Configuration
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Override
	public void load(@Nonnull File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
		if (file == null)
			throw new FileNotFoundException("File is null");
		this.load(new InputStreamReader(new FileInputStream(file), charset));
	}
}
