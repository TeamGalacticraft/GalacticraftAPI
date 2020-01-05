# Addon-API
The [Galacticraft: Rewoven](https://github.com/StellarHorizons/Addon-API) addon API.

## Feautes
Here are a list of features, both planned an implemented.

* [x] Addon entrypoint loading.
* [x] Celestial bodies.
* [x] Celestial body registry.
* [ ] Celestial body display info.
* [ ] Rockets/Rocket parts.

## Quickstart

### Installing
You will need to add the Horizon Studio maven repository to your mod's `build.gradle`.

```gradle
repositories {
    maven { url "https://mvn.hrzn.studio" }
    // NOTE: this is currently offline
}
```

After that you can add Galacticraft and the Addon API.

```gradle
dependencies {
    // add both the addon api and mod
    modImplementation("com.hrznstudio:Addon-API:{VERSION}")
    modRuntime("com.hrznstudio:Galacticraft-Rewoven:{VERSION}")
    // but only include the addon api 
    include "com.hrznstudio:Addon-API:{VERSION}"
}
```

You will also need to add the addon API to your `fabric.mod.json` in the `requires` section.
```json
{
    "requires": {
        "galacitcraft-api": "^{VERSION}"
    },
    "suggests": {
        "galacticraft-rewoven": "^{VERSION}"
    }
}
```

Replace `{VERSION}` with the version of Galacticraft you want to use. Versions use the format `MAJOR.MINOR.PATCH[-TYPE]+MC_VERSION`, eg. `0.1.0-alpha+1.14.4` & `1.0.0+1.15.1`. Note: GC:R wont be supported for every Minecraft version, only the final patch for a minor. (Eg. We wont support 1.14 because 1.14.4 exists)

For more versioning information visit the [semver](https://semver.org/) website.

### The Addon Entry Point
Your addon needs somewhere to start, so why take adantage of the same system Fabric uses?

To start you will need to create a new class that implements `GCAddonInitializer`. (You will still need to setup the normal Fabric mod initializers.)

```java
import com.hrznstudio.galacticraft.api.addon.*;

public class MyNewAddonGCHook implements GCAddonInitializer {
    @Override
    // boolean to tell the api if your addon loaded corretly.
    public boolean onInitialize() {
        return true;
    }

    @Override
    public String getModId() {
        return "mynewaddon";
    }
}
```
And add this class to your `fabric.mod.json` in the entrypoints section.

```json

{
    "entrypoints": {
        "gc_addon": "mynewaddon.hooks.MyNewAddonGCHook"
    }
}
```
This will tell the addon api to load your addon from here.
### More Examples
An example addon has been created to make it easier for developers to see how feautre of the API are used. The example addon can be found in [StellarHorizons/ExampleAddon]().