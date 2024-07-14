package net.codersky.mcutils.worldgen;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;

/**
 * A simple {@link BiomeProvider} to generate worlds
 * with one single {@link Biome}. This can be used
 * with {@link VoidGenerator} to create void worlds
 * that only have one {@link Biome}.
 * 
 * @since MCUtils 1.0.0
 *
 * @author xDec0de_
 */
public class SingleBiomeProvider extends BiomeProvider {

	private final Biome biome;

	/**
	 * Constructs a {@link SingleBiomeProvider} with the
	 * specified <b>biome</b>. This <b>biome</b> cannot
	 * be {@code null} and must exist in whatever game
	 * version the server is running.
	 * 
	 * @param biome the {@link Biome} to use, cannot be
	 * {@code null}.
	 * 
	 * @throws NullPointerException if <b>biome</b> is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public SingleBiomeProvider(@Nonnull Biome biome) {
		this.biome = Objects.requireNonNull(biome, "Biome cannot be null.");
	}

	/**
	 * Gets the {@link Biome} being used by this {@link SingleBiomeProvider}.
	 * This {@link Biome} will obviously always be the same and will never
	 * be {@code null}.
	 * 
	 * @return The {@link Biome} being used by this {@link SingleBiomeProvider}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Nonnull
	public final Biome getBiome() {
		return biome;
	}

	@Override
	public final Biome getBiome(WorldInfo worldInfo, int x, int y, int z) {
		return biome;
	}

	@Override
	public final List<Biome> getBiomes(WorldInfo worldInfo) {
		return Collections.singletonList(biome);
	}
}
