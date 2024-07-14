
group = "net.codersky"
version = "1.0.0"
description = "An open source collection of utilities for Spigot plugins designed to make your life easier"

plugins {
	`java-library`
}

java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
	mavenCentral()
}

java {
	withSourcesJar()
	withJavadocJar()
}

tasks {

	jar {
		// Set jar file name to "MCUtils-platform [version].jar"
		archiveFileName.set("MCUtils-${project.name} [$version].jar")
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
