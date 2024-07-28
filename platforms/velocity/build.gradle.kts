plugins {
	mcutils.`shadow-conventions`
	mcutils.`library-conventions`
}

repositories {
	maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
	api(project(":shared"))
	compileOnly(libs.velocity)
}

