package net.codersky.mcutils.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.codersky.mcutils.java.math.MCNumbers;

/**
 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
 * class will be removed before 1.0.0 releases</b>
 */
@Deprecated(forRemoval = true)
public abstract class MCLists {

	/*
	 * asList methods
	 */

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nonnull
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <E> List<E> asList(E... elements) {
		if (elements == null)
			return new ArrayList<E>(0);
		final List<E> lst = new ArrayList<E>(elements.length);
		for (int i = 0; i < elements.length; i++)
			lst.add(elements[i]);
		return lst;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nonnull
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <E> List<E> asList(E[]... arrays) {
		if (arrays == null)
			return new ArrayList<E>(0);
		final List<E> lst = new ArrayList<E>();
		for (int i = 0; i < arrays.length; i++) {
			E[] elements = arrays[i];
			for (int j = 0; j < elements.length; j++)
				lst.add(elements[j]);
		}
		return lst;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nonnull
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <E> List<E> asList(@Nonnull Iterator<E>... iterators) {
		if (iterators == null)
			return new ArrayList<E>(0);
		final List<E> lst = new ArrayList<E>();
		for (Iterator<E> iterator : iterators)
			while (iterator.hasNext())
				lst.add(iterator.next());
		return lst;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nonnull
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <E> List<E> asList(@Nullable E[] array, @Nullable E... elements) {
		final List<E> result = new ArrayList<E>();
		if (array != null)
			for (int i = 0; i < array.length; i++)
				result.add(array[i]);
		if (elements != null)
			for (int i = 0; i < elements.length; i++)
				result.add(elements[i]);
		return result;
	}

	/*
	 * Addition
	 */

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nullable
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <E> List<E> add(@Nonnull List<E> list, @Nullable E... elements) {
		if (list == null)
			return null;
		List<E> result = new ArrayList<E>(list);
		if (elements != null)
			for (int i = 0; i < elements.length; i++)
				result.add(elements[i]);
		return result;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <E> List<E> add(@Nonnull List<E> list, @Nullable E[]... arrays) {
		if (list == null)
			return null;
		List<E> result = new ArrayList<E>(list);
		if (arrays != null)
			for (E[] array : arrays)
				for (int i = 0; i < array.length; i++)
					result.add(array[i]);
		return result;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nullable
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <E> List<E> add(@Nonnull List<E> list, @Nonnull Collection<E>... collections) {
		if (list == null)
			return null;
		List<E> result = new ArrayList<E>(list);
		if (collections != null)
			for (Collection<E> collection : collections)
				result.addAll(collection);
		return result;
	}

	/*
	 * Filtering
	 */

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <T> Collection<T> filter(Predicate<T> predicate, Collection<T> collections) {
		return collections.stream().filter(predicate).toList();
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <T> Set<T> filter(Predicate<T> predicate, Set<T> collections) {
		return collections.stream().filter(predicate).collect(Collectors.toSet());
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <T> List<T> filter(Predicate<T> predicate, List<T> lst) {
		return lst.stream().filter(predicate).toList();
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <T> List<T> filter(Predicate<T> predicate, T... elements) {
		return filter(predicate, asList(elements));
	}

	/*
	 * Mapping
	 */

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <R, T> List<R> map(@Nonnull Function<T, R> mapper, @Nonnull List<T>... lists) {
		List<R> result = new ArrayList<>();
		for (List<T> list : lists)
			for (T element : list)
				result.add(mapper.apply(element));
		return result;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <R, T> List<R> map(@Nonnull Function<T, R> mapper, @Nonnull Collection<T> collection) {
		List<R> result = new ArrayList<>();
		for (T element : collection)
			result.add(mapper.apply(element));
		return result;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <R, T> Set<R> map(@Nonnull Function<T, R> mapper, @Nonnull Set<T> set) {
		Set<R> result = new HashSet<>();
		for (T element : set)
			result.add(mapper.apply(element));
		return result;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <R, T> List<R> map(@Nonnull Function<T, R> mapper, @Nonnull T... elements) {
		return map(mapper, asList(elements));
	}

	/*
	 * Removing
	 */

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <T> List<T> remove(List<T> lst, List<T> elements) {
		if (elements == null)
			return lst;
		for (T element : elements)
			lst.remove(element);
		return lst;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@SafeVarargs
	@Deprecated(forRemoval = true)
	public static <T> List<T> remove(List<T> lst, T... elements) {
		return remove(lst, Arrays.asList(elements));
	}

	@Deprecated(forRemoval = true)
	public static <T> List<T> removeDuplicates(List<T> lst) {
		List<T> result = new ArrayList<>(lst.size());
		for (T element : lst)
			if (!result.contains(lst))
				result.add(element);
		return result;
	}

	/*
	 * Contains
	 */

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <E> boolean contains(@Nonnull Iterable<E> iterable, @Nonnull Predicate<E> condition) {
		for (E element : iterable)
			if (condition.test(element))
				return true;
		return false;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <E> boolean contains(@Nonnull E[] array, @Nonnull Predicate<E> condition) {
		for (E element : array)
			if (condition.test(element))
				return true;
		return false;
	}

	/*
	 * Getters
	 */

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nullable
	@Deprecated(forRemoval = true)
	public static <E> E get(@Nonnull Iterable<E> iterable, @Nonnull Predicate<E> condition, @Nullable E def) {
		for (E element : iterable)
			if (condition.test(element))
				return element;
		return def;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nullable
	@Deprecated(forRemoval = true)
	public static <E> E get(@Nonnull Iterable<E> iterable, @Nonnull Predicate<E> condition) {
		return get(iterable, condition, null);
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nullable
	@Deprecated(forRemoval = true)
	public static <E> E get(@Nonnull E[] array, @Nonnull Predicate<E> condition, @Nullable E def) {
		for (E element : array)
			if (condition.test(element))
				return element;
		return def;
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nullable
	@Deprecated(forRemoval = true)
	public static <E> E get(@Nonnull E[] array, @Nonnull Predicate<E> condition) {
		return get(array, condition, null);
	}

	/*
	 * Randoms
	 */

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Nullable
	@Deprecated(forRemoval = true)
	public static <E> E getRandomFrom(@Nonnull List<E> list) {
		final int size = list.size();
		if (size == 0)
			return null;
		return size == 1 ? list.get(0) : list.get(MCNumbers.random().nextInt(size));
	}

	/**
	 * @deprecated <b>Use {@link MCCollections}, a more flexible alternative. This
	 * class will be removed before 1.0.0 releases</b>
	 */
	@Deprecated(forRemoval = true)
	public static <E> E getRandomFrom(@Nonnull E[] array) {
		if (array.length == 0)
			return null;
		return array.length == 1 ? array[0] : array[MCNumbers.random().nextInt(array.length)];
	}
}
