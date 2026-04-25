package com.dsvv.cbcat.cannon.heavy_autocannon.munitions;

import com.dsvv.cbcat.registry.DataComponentRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import javax.annotation.Nullable;

public interface HeavyAutocannonAmmoItem {
    @Nullable AbstractHeavyAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level);
    @Nullable
    EntityType<?> getEntityType(ItemStack stack);

    AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack);

    boolean isTracer(ItemStack stack);
    void setTracer(ItemStack stack, boolean value);

    ItemStack getSpentItem(ItemStack stack);

    HeavyAutocannonAmmoType getType();

    default boolean isStrong(ItemStack stack) {
        if (stack.has(DataComponentRegistry.HA_STRONG_ROUND))
            return stack.get(DataComponentRegistry.HA_STRONG_ROUND);
        return false;
    }
}
