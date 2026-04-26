package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.ap_shot;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class HA_APProjectile extends AbstractHeavyAutocannonProjectile
{
    public HA_APProjectile(EntityType<? extends AbstractHeavyAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    @Nonnull
    @Override
    public EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                30,
                false,
                true,
                false,
                1.825f
        );
    }

    @Nonnull
    @Override
    protected BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.03,
                0.013,
                true,
                3.25f,
                2.75f,
                1.33f,
                0.66f
        );
    }
}
