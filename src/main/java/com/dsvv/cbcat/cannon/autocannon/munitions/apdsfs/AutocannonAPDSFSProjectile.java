package com.dsvv.cbcat.cannon.autocannon.munitions.apdsfs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class AutocannonAPDSFSProjectile extends AbstractAutocannonProjectile {

    public AutocannonAPDSFSProjectile(EntityType<? extends AbstractAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public @NotNull EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                12,
                false,
                true,
                false,
                0.3f
        );
    }

    @Override
    protected @NotNull BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.018,
                0.00875,
                false,
                2.75f,
                2.625f,
                2f,
                0.7f
        );
    }
}
