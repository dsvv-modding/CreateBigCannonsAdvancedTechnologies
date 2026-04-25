package com.dsvv.cbcat.cannon.medium_rocketpod.munitions;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import javax.annotation.Nullable;

public interface MediumRocketItem {
    @Nullable
    AbstractMediumRocket getAutocannonProjectile(ItemStack stack, Level level);
    @Nullable
    EntityType<?> getEntityType(ItemStack stack);

    AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack);

    boolean isTracer(ItemStack stack);
    void setTracer(ItemStack stack, boolean value);

    MediumRocketType getType();
}
