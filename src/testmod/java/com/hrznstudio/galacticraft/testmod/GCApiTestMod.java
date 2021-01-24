package com.hrznstudio.galacticraft.testmod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hrznstudio.galacticraft.api.celestialbodies.CelestialBodyType;
import com.hrznstudio.galacticraft.api.celestialbodies.SolarSystemType;
import com.hrznstudio.galacticraft.api.regisry.AddonRegistry;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GCApiTestMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void onInitialize() {
        LOGGER.info("Loaded test mod!");
//        MinecraftClient.getInstance().execute(() -> {
//            AddonRegistry.CELESTIAL_BODIES.get(0); CelestialBodyType.EARTH.getAtmosphere();
//            System.out.println(AddonRegistry.SOLAR_SYSTEMS.get(0));
//        DataResult<JsonElement> dataResult = CelestialBodyType.CODEC.encode(CelestialBodyType.EARTH, JsonOps.INSTANCE, new JsonObject());
//        LOGGER.info(dataResult.getOrThrow(false, LOGGER::info));
//        dataResult = SolarSystemType.REGISTRY_CODEC.encode(() -> SolarSystemType.SOL, JsonOps.INSTANCE, new JsonObject());
//        LOGGER.info(dataResult.getOrThrow(false, LOGGER::info));
//            dataResult = CelestialBodyType.CODEC.encode(CelestialBodyType.EARTH, JsonOps.INSTANCE, new JsonObject());
//            LOGGER.info(dataResult.getOrThrow(false, LOGGER::info));
//        });
    }
}
