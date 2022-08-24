package es.xdec0de.mcutils.general;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import es.xdec0de.mcutils.strings.MCStrings;

/**
 * A class containing player utilities.
 * This class uses the default constructor,
 * creating new instances by calling it is safe
 * and the intended way of using this class so
 * Java's GC can remove said instance once it isn't needed.
 * 
 * @since MCUtils 1.0.0
 * 
 * @author xDec0de_
 */
public class PlayerUtils {

	/**
	 * Gets a string list containing the names of the players
	 * in <b>players</b>, if <b>players</b> is null, null
	 * will be returned, if any player is null, it will be
	 * ignored and won't get added to the list.
	 * 
	 * @param <T> must extend {@link OfflinePlayer}
	 * @param players the {@link Iterable} of players to use.
	 * 
	 * @return A string list containing the names of the players
	 * in <b>players</b>
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see OfflinePlayer#getName()
	 * @see MCStrings#asString(List, String)
	 */
	@Nullable
	public <T extends OfflinePlayer> List<String> getNames(@Nullable Iterable<T> players) {
		if (players == null)
			return null;
		List<String> names = new ArrayList<>();
		for (T p : players)
			if (p != null)
				names.add(p.getName());
		return names;
	}

	/**
	 * Gets a string list containing the display names of the players
	 * in <b>players</b>, if <b>players</b> is null, null
	 * will be returned, if any player is null, it will be
	 * ignored and won't get added to the list.
	 * 
	 * @param <T> must extend {@link Player}
	 * @param players the {@link Iterable} of players to use.
	 * 
	 * @return A string list containing the names of the players
	 * in <b>players</b>
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see Player#getDisplayName()
	 * @see MCStrings#asString(List, String)
	 */
	@Nullable
	public <T extends Player> List<String> getDisplayNames(@Nullable Iterable<T> players) {
		if (players == null)
			return null;
		List<String> names = new ArrayList<>();
		for (T p : players)
			if (p != null)
				names.add(p.getDisplayName());
		return names;
	}
}
