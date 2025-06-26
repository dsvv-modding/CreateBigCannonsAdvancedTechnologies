package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.apdsfs;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class HA_APDSFSProjectile extends AbstractHeavyAutocannonProjectile
{
    public HA_APDSFSProjectile(EntityType<? extends AbstractHeavyAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    @Nonnull
    @Override
    public EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                26,
                false,
                true,
                false,
                1.25f
        );
    }

    @Nonnull
    @Override
    protected BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.0175,
                0.00325,
                true,
                1.33f,
                1f,
                0.95f,
                0.7f
        );

    }
}
