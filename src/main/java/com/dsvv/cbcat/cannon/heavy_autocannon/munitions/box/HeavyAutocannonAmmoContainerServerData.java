package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box;

import net.minecraft.world.inventory.ContainerData;

import javax.annotation.Nullable;

public class HeavyAutocannonAmmoContainerServerData implements ContainerData {
    @Nullable
    private final HeavyAutocannonAmmoContainerBlockEntity be;

    public HeavyAutocannonAmmoContainerServerData(@Nullable HeavyAutocannonAmmoContainerBlockEntity be) {
        this.be = be;
    }

    @Override
    public int get(int index) {
        return this.be != null && index == 0 ? this.be.getSpacing() : 1;
    }

    @Override
    public void set(int index, int value) {
        if (this.be != null && index == 0) this.be.setSpacing(value);
    }

    @Override public int getCount() { return 1; }

}
