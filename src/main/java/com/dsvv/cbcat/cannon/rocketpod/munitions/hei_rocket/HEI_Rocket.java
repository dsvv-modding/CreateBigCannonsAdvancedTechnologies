package com.dsvv.cbcat.cannon.rocketpod.munitions.hei_rocket;

import com.dsvv.cbcat.cannon.autocannon.munitions.hei.AutocannonHEIProjectile;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractFuzedRocket;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocket;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class HEI_Rocket extends AbstractFuzedRocket<AutocannonHEIProjectile> {
    public HEI_Rocket(EntityType<? extends AbstractRocket> type, Level level) {
        super(type, level, 0.025, 2.5, EntityRegister.HEI_PROJECTILE);
    }
}
