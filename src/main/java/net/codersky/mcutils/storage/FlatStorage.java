package net.codersky.mcutils.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Nonnull;

import net.codersky.mcutils.java.strings.MCStrings;

/**
 * {@link StorageHandler} class used to store data on
 * simple flat files while trying to keep the size of
 * said file to a minimum. The data is stored as text
 * without spaces nor indentation, supporting basic
 * data types. About memory usage, this class only
 * stores a {@link HashMap} with all keys and values
 * and the {@link File} itself.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public class FlatStorage extends StorageHandler {

	private final File file;

	public FlatStorage(@Nonnull String path) {
		this.file = new File(path.endsWith(".mcufs") ? path : path + ".mcufs");
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Utility
	 */

	@Nonnull
	public final File asFile() {
		return file;
	}

	public final boolean exists() {
		return file.exists();
	}

	/*
	 * Saving
	 */

	@Override
	public boolean save() {
		int errors = 0;
		try {
			final FileWriter writer = new FileWriter(file);
			for (Entry<String, Object> entry : keys.entrySet()) {
				final String toWrite;
				if (entry.getValue() instanceof List)
					toWrite = toWrite(entry.getKey(), (List<?>) entry.getValue());
				else
					toWrite = toWrite(entry.getKey(), entry.getValue());
				if (toWrite != null)
					writer.write(toWrite);
				else
					errors++;
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (errors != 0)
			System.err.println("Failed to save " + file.getPath() + " because of " + errors + " error(s) shown above.");
		return errors == 0;
	}

	// Saving - Simple objects //

	private String toWrite(String key, Object value) {
		final StringBuilder builder = new StringBuilder();
		if (value instanceof CharSequence)
			builder.append('s').append(key).append(':').append(value.toString());
		else if (value instanceof Character)
			builder.append('c').append(key).append(':').append(((char) value) == '\n' ? "\\n" : (char) value);
		else if (value instanceof Boolean)
			builder.append('b').append(key).append(':').append(((boolean) value ? "t" : "f"));
		else if (value instanceof UUID)
			builder.append('u').append(key).append(':').append(value.toString());
		else if (value instanceof Number) // Number identification character is upper case.
			builder.append(value.getClass().getSimpleName().charAt(0)).append(key).append(':').append(value.toString());
		else {
			System.err.println("Unsupported data of type " + value.getClass().getName() + " with a string value of \"" + value + "\"");
			return null;
		}
		return builder.append('\n').toString();
	}

	// Saving - Lists //

	/**
	 * @throws ClassCastException If lst contains elements of different types (Try with Arrays.asList("exception", 10))
	 */
	@SuppressWarnings("unchecked")
	private String toWrite(String key, List<?> lst) {
		final Object first = lst.get(0);
		final StringBuilder builder = new StringBuilder("*");
		if (first instanceof CharSequence)
			listAppend(key, builder.append('s'), (List<CharSequence>) lst);
		else if (first instanceof Character)
			listAppend(key, builder.append('c'), (List<Character>) lst, c -> c == '\n' ? "\\n" : c.toString());
		else if (first instanceof Boolean)
			listAppend(key, builder.append('b'), (List<Boolean>) lst, b -> b ? "t" : "f");
		else if (first instanceof UUID)
			listAppend(key, builder.append('u'), (List<UUID>) lst, u -> u.toString());
		else if (first instanceof Number) // Number identification character is upper case.
			listAppend(key, builder.append(first.getClass().getSimpleName().charAt(0)), (List<Number>) lst, n -> n.toString());
		else {
			System.err.println("Unsupported list data of type " + first.getClass().getName());
			return null;
		}
		return builder.append('\n').toString();
	}

	private <T> String listAppend(String key, StringBuilder builder, List<T> lst, Function<T, String> modifier) {
		builder.append(key).append(':');
		final int size = lst.size() - 1;
		for (int lstI = 0; lstI <= size; lstI++) {
			builder.append(modifier.apply(lst.get(lstI)));
			if (lstI != size)
				builder.append(',');
		}
		// Result will be *?(key):(value), "*?" is appended on toWrite, with ? being the char of the list type.
		return builder.toString();
	}

	// *s(key):(value)
	private String listAppend(String key, StringBuilder builder, List<CharSequence> lst) {
		return listAppend(key, builder, lst, seq -> {
			final int len = seq.length();
			final StringBuilder seqBuilder = new StringBuilder(len);
			// ',' chars inside strings will be marked with a '\' to avoid breaking load logic.
			for (int seqI = 0; seqI < len; seqI++) {
				final char ch = seq.charAt(seqI);
				seqBuilder.append(ch == ',' ? "\\," : ch);
			}
			return seqBuilder.toString();
		});
	}

	/*
	 * Loading
	 */

	@Override
	public boolean load() {
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			while ((line = reader.readLine()) != null)
				parseLine(line);
			reader.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean parseLine(final String line) {
		int separatorIndex = line.indexOf(':');
		if (separatorIndex == -1)
			return false;
		final String key = line.substring(1, separatorIndex);
		final String value = line.substring(separatorIndex + 1);
		if (line.charAt(0) == '*')
			return loadLstFromLine(line.charAt(1), key.substring(1), value);
		else
			return loadObjFromLine(line.charAt(0), key, value);
	}

	// Loading - Simple objects //

	private boolean loadObjFromLine(final char type, final String key, final String value) {
		return switch (type) {
		case 's' -> set(key, value);
		case 'c' -> set(key, value.equals("\\n") ? '\n' : value.charAt(0));
		case 'b' -> set(key, value.charAt(0) == 't' ? true : false);
		case 'u' -> set(key, MCStrings.toUUID(value));
		case 'B' -> set(key, Byte.parseByte(value));
		case 'S' -> set(key, Short.parseShort(value));
		case 'I' -> set(key, Integer.parseInt(value));
		case 'L' -> set(key, Long.parseLong(value));
		case 'F' -> set(key, Float.parseFloat(value));
		case 'D' -> set(key, Double.parseDouble(value));
		default -> null;
		} != null;
	}

	// Loading - Lists //

	private boolean loadLstFromLine(final char type, final String key, final String value) {
		return switch (type) {
		case 's' -> loadList(key, value);
		case 'c' -> loadList(key, value, s -> s.equals("\\n") ? '\n' : s.charAt(0));
		case 'b' -> loadList(key, value, s -> s.charAt(0) == 't' ? true : false);
		case 'u' -> loadList(key, value, s -> MCStrings.toUUID(s));
		case 'B' -> loadList(key, value, s -> Byte.parseByte(s));
		case 'S' -> loadList(key, value, s -> Short.parseShort(s));
		case 'I' -> loadList(key, value, s -> Integer.parseInt(s));
		case 'L' -> loadList(key, value, s -> Long.parseLong(s));
		case 'F' -> loadList(key, value, s -> Float.parseFloat(s));
		case 'D' -> loadList(key, value, s -> Double.parseDouble(s));
		default -> false;
		};
	}

	private <T> boolean loadList(final String key, final String lstStr, Function<String, T> modifier) {
		final int len = lstStr.length();
		final LinkedList<T> result = new LinkedList<>();
		StringBuilder element = new StringBuilder();
		for (int i = 0; i < len; i++) {
			final char ch = lstStr.charAt(i);
			if (ch == ',') { // Element separator
				result.add(modifier.apply(element.toString()));
				element = new StringBuilder();
			} else
				element.append(ch);
		}
		result.add(modifier.apply(element.toString()));
		set(key, result);
		return true;
	}

	// Specific method for strings to handle the '\' character to avoid counting
	// Strings that contain commas as different strings.
	private boolean loadList(final String key, final String lstStr) {
		final int len = lstStr.length();
		final LinkedList<String> result = new LinkedList<>();
		StringBuilder element = new StringBuilder();
		for (int i = 0; i < len; i++) {
			final char ch = lstStr.charAt(i);
			if (ch == '\\' && i != len + 1 && lstStr.charAt(i + 1) == ',') {
				element.append(',');
				i++;
			} else if (ch == ',') {
				result.add(element.toString());
				element = new StringBuilder();
			} else
				element.append(ch);
		}
		result.add(element.toString());
		set(key, result);
		return true;
	}
}
