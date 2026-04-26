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
                24.5f,
                false,
                true,
                false,
                1.2f
        );
    }

    @Nonnull
    @Override
    protected BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.0175,
                0.008,
                true,
                2.67f,
                3.33f,
                1.5f,
                0.66f
        );

    }
}
