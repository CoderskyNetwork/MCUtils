plugins {
	`java-library`
}

repositories {
	mavenLocal()
	mavenCentral()
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(21)
	withSourcesJar()
	withJavadocJar()
	disableAutoTargetJvm()
}

tasks {
	jar {
		archiveClassifier = "default"
	}

	processResources {
		filesMatching("plugin.yml") {
			expand(project.properties)
		}
	}

	withType<JavaCompile> {
		options.encoding = Charsets.UTF_8.name()
		options.release = 21
	}

	defaultTasks("build")
}
