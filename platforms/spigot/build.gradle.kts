plugins {
	mcutils.`shadow-conventions`
	mcutils.`library-conventions`
}

dependencies {
	api(project(":api"))
	compileOnly(libs.jetbrains.annotations)
	compileOnly(libs.spigot)
}
