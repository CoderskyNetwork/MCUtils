rootProject.name = "MCUtils"

include("api")
include("platforms:spigot")

dependencyResolutionManagement {
	versionCatalogs {
		create("libs") {
			from(files("libs.versions.toml"))
		}
	}
}
