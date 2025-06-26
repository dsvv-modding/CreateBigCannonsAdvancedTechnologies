package com.dsvv.cbcat.base;

import org.jetbrains.annotations.NotNull;

public interface IBigCannonBlockPhysics
{
    default CustomPropellantContext applyBarrelPhysic(@NotNull CustomPropellantContext propCtx)
    {
        CustomPropellantContext newCtx = new CustomPropellantContext();
        newCtx.explosionGas = propCtx.explosionGas - 0.32f;
        newCtx.drag = propCtx.drag + 0.08f * (propCtx.getVelocity() * propCtx.getVelocity());
        newCtx.recoil = propCtx.recoil;
        newCtx.spread = propCtx.spread * 0.8f;
        newCtx.smokeScale = propCtx.smokeScale;
        newCtx.stress = propCtx.stress;
        newCtx.volume = propCtx.volume;
        return newCtx;
    }
}