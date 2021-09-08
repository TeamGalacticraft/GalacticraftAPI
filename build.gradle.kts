import java.time.Year
import java.time.format.DateTimeFormatter

plugins {
    java
    `maven-publish`
    id("fabric-loom") version "0.9-SNAPSHOT"
    id("org.cadixdev.licenser") version "0.6.1"
}

val mc = "1.17.1"
val yarn = "52"
val loader = "0.11.6"
val fabric = "0.40.0+1.17"
val lba = "0.9.1-pre.1"

group = "dev.galacticraft"
version ="0.4.0-prealpha.19+$mc"

base.archivesName.set("GalacticraftAPI")

java {
    targetCompatibility = JavaVersion.VERSION_16
    sourceCompatibility = JavaVersion.VERSION_16

    withSourcesJar()
    withJavadocJar()
}

val gametestSourceSet = sourceSets.create("gametest") {
    compileClasspath += sourceSets.main.get().compileClasspath
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
    java.srcDir("src/gametest/java")
    resources.srcDir("src/gametest/resources")
}

loom {
    accessWidenerPath.set(project.file("src/main/resources/galacticraft-api.accesswidener"))
    mixin {
        add(sourceSets.main.get(), "galacticraft-api.refmap.json")
    }

    runs {
        register("gametest") {
            server()
            name("Game Test")
            source(gametestSourceSet)
            property("fabric.log.level", "debug")
            vmArg("-Dfabric-api.gametest=true")
            vmArg("-ea")
        }
    }
}

repositories {
    maven("https://alexiil.uk/maven/") {
        content {
            includeGroup("alexiil.mc.lib")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$mc")
    mappings("net.fabricmc:yarn:$mc+build.$yarn:v2")
    modImplementation("net.fabricmc:fabric-loader:$loader")

    listOf(
        "fabric-api-base",
        "fabric-command-api-v1",
        "fabric-gametest-api-v1",
        "fabric-lifecycle-events-v1",
        "fabric-networking-api-v1",
        "fabric-registry-sync-v0",
        "fabric-renderer-api-v1",
        "fabric-resource-loader-v0",
    ).forEach {
        modImplementation(fabricApi.module(it, fabric))
    }

    modImplementation("alexiil.mc.lib:libblockattributes-core:$lba")
    modImplementation("alexiil.mc.lib:libblockattributes-items:$lba")
    modRuntime("net.fabricmc.fabric-api:fabric-api:$fabric")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }

    // Minify json resources
    // https://stackoverflow.com/questions/41028030/gradle-minimize-json-resources-in-processresources#41029113
    doLast {
        fileTree(mapOf("dir" to outputs.files.asPath, "includes" to listOf("**/*.json", "**/*.mcmeta"))).forEach {
                file: File -> file.writeText(groovy.json.JsonOutput.toJson(groovy.json.JsonSlurper().parse(file)))
        }
    }
}

tasks.withType<JavaCompile> {
    dependsOn(tasks.checkLicenses)
    options.encoding = "UTF-8"
    options.release.set(16)
}

val sourcesJar = tasks.create<Jar>("sourcesJarGC") {
    dependsOn(tasks.classes)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar = tasks.create<Jar>("javadocJarGC") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

tasks.remapSourcesJar.configure {
    dependsOn.add(sourcesJar)
}

tasks.jar {
    from("LICENSE")
    manifest {
        attributes(
            "Implementation-Title"     to "GalacticraftAPI",
            "Implementation-Version"   to "${project.version}",
            "Implementation-Vendor"    to "Team Galacticraft",
            "Implementation-Timestamp" to DateTimeFormatter.ISO_DATE_TIME,
            "Maven-Artifact"           to "${project.group}:GalacticraftAPI:${project.version}",
            "ModSide"                  to "BOTH"
        )
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = "dev.galacticraft"
            artifactId = "GalacticraftAPI"

            artifact(tasks.remapJar) { builtBy(tasks.remapJar) }
            artifact(sourcesJar) { builtBy(tasks.remapSourcesJar) }
            artifact(javadocJar)
        }
    }
    repositories {
        if (net.fabricmc.loom.util.OperatingSystem.isCIBuild()) {
            maven {
                setUrl("s3://maven.galacticraft.dev")
                authentication {
                    register("awsIm", AwsImAuthentication::class)
                }
            }
        } else {
            mavenLocal()
        }
    }
}

license {
    setHeader(project.file("LICENSE_HEADER.txt"))
    include("**/dev/galacticraft/**/*.java")
    include("build.gradle.kts")
    ext {
        set("year", Year.now().value)
        set("company", "Team Galacticraft")
    }
}

tasks.named<ProcessResources>("processGametestResources") {
    duplicatesStrategy = DuplicatesStrategy.WARN
}

tasks.getByName("gametestClasses").dependsOn("classes")
