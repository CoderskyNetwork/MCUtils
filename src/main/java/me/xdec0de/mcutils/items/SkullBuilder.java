package me.xdec0de.mcutils.items;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

/**
 * An extension of {@link ItemBuilder} designed
 * for player skulls.
 * 
 * @author xDec0de_
 * 
 * @since MCUtils 1.0.0
 */
public class SkullBuilder extends ItemBuilder {

	/**
	 * Creates a new {@link SkullBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public SkullBuilder() {
		super(Material.PLAYER_HEAD);
	}

	/**
	 * Gets the {@link SkullMeta} being used by this {@link SkullBuilder}.
	 * 
	 * @return The {@link SkullMeta} being used, can be modified.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@Override
	public SkullMeta meta() {
		return (SkullMeta)meta;
	}

	/**
	 * Sets the owner of the skull by {@link OfflinePlayer}.
	 * 
	 * @param owner The new owner of the skull.
	 * 
	 * @return This {@link SkullBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public SkullBuilder setOwner(@Nullable OfflinePlayer owner) {
		meta().setOwningPlayer(owner);
		return this;
	}

	/**
	 * Sets the owner of the skull by {@link PlayerProfile}.
	 * 
	 * @param owner The new owner of the skull.
	 * 
	 * @return This {@link SkullBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public SkullBuilder setOwner(PlayerProfile owner) {
		meta().setOwnerProfile(owner);
		return this;
	}

	/**
	 * Sets the url skin of this skull, note that the url must be from
	 * <i>http://textures.minecraft.net/</i>, otherwise, this
	 * method won't work. If you want to use custom skins generate them
	 * at <a href="https://mineskin.org">MineSkin</a>. MCUtils doesn't
	 * support custom skin URLs just yet.
	 * 
	 * @param url the url of the skin to get, must be from <i>http://textures.minecraft.net</i>.
	 * 
	 * @return This {@link SkullBuilder}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	public SkullBuilder setUrl(@Nonnull String url) {
		if (url == null || url.isEmpty())
			return this;
		final PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
		try {
			profile.getTextures().setSkin(new URL(url));
		} catch (MalformedURLException e) {
			return this;
		}
		return setOwner(profile);
	}
}
