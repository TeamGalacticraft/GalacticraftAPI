/*
 * Copyright (c) 2019-2022 Team Galacticraft
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

import java.time.format.DateTimeFormatter

plugins {
    java
    `maven-publish`
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("org.cadixdev.licenser") version "0.6.1"
    id("io.github.juuxel.loom-quiltflower") version("1.7.3")
}

val modId           = project.property("mod.id").toString()
val modVersion      = project.property("mod.version").toString()
val modName         = project.property("mod.name").toString()
val modGroup        = project.property("mod.group").toString()

val minecraft       = project.property("minecraft.version").toString()
val loader          = project.property("loader.version").toString()
val fabric          = project.property("fabric.version").toString()
val machinelib      = project.property("machinelib.version").toString()
val dyndims         = project.property("dyndims.version").toString()

group = modGroup
version ="$modVersion+$minecraft"

base.archivesName.set(modName)

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
}

sourceSets {
    create("gametest") {
        java {
            srcDir("src/gametest/java")
        }
        resources {
            srcDir("src/gametest/resources")
        }
    }
}

loom {
    accessWidenerPath.set(project.file("src/main/resources/${modId}.accesswidener"))
    mixin {
        add(sourceSets.main.get(), "${modId}.refmap.json")
    }

    runs {
        register("gametest") {
            server()
            name("Game Test")
            source(sourceSets.getByName("gametest"))
            property("fabric.log.level", "debug")
            vmArgs("-Dfabric-api.gametest", "-Dfabric-api.gametest.report-file=${project.buildDir}/junit.xml", "-ea")
        }
        register("gametestClient") {
            server()
            name("Game Test Client")
            source(sourceSets.getByName("gametest"))
            property("fabric.log.level", "debug")
            vmArgs("-Dfabric-api.gametest", "-Dfabric-api.gametest.report-file=${project.buildDir}/junit.xml", "-ea")
        }
    }
}

repositories {
    mavenLocal()
    maven("https://maven.galacticraft.net/repository/maven-releases/") {
        name = "Galacticraft Repository"
        content {
            includeGroup("dev.galacticraft")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraft")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$loader")

    listOf(
        "fabric-api-base",
        "fabric-api-lookup-api-v1",
        "fabric-command-api-v2",
        "fabric-convention-tags-v1",
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

    modImplementation("dev.galacticraft:MachineLib:$machinelib")
//    modImplementation("dev.galacticraft:dyndims:$dyndims")
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:$fabric")
    "gametestImplementation"(sourceSets.main.get().output)
    "gametestImplementation"(sourceSets.main.get().compileClasspath)
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
    options.release.set(17)
}

tasks.jar {
    from("LICENSE")
    manifest {
        attributes(
            "Implementation-Title"     to modName,
            "Implementation-Version"   to "${project.version}",
            "Implementation-Vendor"    to "Team Galacticraft",
            "Implementation-Timestamp" to DateTimeFormatter.ISO_DATE_TIME,
            "Maven-Artifact"           to "${project.group}:${modName}:${project.version}",
            "ModSide"                  to "BOTH"
        )
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            groupId = "dev.galacticraft"
            artifactId = modName

            from(components["java"])
        }
    }
    repositories {
        if (System.getenv().containsKey("NEXUS_REPOSITORY_URL")) {
            maven(System.getenv("NEXUS_REPOSITORY_URL")) {
                credentials {
                    username = System.getenv("NEXUS_USER")
                    password = System.getenv("NEXUS_PASSWORD")
                }
            }
        } else {
            println("No nexus repository url found, publishing to local maven repo")
            mavenLocal()
        }
    }
}

tasks.javadoc {
    exclude("**/impl/**")
}

license {
    setHeader(project.file("LICENSE_HEADER.txt"))
    include("**/dev/galacticraft/**/*.java")
    include("build.gradle.kts")
    ext {
        set("year", "2022")
        set("company", "Team Galacticraft")
    }
}

tasks.named<ProcessResources>("processGametestResources") {
    duplicatesStrategy = DuplicatesStrategy.WARN
}

tasks.getByName("gametestClasses").dependsOn("classes")
