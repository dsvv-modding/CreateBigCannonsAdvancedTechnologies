package com.dsvv.cbcat.cannon.rocketpod.munitions.flak_rocket;

import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractFuzedRocket;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectile;

public class Flak_Rocket extends AbstractFuzedRocket<FlakAutocannonProjectile> {
    public Flak_Rocket(EntityType<? extends AbstractRocket> type, Level level) {
        super(type, level, 0.025, 2.5, CBCEntityTypes.FLAK_AUTOCANNON);
    }
}
