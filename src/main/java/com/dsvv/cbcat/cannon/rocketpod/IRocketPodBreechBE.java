package com.dsvv.cbcat.cannon.rocketpod;

import net.minecraft.world.item.ItemStack;

public interface IRocketPodBreechBE {
    boolean canFire();

    ItemStack extractNextInput();

    void handleFiring();

    void setFireRate(int firePower);

    int getActualFireRate();

    boolean isManual();
}
