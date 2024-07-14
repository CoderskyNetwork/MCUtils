package net.codersky.mcutils.java;

import net.codersky.mcutils.java.math.MCNumbers;
import org.checkerframework.checker.index.qual.Positive;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

	public static <E> ArrayList<E> asArrayList(@Nonnull E element) {
		final ArrayList<E> list = new ArrayList<>();
		list.add(element);
		return list;
	}

	@Nonnull
	@SafeVarargs
	public static <E> ArrayList<E> asArrayList(@Nonnull E... elements) {
		return add(new ArrayList<>(elements.length), elements);
	}

	@Nonnull
	public static <E> ArrayList<E> asArrayList(@Nonnull Iterable<E> elements) {
		return add(new ArrayList<>(), elements);
	}

	// List creation - LinkedList //

	public static <E> LinkedList<E> asLinkedList(@Nonnull E element) {
		final LinkedList<E> list = new LinkedList<>();
		list.add(element);
		return list;
	}

	@Nonnull
	@SafeVarargs
	public static <E> LinkedList<E> asLinkedList(@Nonnull E... elements) {
		return add(new LinkedList<>(), elements);
	}

	@Nonnull
	public static <E> LinkedList<E> asLinkedList(@Nonnull Iterable<E> elements) {
		return add(new LinkedList<>(), elements);
	}

	// Set creation - HashSet //

	@Nonnull
	public static <E> HashSet<E> asHashSet(@Nonnull E element) {
		final HashSet<E> set = new HashSet<>();
		set.add(element);
		return set;
	}

	@Nonnull
	@SafeVarargs
	public static <E> HashSet<E> asHashSet(@Nonnull E... elements) {
		return add(new HashSet<>(elements.length), elements);
	}

	@Nonnull
	public static <E> HashSet<E> asHashSet(@Nonnull Iterable<E> elements) {
		return add(new HashSet<>(), elements);
	}

	// Set creation - LinkedHashSet //

	@Nonnull
	public static <E> LinkedHashSet<E> asLinkedHashSet(@Nonnull E element) {
		final LinkedHashSet<E> set = new LinkedHashSet<>();
		set.add(element);
		return set;
	}

	@Nonnull
	@SafeVarargs
	public static <E> LinkedHashSet<E> asLinkedHashSet(@Nonnull E... elements) {
		return add(new LinkedHashSet<>(elements.length), elements);
	}

	@Nonnull
	public static <E> LinkedHashSet<E> asLinkedHashSet(@Nonnull Iterable<E> elements) {
		return add(new LinkedHashSet<>(), elements);
	}

	// Set creation - TreeSet //

	@Nonnull
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@Nonnull E element) {
		final TreeSet<E> set = new TreeSet<>();
		set.add(element);
		return set;
	}

	@Nonnull
	@SafeVarargs
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@Nonnull E... elements) {
		return add(new TreeSet<>(), elements);
	}

	@Nonnull
	public static <E extends Comparable<C>, C> TreeSet<E> asTreeSet(@Nonnull Iterable<E> elements) {
		return add(new TreeSet<>(), elements);
	}

	// Set creation - EnumSet //

	@Nonnull
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@Nonnull Class<E> enumClass) {
		return EnumSet.noneOf(enumClass);
	}

	@Nonnull
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@Nonnull E element) {
		return EnumSet.of(element);
	}

	@Nonnull
	@SafeVarargs
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@Nonnull E first, @Nonnull E... rest) {
		return EnumSet.of(first, rest);
	}

	@Nullable
	public static <E extends Enum<E>> EnumSet<E> asEnumSet(@Nonnull Collection<E> elements) {
		final E first = elements.stream().findFirst().orElse(null);
		return first == null ? null : add(asEnumSet(first.getDeclaringClass()), elements);
	}

	/*
	 * Collection editor
	 */

	@Nonnull
	@SafeVarargs
	public static <C extends Collection<E>, E> C edit(@Nonnull C list, Consumer<C>... edits) {
		for (Consumer<C> consumer : edits)
			consumer.accept(list);
		return list;
	}

	/*
	 * Cloning
	 */

	// Cloning - List //

	@Nonnull
	public static <E> List<E> clone(@Nonnull List<E> list) {
		return add(new ArrayList<>(list.size()), list);
	}

	@Nonnull
	@SafeVarargs
	public static <E> List<E> clone(@Nonnull List<E> list, @Nonnull Consumer<List<E>>... edits) {
		return edit(clone(list), edits);
	}

	@Nonnull
	public static <E> List<E> clone(@Nonnull List<E> list, @Nonnull Predicate<E> filter) {
		return add(list, clone(list), filter);
	}

	// Cloning - ArrayList //

	@Nonnull
	public static <E> ArrayList<E> clone(@Nonnull ArrayList<E> list) {
		return add(new ArrayList<>(list.size()), list);
	}

	@Nonnull
	@SafeVarargs
	public static <E> ArrayList<E> clone(@Nonnull ArrayList<E> list, @Nonnull Consumer<ArrayList<E>>... edits) {
		return edit(clone(list), edits);
	}

	@Nonnull
	public static <E> ArrayList<E> clone(@Nonnull ArrayList<E> list, @Nonnull Predicate<E> filter) {
		return add(list, clone(list), filter);
	}

	// Cloning - LinkedList

	@Nonnull
	public static <E> LinkedList<E> clone(@Nonnull LinkedList<E> list) {
		return add(new LinkedList<>(), list);
	}

	@Nonnull
	@SafeVarargs
	public static <E> LinkedList<E> clone(@Nonnull LinkedList<E> list, @Nonnull Consumer<LinkedList<E>>... edits) {
		return edit(clone(list), edits);
	}

	@Nonnull
	public static <E> LinkedList<E> clone(@Nonnull LinkedList<E> list, @Nonnull Predicate<E> filter) {
		return add(list, clone(list), filter);
	}

	// Cloning - Set //

	@Nonnull
	public static <E> Set<E> clone(@Nonnull Set<E> set) {
		return add(new HashSet<>(set.size()), set);
	}

	@Nonnull
	@SafeVarargs
	public static <E> Set<E> clone(@Nonnull Set<E> set, @Nonnull Consumer<Set<E>>... edits) {
		return edit(clone(set), edits);
	}

	@Nonnull
	public static <E> Set<E> clone(@Nonnull Set<E> set, @Nonnull Predicate<E> filter) {
		return add(set, clone(set), filter);
	}

	// Cloning - HashSet //

	@Nonnull
	public static <E> HashSet<E> clone(@Nonnull HashSet<E> set) {
		return add(new HashSet<>(set.size()), set);
	}

	@Nonnull
	@SafeVarargs
	public static <E> HashSet<E> clone(@Nonnull HashSet<E> set, @Nonnull Consumer<HashSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@Nonnull
	public static <E> HashSet<E> clone(@Nonnull HashSet<E> set, @Nonnull Predicate<E> filter) {
		return add(set, clone(set), filter);
	}

	// Cloning - LinkedHashSet //

	@Nonnull
	public static <E> LinkedHashSet<E> clone(@Nonnull LinkedHashSet<E> set) {
		return add(new LinkedHashSet<>(set.size()), set);
	}

	@Nonnull
	@SafeVarargs
	public static <E> LinkedHashSet<E> clone(@Nonnull LinkedHashSet<E> set, @Nonnull Consumer<LinkedHashSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@Nonnull
	public static <E> LinkedHashSet<E> clone(@Nonnull LinkedHashSet<E> set, @Nonnull Predicate<E> filter) {
		return add(set, clone(set), filter);
	}

	// Cloning - TreeSet //

	@Nonnull
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@Nonnull TreeSet<E> set) {
		return add(new TreeSet<>(), set);
	}

	@Nonnull
	@SafeVarargs
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@Nonnull TreeSet<E> set, @Nonnull Consumer<TreeSet<E>>... edits) {
		return edit(clone(set), edits);
	}

	@Nonnull
	public static <E extends Comparable<C>, C> TreeSet<E> clone(@Nonnull TreeSet<E> set, @Nonnull Predicate<E> filter) {
		return add(set, clone(set), filter);
	}

	/*
	 * Addition
	 */

	@Nonnull
	@SafeVarargs
	public static <C extends Collection<E>, E> C add(@Nonnull C collection, @Nonnull E... elements) {
		Collections.addAll(collection, elements);
		return collection;
	}

	@Nonnull
	public static <C extends Collection<E>, E> C add(@Nonnull C collection, @Nonnull Collection<E> other) {
		collection.addAll(other);
		return collection;
	}

	@Nonnull
	public static <C extends Collection<E>, E> C add(@Nonnull C collection, @Nonnull Iterable<E> elements) {
		for(E element : elements)
			collection.add(element);
		return collection;
	}

	@Nonnull
	public static <C extends Collection<E>, E> C add(@Nonnull C src, @Nonnull C target, @Nonnull Predicate<E> condition) {
		for (E element : src)
			if (condition.test(element))
				target.add(element);
		return target;
	}

	/*
	 * Removal
	 */

	@Nonnull
	@SafeVarargs
	public static <C extends Collection<E>, E> C remove(@Nonnull C collection, @Nonnull E... elements) {
		for (E element : elements)
			collection.remove(element);
		return collection;
	}

	@Nonnull
	public static <C extends Collection<E>, E> C remove(@Nonnull C collection, @Nonnull Collection<E> other) {
		collection.removeAll(other);
		return collection;
	}

	@Nonnull
	public static <C extends Collection<E>, E> C remove(@Nonnull C collection, @Nonnull Iterable<E> elements) {
		for (E element : elements)
			collection.remove(element);
		return collection;
	}

	@Nonnull
	public static <C extends Collection<E>, E> C remove(@Nonnull C collection, @Nonnull Predicate<E> condition) {
		collection.removeIf(condition);
		return collection;
	}

	/*
	 * Mapping
	 */

	@Nonnull
	public static <S extends Collection<O>, T extends Collection<R>, O, R> T map(@Nonnull S src, T target, @Nonnull Function<O, R> mapper) {
		for (O element : src)
			target.add(mapper.apply(element));
		return target;
	}

	// Mapping - Collection //

	public static <S, R> Collection<R> map(@Nonnull Collection<S> collection, @Nonnull Function<S, R> mapper) {
		return map(collection, new ArrayList<>(collection.size()), mapper);
	}

	// Mapping - Lists //

	@Nonnull
	public static <S, R> List<R> map(@Nonnull List<S> list, @Nonnull Function<S, R> mapper) {
		return map(list, new ArrayList<>(list.size()), mapper);
	}

	@Nonnull
	public static <S, R> ArrayList<R> map(@Nonnull ArrayList<S> list, @Nonnull Function<S, R> mapper) {
		return map(list, new ArrayList<>(list.size()), mapper);
	}

	@Nonnull
	public static <S, R> LinkedList<R> map(@Nonnull LinkedList<S> list, @Nonnull Function<S, R> mapper) {
		return map(list, new LinkedList<>(), mapper);
	}

	// Mapping - Sets //

	@Nonnull
	public static <S, R> Set<R> map(@Nonnull Set<S> set, @Nonnull Function<S, R> mapper) {
		return map(set, new HashSet<>(set.size()), mapper);
	}

	@Nonnull
	public static <S, R> HashSet<R> map(@Nonnull HashSet<S> set, @Nonnull Function<S, R> mapper) {
		return map(set, new HashSet<>(set.size()), mapper);
	}

	@Nonnull
	public static <S, R> LinkedHashSet<R> map(@Nonnull LinkedHashSet<S> set, @Nonnull Function<S, R> mapper) {
		return map(set, new LinkedHashSet<>(set.size()), mapper);
	}

	@Nonnull
	public static <S extends Comparable<SC>, R extends Comparable<RC>, SC, RC> TreeSet<R> map(@Nonnull TreeSet<S> set, @Nonnull Function<S, R> mapper) {
		return map(set, new TreeSet<>(), mapper);
	}

	/*
	 * Element getters
	 */

	@Nullable
	public static <E> E get(@Nonnull Iterable<E> iterable, @Nonnull Predicate<E> condition, @Nullable E def) {
		for (E element : iterable)
			if (condition.test(element))
				return element;
		return def;
	}

	@Nullable
	public static <E> E get(@Nonnull Iterable<E> iterable, @Nonnull Predicate<E> condition) {
		return get(iterable, condition, null);
	}

	public static <E> E get(@Nonnull Iterable<E> iterable, @Nonnegative int index) {
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

	public static <E> boolean contains(@Nonnull Iterable<E> iterable, @Nonnull Predicate<E> condition) {
		return get(iterable, condition) != null;
	}

	// Element getters - Random //

	@Nullable
	public static <E> E getRandom(@Nonnull E[] array) {
		return array[MCNumbers.random().nextInt(0, array.length)];
	}

	@Nullable
	public static <E> E getRandom(@Nonnull Collection<E> collection) {
		return get(collection, MCNumbers.random().nextInt(0, collection.size()));
	}

	@Nullable
	public static <E> E getRandom(@Nonnull List<E> list) {
		return list.get(MCNumbers.random().nextInt(0, list.size()));
	}

	@Nonnull
	public static <E> List<E> getRandom(@Nonnull Collection<E> collection, @Positive int amount, boolean allowDuplicates) {
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

	@Nonnull
	public static <E> List<E> getRandom(@Nonnull Collection<E> collection, @Positive int amount) {
		return getRandom(collection, amount, false);
	}
}
