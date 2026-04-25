package com.dsvv.cbcat.cannon.medium_rocketpod.munitions.medium_ap_rocket;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.ap_shot.HA_APProjectile;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.AbstractMediumRocket;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class MediumAPRocket extends AbstractMediumRocket<HA_APProjectile>
{
    public MediumAPRocket(EntityType<? extends AbstractMediumRocket> type, Level level) {
        super(type, level, 0.05, 3.75, EntityRegister.HA_AP_PROJECTILE);
    }
}
