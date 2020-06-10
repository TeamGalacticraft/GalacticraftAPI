# Addon-API
The [Galacticraft: Rewoven](https://github.com/StellarHorizons/Galacticraft-Rewoven) addon API.

## Feautes
Here are a list of features, both planned and implemented.

* [x] Celestial bodies.
* [x] Celestial body registry.
* [x] Celestial body display info.
* [x] Atmospheric Info.
* [x] Atmospheric Gases.
* [ ] Rockets/Rocket parts.

## Quickstart

### Installing
You will need to add the Horizon Studio maven repository to your mod's `build.gradle`.

```gradle
repositories {
    maven { url "https://cdn.hrzn.studio/maven" }
}
```

After that you can add Galacticraft and the Addon API.

```gradle
dependencies {
    // add both the addon api and mod
    modImplementation("com.hrznstudio:GalacticraftAPI:{VERSION}")
    //modRuntime("com.hrznstudio:Galacticraft-Rewoven:{VERSION}") // not up yet
    // but only include the addon api 
    include "com.hrznstudio:GalacticraftAPI:{VERSION}"
}
```

You will also need to add the addon API to your `fabric.mod.json` in the `depends` section.
```json
{
    "depends": {
        "galacticraft-api": "^{VERSION}"
    },
    "suggests": {
        "galacticraft-rewoven": "^{VERSION}"
    }
}
```

Replace `{VERSION}` with the version of Galacticraft you want to use. Versions use the format `MAJOR.MINOR.PATCH[-TYPE]+MC_VERSION`, eg. `0.1.0-alpha+1.14.4` & `1.0.0+1.15.1`. Note: GC:R wont be supported for every Minecraft version, only the final patch for a minor. (Eg. We wont support 1.14 because 1.14.4 exists)

For more versioning information visit the [semver](https://semver.org/) website.

### Adding Celestial Bodies
A Celestial Body is, at its core, a dimension with a fancy name. To learn how to make a dimension you can follow [this](https://fabricmc.net/wiki/tutorial:dimension) tutorial on the FabricMC wiki.

Now you have your dimension you can register it as a celestial body. Let's start by creating a `CelestialBodyType`, for this example we'll setup the Moon.

```java
public class MyCelestialBodies {
    public static final CelestialBodyType MOON = new CelestialBodyType.Builder(
        new Identifier("mymod", "moon")) // the body's id
            .translationKey("ui.mymod.bodies.moon") // the translation key for its name
            .dimension(GalacticraftDimensions.MOON) // the dimension its linked to
            .parent(CelestialBodyType.EARTH) // the parent body
            .weight(1) // what rocket tier is required to access it
            .gravity(0.16f) // the gravity of the planet. NOTE: the overworld is 1.0f.
            .display(
                new CelestialBodyDisplayInfo.Builder()
                    .texture(new Identifier("mymod", "planet_icons")) // icon identifier
                    .distance(5d) // distance from parent body
                    .time(46656000d) // 27 mc days in ticks
                    .build())
            .build();
}
```
Now we have our body type let's go ahead and register it in your mod's main initializer.
```java
public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CelestialBodyRegistryCallback.EVENT.register(registry -> Registry.register(registry, MyCelestialBodies.MOON.getId(), MyCelestialBodies.MOON));
    }
}
```
You're done, enjoy your new celestial body.