package net.codersky.mcutils.storage.files.yaml;

import net.codersky.mcutils.storage.Config;
import net.codersky.mcutils.Reloadable;
import net.codersky.mcutils.java.MCCollections;
import net.codersky.mcutils.java.MCFiles;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class YamlConfig extends Config implements Reloadable {

	private final File file;
	private final Yaml yaml;

	public YamlConfig(String path) {
		final DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		this.yaml = new Yaml(dumperOptions);
		this.file = new File(path);
	}

	/*
	 * File utils
	 */

	@NotNull
	public File asFile() {
		return file;
	}

	public boolean exists() {
		return file.exists();
	}

	/*
	 * Reloadable implementation
	 */

	@Override
	public boolean setup() {
		return MCFiles.create(file);
	}

	@Override
	public boolean reload() {
		try {
			clear();
			this.keys.putAll(this.yaml.load(new FileInputStream(this.file)));
			return true;
		} catch (FileNotFoundException | SecurityException ex) {
			return false;
		}
	}

	@Override
	public boolean save() {
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
			writer.write(yaml.dump(keys));
			writer.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
