import org.gradle.api.tasks.compile.JavaCompile

plugins {
    `java-library`
}

// Minecraft 26.3 ships Java 25 class files (major 69), so the mod cannot target anything older.
tasks.withType<JavaCompile>().configureEach {
    options.release.set(25)
}

tasks {
    processResources {
        filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
            expand(
                "id" to "hurricane",
                "name" to "Hurricane",
                "version" to project.version,
                "description" to "Hacky fixes to make Bedrock players that join via Geyser happy. Fabric port of Hurricane by GeyserMC.",
                "url" to "https://geysermc.org",
                "author" to "onebeastchris",
                "minecraft_version" to "26.3"
            )
        }
    }
}