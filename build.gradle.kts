group = "net.codersky"
version = "1.0.0"
description = "An open source collection of utilities for Spigot plugins designed to make your life easier"

tasks {
	wrapper {
		gradleVersion = "8.8"
		distributionType = Wrapper.DistributionType.ALL
	}

	fun subModuleTasks(taskName: String): List<Task> {
		return subprojects
			.filter { it.name != "platforms" }
			.mapNotNull { it.tasks.findByName(taskName) }
	}

	register("build") {
		val subModuleBuildTasks = subModuleTasks("build")
		dependsOn(subModuleBuildTasks)
		group = "build"

		doLast {
			val buildOut = project.layout.buildDirectory.dir("libs").get().asFile.apply {
				if (!exists()) mkdirs()
			}

			subprojects.forEach { subproject ->
				val subIn = subproject.layout.buildDirectory.dir("libs").get().asFile
				if (subIn.exists()) {
					copy {
						from(subIn) {
							include("MCUtils-*.jar")
							exclude("*-javadoc.jar", "*-sources.jar")
						}
						into(buildOut)
					}
				}
			}
		}
	}

	register<Delete>("clean") {
		val cleanTasks = subModuleTasks("clean")
		dependsOn(cleanTasks)
		group = "build"
		delete(rootProject.layout.buildDirectory)
	}

	defaultTasks("build")
}