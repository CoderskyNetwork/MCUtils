package net.codersky.mcutils.spigot.regions.event;

import net.codersky.mcutils.spigot.events.player.MCPlayerEvent;
import net.codersky.mcutils.spigot.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class RegionLeaveEvent extends MCPlayerEvent {

	private final Region region;
	private final static HandlerList handlers = new HandlerList();

	public RegionLeaveEvent(@Nonnull Player player, @Nonnull Region region) {
		super(player);
		this.region = region;
	}

	@Nonnull
	public final Region getRegion() {
		return region;
	}

	@Nonnull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Nonnull
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
