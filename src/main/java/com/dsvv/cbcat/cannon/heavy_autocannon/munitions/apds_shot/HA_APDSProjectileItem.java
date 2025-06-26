package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.apds_shot;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HA_APDSProjectileItem extends AbstractHeavyAutocannonProjectileItem
{
    public HA_APDSProjectileItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public AbstractHeavyAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
        return EntityRegister.HA_APDS_PROJECTILE.create(level);
    }

    @Override
    public EntityType<?> getEntityType(ItemStack stack) {
        return EntityRegister.HA_APDS_PROJECTILE.get();
    }
}