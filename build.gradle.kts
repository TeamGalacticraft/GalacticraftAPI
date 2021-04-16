import net.fabricmc.loom.task.RunClientTask
import net.fabricmc.loom.task.RunServerTask
import java.time.Year
import java.time.format.DateTimeFormatter

plugins {
    java
    `maven-publish`
    id("fabric-loom") version "0.6-SNAPSHOT"
    id("org.cadixdev.licenser") version "0.5.0"
}

val mc = "1.16.5"
val yarn = "4"
val loader = "0.11.1"
val fabric = "0.30.0+1.16"

group = "dev.galacticraft"
version ="0.3.1-alpha+$mc"
base {
    archivesBaseName = "GalacticraftAPI"
}

loom {
    refmapName = "galacticraft-api.refmap.json"
    accessWidener("src/main/resources/galacticraft-api.accesswidener")
}

val testmodCompile by configurations.creating { extendsFrom(configurations.compile.get()) }

dependencies {
    minecraft("com.mojang:minecraft:$mc")
    mappings("net.fabricmc:yarn:$mc+build.$yarn:v2")
    modImplementation("net.fabricmc:fabric-loader:$loader")
	
	modImplementation(fabricApi.module("fabric-api-base", fabric))
    modImplementation(fabricApi.module("fabric-command-api-v1", fabric))
    modImplementation(fabricApi.module("fabric-lifecycle-events-v1", fabric))
    modImplementation(fabricApi.module("fabric-registry-sync-v0", fabric))
    modImplementation(fabricApi.module("fabric-resource-loader-v0", fabric))

    testmodCompile(sourceSets.main.get().output)
}

val testmodSourceSet = sourceSets.create("testmod") {
    compileClasspath += sourceSets.main.get().compileClasspath
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
    java.srcDir("src/testmod/java")
    resources.srcDir("src/testmod/resources")
}

tasks.processResources {
    inputs.property("version", project.version)

    from(sourceSets.main.get().resources.srcDirs) {
        include("fabric.mod.json")
        expand(mutableMapOf("version" to project.version))
    }

    from(sourceSets.main.get().resources.srcDirs) {
        exclude("fabric.mod.json")
    }
}

java {
    withSourcesJar()
    withJavadocJar()

    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
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

val runTestmodClient = tasks.create<RunClientTask>("runTestmodClient") {
    classpath(sourceSets.getByName("testmod").runtimeClasspath)
}

val runTestmodServer = tasks.create<RunServerTask>("runTestmodServer") {
    classpath(sourceSets.getByName("testmod").runtimeClasspath)
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
        maven {
            setUrl("s3://maven.galacticraft.dev")
            authentication {
                register("awsIm", AwsImAuthentication::class)
            }
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