package com.dsvv.cbcat.cannon.medium_rocketpod.munitions.medium_heat_rocket;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.heat_shell.HA_HEATProjectile;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.AbstractMediumFuzedRocket;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.AbstractMediumRocket;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MediumHEATRocket extends AbstractMediumFuzedRocket<HA_HEATProjectile> {
    public MediumHEATRocket(EntityType<? extends AbstractMediumRocket> type, Level level) {
        super(type, level, 0.05, 3.75, EntityRegister.HA_HEAT_PROJECTILE);
    }
}
