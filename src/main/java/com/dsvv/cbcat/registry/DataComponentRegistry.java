package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.function.UnaryOperator;

public class DataComponentRegistry {
    public static final DataComponentType<Byte> ROCKET_FUEL = register("rocket_fuel",
            builder -> builder.persistent(Codec.BYTE).networkSynchronized(ByteBufCodecs.BYTE));

    public static final DataComponentType<Boolean> HA_STRONG_ROUND = register("ha_strong_round",
            builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DataComponentType<ItemContainerContents> CLUSTER_FUZES = register("cluster_fuzes",
            builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC));

    public static final DataComponentType<String> CLUSTER_PROJECTILE = register("cluster_projectile",
            builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(CreateBigCannons_AdvancedTechnology.MOD_ID, name), builder.apply(DataComponentType.builder()).build());
    }

    public static void register() {}
}
