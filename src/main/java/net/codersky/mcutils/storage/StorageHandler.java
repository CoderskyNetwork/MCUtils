package net.codersky.mcutils.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.codersky.mcutils.java.MCLists;

public abstract class StorageHandler {

	protected final HashMap<String, Object> keys = new HashMap<>();

	public abstract boolean save();

	public abstract boolean load();

	@Nonnull
	public Set<String> getKeys() {
		return keys.keySet();
	}

	/*
	 * Setters
	 */

	// Utility //

	@Nullable
	protected <T extends Object> T set(@Nonnull String key, @Nullable T value) {
		if (value == null)
			keys.remove(Objects.requireNonNull(key));
		else
			keys.put(Objects.requireNonNull(key), value);
		return value;
	}

	@Nullable
	protected <T extends Object> List<T> setList(@Nonnull String key, @Nullable List<T> list) {
		if (list == null || list.isEmpty())
			keys.remove(Objects.requireNonNull(key));
		else {
			final ArrayList<T> lst = list instanceof ArrayList ? (ArrayList<T>) list : new ArrayList<T>(list);
			keys.put(Objects.requireNonNull(key), lst);
		}
		return list;
	}

	// Strings //

	@Nullable
	public String setString(@Nonnull String key, @Nullable String value) {
		return set(key, value);
	}

	@Nullable
	public List<String> setStrings(@Nonnull String key, @Nullable List<String> value) {
		return setList(key, value);
	}

	// Chars //

	@Nullable
	public Character setChar(@Nonnull String key, @Nullable Character value) {
		return set(key, value);
	}

	@Nullable
	public List<Character> setChars(@Nonnull String key, @Nullable List<Character> value) {
		return set(key, value);
	}

	// Booleans //

	@Nullable
	public Boolean setBoolean(@Nonnull String key, @Nullable Boolean value) {
		return set(key, value);
	}

	@Nullable
	public List<Boolean> setBooleans(@Nonnull String key, @Nullable List<Boolean> value) {
		return set(key, value);
	}

	// UUID //

	@Nullable
	public UUID setUUID(@Nonnull String key, @Nullable UUID value) {
		return set(key, value);
	}

	@Nullable
	public List<UUID> setUUIDs(@Nonnull String key, @Nullable List<UUID> value) {
		return set(key, value);
	}

	// Bytes //

	@Nullable
	public Byte setByte(@Nonnull String key, @Nullable Byte value) {
		return set(key, value);
	}

	@Nullable
	public List<Byte> setBytes(@Nonnull String key, List<Byte> value) {
		return set(key, value);
	}

	// Shorts //

	@Nullable
	public Short setShort(@Nonnull String key, @Nullable Short value) {
		return set(key, value);
	}

	@Nullable
	public List<Short> setShorts(@Nonnull String key, @Nullable List<Short> value) {
		return set(key, value);
	}

	// Integers //

	@Nullable
	public Integer setInt(@Nonnull String key, @Nullable Integer value) {
		return set(key, value);
	}

	@Nullable
	public List<Integer> setInts(@Nonnull String key, @Nullable List<Integer> value) {
		return set(key, value);
	}

	// Longs //

	@Nullable
	public Long setLong(@Nonnull String key, @Nullable Long value) {
		return set(key, value);
	}

	@Nullable
	public List<Long> setLongs(@Nonnull String key, @Nullable List<Long> value) {
		return set(key, value);
	}

	// Floats //

	@Nullable
	public Float setFloat(@Nonnull String key, @Nullable Float value) {
		return set(key, value);
	}

	@Nullable
	public List<Float> setFloats(@Nonnull String key, @Nullable List<Float> value) {
		return set(key, value);
	}

	// Doubles //

	@Nullable
	public Double setDouble(@Nonnull String key, @Nullable Double value) {
		return set(key, value);
	}

	@Nullable
	public List<Double> setDoubles(@Nonnull String key, @Nullable List<Double> value) {
		return setList(key, value);
	}

	/*
	 * Setters by conversion
	 */
	
	// Date //

	@Nullable
	public Date setDate(@Nonnull String key, @Nullable Date value) {
		set(key, value == null ? null : value.toInstant().toEpochMilli());
		return value;
	}

	@Nullable
	public List<Date> setDates(@Nonnull String key, @Nullable List<Date> value) {
		setList(key, value == null ? null : MCLists.map(date -> date.toInstant().toEpochMilli(), value));
		return value;
	}

	/*
	 * Getters
	 */

	// Utility //

	@SuppressWarnings("unchecked")
	protected <T extends Object> T get(@Nonnull String key, @Nonnull Class<?> type, @Nullable T def) {
		final Object obj = keys.get(key);
		return (obj != null && obj.getClass().equals(type)) ? (T) obj : def;
	}

	protected <T> List<T> getList(@Nonnull String key, @Nullable List<T> def) {
		return get(key, ArrayList.class, def);
	}

	// Strings //

	@Nullable
	public String getString(@Nonnull String key, @Nullable String def) {
		return get(key, String.class, def);
	}

	@Nullable
	public String getString(@Nonnull String key) {
		return getString(key, null);
	}

	@Nullable
	public List<String> getStrings(@Nonnull String key, @Nullable List<String> def) {
		return getList(key, def);
	}

	@Nullable
	public List<String> getStrings(@Nonnull String key) {
		return getStrings(key, null);
	}

	// Chars //

	@Nullable
	public Character getChar(String key, @Nullable Character def) {
		return get(key, Character.class, def);
	}

	@Nullable
	public Character getChar(String key) {
		return getChar(key, null);
	}

	@Nullable
	public List<Character> getChars(String key, @Nullable List<Character> def) {
		return getList(key, def);
	}

	@Nullable
	public List<Character> getChars(String key) {
		return getChars(key, null);
	}

	// Booleans //

	@Nullable
	public Boolean getBoolean(String key, @Nullable Boolean def) {
		return get(key, Boolean.class, def);
	}

	@Nullable
	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	@Nullable
	public List<Boolean> getBooleans(String key, @Nullable List<Boolean> def) {
		return getList(key, def);
	}

	@Nullable
	public List<Boolean> getBooleans(String key) {
		return getBooleans(key, null);
	}

	// UUID //

	@Nullable
	public UUID getUUID(@Nonnull String key, @Nullable UUID def) {
		return get(key, UUID.class, def);
	}

	@Nullable
	public UUID getUUID(@Nonnull String key) {
		return getUUID(key, null);
	}

	@Nullable
	public List<UUID> getUUIDs(@Nonnull String key, @Nullable List<UUID> def) {
		return getList(key, def);
	}

	@Nullable
	public List<UUID> getUUIDs(@Nonnull String key) {
		return getUUIDs(key, null);
	}

	// Bytes //

	@Nullable
	public Byte getByte(@Nonnull String key, @Nullable Byte def) {
		return get(key, Byte.class, def);
	}

	@Nullable
	public Byte getByte(@Nonnull String key) {
		return getByte(key, null);
	}

	@Nullable
	public List<Byte> getBytes(@Nonnull String key, @Nullable List<Byte> def) {
		return getList(key, def);
	}

	@Nullable
	public List<Byte> getBytes(@Nonnull String key) {
		return getBytes(key, null);
	}

	// Shorts //

	@Nullable
	public Short getShort(@Nonnull String key, @Nullable Short def) {
		return get(key, Short.class, def);
	}

	@Nullable
	public Short getShort(@Nonnull String key) {
		return getShort(key, null);
	}

	@Nullable
	public List<Short> getShorts(@Nonnull String key, @Nullable List<Short> def) {
		return getList(key, def);
	}

	@Nullable
	public List<Short> getShorts(@Nonnull String key) {
		return getShorts(key, null);
	}

	// Integers //

	@Nullable
	public Integer getInt(@Nonnull String key, @Nullable Integer def) {
		return get(key, Integer.class, def);
	}

	@Nullable
	public Integer getInt(@Nonnull String key) {
		return getInt(key, null);
	}

	@Nullable
	public List<Integer> getInts(@Nonnull String key, @Nullable List<Integer> def) {
		return getList(key, def);
	}

	@Nullable
	public List<Integer> getInts(@Nonnull String key) {
		return getInts(key, null);
	}

	// Longs //

	@Nullable
	public Long getLong(@Nonnull String key, @Nullable Long def) {
		return get(key, Long.class, def);
	}

	@Nullable
	public Long getLong(@Nonnull String key) {
		return getLong(key, null);
	}

	@Nullable
	public List<Long> getLongs(@Nonnull String key, @Nullable List<Long> def) {
		return getList(key, def);
	}

	@Nullable
	public List<Long> getLongs(@Nonnull String key) {
		return getLongs(key, null);
	}

	// Floats //

	@Nullable
	public Float getFloat(@Nonnull String key, @Nullable Float def) {
		return get(key, Float.class, def);
	}

	@Nullable
	public Float getFloat(@Nonnull String key) {
		return getFloat(key, null);
	}

	@Nullable
	public List<Float> getFloats(@Nonnull String key, @Nullable List<Float> def) {
		return getList(key, def);
	}

	@Nullable
	public List<Float> getFloats(@Nonnull String key) {
		return getFloats(key, null);
	}

	// Doubles //

	@Nullable
	public Double getDouble(@Nonnull String key, @Nullable Double def) {
		return get(key, Double.class, def);
	}

	@Nullable
	public Double getDouble(@Nonnull String key) {
		return getDouble(key, null);
	}

	@Nullable
	public List<Double> getDoubles(@Nonnull String key, @Nullable List<Double> def) {
		return getList(key, def);
	}

	@Nullable
	public List<Double> getDoubles(@Nonnull String key) {
		return getDoubles(key, null);
	}

	/*
	 * Getters by conversion
	 */

	// Date //

	@Nullable
	public Date getDate(@Nonnull String key, @Nullable Date def) {
		final Long millis = getLong(key);
		return millis == null ? def : new Date(millis);
	}

	@Nullable
	public Date getDate(@Nonnull String key) {
		return getDate(key, null);
	}

	@Nullable
	public List<Date> getDates(@Nonnull String key, @Nullable List<Date> def) {
		final List<Long> longs = getLongs(key);
		return longs == null ? def : MCLists.map(millis -> new Date(millis), longs);
	}

	@Nullable
	public List<Date> getDates(@Nonnull String key) {
		return getDates(key, null);
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
