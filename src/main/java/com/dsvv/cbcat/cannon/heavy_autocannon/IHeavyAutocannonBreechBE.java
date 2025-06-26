package com.dsvv.cbcat.cannon.heavy_autocannon;

import net.minecraft.world.item.ItemStack;

public interface IHeavyAutocannonBreechBE {
    boolean canFire();

    ItemStack extractNextInput();

    void handleFiring();

    void handleFiring(ItemStack eject);

    void setFireRate(int firePower);

    int getActualFireRate();

    boolean isManual();
}
