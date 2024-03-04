package net.codersky.mcutils.storage;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class StorageHandler {

	protected final HashMap<String, Object> keys = new HashMap<>();

	public abstract boolean save();

	public abstract boolean load();

	@SuppressWarnings("unchecked")
	public <T extends Object> T set(String key, T value) {
		if (value == null)
			keys.remove(key);
		else if (Iterable.class.isAssignableFrom(value.getClass())) {
			final LinkedList<T> lst = toLinkedList((Iterable<T>)value);
			if (lst != null && !lst.isEmpty())
				keys.put(key, lst);
			else
				keys.remove(key);
		} else
			keys.put(key, value);
		return value;
	}

	private <T extends Object> LinkedList<T> toLinkedList(@Nonnull Iterable<T> iterable) {
		if (iterable instanceof LinkedList)
			return (LinkedList<T>) iterable;
		final LinkedList<T> lst = new LinkedList<>();
		for (T element : iterable)
			lst.add(element);
		return lst;
	}

	public Set<String> getKeys() {
		return keys.keySet();
	}

	// Utility generic getter //

	@SuppressWarnings("unchecked")
	protected <T extends Object> T get(@Nonnull String key, @Nonnull Class<?> type, @Nullable T def) {
		final Object obj = keys.get(key);
		return (obj != null && obj.getClass().equals(type)) ? (T) obj : def;
	}

	// Strings //

	public String getString(String key, String def) {
		return get(key, String.class, def);
	}

	public String getString(String key) {
		return getString(key, null);
	}

	// Chars //
	
	public char getChar(String key, char def) {
		return get(key, Character.class, def);
	}
	
	public char getChar(String key) {
		return getChar(key, '\0');
	}

	// Booleans //
	
	public boolean getBoolean(String key, boolean def) {
		return get(key, Boolean.class, def);
	}
	
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	// UUID //
	
	public UUID getUUID(String key, UUID def) {
		return get(key, UUID.class, def);
	}
	
	public UUID getUUID(String key) {
		return getUUID(key, null);
	}

	// Bytes //

	public byte getByte(String key, byte def) {
		return get(key, Byte.class, def);
	}

	public byte getByte(String key) {
		return getByte(key, (byte) 0);
	}
	
	// Shorts //
	
	public short getShort(String key, short def) {
		return get(key, Short.class, def);
	}
	
	public short getShort(String key) {
		return getShort(key, (short) 0);
	}

	// Integers //
	
	public int getInt(String key, int def) {
		return get(key, Integer.class, def);
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}

	// Longs //

	public long getLong(String key, long def) {
		return get(key, Long.class, def);
	}

	public long getLong(String key) {
		return getLong(key, 0);
	}

	// Floats //

	public float getFloat(String key, float def) {
		return get(key, Float.class, def);
	}

	public float getFloat(String key) {
		return getFloat(key, 0.0f);
	}

	// Doubles //

	public double getDouble(String key, double def) {
		return get(key, Double.class, def);
	}

	public double getDouble(String key) {
		return getDouble(key, 0.0d);
	}

	// Lists //

	public <T> List<T> getList(String key, List<T> def) {
		return get(key, LinkedList.class, def);
	}

	public <T> List<T> getList(String key) {
		return getList(key, null);
	}

	/*
	 * Additional getters by conversion
	 */

	// Date //

	public Date getDate(String key, Date def) {
		long millis = getLong(key, -1);
		return millis == -1 ? def : new Date(millis);
	}

	public Date getDate(String key) {
		return getDate(key, null);
	}

	/*
	 * Object class
	 */

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		return ((StorageHandler)obj).keys.equals(this.keys);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(keys);
	}

	@Override
	public String toString() {
		return "StorageHandler" + keys.toString();
	}
}
