import java.time.Year
import java.time.format.DateTimeFormatter

plugins {
    java
    `maven-publish`
    id("fabric-loom") version "0.8-SNAPSHOT"
    id("org.cadixdev.licenser") version "0.5.1"
}

val mc = "1.17"
val yarn = "10"
val loader = "0.11.3"
val fabric = "0.35.1+1.17"
val lba = "0.9.0"

group = "dev.galacticraft"
version ="0.4.0-prealpha6+$mc"

base {
    archivesBaseName = "GalacticraftAPI"
}

val testmodSourceSet = sourceSets.create("testmod") {
    compileClasspath += sourceSets.main.get().compileClasspath
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
    java.srcDir("src/testmod/java")
    resources.srcDir("src/testmod/resources")
}

loom {
    refmapName = "galacticraft-api.refmap.json"
    accessWidener(project.file("src/main/resources/galacticraft-api.accesswidener"))

    runs {
        register("TestModClient") {
            client()
            source(testmodSourceSet)
            vmArgs("-ea")
            property("fabric.log.level", "debug")
            name("TestMod Client")
        }
        register("TestModServer") {
            server()
            source(testmodSourceSet)
            vmArgs("-ea",)
            property("fabric.log.level", "debug")
            name("TestMod Server")
        }
    }
}

val testmodCompile by configurations.creating { extendsFrom(configurations.runtimeOnly.get()) }

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
        "fabric-lifecycle-events-v1",
        "fabric-networking-api-v1",
        "fabric-registry-sync-v0",
        "fabric-renderer-api-v1",
        "fabric-resource-loader-v0",
    ).forEach {
        modImplementation(fabricApi.module(it, fabric))
    }

    modImplementation("alexiil.mc.lib:libblockattributes-core:$lba")
    modRuntime("net.fabricmc.fabric-api:fabric-api:$fabric")

    testmodCompile(sourceSets.main.get().output)
}

tasks.processResources {
    inputs.property("version", project.version)
    duplicatesStrategy = DuplicatesStrategy.WARN

    from(sourceSets.main.get().resources.srcDirs) {
        include("fabric.mod.json")
        expand(mutableMapOf("version" to project.version))
    }

    from(sourceSets.main.get().resources.srcDirs) {
        exclude("fabric.mod.json")
    }
}

tasks.getByName<ProcessResources>("processTestmodResources") {
    duplicatesStrategy = DuplicatesStrategy.WARN
}

java {
    withSourcesJar()
    withJavadocJar()

    targetCompatibility = JavaVersion.VERSION_16
    sourceCompatibility = JavaVersion.VERSION_16
}

tasks.withType<JavaCompile> {
    dependsOn(tasks.checkLicenses)
    options.encoding = "UTF-8"
    options.release.set(16)
}

val sourcesJar = tasks.create<Jar>("sourcesJarGC") {
    dependsOn(tasks.classes)
    classifier = "sources"
    from(sourceSets.main.get().allSource)
}

val javadocJar = tasks.create<Jar>("javadocJarGC") {
    classifier = "javadoc"
    from(tasks.javadoc)
}

tasks.remapSourcesJar.configure {
    dependsOn.add(sourcesJar)
}

tasks.jar {
    from("LICENSE")
    manifest {
        attributes(mapOf(
            "Implementation-Title"     to "GalacticraftAPI",
            "Implementation-Version"   to "${project.version}",
            "Implementation-Vendor"    to "Team Galacticraft",
            "Implementation-Timestamp" to DateTimeFormatter.ISO_DATE_TIME,
            "Maven-Artifact"           to "${project.group}:GalacticraftAPI:${project.version}",
            "ModSide"                  to "BOTH"
        ))
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
    header = project.file("LICENSE_HEADER.txt")
    include("**/dev/galacticraft/**/*.java")
    include("build.gradle.kts")
    ext {
        set("year", Year.now().value)
        set("company", "Team Galacticraft")
    }
}