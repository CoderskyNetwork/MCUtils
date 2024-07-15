plugins {
    java
    io.github.goooler.shadow
}

tasks {
    shadowJar {
        archiveFileName = "MCUtils-${project.name}-${rootProject.version}.jar"
        archiveClassifier = null

        relocate("org.jetbrains.annotations", "net.codersky.mcutils.shaded.jetbrains.annotations")
        relocate("org.intellij.lang.annotations", "net.codersky.mcutils.shaded.intellij.annotations")

        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
    }
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())