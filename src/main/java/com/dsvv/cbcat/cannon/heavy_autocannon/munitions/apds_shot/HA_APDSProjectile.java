package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.apds_shot;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class HA_APDSProjectile extends AbstractHeavyAutocannonProjectile
{
    public HA_APDSProjectile(EntityType<? extends AbstractHeavyAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    @Nonnull
    @Override
    public EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                25,
                false,
                true,
                false,
                1.3f
        );
    }

    @Nonnull
    @Override
    protected BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.02,
                0.011,
                true,
                3f,
                3.1f,
                1.45f,
                0.66f
        );
    }
}
