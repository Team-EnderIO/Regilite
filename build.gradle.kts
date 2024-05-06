import java.net.URI

plugins {
    id("java-library")
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("net.neoforged.gradle.userdev") version "7.0.109"
}

val minecraft_version: String by project
val minecraft_version_range: String by project

val neo_version: String by project
val neo_version_range: String by project
val loader_version_range: String by project

val testmod_id = "examplemod"

version = getVersionString()
group = "com.enderio"

repositories {
    mavenLocal()
}

base {
    archivesName.set("Regilite")
}

// Mojang ships Java 21 to end users in 1.20.5+, so your mod should target Java 21.
java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

runs {
    configureEach {
        systemProperty("forge.logging.markers", "REGISTRIES")
        systemProperty("forge.logging.console.level", "debug")

        modSource(project.sourceSets.main.get())
        modSource(project.sourceSets.test.get())
    }

    create("client") {
        // Comma-separated list of namespaces to load gametests from. Empty = all namespaces.
        systemProperty("forge.enabledGameTestNamespaces", testmod_id)
    }

    create("server") {
        systemProperty("forge.enabledGameTestNamespaces", testmod_id)
        programArgument("--nogui")
    }

    // This run config launches GameTestServer and runs all registered gametests, then exits.
    // By default, the server will crash when no gametests are provided.
    // The gametest system is also enabled by default for other run configs under the /test command.
    create("gameTestServer") {
        systemProperty("forge.enabledGameTestNamespaces", testmod_id)
    }

    create("data") {
        // example of overriding the workingDirectory set in configureEach above, uncomment if you want to use it
        // workingDirectory project.file('run-data')

        // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
        programArguments.addAll(
            "--mod", testmod_id,
            "--all",
            "--output", file("src/generated/resources/").getAbsolutePath(),
            "--existing", file("src/test/resources/").getAbsolutePath())
    }
}

sourceSets {
    main {
        java {}
    }
    test {
        java {
            compileClasspath += main.get().output
            runtimeClasspath += main.get().output
        }
    }
}

//sourceSets.test.runs {
//    modIdentifier = "regilite"
//}

// Include resources generated by data generators.
sourceSets.test {
    resources.srcDir("src/generated/resources")
}

dependencies {
    implementation("net.neoforged:neoforge:${neo_version}")
}

tasks.withType<ProcessResources>().configureEach {
    var replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "neo_version" to neo_version,
        "neo_version_range" to neo_version_range,
        "loader_version_range" to loader_version_range,
    )
    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/neoforge.mods.toml", "pack.mcmeta")) {
        expand(replaceProperties)
        expand(mutableMapOf("project" to project))
    }
}

java.withSourcesJar()

tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
            "FMLModType" to "GAMELIBRARY",
        ))
    }
}

publishing {
    publications {

        create<MavenPublication>("regilite") {
            groupId = "com.enderio"
            artifactId = "Regilite"
            version = version

            artifact(tasks.getByName("jar"))
            artifact(tasks.getByName("sourcesJar"))

            pom {
                name.set("Regilite")
                description.set("Regilite is a helper library that handles registries and basic data generation.")
                url.set("https://github.com/Team-EnderIO/Regilite")

                licenses {
                    license {
                        name.set("Unlicense")
                        url.set("https://github.com/Team-EnderIO/Regilite/blob/dev/${minecraft_version}/LICENSE.txt")
                    }
                }

                scm {
                    url.set("https://github.com/Team-EnderIO/Regilite.git")
                }
            }
        }
    }

    repositories {
        if (System.getenv("RVR_MAVEN_USER") != null) {
            maven {
                name = "Rover656"
                url = URI("https://maven.rover656.dev/releases")

                credentials {
                    username = System.getenv("RVR_MAVEN_USER")
                    password = System.getenv("RVR_MAVEN_PASSWORD")
                }
            }
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8" // Use the UTF-8 charset for Java compilation
}

// ============
// Utilities
// ============

//   * enderio-1.19.1-6.0.1-alpha.jar :: release version 6.0.1-alpha for mc 1.19.1
//   * enderio-1.19.1-nightly-4       :: nightly build no. 4 for mc 1.19.1
//   * enderio-1.19.1-dev-c91c8ee6e   :: dev (local) build for commit c91c8ee6e
fun getVersionString(): String {
    val build_server = System.getenv("CI") != null || System.getenv("BUILD_NUMBER") != null

    if (System.getenv("BUILD_VERSION") != null) {
        var version_number = System.getenv("BUILD_VERSION")
        if (version_number.startsWith("v")) {
            version_number = version_number.substring(1)
        }

        return "${minecraft_version}-${version_number}"
    }

    if (System.getenv("NIGHTLY") != null) {
        var version_patch_lc = "0"
        if (System.getenv("BUILD_NUMBER") != null) {
            version_patch_lc = System.getenv("BUILD_NUMBER")
        }

        return "${minecraft_version}-nightly-${version_patch_lc}"
    }

    var version_hash = ""
    var branch_name = ""
    if (!build_server) {
        try {
            version_hash = "-" + shellRunAndRead("git rev-parse --short HEAD").trim()
        } catch (ignored: Exception) {
        }

        try {
            branch_name = shellRunAndRead("git rev-parse --abbrev-ref HEAD").trim()
            branch_name = "-" + branch_name.substring(branch_name.lastIndexOf("/") + 1)
        } catch (ignored: Exception) {
        }
    }

    return "${minecraft_version}-dev${branch_name}${version_hash}"
}

fun shellRunAndRead(command: String): String {
    val process = ProcessBuilder()
        .command(command.split(" "))
        .directory(rootProject.projectDir)
        .start()
    return process.inputStream.bufferedReader().readText()
}