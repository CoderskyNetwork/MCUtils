package net.codersky.mcutils.math.chance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

/**
 * An interface to represent a generator that can
 * generate a list of elements randomly, each element
 * with a predefined chance to be present on the list.
 * 
 * @author xDec0de_
 *
 * @param <T> The type of elements to generate
 * 
 * @since MCUtils 1.0.0
 */
public interface ChanceGenerator<T> {

	/**
	 * Adds a new <b>element</b> to be {@link #generate()
	 * generated} randomly with an specific <b>chance</b>
	 * 
	 * @param element the element to add.
	 * @param chance the chance this element will have to
	 * be present on the {@link List} returned by {@link #generate()}
	 * each time the method is called. This chance is a percentage
	 * that may be required by implementations to be higher than 0
	 * and smaller than 100, as otherwise no "chance" will be involved.
	 * 
	 * @return This {@link ChanceGenerator}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public ChanceGenerator<T> add(T element, float chance);

	@Nonnull
	public List<T> generate();

	@Nonnull
	public List<T> generate(int max);

	/**
	 * Auxiliary internal method to help with {@link #generate(int)}.
	 * Creates a limited {@link List} of <b>max</b> elements by a {@link HashMap}
	 * that must store any generated elements with their chance of being generated,
	 * as the list will only contain the rarest elements if <b>max</b> is lower than
	 * the size of the <b>elements</b>.
	 * <p>
	 * Help: You can see this method being used by MCUtils on {@link GenericChanceGenerator#generate(int)}.
	 * 
	 * @param elements the map of elements to limit on size.
	 * @param max the max size of the {@link List} that will be returned.
	 * 
	 * @return A {@link List} sorted by the rarest <b>elements</b>, limited to a <b>max</b>
	 * size. If the size of the <b>elements</b> is smaller than <b>max</b>, then a list
	 * of the {@link HashMap#keySet() key set} of <b>elements</b> will be returned without
	 * doing any sorting.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	default List<T> limitElements(@Nonnull HashMap<T, Float> elements, int max) {
		if (max >= elements.size())
			return new ArrayList<>(elements.keySet());
		final List<T> result = new ArrayList<>(max);
		for (int i = 0; i < max; i++) {
			Entry<T, Float> lowest = null;
			for (Entry<T, Float> entry : elements.entrySet()) {
				if (lowest == null || entry.getValue() < lowest.getValue())
					lowest = entry;
			}
			elements.remove(lowest.getKey());
			result.add(lowest.getKey());
		}
		return result;
	}
}
