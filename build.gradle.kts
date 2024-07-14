
group = "net.codersky"
version = "1.0.0"
description = "An open source collection of utilities for Spigot plugins designed to make your life easier"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
	`java-library`
	`maven-publish` // Used for testing (Publish to maven local)
}

repositories {
	mavenCentral() // Used for Spigot's dependencies
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
	compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
}

tasks {
	jar {
		// Set jar file name to "MCUtils [version].jar"
		archiveFileName.set("${project.name} [$version].jar")
	}

	processResources {
		filesMatching("plugin.yml") {
			expand(project.properties)
		}
	}

	compileJava { // Encode project with UTF-8
		options.encoding = "UTF-8"
	}
}
