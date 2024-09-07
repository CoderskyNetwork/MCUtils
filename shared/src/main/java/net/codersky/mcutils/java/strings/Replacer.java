package net.codersky.mcutils.java.strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.codersky.mcutils.java.MCCollections;
import net.codersky.mcutils.java.math.MCNumbers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextReplacementConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a replacer to replace parts of a {@link String} or {@link Component} with other objects.
 * If you want to use the same replacements for multiple objects, you should create a replacer variable and
 * apply it to as many objects as you want to <b>avoid creating multiple instances of the same replacements</b>,
 * also, make sure that the amount of objects added to the replacer are <b>even</b>,
 * otherwise, an {@link IllegalArgumentException} will be thrown.
 * <p>
 * <b>Numeric support:</b>
 * <p>
 * This feature is always enabled with Replacers and adds the possibility to apply different replacements
 * depending on numeric values, don't worry about performance, the impact isn't noticeable unless you are
 * parsing millions of strings as this doesn't use regular expressions. Here is a common example on how to use it:
 * <p>
 * <code>new Replacer("%points%", 10).replaceAt("You have %points% <%points%:point:points>"</code>
 * <p>
 * With this example, the returning string will be "<b>You have 10 points</b>".
 * If instead of 10 we had 1 (or -1) point, the result would be "<b>You have 1 point</b>",
 * 
 * @since MCUtils 1.0.0
 * 
 * @see #Replacer(Object...)
 *
 * @author xDec0de_
 */
public class Replacer {

	private final ArrayList<Object> replaceList = new ArrayList<>();

	/**
	 * Creates a replacer to replace parts of a string with other strings,
	 * if you want to use the same replacements for multiple strings, you should 
	 * create a {@link Replacer} variable and apply it to as many strings as you
	 * want to <b>avoid creating multiple instances of the same replacements</b>,
	 * also,  make sure that the amount of strings added to the {@link Replacer}
	 * are <b>even</b>, otherwise, an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param replacements The new strings to be replaced, the format is <i>"str1", "obj1", "str2", "obj2"...</i>
	 * 
	 * @throws IllegalArgumentException if {@code replacements} is {@code null} or the amount of
	 * objects is not even, more technically, if {@code replacements}
	 * size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #add(Replacer...)
	 * @see #add(Object...)
	 * @see #replaceAt(String)
	 * @see #replaceAt(Component) 
	 */
	public Replacer(@NotNull Object... replacements) {
		add(Objects.requireNonNull(replacements, "Replacements cannot be null."));
	}

	/**
	 * Adds new {@code replacements} to an existing {@link Replacer}. the amount of replacements must also be even
	 * note that existing replacements will be added to the list but the new replacer won't overwrite them.
	 * Because of the way {@link Replacer Replacers} work, only the first replacement added for a string will take
	 * effect if there is another replacement added to said string later on.
	 * <p>
	 * Example: text is <i>"Replace %test%"</i>, we add <i>"%test%", "Hello"</i> and <i>"%test%", "World"</i>. The
	 * result will be <i>"Replace Hello"</i>, as only the first replacement over <i>%test%</i> will take effect.
	 * 
	 * @param replacements The replacements to add. The format is <i>"str1", "obj1", "str2", "obj2"...</i>
	 * 
	 * @return This {@link Replacer} with the new <b>replacements</b> added to it.
	 *
	 * @throws NullPointerException If {@code replacements} or any of the elements inside of {@code replacements}
	 * are {@code null}.
	 * @throws IllegalArgumentException If the amount of objects is not even, more technically, if {@code replacements}
	 * size % 2 is not equal to 0.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #add(Replacer...)
	 * @see #replaceAt(String)
	 * @see #replaceAt(Component)
	 */
	@NotNull
	public Replacer add(@NotNull Object... replacements) {
		if (replacements.length % 2 != 0)
			throw new IllegalArgumentException(replacements[replacements.length -1] + " does not have a replacement! Add one more element.");
		for (Object replacement : replacements) {
			if (replacement instanceof Replacement iReplacement)
				replaceList.add(iReplacement.asReplacement());
			else
				replaceList.add(Objects.requireNonNull(replacement, "Null replacements are not allowed"));
		}
		return this;
	}

	/**
	 * Adds the replacements of the specified {@code replacers} to this {@link Replacer}, joining them.
	 * Note that existing replacements will be added to the list but the new {@link Replacer} won't overwrite them.
	 * Because of the way {@link Replacer Replacers} work, only the first replacement added for a string will take
	 * effect if there is another replacement added to said string later on.
	 * <p>
	 * Example: text is <i>"Replace %test%"</i>, we add <i>"%test%", "Hello"</i> and <i>"%test%", "World"</i>. The
	 * result will be <i>"Replace Hello"</i>, as only the first replacement over <i>%test%</i> will take effect.
	 * 
	 * @param replacers The {@link Replacer Replacers} to join to the existing {@link Replacer}.
	 * 
	 * @return The old {@link Replacer} with the replacements of {@code replacer} added to it.
	 * 
	 * @since MCUtils 1.0.0
	 *
	 * @see #add(Object...) 
	 * @see #replaceAt(String)
	 * @see #replaceAt(Component)
	 */
	@NotNull
	public Replacer add(@NotNull Replacer... replacers) {
		for (Replacer replacer : replacers)
			replaceList.addAll(replacer.replaceList);
		return this;
	}

	/**
	 * Clones this {@link Replacer} creating a new one with the same replacements.
	 *
	 * @return A copy of this {@link Replacer}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #Replacer(Object...)
	 * @see #add(Replacer...)
	 */
	@NotNull
	@Override
	public Replacer clone() {
		return new Replacer().add(this);
	}

	/*
	 * String replacements
	 */

	/**
	 * Applies this {@link Replacer} to the specified {@link String}.
	 * 
	 * @param str The {@link String} to apply the replacements to.
	 * 
	 * @return A new {@link String} with all {@link #getReplacements() replacements} applied to it.
	 *
	 * @throws NullPointerException if {@code str} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 * 
	 * @see #replaceAt(String...)
	 * @see #replaceAt(Component)
	 * @see #replaceAt(Component...)
	 * @see #replaceAtStrings(List)
	 * @see #replaceAtComponents(List)
	 */
	@NotNull
	public String replaceAt(@NotNull String str) {
		final int repLstLen = replaceList.size();
		if (repLstLen == 0 || str.isEmpty())
			return str;
		final StringBuilder res = new StringBuilder(str);
		for (int i = 0; i <= repLstLen - 1; i += 2) {
			final String toSearch = replaceList.get(i).toString();
			final int searchLen = toSearch.length();
			// TODO Handle components, maybe just by adding the content? #toString is weird here.
			final String replacement = replaceList.get(i + 1).toString();
			final int replacementLen = replacement.length();
			int index = res.indexOf(toSearch);
			while (index != -1) {
				res.replace(index, index + searchLen, replacement);
				index = res.indexOf(toSearch, index + replacementLen);
			}
		}
		return applyNumSupport(res).toString();
	}

	/**
	 * Applies this {@link Replacer} to the specified {@link List} of {@link String strings}.
	 * 
	 * @param list The {@link String} {@link List} to apply the replacements to.
	 * 
	 * @return A new <b>modifiable</b> {@link String} {@link List} with the replacements applied to it.
	 *
	 * @throws NullPointerException if {@code list} or any element of it is {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAt(String...)
	 * @see #replaceAt(Component)
	 * @see #replaceAt(Component...)
	 * @see #replaceAtComponents(List)
	 */
	@NotNull
	public List<String> replaceAtStrings(@NotNull List<String> list) {
		return MCCollections.map(list, this::replaceAt);
	}

	/**
	 * Applies this {@link Replacer} to the specified {@link String strings}.
	 *
	 * @param strings The {@link String strings} to apply the replacements to.
	 *
	 * @return A new <b>modifiable</b> {@link String} {@link List} with the replacements applied to it.
	 *
	 *
	 * @throws NullPointerException if {@code strings} or any {@link String} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAt(Component)
	 * @see #replaceAt(Component...)
	 * @see #replaceAtStrings(List)
	 * @see #replaceAtComponents(List)
	 */
	@NotNull
	public List<String> replaceAt(@NotNull String... strings) {
		return replaceAtStrings(List.of(strings));
	}

	/*
	 * Adventure component replacements
	 */

	/**
	 * Applies this {@link Replacer} to the specified {@link Component}.
	 *
	 * @param component The {@link Component} to apply the replacements to.
	 *
	 * @return A new {@link Component} with all {@link #getReplacements() replacements} applied to it.
	 *
	 * @throws NullPointerException if {@code component} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAt(String...)
	 * @see #replaceAt(Component...)
	 * @see #replaceAtStrings(List)
	 * @see #replaceAtComponents(List)
	 */
	@NotNull
	public Component replaceAt(@NotNull Component component) {
		final int repLstLen = replaceList.size();
		if (repLstLen == 0)
			return component;
		Component result = component;
		for (int i = 0; i <= repLstLen - 1; i += 2) {
			final String match = replaceList.get(i).toString();
			final Object value = replaceList.get(i + 1);
			if (value instanceof ComponentLike componentLike)
				result = result.replaceText(b -> b.matchLiteral(match).replacement(componentLike));
			else
				result = result.replaceText(b -> b.matchLiteral(match).replacement(value.toString()));
		}
		return result;
	}

	/**
	 * Applies this {@link Replacer} to the specified {@link List} of {@link Component components}.
	 *
	 * @param list The {@link Component} {@link List} to apply the replacements to.
	 *
	 * @return A new <b>modifiable</b> {@link Component} {@link List} with the replacements applied to it.
	 *
	 * @throws NullPointerException if {@code list} or any element of it is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAt(String...)
	 * @see #replaceAt(Component)
	 * @see #replaceAt(Component...)
	 * @see #replaceAtComponents(List)
	 */
	@NotNull
	public List<Component> replaceAtComponents(@NotNull List<Component> list) {
		return MCCollections.map(list, this::replaceAt);
	}

	/**
	 * Applies this {@link Replacer} to the specified {@link Component components}.
	 *
	 * @param components The {@link Component components} to apply the replacements to.
	 *
	 * @return A new <b>modifiable</b> {@link Component} {@link List} with the replacements applied to it.
	 *
	 * @throws NullPointerException if {@code components} or any {@link Component} is {@code null}.
	 *
	 * @since MCUtils 1.0.0
	 *
	 * @see #replaceAt(String)
	 * @see #replaceAt(String...)
	 * @see #replaceAt(Component)
	 * @see #replaceAtStrings(List)
	 * @see #replaceAtComponents(List)
	 */
	@NotNull
	public List<Component> replaceAt(@NotNull Component... components) {
		return replaceAtComponents(List.of(components));
	}

	/*
	 * Numeric support
	 */

	// TODO Redo this method to work with Components, maybe a new MCStrings pattern?
	// TODO Seems weird to have a pattern on the Replacer class at this point...

	@Deprecated(forRemoval = true)
	private StringBuilder applyNumSupport(StringBuilder res) {
		int start = 0;
		while (start < res.length()) {
			final int open = res.indexOf("<", start);
			final int close = res.indexOf(">", open);
			if (open == -1 || close == -1 || open > close)
				break;
			final String[] parts = res.substring(open + 1, close).split(":");
			if (parts.length != 3) {
				start = close + 1;
				continue;
			}
			if (MCNumbers.isNumeric(parts[0])) {
				final int value = Integer.parseInt(parts[0]);
				final String replacement = value == 1 || value == -1 ? parts[1] : parts[2];
				res.replace(open, close + 1, replacement);
				start = open + replacement.length();
			} else
				start = close + 1;
		}
		return res;
	}

	/**
	 * Gets the replacements being used by this {@link Replacer}.
	 * Modifying this list will have no effect, it can be used
	 * for debugging or to create your own {@link Replacer} type
	 * while being able to {@link #clone()} it. This can be done
	 * by calling the {@link #Replacer(Object...)} constructor.
	 * 
	 * @return The replacements being used by this {@link Replacer}.
	 * 
	 * @since MCUtils 1.0.0
	 */
	@NotNull
	public List<Object> getReplacements() {
		return new ArrayList<>(replaceList);
	}

	/*
	 * Object override
	 */

	@NotNull
	@Override
	public String toString() {
		return "Replacer" + replaceList.toString();
	}
}
