package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.heat_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class HA_CopperRay extends AbstractHeavyAutocannonProjectile {
    public HA_CopperRay(EntityType<? extends AbstractHeavyAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public @NotNull EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                50,
                false,
                true,
                false,
                1.2f
        );
    }

    @Override
    protected @NotNull BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.04,
                0.01,
                true,
                2.75f,
                5f,
                2f,
                0.7f
        );
    }
}
