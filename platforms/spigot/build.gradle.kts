
plugins {
	mcutils.`library-conventions`
}

repositories {
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
	api(project(":api"))
	compileOnly(libs.spigot)
	implementation(libs.jetbrains.annotations)
}
