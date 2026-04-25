package com.dsvv.cbcat.config;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CBCATConfigs
{
    private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

    public static CBCATServerConfig SERVER;

    public static void registerConfigs(BiConsumer<ModConfig.Type, ModConfigSpec> cons)
    {
        SERVER = register(CBCATServerConfig::new, ModConfig.Type.SERVER);

        cons.accept(ModConfig.Type.SERVER, CONFIGS.get(ModConfig.Type.SERVER).specification);
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }
}
