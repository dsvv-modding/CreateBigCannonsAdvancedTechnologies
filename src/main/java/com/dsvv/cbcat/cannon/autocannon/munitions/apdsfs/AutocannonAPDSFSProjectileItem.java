package com.dsvv.cbcat.cannon.autocannon.munitions.apdsfs;

import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

public class AutocannonAPDSFSProjectileItem extends AutocannonRoundItem
{
    public AutocannonAPDSFSProjectileItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractAutocannonProjectile getAutocannonProjectile(ItemStack itemStack, Level level) {
        return EntityRegister.APDSFS_PROJECTILE.create(level);
    }

    @Override
    public EntityType<?> getEntityType(ItemStack itemStack) {
        return EntityRegister.APDSFS_PROJECTILE.get();
    }

    @Override
    public @NotNull AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
        return new AutocannonProjectilePropertiesComponent(1, true);
    }
}
