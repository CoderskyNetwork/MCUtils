package net.codersky.mcutils.regions;

import net.codersky.mcutils.MCPlugin;
import net.codersky.mcutils.events.player.CancellableMCPlayerEvent;
import net.codersky.mcutils.java.annotations.Internal;
import net.codersky.mcutils.java.MCCollections;
import net.codersky.mcutils.regions.event.RegionEnterEvent;
import net.codersky.mcutils.regions.event.RegionEnteringEvent;
import net.codersky.mcutils.regions.event.RegionLeaveEvent;
import net.codersky.mcutils.regions.event.RegionLeavingEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class RegionHandler implements Listener {

	private final HashMap<UUID, HashSet<Region>> regions = new HashMap<>();

	public RegionHandler init(@Nonnull MCPlugin plugin) {
		return plugin.registerEvents(new RegionHandler());
	}

	@Nonnull
	public Set<Region> getRegionsAt(@Nonnull World world) {
		final Set<Region> worldRegions = regions.get(world.getUID());
		return worldRegions == null ? Collections.emptySet() : MCCollections.clone(worldRegions);
	}

	@Nonnull
	public Set<Region> getRegionsAt(@Nonnull Location location) {
		if (!location.isWorldLoaded() || location.getWorld() == null)
			return Collections.emptySet();
		final Set<Region> worldRegions = getRegionsAt(location.getWorld());
		final int x = location.getBlockX();
		final int y = location.getBlockY();
		final int z = location.getBlockZ();
		return MCCollections.remove(worldRegions, region -> !region.contains(x, y, z));
	}

	public Region getPriorityRegionAt(@Nonnull Location loc) {
		return null;
	}

	public boolean addRegion(@Nonnull Region region) {
		final UUID worldId = region.getWorld().getUID();
		final HashSet<Region> worldRegions = regions.get(worldId);
		if (worldRegions == null) {
			regions.put(worldId, MCCollections.asHashSet(region));
		} else
			return worldRegions.add(region);
		return true;
	}

	/*
	 * Enter / Leave handling
	 */

	@Internal
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (!hasChangedBlock(e.getFrom(), e.getTo()))
			return;
		// Get all regions at from and to
		final Set<Region> to = getRegionsAt(e.getTo());
		final Set<Region> from = getRegionsAt(e.getFrom());
		// Filter regions that are being entered or left
		final Set<Region> entering = getRegionChanges(from, to, r -> new RegionEnteringEvent(e.getPlayer(), r));
		final Set<Region> leaving = getRegionChanges(to, from, r -> new RegionLeavingEvent(e.getPlayer(), r));
		// Cancel event if any called event was cancelled
		if (entering == null || leaving == null)
			e.setCancelled(true);
		else { // Call success events
			entering.forEach(entered -> new RegionEnterEvent(e.getPlayer(), entered).call());
			leaving.forEach(left -> new RegionLeaveEvent(e.getPlayer(), left).call());
		}
	}

	private boolean hasChangedBlock(@Nonnull Location from, @Nullable Location to) {
		if (to == null)
			return false;
		if (!Objects.equals(from.getWorld(), to.getWorld()))
			return true;
		return from.getBlockX() != to.getBlockX() ||
				from.getBlockY() != to.getBlockY() ||
				from.getBlockZ() != to.getBlockZ();
	}

	private Set<Region> getRegionChanges(Set<Region> a, Set<Region> b, Function<Region, CancellableMCPlayerEvent> fun) {
		final Set<Region> entered = new HashSet<>();
		for (Region onB : b) {
			if (a.contains(onB))
				continue;
			if (!fun.apply(onB).call().isCancelled())
				return null;
			else
				entered.add(onB);
		}
		return entered;
	}
}
