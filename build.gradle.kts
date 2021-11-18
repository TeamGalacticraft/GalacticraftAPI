/*
 * Copyright (c) 2019-2021 Team Galacticraft
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.time.Year
import java.time.format.DateTimeFormatter

plugins {
    java
    `maven-publish`
    id("fabric-loom") version "0.10-SNAPSHOT"
    id("org.cadixdev.licenser") version "0.6.1"
   id("io.github.juuxel.loom-quiltflower-mini") version("1.1.0")
}

val mc = "1.18-pre4"
val yarn = "3"
val loader = "0.12.5"
val fabric = "0.42.7+1.18"
val junit = "5.8.1"
val gameunit = "0.1.0"

group = "dev.galacticraft"
version ="0.4.0-prealpha.20+$mc"
println("GalacticraftAPI: $version")
base.archivesName.set("GalacticraftAPI")

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
    withJavadocJar()
}

val gametestSourceSet = sourceSets.create("gametest") {
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
            vmArg("-Dfabric-api.gametest")
        }
        register("gametestClient") {
            client()
            name("Game Test Client")
            source(gametestSourceSet)
            property("fabric.log.level", "debug")
            vmArg("-Dfabric-api.gametest")
        }
    }
}

repositories {
    maven("https://alexiil.uk/maven/") {
        content {
            includeGroup("alexiil.mc.lib")
        }
    }
    maven("https://maven.galacticraft.dev") {
        content {
            includeGroup("dev.galacticraft")
        }
    }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:$mc")
    mappings("net.fabricmc:yarn:$mc+build.$yarn:v2")
    modImplementation("net.fabricmc:fabric-loader:$loader")

    listOf(
        "fabric-api-base",
        "fabric-api-lookup-api-v1",
        "fabric-command-api-v1",
        "fabric-gametest-api-v1",
        "fabric-lifecycle-events-v1",
        "fabric-networking-api-v1",
        "fabric-registry-sync-v0",
        "fabric-renderer-api-v1",
        "fabric-resource-loader-v0",
        "fabric-transfer-api-v1"
    ).forEach {
        modImplementation(fabricApi.module(it, fabric))
    }

    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:$fabric")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
    modRuntime("dev.galacticraft:GameUnit:$gameunit")
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

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    dependsOn(tasks.checkLicenses)
    options.encoding = "UTF-8"
    options.release.set(17)
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

            from(components["java"])
        }
    }
    repositories {
        maven("https://maven.galacticraft.dev/") {
            name = "maven"
            credentials(PasswordCredentials::class)
            authentication {
                register("basic", BasicAuthentication::class)
            }
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
gametestSourceSet.compileClasspath += sourceSets.main.get().compileClasspath + sourceSets.main.get().output
gametestSourceSet.runtimeClasspath += sourceSets.main.get().runtimeClasspath + sourceSets.main.get().output