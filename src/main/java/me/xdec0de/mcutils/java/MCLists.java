package me.xdec0de.mcutils.java;

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

/**
 * A class with utilities for lists, we
 * recommend using this class with {@link Arrays}
 * and {@link Lists}, it is unlikely that we will
 * ever add methods that already exist on those classes,
 * unless we want to expand a method such as {@link #asList(Object...)},
 * which is present on {@link Arrays} but offers more parameters
 * on this class.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public abstract class MCLists {

	/*
	 * asList methods
	 */

	/**
	 * Creates a modifiable list of any number of <b>elements</b>.
	 * If <b>elements</b> is null, an empty list will be returned.
	 * The capacity of the returned list will be the length of <b>elements</b>.
	 * 
	 * @param <E> the type of elements of the new {@link List}.
	 * @param elements the elements of the new {@link List}.
	 * 
	 * @return A never null, modifiable list with the specified <b>elements</b>,
	 * an empty list if <b>elements</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@SafeVarargs
	public static <E> List<E> asList(E... elements) {
		if (elements == null)
			return new ArrayList<E>(0);
		final List<E> lst = new ArrayList<E>(elements.length);
		for (int i = 0; i < elements.length; i++)
			lst.add(elements[i]);
		return lst;
	}

	/**
	 * Creates a modifiable list from any number of <b>arrays</b>.
	 * If <b>arrays</b> is null, an empty list will be returned.
	 * Note that this method doesn't create a list of arrays but
	 * a list of the <b>elements of all arrays</b>.
	 * 
	 * @param <E> the type of elements of the new {@link List}.
	 * @param arrays the arrays that will be added to the new {@link List}.
	 * 
	 * @return A never null, modifiable list with the specified <b>elements</b>,
	 * an empty list if <b>elements</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@SafeVarargs
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
	 * Creates a modifiable list of any number of <b>iterators</b>.
	 * 
	 * @param <E> the type of elements of the {@link Iterator}.
	 * @param iterators the {@link Iterator iterators} to use.
	 * 
	 * @return Any number of <b>iterators</b> as a {@link ArrayList list}.
	 * 
	 * @throws NullPointerException if any {@link Iterator} from <b>iterators</b> is null.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@SafeVarargs
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
	 * Creates a modifiable list from the specified <b>array</b>,
	 * then, adds any additional <b>elements</b> to it.
	 * 
	 * @param <E> the type of elements of the new {@link List}.
	 * 
	 * @param array the base array to use for the list, if null, only
	 * <b>elements</b> will be added to the list, although we recommend
	 * just using {@link #asList(Object...)} in that case.
	 * @param elements any other elements to add to the list, if null,
	 * the list will only contain the elements of <b>arr</b>.
	 * 
	 * @return A list with all the elements of the specified <b>array</b>
	 * and any other <b>elements</b> added to it, empty if both <b>array</b>
	 * and <b>elements</b> are null or empty.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	@SafeVarargs
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
	 * Adds all the specified <b>elements</b> to the specified <b>list</b>.
	 * 
	 * @param <E> the type of elements of <b>list</b>.
	 * @param list the base list to use.
	 * @param elements the elements to add to the new list.
	 * 
	 * @return A <b>new</b> modifiable {@link ArrayList list} with the specified
	 * <b>elements</b> added to it, if <b>list</b> is null, null will be returned.
	 * 
	 * @throws UnsupportedOperationException if the add operation is not supported by <b>list</b>
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	@SafeVarargs
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
	 * Adds all the elements of the specified <b>arrays</b> to the specified <b>list</b>.
	 * 
	 * @param <E> the type of elements of <b>list</b>.
	 * @param list the base list to use.
	 * @param arrays the arrays to get the elements to add to the new list.
	 * 
	 * @return A <b>new</b> modifiable {@link ArrayList list} with the elements of all
	 * <b>arrays</b> added to it, if <b>list</b> is null, null will be returned.
	 * 
	 * @throws UnsupportedOperationException if the add operation is not supported by <b>list</b>
	 * 
	 * @since MCUtils 1.0.0
	 */
	@SafeVarargs
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
	 * Adds all the elements of the specified <b>collections</b> to the
	 * specified <b>list</b>. If you only want to add one {@link Collection},
	 * then {@link List#addAll(Collection)} is recommended over this method.
	 * 
	 * @param <E> the type of elements of <b>list</b>.
	 * @param list the base list to use.
	 * @param collections the collections to get the elements to add to the new list.
	 * 
	 * @return A <b>new</b> modifiable {@link ArrayList list} with the elements of all
	 * <b>collections</b> added to it, if <b>list</b> is null, null will be returned.
	 * 
	 * @throws UnsupportedOperationException if the add operation is not supported by <b>list</b>
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nullable
	@SafeVarargs
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

	public static <T> Collection<T> filter(Predicate<T> predicate, Collection<T> collections) {
		return collections.stream().filter(predicate).toList();
	}

	public static <T> Set<T> filter(Predicate<T> predicate, Set<T> collections) {
		return collections.stream().filter(predicate).collect(Collectors.toSet());
	}

	public static <T> List<T> filter(Predicate<T> predicate, List<T> lst) {
		return lst.stream().filter(predicate).toList();
	}

	@SafeVarargs
	public static <T> List<T> filter(Predicate<T> predicate, T... elements) {
		return filter(predicate, asList(elements));
	}

	/*
	 * Mapping
	 */

	/**
	 * This method doesn't use {@link Stream Streams} as you may expect but
	 * instead iterates over all <b>lists</b> and applies <b>function</b> to every
	 * element of them, adding the results to a new {@link ArrayList list}, returning
	 * that new modifiable {@link ArrayList list}.
	 * 
	 * @param <T> the type of the input to the mapper {@link Function}.
	 * @param <R> the type of the result of the mapper {@link Function}.
	 * @param mapper the {@link Function} used to map the elements of all <b>lists</b>.
	 * @param lists the lists to be mapped for the new list.
	 * 
	 * @return
	 * 
	 * @throws NullPointerException If <b>mapper</b> or <b>lists</b> are null. Also this exception
	 * may be thrown if <b>mapper</b> doesn't accept null parameters but any element of the specified
	 * <b>lists</b> contains a null element, this last case is <b>NOT</b> controlled by MCUtils.
	 */
	@SafeVarargs
	public static <R, T> List<R> map(@Nonnull Function<T, R> mapper, @Nonnull List<T>... lists) {
		List<R> result = new ArrayList<>();
		for (List<T> list : lists)
			for (T element : list)
				result.add(mapper.apply(element));
		return result;
	}

	public static <R, T> List<R> map(@Nonnull Function<T, R> mapper, @Nonnull Collection<T> collection) {
		List<R> result = new ArrayList<>();
		for (T element : collection)
			result.add(mapper.apply(element));
		return result;
	}

	public static <R, T> Set<R> map(@Nonnull Function<T, R> mapper, @Nonnull Set<T> set) {
		Set<R> result = new HashSet<>();
		for (T element : set)
			result.add(mapper.apply(element));
		return result;
	}

	@SafeVarargs
	public static <R, T> List<R> map(@Nonnull Function<T, R> mapper, @Nonnull T... elements) {
		return map(mapper, asList(elements));
	}

	/*
	 * Removing
	 */

	public static <T> List<T> remove(List<T> lst, List<T> elements) {
		if (elements == null)
			return lst;
		for (T element : elements)
			lst.remove(element);
		return lst;
	}

	@SafeVarargs
	public static <T> List<T> remove(List<T> lst, T... elements) {
		return remove(lst, Arrays.asList(elements));
	}

	/*
	 * Randoms
	 */

	/**
	 * Gets a random element from the specified <b>list</b>,
	 * note that a new {@link Random} instance is not being
	 * created every time this method is called but only the first,
	 * as this method uses {@link MCNumbers#random()}.
	 * If the size of <b>list</b> is equal to 1, no randomization
	 * will be done and the first and only element
	 * of the list will be returned.
	 * 
	 * @param <E> the type of elements in the <b>list</b>
	 * @param list the {@link List} to use.
	 * 
	 * @return A random element from the specified <b>list</b> or
	 * null if the <b>list</b> was null.
	 */
	@Nullable
	public static <E> E getRandomFrom(@Nullable List<E> list) {
		if (list == null)
			return null;
		final int size = list.size();
		return size == 1 ? list.get(0) : list.get(MCNumbers.random().nextInt(size));
	}
}
