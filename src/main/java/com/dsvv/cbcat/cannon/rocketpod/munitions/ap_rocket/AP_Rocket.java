package com.dsvv.cbcat.cannon.rocketpod.munitions.ap_rocket;

import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.autocannon.ap_round.APAutocannonProjectile;

public class AP_Rocket extends AbstractRocket<APAutocannonProjectile>
{
    public AP_Rocket(EntityType<? extends AbstractRocket> type, Level level) {
        super(type, level, 0.025, 2.5, CBCEntityTypes.AP_AUTOCANNON);
    }
}
