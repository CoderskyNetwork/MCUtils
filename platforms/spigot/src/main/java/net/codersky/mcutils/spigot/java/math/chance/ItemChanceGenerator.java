package net.codersky.mcutils.spigot.java.math.chance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.codersky.mcutils.java.math.chance.ChanceGenerator;
import net.codersky.mcutils.java.math.chance.GenericChanceGenerator;
import net.codersky.mcutils.spigot.java.tuple.pair.ImmutablePair;
import net.codersky.mcutils.java.math.MCNumbers;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.codersky.mcutils.spigot.java.tuple.pair.Pair;

/**
 * A {@link ChanceGenerator} made for {@link ItemStack ItemStacks},
 * providing methods that provide different chances for different
 * amounts of the same {@link ItemStack} to be generated. If you
 * don't need this feature, you can use a {@link GenericChanceGenerator}
 * with {@link ItemStack} as a type.
 * 
 * @author xDec0de_
 *
 * @since MCUtils 1.0.0
 */
public class ItemChanceGenerator implements ChanceGenerator<ItemStack> {

	private final HashMap<ItemStack, List<Pair<Integer, Float>>> map = new HashMap<>();

	/*
	 * ItemStack addition
	 */

	@Nonnull
	public ItemChanceGenerator add(@Nonnull ItemStack element, @Nonnull int[] amounts, @Nonnull float[] chances) {
		final List<Pair<Integer, Float>> chanceList = new ArrayList<>(amounts.length);
		if (amounts.length != chances.length)
			throw new IllegalArgumentException("Amounts length (" + amounts.length + ") is not equal to chances length (" + chances.length + ")");
		for (int i = 0; i < amounts.length; i++) {
			if (amounts[i] <= 0 || amounts[i] > element.getType().getMaxStackSize())
				throw new IllegalArgumentException("Illegal stack size for " + element.getType() + ": " + amounts[i]);
			else if (chances[i] <= 0)
				throw new IllegalArgumentException("Impossible chance (<= 0) on index " + i);
			chanceList.add(new ImmutablePair<>(amounts[i], chances[i]));
		}
		map.put(element, chanceList);
		return this;
	}

	@Nonnull
	public ItemChanceGenerator add(@Nonnull ItemStack element, @Nonnull float... chanceMap) {
		if (chanceMap.length % 2 != 0)
			throw new IllegalArgumentException("Missing chance percent! Add one more element.");
		final int[] amounts = new int[chanceMap.length / 2];
		final float[] chances = new float[chanceMap.length / 2];
		for (int i = 0, j = 0; i < chanceMap.length; i += 2, j++)
			amounts[j] = (int) chanceMap[i];
		for (int i = 1, j = 0; i < chanceMap.length; i += 2, j++)
			chances[j] = chanceMap[i];
		return add(element, amounts, chances);
	}

	@Nonnull
	@Override
	public ItemChanceGenerator add(@Nonnull ItemStack element, float chance) {
		return add(element, new int[] {element.getAmount()}, new float[] {chance});
	}

	/*
	 * Material addition
	 */

	@Nonnull
	public ItemChanceGenerator add(@Nonnull Material element, @Nonnull int[] amounts, @Nonnull float[] chances) {
		return add(new ItemStack(Objects.requireNonNull(element, "Element cannot be null")), amounts, chances);
	}

	@Nonnull
	public ItemChanceGenerator add(@Nonnull Material element, @Nonnull float... chanceMap) {
		return add(new ItemStack(Objects.requireNonNull(element, "Element cannot be null")), chanceMap);
	}

	@Nonnull
	public ItemChanceGenerator add(@Nonnull Material element, float chance) {
		return add(new ItemStack(Objects.requireNonNull(element, "Element cannot be null")), chance);
	}

	/*
	 * Generator
	 */

	@Nonnull
	@Override
	public List<ItemStack> generate() {
		final List<ItemStack> result = new ArrayList<>(map.size());
		for (Entry<ItemStack, List<Pair<Integer, Float>>> entry : map.entrySet()) {
			final float requirement = MCNumbers.random().nextFloat(0, 100);
			float lowestChance = 101;
			int rarestAmount = 0;
			for (Pair<Integer, Float> info : entry.getValue())
				if (info.getSecond() <= requirement && lowestChance > info.getSecond())
					rarestAmount = info.getFirst();
			if (rarestAmount <= 0)
				continue;
			entry.getKey().setAmount(rarestAmount);
			result.add(entry.getKey());
		}
		return result;
	}

	@Override
	public List<ItemStack> generate(int max) {
		final HashMap<ItemStack, Float> result = new HashMap<>(map.size());
		for (Entry<ItemStack, List<Pair<Integer, Float>>> entry : map.entrySet()) {
			final float requirement = MCNumbers.random().nextFloat(0, 100);
			float lowestChance = 101;
			int rarestAmount = 0;
			for (Pair<Integer, Float> info : entry.getValue())
				if (info.getSecond() <= requirement && lowestChance > info.getSecond())
					rarestAmount = info.getFirst();
			if (rarestAmount <= 0)
				continue;
			entry.getKey().setAmount(rarestAmount);
			result.put(entry.getKey(), lowestChance);
		}
		return limitElements(result, max);
	}
}