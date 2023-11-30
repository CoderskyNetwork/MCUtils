package net.codersky.mcutils.math.chance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import net.codersky.mcutils.math.MCNumbers;

/**
 * A very basic {@link ChanceGenerator} for generic element types.
 * 
 * @author xDec0de_
 *
 * @param <T> The type of elements to generate
 * 
 * @since MCUtils 1.0.0
 */
public class GenericChanceGenerator<T> implements ChanceGenerator<T> {

	private final HashMap<T, Float> map = new HashMap<>();

	/**
	 * Adds a new <b>element</b> to be {@link #generate()
	 * generated} randomly with an specific <b>chance</b>
	 * 
	 * @param element the element to add.
	 * @param chance the chance this element will have to
	 * be present on the {@link List} returned by {@link #generate()}
	 * each time the method is called. This chance is a percentage
	 * that must be higher than 0 and smaller than 100.
	 * 
	 * @return This {@link ChanceGenerator}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Override
	public GenericChanceGenerator<T> add(T element, float chance) {
		if (chance > 0)
			map.put(element, chance);
		return this;
	}

	@Nonnull
	@Override
	public List<T> generate() {
		final List<T> result = new ArrayList<>(map.size());
		for (Entry<T, Float> entry : map.entrySet())
			if (MCNumbers.tryChance(entry.getValue()))
				result.add(entry.getKey());
		return result;
	}

	@Nonnull
	@Override
	public List<T> generate(int max) {
		final HashMap<T, Float> results = new HashMap<>(map.size());
		for (Entry<T, Float> entry : map.entrySet())
			if (MCNumbers.tryChance(entry.getValue()))
				results.put(entry.getKey(), entry.getValue());
		return limitElements(results, max);
	}
}