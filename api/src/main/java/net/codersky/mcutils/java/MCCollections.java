package net.codersky.mcutils.java;

import jdk.jfr.Unsigned;
import net.codersky.mcutils.java.math.MCNumbers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utility class designed to create and use {@link Collection collections} fast.
 * <p>
 * Please keep in mind that this class doesn't intend to replace
 * {@link Collections} and the use of that class is preferred over this one
 * when possible, think of this class as an extension of said class, even though
 * it doesn't extend it (Because we can't).
 * <p>
 * This class is currently designed to work with the following types of {@link List lists}:
 * <ul>
 *     <li>{@link ArrayList} - Default for {@link List}</li>
 *     <li>{@link LinkedList}</li>
 * </ul>
 * And {@link Set sets}:
 * <ul>
 *     <li>{@link HashSet} - Default for {@link Set}</li>
 *     <li>{@link LinkedHashSet}</li>
 *     <li>{@link TreeSet}</li>
 *     <li>{@link EnumSet}</li>
 * </ul>
 *
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public class MCCollections {

	/*
	 * Creation
	 */

	// List creation - ArrayList //

	public static <E> ArrayList<E> asArrayList(@NotNull E element) {
		final ArrayList<E> list = new ArrayList<>();
		list.add(element);
		return list;
	}

	@NotNull
	@SafeVarargs
	public static <E> ArrayList<E> asArrayList(@NotNull E... elements) {
		return add(new ArrayList<>(elements.length), elements);
	}

	@NotNull
	public static <E> ArrayList<E> asArrayList(@NotNull Iterable<E> elements) {
		return add(new ArrayList<>(), elements);
	}

	// List creation - LinkedList //

	public static <E> LinkedList<E> asLinkedList(@NotNull E element) {
		final LinkedList<E> list = new LinkedList<>();
		list.add(element);
		return list;
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedList<E> asLinkedList(@NotNull E... elements) {
		return add(new LinkedList<>(), elements);
	}

	@NotNull
	public static <E> LinkedList<E> asLinkedList(@NotNull Iterable<E> elements) {
		return add(new LinkedList<>(), elements);
	}

	// Set creation - HashSet //

	@NotNull
	public static <E> HashSet<E> asHashSet(@NotNull E element) {
		final HashSet<E> set = new HashSet<>();
		set.add(element);
		return set;
	}

	@NotNull
	@SafeVarargs
	public static <E> HashSet<E> asHashSet(@NotNull E... elements) {
		return add(new HashSet<>(elements.length), elements);
	}

	@NotNull
	public static <E> HashSet<E> asHashSet(@NotNull Iterable<E> elements) {
		return add(new HashSet<>(), elements);
	}

	// Set creation - LinkedHashSet //

	@NotNull
	public static <E> LinkedHashSet<E> asLinkedHashSet(@NotNull E element) {
		final LinkedHashSet<E> set = new LinkedHashSet<>();
		set.add(element);
		return set;
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedHashSet<E> asLinkedHashSet(@NotNull E... elements) {
		return add(new LinkedHashSet<>(elements.length), elements);
	}

	@NotNull
	public static <E> LinkedHashSet<E> asLinkedHashSet(@NotNull Iterable<E> elements) {
		return add(new LinkedHashSet<>(), elements);
	}

	// Set creation - TreeSet //

	@NotNull
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@NotNull E element) {
		final TreeSet<E> set = new TreeSet<>();
		set.add(element);
		return set;
	}

	@NotNull
	@SafeVarargs
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@NotNull E... elements) {
		return add(new TreeSet<>(), elements);
	}

	@NotNull
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@NotNull Iterable<E> elements) {
		return add(new TreeSet<>(), elements);
	}

	// Set creation - EnumSet //

	@NotNull
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@NotNull Class<E> enumClass) {
		return EnumSet.noneOf(enumClass);
	}

	@NotNull
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@NotNull E element) {
		return EnumSet.of(element);
	}

	@NotNull
	@SafeVarargs
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@NotNull E first, @NotNull E... rest) {
		return EnumSet.of(first, rest);
	}

	@Nullable
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@NotNull Collection<E> elements) {
		final E first = elements.stream().findFirst().orElse(null);
		return first == null ? null : add(asEnumSet(first.getDeclaringClass()), elements);
	}

	/*
	 * Collection editor
	 */

	@NotNull
	@SafeVarargs
	public static <C extends Collection<E>, E> C edit(@NotNull C list, Consumer<C>... edits) {
		for (Consumer<C> consumer : edits)
			consumer.accept(list);
		return list;
	}

	/*
	 * Cloning
	 */

	// Cloning - List //

	@NotNull
	public static <E> List<E> clone(@NotNull List<E> list) {
		return add(new ArrayList<>(list.size()), list);
	}

	@NotNull
	@SafeVarargs
	public static <E> List<E> clone(@NotNull List<E> list, @NotNull Consumer<List<E>>... edits) {
		return edit(clone(list), edits);
	}

	@NotNull
	public static <E> List<E> clone(@NotNull List<E> list, @NotNull Predicate<E> filter) {
		return add(list, clone(list), filter);
	}

	// Cloning - ArrayList //

	@NotNull
	public static <E> ArrayList<E> clone(@NotNull ArrayList<E> list) {
		return add(new ArrayList<>(list.size()), list);
	}

	@NotNull
	@SafeVarargs
	public static <E> ArrayList<E> clone(@NotNull ArrayList<E> list, @NotNull Consumer<ArrayList<E>>... edits) {
		return edit(clone(list), edits);
	}

	@NotNull
	public static <E> ArrayList<E> clone(@NotNull ArrayList<E> list, @NotNull Predicate<E> filter) {
		return add(list, clone(list), filter);
	}

	// Cloning - LinkedList

	@NotNull
	public static <E> LinkedList<E> clone(@NotNull LinkedList<E> list) {
		return add(new LinkedList<>(), list);
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedList<E> clone(@NotNull LinkedList<E> list, @NotNull Consumer<LinkedList<E>>... edits) {
		return edit(clone(list), edits);
	}

	@NotNull
	public static <E> LinkedList<E> clone(@NotNull LinkedList<E> list, @NotNull Predicate<E> filter) {
		return add(list, clone(list), filter);
	}

	// Cloning - Set //

	@NotNull
	public static <E> Set<E> clone(@NotNull Set<E> set) {
		return add(new HashSet<>(set.size()), set);
	}

	@NotNull
	@SafeVarargs
	public static <E> Set<E> clone(@NotNull Set<E> set, @NotNull Consumer<Set<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	public static <E> Set<E> clone(@NotNull Set<E> set, @NotNull Predicate<E> filter) {
		return add(set, clone(set), filter);
	}

	// Cloning - HashSet //

	@NotNull
	public static <E> HashSet<E> clone(@NotNull HashSet<E> set) {
		return add(new HashSet<>(set.size()), set);
	}

	@NotNull
	@SafeVarargs
	public static <E> HashSet<E> clone(@NotNull HashSet<E> set, @NotNull Consumer<HashSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	public static <E> HashSet<E> clone(@NotNull HashSet<E> set, @NotNull Predicate<E> filter) {
		return add(set, clone(set), filter);
	}

	// Cloning - LinkedHashSet //

	@NotNull
	public static <E> LinkedHashSet<E> clone(@NotNull LinkedHashSet<E> set) {
		return add(new LinkedHashSet<>(set.size()), set);
	}

	@NotNull
	@SafeVarargs
	public static <E> LinkedHashSet<E> clone(@NotNull LinkedHashSet<E> set, @NotNull Consumer<LinkedHashSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	public static <E> LinkedHashSet<E> clone(@NotNull LinkedHashSet<E> set, @NotNull Predicate<E> filter) {
		return add(set, clone(set), filter);
	}

	// Cloning - TreeSet //

	@NotNull
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@NotNull TreeSet<E> set) {
		return add(new TreeSet<>(), set);
	}

	@NotNull
	@SafeVarargs
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@NotNull TreeSet<E> set, @NotNull Consumer<TreeSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@NotNull
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@NotNull TreeSet<E> set, @NotNull Predicate<E> filter) {
		return add(set, clone(set), filter);
	}

	/*
	 * Addition
	 */

	@NotNull
	@SafeVarargs
	public static <C extends Collection<E>, E> C add(@NotNull C collection, @NotNull E... elements) {
		Collections.addAll(collection, elements);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C add(@NotNull C collection, @NotNull Collection<E> other) {
		collection.addAll(other);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C add(@NotNull C collection, @NotNull Iterable<E> elements) {
		for(E element : elements)
			collection.add(element);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C add(@NotNull C src, @NotNull C target, @NotNull Predicate<E> condition) {
		for (E element : src)
			if (condition.test(element))
				target.add(element);
		return target;
	}

	/*
	 * Removal
	 */

	@NotNull
	@SafeVarargs
	public static <C extends Collection<E>, E> C remove(@NotNull C collection, @NotNull E... elements) {
		for (E element : elements)
			collection.remove(element);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C remove(@NotNull C collection, @NotNull Collection<E> other) {
		collection.removeAll(other);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C remove(@NotNull C collection, @NotNull Iterable<E> elements) {
		for (E element : elements)
			collection.remove(element);
		return collection;
	}

	@NotNull
	public static <C extends Collection<E>, E> C remove(@NotNull C collection, @NotNull Predicate<E> condition) {
		collection.removeIf(condition);
		return collection;
	}

	/*
	 * Mapping
	 */

	@NotNull
	public static <S extends Collection<O>, T extends Collection<R>, O, R> T map(@NotNull S src, T target, @NotNull Function<O, R> mapper) {
		for (O element : src)
			target.add(mapper.apply(element));
		return target;
	}

	// Mapping - Collection //

	public static <S, R> Collection<R> map(@NotNull Collection<S> collection, @NotNull Function<S, R> mapper) {
		return map(collection, new ArrayList<>(collection.size()), mapper);
	}

	// Mapping - Lists //

	@NotNull
	public static <S, R> List<R> map(@NotNull List<S> list, @NotNull Function<S, R> mapper) {
		return map(list, new ArrayList<>(list.size()), mapper);
	}

	@NotNull
	public static <S, R> ArrayList<R> map(@NotNull ArrayList<S> list, @NotNull Function<S, R> mapper) {
		return map(list, new ArrayList<>(list.size()), mapper);
	}

	@NotNull
	public static <S, R> LinkedList<R> map(@NotNull LinkedList<S> list, @NotNull Function<S, R> mapper) {
		return map(list, new LinkedList<>(), mapper);
	}

	// Mapping - Sets //

	@NotNull
	public static <S, R> Set<R> map(@NotNull Set<S> set, @NotNull Function<S, R> mapper) {
		return map(set, new HashSet<>(set.size()), mapper);
	}

	@NotNull
	public static <S, R> HashSet<R> map(@NotNull HashSet<S> set, @NotNull Function<S, R> mapper) {
		return map(set, new HashSet<>(set.size()), mapper);
	}

	@NotNull
	public static <S, R> LinkedHashSet<R> map(@NotNull LinkedHashSet<S> set, @NotNull Function<S, R> mapper) {
		return map(set, new LinkedHashSet<>(set.size()), mapper);
	}

	@NotNull
	public static <S extends Comparable<SC>, R extends Comparable<RC>, SC, RC> TreeSet<R> map(@NotNull TreeSet<S> set, @NotNull Function<S, R> mapper) {
		return map(set, new TreeSet<>(), mapper);
	}

	/*
	 * Element getters
	 */

	@Nullable
	public static <E> E get(@NotNull Iterable<E> iterable, @NotNull Predicate<E> condition, @Nullable E def) {
		for (E element : iterable)
			if (condition.test(element))
				return element;
		return def;
	}

	@Nullable
	public static <E> E get(@NotNull Iterable<E> iterable, @NotNull Predicate<E> condition) {
		return get(iterable, condition, null);
	}

	public static <E> E get(@NotNull Iterable<E> iterable, int index) {
		int i = index;
		for (E element : iterable) {
			if (i == 0)
				return element;
			i--;
		}
		return null;
	}

	/*
	 * Contains element
	 */

	public static <E> boolean contains(@NotNull Iterable<E> iterable, @NotNull Predicate<E> condition) {
		return get(iterable, condition) != null;
	}

	// Element getters - Random //

	@Nullable
	public static <E> E getRandom(@NotNull E[] array) {
		return array[MCNumbers.random().nextInt(0, array.length)];
	}

	@Nullable
	public static <E> E getRandom(@NotNull Collection<E> collection) {
		return get(collection, MCNumbers.random().nextInt(0, collection.size()));
	}

	@Nullable
	public static <E> E getRandom(@NotNull List<E> list) {
		return list.get(MCNumbers.random().nextInt(0, list.size()));
	}

	@NotNull
	public static <E> List<E> getRandom(@NotNull Collection<E> collection, int amount, boolean allowDuplicates) {
		if (amount >= collection.size())
			return new ArrayList<>(collection);
		final List<E> res = new ArrayList<>(amount);
		while (res.size() != amount) {
			final E element = getRandom(collection);
			if (allowDuplicates || !res.contains(element))
				res.add(element);
		}
		return res;
	}

	@NotNull
	public static <E> List<E> getRandom(@NotNull Collection<E> collection, int amount) {
		return getRandom(collection, amount, false);
	}
}
