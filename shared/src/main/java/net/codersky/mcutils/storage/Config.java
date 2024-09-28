package net.codersky.mcutils.storage;

import net.codersky.mcutils.Reloadable;
import net.codersky.mcutils.java.MCCollections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class Config implements Reloadable {

	protected final HashMap<String, Object> keys = new HashMap<>();

	/**
	 * Does any necessary tasks in order to set up this {@link Config}.
	 * Keep in mind that this does <b>NOT</b> load the config, this method
	 * is intended for tasks such as creating any necessary file, obviously
	 * depending on the {@link Config} type.
	 *
	 * @return {@code true} if the {@link Config} was set up correctly,
	 * {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean setup();

	/**
	 * Saves the cached data of this {@link Config} to
	 * the actual config. Some configs may take a long
	 * time to save, so it is recommended to call this
	 * method <b>asynchronously</b> in order to prevent
	 * possible performance issues.
	 *
	 * @return {@code true} if this {@link Config} was
	 * able to save correctly, {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean save();

	/**
	 * Loads the data stored on this config to the cache
	 * of this {@link Config} instance. Some configs may
	 * take a long time to load, so it is recommended to
	 * call this method <b>asynchronously</b> in order
	 * to prevent possible performance issues.
	 *
	 * @return {@code true} if this {@link Storage} was
	 * able to load correctly, {@code false} otherwise.
	 *
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean reload();

	/*
	 * Key access
	 */

	@NotNull
	public HashMap<String, Object> getMap() {
		return new HashMap<>(keys);
	}

	public Set<Map.Entry<String, Object>> getEntries() {
		return keys.entrySet();
	}

	public Set<Map.Entry<String, Object>> getEntries(@NotNull Predicate<String> filter) {
		return MCCollections.clone(getEntries(), entry -> filter.test(entry.getKey()));
	}

	@NotNull
	public Config removeEntries(@NotNull String... keys) {
		for (String key : keys)
			this.keys.remove(key);
		return this;
	}

	/**
	 * Gets the {@link Set} of keys that are currently
	 * cached on this {@link Storage}. This set
	 * supports element removal but not addition as
	 * explained on {@link HashMap#keySet()}. Elements
	 * removed from this {@link Set} will also be removed
	 * from the storage cache. This can be used, for example
	 * to {@link Set#clear() clear} the file.
	 *
	 * @return The {@link Set} of keys that are currently
	 * cached on this {@link Config}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #removeEntries(String...)
	 * @see #containsKey(String...)
	 */
	public Set<String> getKeys() {
		return keys.keySet();
	}

	@NotNull
	public Set<String> getKeys(@NotNull Predicate<String> filter) {
		return MCCollections.clone(keys.keySet(), filter);
	}

	public boolean containsKey(@NotNull String... keys) {
		for (String key : keys)
			if (!this.keys.containsKey(key))
				return false;
		return true;
	}

	@NotNull
	public Config clear() {
		keys.clear();
		return this;
	}

	/*
	 * Setters
	 */

	// - Utility - //

	@NotNull
	protected <T> T set(@NotNull String key, @NotNull T value) {
		keys.put(Objects.requireNonNull(key), Objects.requireNonNull(value));
		return value;
	}

	@NotNull
	protected <T> List<T> setList(@NotNull String key, @NotNull List<T> list) {
		Objects.requireNonNull(list);
		final ArrayList<T> lst = list instanceof ArrayList ? (ArrayList<T>) list : new ArrayList<>(list);
		keys.put(Objects.requireNonNull(key), lst);
		return list;
	}

	// - Strings - //

	@NotNull
	public String setString(@NotNull String key, @NotNull String value) {
		return set(key, value);
	}

	@NotNull
	public List<String> setStrings(@NotNull String key, @NotNull List<String> value) {
		return setList(key, value);
	}

	// - Characters - //

	public char setChar(@NotNull String key, char value) {
		return set(key, value);
	}

	@NotNull
	public List<Character> setChars(@NotNull String key, @NotNull List<Character> value) {
		return setList(key, value);
	}

	// - Booleans - //

	public boolean setBoolean(@NotNull String key, boolean value) {
		return set(key, value);
	}

	@NotNull
	public List<Boolean> setBooleans(@NotNull String key, @NotNull List<Boolean> value) {
		return setList(key, value);
	}

	// - Integers - //

	public int setInt(@NotNull String key, int value) {
		return set(key, value);
	}

	@NotNull
	public List<Integer> setInts(@NotNull String key, @NotNull List<Integer> value) {
		return setList(key, value);
	}

	// - Longs - //

	public long setLong(@NotNull String key, long value) {
		return set(key, value);
	}

	@NotNull
	public List<Long> setLongs(@NotNull String key, @NotNull List<Long> value) {
		return setList(key, value);
	}

	// - Floats - //

	public float setFloat(@NotNull String key, float value) {
		return set(key, value);
	}

	public List<Float> setFloats(@NotNull String key, @NotNull List<Float> value) {
		return setList(key, value);
	}

	// - Doubles - //

	public double setDouble(@NotNull String key, double value) {
		return set(key, value);
	}

	@NotNull
	public List<Double> setDoubles(@NotNull String key, @NotNull List<Double> value) {
		return setList(key, value);
	}

	// - UUIDs - //

	@NotNull
	public UUID setUUID(@NotNull String key, @NotNull UUID value) {
		return set(key, value);
	}

	@NotNull
	public List<UUID> setUUIDs(@NotNull String key, @NotNull List<UUID> value) {
		return setList(key, value);
	}

	/*
	 * Getters
	 */

	// - Utility - //

	@SuppressWarnings("unchecked")
	protected <T> T get(@NotNull String key, @NotNull Class<T> type) {
		final Object obj = keys.get(key);
		return (obj != null && obj.getClass().equals(type)) ? (T) obj : null;
	}

	@SuppressWarnings("unchecked")
	protected <T> List<T> getList(@NotNull String key, @NotNull Class<T> type) {
		final Object obj = keys.get(key);
		if (obj instanceof ArrayList<?> lst)
			return lst.getClass().getTypeParameters()[0].getClass().equals(type) ? (List<T>) lst : null;
		return null;
	}

	// - Strings - //

	/**
	 * Gets a {@link String} from this {@link Reloadable}.
	 *
	 * @param key the key to get the {@link String} from.
	 *
	 * @return The stored {@link String} or {@code null} if {@code key}
	 * didn't exist or contains a different type of value.
	 *
	 * @throws NullPointerException if {@code key} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getString(@NotNull String key) {
		return get(key, String.class);
	}

	@NotNull
	public String getString(@NotNull String key, @NotNull String def) {
		final String str = getString(key);
		return str == null ? def : str;
	}

	@Nullable
	public List<String> getStrings(@NotNull String key) {
		return getList(key, String.class);
	}

	@NotNull
	public List<String> getStrings(@NotNull String key, @NotNull List<String> def) {
		final List<String> lst = getStrings(key);
		return lst == null ? def : lst;
	}

	// - Characters - //

	@Nullable
	public Character getChar(@NotNull String key) {
		return get(key, Character.class);
	}

	public char getChar(@NotNull String key, char def) {
		final Character ch = getChar(key);
		return ch == null ? def : ch;
	}

	@Nullable
	public List<Character> getChars(@NotNull String key) {
		return getList(key, Character.class);
	}

	@NotNull
	public List<Character> getChars(@NotNull String key, @NotNull List<Character> def) {
		final List<Character> lst = getChars(key);
		return lst == null ? def : lst;
	}

	// - Booleans - //

	@Nullable
	public Boolean getBoolean(@NotNull String key) {
		return get(key, Boolean.class);
	}

	public boolean getBoolean(@NotNull String key, boolean def) {
		final Boolean bool = getBoolean(key);
		return bool == null ? def : bool;
	}

	@Nullable
	public List<Boolean> getBooleans(@NotNull String key) {
		return getList(key, Boolean.class);
	}

	@NotNull
	public List<Boolean> getBooleans(@NotNull String key, @NotNull List<Boolean> def) {
		final List<Boolean> lst = getBooleans(key);
		return lst == null ? def : lst;
	}

	// - Integers - //

	@Nullable
	public Integer getInt(@NotNull String key) {
		return get(key, Integer.class);
	}

	public int getInt(@NotNull String key, int def) {
		final Integer i = getInt(key);
		return i == null ? def : i;
	}

	@Nullable
	public List<Integer> getInts(@NotNull String key) {
		return getList(key, Integer.class);
	}

	@NotNull
	public List<Integer> getInts(@NotNull String key, @NotNull List<Integer> def) {
		final List<Integer> lst = getInts(key);
		return lst == null ? def : lst;
	}

	// - Longs - //

	@Nullable
	public Long getLong(@NotNull String key) {
		return get(key, Long.class);
	}

	public long getLong(@NotNull String key, long def) {
		final Long l = getLong(key);
		return l == null ? def : l;
	}

	@Nullable
	public List<Long> getLongs(@NotNull String key) {
		return getList(key, Long.class);
	}

	@NotNull
	public List<Long> getLongs(@NotNull String key, @NotNull List<Long> def) {
		final List<Long> lst = getLongs(key);
		return lst == null ? def : lst;
	}

	// - Floats - //

	@Nullable
	public Float getFloat(@NotNull String key) {
		return get(key, Float.class);
	}

	public float getFloat(@NotNull String key, float def) {
		final Float f = getFloat(key);
		return f == null ? def : f;
	}

	@Nullable
	public List<Float> getFloats(@NotNull String key) {
		return getList(key, Float.class);
	}

	@NotNull
	public List<Float> getFloats(@NotNull String key, @NotNull List<Float> def) {
		final List<Float> lst = getFloats(key);
		return lst == null ? def : lst;
	}

	// - Doubles - //

	@Nullable
	public Double getDouble(@NotNull String key) {
		return get(key, Double.class);
	}

	public double getDouble(@NotNull String key, double def) {
		final Double d = getDouble(key);
		return d == null ? def : d;
	}

	@Nullable
	public List<Double> getDoubles(@NotNull String key)  {
		return getList(key, Double.class);
	}

	@NotNull
	public List<Double> getDoubles(@NotNull String key, @NotNull List<Double> def) {
		final List<Double> lst = getDoubles(key);
		return lst == null ? def : lst;
	}

	// - UUID - //

	@Nullable
	public UUID getUUID(@NotNull String key) {
		return get(key, UUID.class);
	}

	@NotNull
	public UUID getUUID(@NotNull String key, @NotNull UUID def) {
		final UUID uuid = getUUID(key);
		return uuid == null ? def : uuid;
	}

	@Nullable
	public List<UUID> getUUIDs(@NotNull String key) {
		return getList(key, UUID.class);
	}

	@NotNull
	public List<UUID> getUUIDs(@NotNull String key, @NotNull List<UUID> def) {
		final List<UUID> lst = getUUIDs(key);
		return lst == null ? def : lst;
	}

	/*
	 * Object class
	 */

	@Override
	public boolean equals(@Nullable Object obj) {
		return obj instanceof final Config cfg && cfg.keys.equals(this.keys);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(keys);
	}

	@Override
	public String toString() {
		return "Config" + keys;
	}
}
