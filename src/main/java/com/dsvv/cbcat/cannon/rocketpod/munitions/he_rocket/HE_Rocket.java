package com.dsvv.cbcat.cannon.rocketpod.munitions.he_rocket;

import com.dsvv.cbcat.cannon.autocannon.munitions.he.AutocannonHEProjectile;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractFuzedRocket;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocket;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectile;

public class HE_Rocket extends AbstractFuzedRocket<AutocannonHEProjectile> {
    public HE_Rocket(EntityType<? extends AbstractRocket> type, Level level) {
        super(type, level, 0.025, 2.5, EntityRegister.HE_PROJECTILE);
    }
}
