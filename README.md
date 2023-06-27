[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.galacticraft.dev%2Fdev%2Fgalacticraft%2FGalacticraftAPI%2Fmaven-metadata.xml&style=flat-square)](https://maven.galacticraft.dev/dev/galacticraft/GalacticraftAPI/)
[![Team Galacticraft Discord](https://img.shields.io/discord/775251052517523467.svg?colorB=7289DA&label=discord&style=flat-square)](https://discord.gg/n3QqhMYyFK)
[![Issues](https://img.shields.io/github/issues/TeamGalacticraft/GalacticraftAPI?style=flat-square)](https://github.com/TeamGalacticraft/GalacticraftAPI/issues)
[![GalacticraftDev Twitch](https://img.shields.io/twitch/status/galacticraftdev.svg?style=flat-square)](https://twitch.tv/GalacticraftDev)
# Galacticraft API
The [Galacticraft](https://github.com/TeamGalacticraft/Galacticraft) addon API.

## Features
Here are a list of features, both planned and implemented.

* [x] Celestial bodies.
* [x] Celestial body registry.
* [x] Celestial body display info.
* [x] Atmospheric Info.
* [x] Atmospheric Gases.
* [ ] Rockets/Rocket parts.

## Installing
In your mod's `build.gradle` (*Groovy*) or `build.gradle.kts` (*Kotlin*):

<details>
<summary>Important Note</summary>
    
Replace `{VERSION}` with the version of Galacticraft you want to use.<br>
Versions use the format `MAJOR.MINOR.PATCH[-TYPE]+MC_VERSION`, e.g. `0.1.0-alpha+1.14.4` & `1.0.0+1.15.1`.<br>
For more versioning information visit the [semver](https://semver.org/) website.

</details>

1. **Add the Team Galacticraft maven repository**
     - <details>
       <summary>Groovy</summary>

       ```groovy
        repositories {
            maven { url "https://maven.galacticraft.dev/repository/maven-releases/" }
        }
        ```

       </details>
     - <details>
       <summary>Kotlin</summary>

       ```kotlin
        repositories {
            maven("https://maven.galacticraft.dev/repository/maven-releases/")
        }
        ```

       </details>
2. **Add Galacticraft API as a dependency**

     > Mostly the same for both Groovy and Kotlin, adjust as needed
    ```groovy
    dependencies {
        // add both the addon api and mod but only include the addon api 
        modImplementation("dev.galacticraft:GalacticraftAPI:{VERSION}")
        //modRuntimeOnly("dev.galacticraft:Galacticraft:{VERSION}") // not up yet
    }
    ```

3. **Add the Addon API to your `fabric.mod.json` in the `depends` section.**
    ```json
    {
        "depends": {
            "galacticraft-api": ">={VERSION}"
        },
        "suggests": {
            "galacticraft": ">={VERSION}"
        }
    }
    ```
