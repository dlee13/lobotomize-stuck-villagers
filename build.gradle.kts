plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.8"
}

group = "xyz.holocons.mc"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    maven("https://repo.spongepowered.org/maven/")
}

dependencies {
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    compileOnly("space.vectrix.ignite:ignite-api:0.8.0")
    compileOnly("org.spongepowered:mixin:0.8.5")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()

        val modProperties = mapOf(
            "id" to project.name,
            "version" to project.version,
            "main" to "xyz.holocons.mc.lobotomizestuckvillagers.LobotomizeStuckVillagersMod",
        )

        filesMatching("ignite.mod.json") {
            expand(modProperties)
        }
    }
    reobfJar {
        remapperArgs.add("--mixin")
    }
}
