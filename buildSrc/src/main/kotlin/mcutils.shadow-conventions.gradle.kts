plugins {
    java
    io.github.goooler.shadow
}

tasks {
    shadowJar {
        archiveFileName = "MCUtils-${project.name}-${rootProject.version}.jar"
        archiveClassifier = null

        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
    }
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())