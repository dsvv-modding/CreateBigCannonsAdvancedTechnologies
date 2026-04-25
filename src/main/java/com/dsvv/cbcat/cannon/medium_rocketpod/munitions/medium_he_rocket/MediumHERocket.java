package com.dsvv.cbcat.cannon.medium_rocketpod.munitions.medium_he_rocket;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.he_shell.HA_HEProjectile;
import com.dsvv.cbcat.registry.EntityRegister;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.AbstractMediumFuzedRocket;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.AbstractMediumRocket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MediumHERocket extends AbstractMediumFuzedRocket<HA_HEProjectile> {
    public MediumHERocket(EntityType<? extends AbstractMediumRocket> type, Level level) {
        super(type, level, 0.05, 3.75, EntityRegister.HA_HE_PROJECTILE);
    }
}
