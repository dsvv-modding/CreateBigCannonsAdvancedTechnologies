package com.dsvv.cbcat.cannon.medium_rocketpod.munitions.medium_hef_rocket;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.hef_shell.HA_HEFProjectile;
import com.dsvv.cbcat.registry.EntityRegister;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.AbstractMediumFuzedRocket;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.AbstractMediumRocket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MediumHEFRocket extends AbstractMediumFuzedRocket<HA_HEFProjectile> {
    public MediumHEFRocket(EntityType<? extends AbstractMediumRocket> type, Level level) {
        super(type, level, 0.05, 3.75, EntityRegister.HA_HEF_PROJECTILE);
    }
}
