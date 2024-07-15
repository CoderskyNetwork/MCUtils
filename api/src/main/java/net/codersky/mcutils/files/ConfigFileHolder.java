package net.codersky.mcutils.files;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigFileHolder extends FileUpdater {

	@Nullable
	String getString(@NotNull String path, @Nullable String def);

	@Nullable
	default String getString(@NotNull String path) {
		return getString(path, null);
	}
}
