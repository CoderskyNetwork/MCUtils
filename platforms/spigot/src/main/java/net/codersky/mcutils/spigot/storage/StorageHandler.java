package net.codersky.mcutils.spigot.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.primitives.Bytes;

import net.codersky.mcutils.spigot.java.MCLists;

/**
 * Abstract class for any type of storage that
 * can be {@link #load() loaded} and {@link #save() saved}.
 * This class also provides methods to get and set most
 * data types, which include:
 * <ul>
 * <li>{@link String Strings}</li>
 * <li>{@link Character Characters}</li>
 * <li>{@link Boolean Booleans}</li>
 * <li>{@link UUID UUIDs}</li>
 * <li>{@link Byte Bytes}</li>
 * <li>{@link Short Shorts}</li>
 * <li>{@link Integer Integers}</li>
 * <li>{@link Long Longs}</li>
 * <li>{@link Float Float}</li>
 * <li>{@link Double Doubles}</li>
 * </ul>
 * Any class extending this one is only required to be
 * able to store the types shown on the list above, as
 * single objects and as lists, as there are methods for
 * both cases, for example {@link #getString(String)}
 * and {@link #getStrings(String)} respectively.
 * <p>
 * Other types that are also supported by this class such as {@link Date}
 * are considered "conversion types", meaning that for example
 * {@link #getDate(String)} instead of getting a {@link Date} object
 * from the {@link #getKeys() keys}, gets a {@link Long}, then converts
 * it to {@link Date}.
 * 
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 * 
 * @see FlatStorage
 */
public abstract class StorageHandler {

	protected final HashMap<String, Object> keys = new HashMap<>();

	/**
	 * Saves the cached data of this {@link StorageHandler} to
	 * the actual storage. Some storages may take a long time
	 * to save, so this method is recommended to be called
	 * <b>asynchronously</b> to prevent performance issues.
	 * 
	 * @return {@code true} if this {@link StorageHandler} was
	 * able to save correctly, {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean save();

	/**
	 * Loads the data stored on this storage to the cache
	 * of {@link StorageHandler}. Some storages may take a long time
	 * to load, so this method is recommended to be called
	 * <b>asynchronously</b> to prevent performance issues.
	 * 
	 * @return {@code true} if this {@link StorageHandler} was
	 * able to load correctly, {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public abstract boolean load();

	/*
	 * Key access
	 */

	/**
	 * Gets the {@link Set} of keys that are currently
	 * cached on this {@link StorageHandler}. This set
	 * supports element removal but not addition as
	 * explained on {@link HashMap#keySet()}. Elements
	 * removed from this {@link Set} will also be removed
	 * from the storage cache. This can be used, for example
	 * to {@link Set#clear() clear} the file.
	 * 
	 * @return The {@link Set} of keys that are currently
	 * cached on this {@link StorageHandler}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #removeKey(String)
	 * @see #containsKey(String)
	 */
	@Nonnull
	public Set<String> getKeys() {
		return keys.keySet();
	}

	/**
	 * Checks if this storage contains the specified {@code key}.
	 * 
	 * @param key the key to check for.
	 * 
	 * @return {@code true} if the {@code key} was found,
	 * {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #containsKeys(String...)
	 * @see #getKeys()
	 * @see #removeKey(String)
	 */
	public boolean containsKey(@Nonnull String key) {
		return keys.containsKey(Objects.requireNonNull(key));
	}

	/**
	 * Checks if this storage contains <b>all</b> the specified {@code keys}.
	 * 
	 * @param keys the keys to check for.
	 * 
	 * @return {@code true} if all the {@code keys} were found,
	 * {@code false} otherwise.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #containsKey(String)
	 * @see #getKeys()
	 * @see #removeKey(String)
	 */
	public boolean containsKeys(@Nonnull String... keys) {
		for (String key : keys)
			if (!this.keys.containsKey(key))
				return false;
		return true;
	}

	/**
	 * Checks if this storage contains the specified
	 * <b>key</b>, then removes it.
	 * 
	 * @param key the key to remove.
	 * 
	 * @return {@code true} if the <b>key</b> was found
	 * and hence could be removed, {@code false} otherwise.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getKeys()
	 * @see #containsKey(String)
	 */
	public boolean removeKey(@Nonnull String key) {
		if (containsKey(key))
			return false;
		keys.remove(key);
		return true;
	}

	/**
	 * Clears this {@link StorageHandler}, removing any <b>cached</b> content
	 * from it, meaning that the data could still be recovered with {@link #load()}
	 * if {@link #save()} isn't called first.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public void clear() {
		keys.clear();
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

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link String} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link String} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public String setString(@Nonnull String key, @Nullable String value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link String Strings}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link String} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<String> setStrings(@Nonnull String key, @Nullable List<String> value) {
		return setList(key, value);
	}

	// Chars //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Character} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Character} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Character setChar(@Nonnull String key, @Nullable Character value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Character Characters}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Character} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<Character> setChars(@Nonnull String key, @Nullable List<Character> value) {
		return setList(key, value);
	}

	// Booleans //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Boolean} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Boolean} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Boolean setBoolean(@Nonnull String key, @Nullable Boolean value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Boolean Booleans}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Boolean} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<Boolean> setBooleans(@Nonnull String key, @Nullable List<Boolean> value) {
		return setList(key, value);
	}

	// UUID //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link UUID} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link UUID} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public UUID setUUID(@Nonnull String key, @Nullable UUID value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link UUID UUIDs}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link UUID} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<UUID> setUUIDs(@Nonnull String key, @Nullable List<UUID> value) {
		return setList(key, value);
	}

	// Bytes //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Byte} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Byte} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Byte setByte(@Nonnull String key, @Nullable Byte value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Byte Bytes}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Byte} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<Byte> setBytes(@Nonnull String key, List<Byte> value) {
		return setList(key, value);
	}

	@Nullable
	public List<Byte> setBytes(@Nonnull String key, byte[] value) {
		return setList(key, Bytes.asList(value));
	}

	// Shorts //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Short} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Short} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Short setShort(@Nonnull String key, @Nullable Short value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Short Shorts}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Short} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<Short> setShorts(@Nonnull String key, @Nullable List<Short> value) {
		return setList(key, value);
	}

	// Integers //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Integer} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Integer} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Integer setInt(@Nonnull String key, @Nullable Integer value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Integer Integers}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Integer} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<Integer> setInts(@Nonnull String key, @Nullable List<Integer> value) {
		return setList(key, value);
	}

	// Longs //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Long} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Long} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Long setLong(@Nonnull String key, @Nullable Long value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Long Longs}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Long} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<Long> setLongs(@Nonnull String key, @Nullable List<Long> value) {
		return setList(key, value);
	}

	// Floats //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Float} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Float} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Float setFloat(@Nonnull String key, @Nullable Float value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Float Floats}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Float} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<Float> setFloats(@Nonnull String key, @Nullable List<Float> value) {
		return setList(key, value);
	}

	// Doubles //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Float} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Double} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Double setDouble(@Nonnull String key, @Nullable Double value) {
		return set(key, value);
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Double Doubles}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Double} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public List<Double> setDoubles(@Nonnull String key, @Nullable List<Double> value) {
		return setList(key, value);
	}

	/*
	 * Setters by conversion
	 */
	
	// Date //

	/**
	 * Sets the value of the specified <b>key</b> to the provided
	 * {@link Date} <b>value</b>. If the <b>value</b> is {@code null},
	 * the <b>key</b> will be directly removed. Keys that already contain
	 * a value of a different type will be overridden, types are not checked.
	 * <p>
	 * <b>Note</b>: This is a <b>conversion method</b>, meaning that the data
	 * that this method stores doesn't correspond to the class used on it.
	 * For this method, {@link Long} is used as {@link Date Dates} are stored
	 * in epoch milliseconds.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Date} value to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
	@Nullable
	public Date setDate(@Nonnull String key, @Nullable Date value) {
		set(key, value == null ? null : value.toInstant().toEpochMilli());
		return value;
	}

	/**
	 * Sets the value of the specified <b>key</b> to the provided {@link List} of
	 * {@link Date Dates}. If the <b>value</b> is {@code null}
	 * or {@link List#isEmpty() empty}, the <b>key</b> will be directly removed.
	 * Keys that already contain a value of a different type will be overridden,
	 * types are not checked.
	 * <p>
	 * <b>Note</b>: This is a <b>conversion method</b>, meaning that the data
	 * that this method stores doesn't correspond to the class used on it.
	 * For this method, {@link Long} is used as {@link Date Dates} are stored
	 * in epoch milliseconds.
	 * 
	 * @param key the key on which the <b>value</b> will be stored.
	 * @param value the {@link Date} {@link List} to store on the <b>key</b>,
	 * which may be {@code null}.
	 * 
	 * @return The provided <b>value</b> {@code null} if <b>value</b>
	 * was {@code null}.
	 */
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

	/**
	 * Gets a {@link String} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link String} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link String} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getString(@Nonnull String key, @Nullable String def) {
		return get(key, String.class, def);
	}

	/**
	 * Gets a {@link String} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link String} from.
	 * 
	 * @return The stored {@link String} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public String getString(@Nonnull String key) {
		return getString(key, null);
	}

	/**
	 * Gets a {@link List} of {@link String Strings} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link String Strings} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link String Strings}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> getStrings(@Nonnull String key, @Nullable List<String> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link String Strings} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link String Strings} from.
	 * 
	 * @return The stored {@link List} of {@link String Strings}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<String> getStrings(@Nonnull String key) {
		return getStrings(key, null);
	}

	// Chars //

	/**
	 * Gets a {@link Character} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Character} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Character} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Character getChar(String key, @Nullable Character def) {
		return get(key, Character.class, def);
	}

	/**
	 * Gets a {@link Character} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Character} from.
	 * 
	 * @return The stored {@link Character} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Character getChar(String key) {
		return getChar(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Character Characters} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Character Characters} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Character Characters}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Character> getChars(String key, @Nullable List<Character> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link Character Characters} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Character Characters} from.
	 * 
	 * @return The stored {@link List} of {@link Character Characters}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Character> getChars(String key) {
		return getChars(key, null);
	}

	// Booleans //

	/**
	 * Gets a {@link Boolean} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Boolean} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Boolean} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Boolean getBoolean(String key, @Nullable Boolean def) {
		return get(key, Boolean.class, def);
	}

	/**
	 * Gets a {@link Boolean} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Boolean} from.
	 * 
	 * @return The stored {@link Boolean} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Boolean Booleans} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Boolean Booleans} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Boolean Booleans}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Boolean> getBooleans(String key, @Nullable List<Boolean> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link Boolean Booleans} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Boolean Booleans} from.
	 * 
	 * @return The stored {@link List} of {@link Boolean Booleans}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Boolean> getBooleans(String key) {
		return getBooleans(key, null);
	}

	// UUID //

	/**
	 * Gets a {@link UUID} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link UUID} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link UUID} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public UUID getUUID(@Nonnull String key, @Nullable UUID def) {
		return get(key, UUID.class, def);
	}

	/**
	 * Gets a {@link UUID} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link UUID} from.
	 * 
	 * @return The stored {@link UUID} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public UUID getUUID(@Nonnull String key) {
		return getUUID(key, null);
	}

	/**
	 * Gets a {@link List} of {@link UUID UUIDs} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link UUID UUIDs} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link UUID UUIDs}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<UUID> getUUIDs(@Nonnull String key, @Nullable List<UUID> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link UUID UUIDs} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link UUID UUIDs} from.
	 * 
	 * @return The stored {@link List} of {@link UUID UUIDs}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<UUID> getUUIDs(@Nonnull String key) {
		return getUUIDs(key, null);
	}

	// Bytes //

	/**
	 * Gets a {@link Byte} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Byte} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Byte} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Byte getByte(@Nonnull String key, @Nullable Byte def) {
		return get(key, Byte.class, def);
	}

	/**
	 * Gets a {@link Byte} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Byte} from.
	 * 
	 * @return The stored {@link Byte} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Byte getByte(@Nonnull String key) {
		return getByte(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Byte Bytes} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Byte Bytes} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Byte Bytes}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Byte> getBytes(@Nonnull String key, @Nullable List<Byte> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link Byte Bytes} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Byte Bytes} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Byte Bytes}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Byte> getBytes(@Nonnull String key) {
		return getBytes(key, null);
	}

	// Shorts //

	/**
	 * Gets a {@link Short} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Short} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Short} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Short getShort(@Nonnull String key, @Nullable Short def) {
		return get(key, Short.class, def);
	}

	/**
	 * Gets a {@link Short} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Short} from.
	 * 
	 * @return The stored {@link Short} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Short getShort(@Nonnull String key) {
		return getShort(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Short Shorts} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Short Shorts} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Short Shorts}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Short> getShorts(@Nonnull String key, @Nullable List<Short> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link Short Shorts} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Short Shorts} from.
	 * 
	 * @return The stored {@link List} of {@link Short Shorts}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Short> getShorts(@Nonnull String key) {
		return getShorts(key, null);
	}

	// Integers //

	/**
	 * Gets an {@link Integer} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Integer} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Integer} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Integer getInt(@Nonnull String key, @Nullable Integer def) {
		return get(key, Integer.class, def);
	}

	/**
	 * Gets an {@link Integer} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Integer} from.
	 * 
	 * @return The stored {@link Integer} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Integer getInt(@Nonnull String key) {
		return getInt(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Integer Integers} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Integer Integers} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Integer Integers}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Integer> getInts(@Nonnull String key, @Nullable List<Integer> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link Integer Integers} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Integer Integers} from.
	 * 
	 * @return The stored {@link List} of {@link Integer Integers}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Integer> getInts(@Nonnull String key) {
		return getInts(key, null);
	}

	// Longs //

	/**
	 * Gets a {@link Long} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Long} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Long} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Long getLong(@Nonnull String key, @Nullable Long def) {
		return get(key, Long.class, def);
	}

	/**
	 * Gets a {@link Long} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Long} from.
	 * 
	 * @return The stored {@link Long} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Long getLong(@Nonnull String key) {
		return getLong(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Long Longs} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Long Longs} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Long Longs}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Long> getLongs(@Nonnull String key, @Nullable List<Long> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link Long Longs} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Long Longs} from.
	 * 
	 * @return The stored {@link List} of {@link Long Longs}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Long> getLongs(@Nonnull String key) {
		return getLongs(key, null);
	}

	// Floats //

	/**
	 * Gets a {@link Float} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Float} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Float} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Float getFloat(@Nonnull String key, @Nullable Float def) {
		return get(key, Float.class, def);
	}

	/**
	 * Gets a {@link Float} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Float} from.
	 * 
	 * @return The stored {@link Float} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Float getFloat(@Nonnull String key) {
		return getFloat(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Float Floats} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Float Floats} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Float Floats}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Float> getFloats(@Nonnull String key, @Nullable List<Float> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link Float Floats} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Float Floats} from.
	 * 
	 * @return The stored {@link List} of {@link Float Floats}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Float> getFloats(@Nonnull String key) {
		return getFloats(key, null);
	}

	// Doubles //

	/**
	 * Gets a {@link Double} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Double} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Double} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Double getDouble(@Nonnull String key, @Nullable Double def) {
		return get(key, Double.class, def);
	}

	/**
	 * Gets a {@link Double} from this {@link StorageHandler}.
	 * 
	 * @param key the key to get the {@link Double} from.
	 * 
	 * @return The stored {@link Double} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Double getDouble(@Nonnull String key) {
		return getDouble(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Double Doubles} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Double Doubles} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Double Doubles}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Double> getDoubles(@Nonnull String key, @Nullable List<Double> def) {
		return getList(key, def);
	}

	/**
	 * Gets a {@link List} of {@link Double Doubles} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * 
	 * @param key the key to get the{@link List} of {@link Double Doubles} from.
	 * 
	 * @return The stored {@link List} of {@link Double Doubles}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Double> getDoubles(@Nonnull String key) {
		return getDoubles(key, null);
	}

	/*
	 * Getters by conversion
	 */

	// Date //

	/**
	 * Gets a {@link Date} from this {@link StorageHandler}.
	 * <p>
	 * <b>Note</b>: This is a <b>conversion method</b>, meaning that the data
	 * that this method stores doesn't correspond to the class used on it.
	 * For this method, {@link Long} is used as {@link Date Dates} are stored
	 * in epoch milliseconds.
	 * 
	 * @param key the key to get the {@link Date} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link Date} or <b>def</b> if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Date getDate(@Nonnull String key, @Nullable Date def) {
		final Long millis = getLong(key);
		return millis == null ? def : new Date(millis);
	}

	/**
	 * Gets a {@link Date} from this {@link StorageHandler}.
	 * <p>
	 * <b>Note</b>: This is a <b>conversion method</b>, meaning that the data
	 * that this method stores doesn't correspond to the class used on it.
	 * For this method, {@link Long} is used as {@link Date Dates} are stored
	 * in epoch milliseconds.
	 * 
	 * @param key the key to get the {@link Date} from.
	 * 
	 * @return The stored {@link Date} or {@code null} if <b>key</b>
	 * didn't exist or contains a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public Date getDate(@Nonnull String key) {
		return getDate(key, null);
	}

	/**
	 * Gets a {@link List} of {@link Date Dates} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * <p>
	 * <b>Note</b>: This is a <b>conversion method</b>, meaning that the data
	 * that this method stores doesn't correspond to the class used on it.
	 * For this method, {@link Long} is used as {@link Date Dates} are stored
	 * in epoch milliseconds.
	 * 
	 * @param key the key to get the{@link List} of {@link Date Dates} from.
	 * @param def the default value to return if the <b>key</b>
	 * doesn't exist or contains a different type of value.
	 * 
	 * @return The stored {@link List} of {@link Date Dates}
	 * or <b>def</b> if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	public List<Date> getDates(@Nonnull String key, @Nullable List<Date> def) {
		final List<Long> longs = getLongs(key);
		return longs == null ? def : MCLists.map(millis -> new Date(millis), longs);
	}

	/**
	 * Gets a {@link List} of {@link Date Dates} from this {@link StorageHandler}.
	 * Note that this method <b>doesn't</b> return a copy of the
	 * stored {@link List}, meaning that any modification done to it will be
	 * reflected on the storage too. If you want to modify the list
	 * freely, you can safely use the {@link ArrayList#ArrayList(Collection)}
	 * constructor, of course checking for {@code null} first.
	 * <p>
	 * <b>Note</b>: This is a <b>conversion method</b>, meaning that the data
	 * that this method stores doesn't correspond to the class used on it.
	 * For this method, {@link Long} is used as {@link Date Dates} are stored
	 * in epoch milliseconds.
	 * 
	 * @param key the key to get the{@link List} of {@link Date Dates} from.
	 * 
	 * @return The stored {@link List} of {@link Date Dates}
	 * or {@code null} if <b>key</b> didn't exist or contains
	 * a different type of value.
	 * 
	 * @throws NullPointerException if <b>key</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
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
