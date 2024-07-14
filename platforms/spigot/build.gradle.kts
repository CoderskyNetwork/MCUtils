plugins {
	mcutils.`library-conventions`
}

repositories {
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
	compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
	api(project(":api"))
}
