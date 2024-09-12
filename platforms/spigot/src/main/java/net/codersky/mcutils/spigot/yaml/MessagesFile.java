package net.codersky.mcutils.spigot.yaml;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nullable;

import net.codersky.mcutils.files.MessagesFileHolder;
import net.codersky.mcutils.java.strings.MCStrings;
import org.bukkit.plugin.java.JavaPlugin;

import net.codersky.mcutils.java.strings.Replacer;
import org.jetbrains.annotations.NotNull;

public class MessagesFile extends PluginFile implements MessagesFileHolder {

	@Nullable
	private Replacer defReplacer = null;

	public MessagesFile(@NotNull JavaPlugin plugin, @Nullable String path, @Nullable Charset charset) {
		super(plugin, path, charset);
		if (!contains("commands.noPlayer"))
			addDefault("commands.noPlayer", "&8&l[&4&l!&8&l] &cThis command cannot be executed by players&8.");
		if (!contains("commands.noConsole"))
			addDefault("commands.noConsole", "&8&l[&4&l!&8&l] &cThis command cannot be executed by the console&8.");
	}

	public MessagesFile(@NotNull JavaPlugin plugin, @Nullable String path) {
		this(plugin, path, StandardCharsets.UTF_8);
	}

	/*
	 * Replacer handling
	 */

	/**
	 * Sets the default {@link Replacer} to be used on this {@link MessagesFile}.
	 * If the default {@link Replacer} is null, no default {@link Replacer} will be used on any
	 * message, the default {@link Replacer} is null by default.
	 * 
	 * @param replacer the default {@link Replacer} to be used on this {@link MessagesFile}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getDefaultReplacer()
	 * @see #setDefaultObjReplacer(Object...)
	 * @see #setDefaultPathReplacer(String...)
	 */
	public void setDefaultReplacer(@Nullable Replacer replacer) {
		this.defReplacer = replacer;
	}

	/**
	 * Creates a new {@link Replacer} with the specified {@code replacements} and
	 * sets it as the default {@link Replacer} to use on this {@link MessagesFile}.
	 * 
	 * @param replacements the replacements to use for the new {@link Replacer}.
	 * 
	 * @throws IllegalArgumentException if {@code replacements} is {@code null} or the amount of
	 * objects is not even, more technically, if {@code replacements}
	 * size % 2 is not equal to 0 as specified by {@link Replacer#Replacer(Object...)}
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getDefaultReplacer()
	 * @see #setDefaultReplacer(Replacer)
	 * @see #setDefaultPathReplacer(String...)
	 */
	public void setDefaultObjReplacer(@NotNull Object... replacements) {
		setDefaultReplacer(new Replacer(replacements));
	}

	/**
	 * Creates a new {@link Replacer} with the specified {@code replacements} and
	 * sets it as the default {@link Replacer} to use on this {@link MessagesFile}.
	 * <p>
	 * The difference between this method and {@link #setDefaultObjReplacer(Object...)}
	 * is that this method will get strings from this file for the replacements, so the
	 * replacements must be paths, take the following code as an example:
	 * <p>
	 * {@code setDefaultPathReplacer("%err%", "error");}
	 * <p>
	 * This default {@link Replacer} will replace every occurrence of "%error%" on the file
	 * with the string found at the "error" path of the file, not the literal string "error".
	 * 
	 * @param replacements the replacements to use for the new {@link Replacer}.
	 * 
	 * @throws IllegalArgumentException if {@code replacements} is {@code null} or the amount of
	 * objects is not even, more technically, if {@code replacements}
	 * size % 2 is not equal to 0 as specified by {@link Replacer#Replacer(Object...)}
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #getDefaultReplacer()
	 * @see #setDefaultReplacer(Replacer)
	 * @see #setDefaultObjReplacer(Object...)
	 */
	public void setDefaultPathReplacer(@NotNull String... replacements) {
		final Object[] rep = new String[replacements.length];
		for (int i = 0; i < replacements.length; i ++) {
			final String element = i % 2 == 0 ? replacements[i] : getString(replacements[i]);
			rep[i] = element == null ? "null" : element;
		}
		this.defReplacer = new Replacer(rep);
	}

	/**
	 * Gets a {@link Replacer#clone() clone} of the default {@link Replacer}
	 * being used by this file.
	 * 
	 * @return A {@link Replacer#clone() clone} of The default {@link Replacer},
	 * {@code null} if no {@link Replacer} has been specified or if it has been
	 * explicitly set to {@code null}.
	 * 
	 * @since MCUtils 1.0.0
	 * 
	 * @see #setDefaultReplacer(Replacer)
	 */
	@Nullable
	public Replacer getDefaultReplacer() {
		return defReplacer == null ? null : defReplacer.clone();
	}

	/*
	 * MessagesFileHolder implementation
	 */

	@Nullable
	@Override
	public String getMessage(@NotNull String path) {
		final String str = super.getString(path);
		if (str == null)
			return null;
		return MCStrings.applyColor(defReplacer == null ? str : defReplacer.replaceAt(str));
	}

	@Nullable
	@Override
	public String getMessage(@NotNull String path, @NotNull Replacer rep) {
		final String str = super.getString(path);
		if (str == null)
			return null;
		final Replacer finalRep = defReplacer == null ? rep : getDefaultReplacer().add(rep);
		return MCStrings.applyColor(finalRep.replaceAt(str));
	}

	@Nullable
	@Override
	public String getMessage(@NotNull String path, @NotNull Object... replacements) {
		final String str = super.getString(path);
		if (str == null)
			return null;
		final Replacer finalRep = defReplacer == null ? new Replacer(replacements) : getDefaultReplacer().add(replacements);
		return MCStrings.applyColor(finalRep.replaceAt(str));
	}
}
