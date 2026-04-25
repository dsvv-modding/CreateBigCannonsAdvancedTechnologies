package com.dsvv.cbcat.cannon.medium_rocketpod;

import net.minecraft.world.item.ItemStack;

public interface IMediumRocketPodBreechBE {
    boolean canFire();

    ItemStack extractNextInput();

    void handleFiring();

    void setFireRate(int firePower);

    int getActualFireRate();

    boolean isManual();
}
