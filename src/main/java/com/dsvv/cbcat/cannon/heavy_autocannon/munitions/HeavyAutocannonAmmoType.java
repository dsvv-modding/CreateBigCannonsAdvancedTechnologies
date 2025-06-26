package com.dsvv.cbcat.cannon.heavy_autocannon.munitions;

import net.minecraft.world.item.ItemStack;

public enum HeavyAutocannonAmmoType {
    NORMAL {
        @Override public int getCapacity() { return 16; }
    },
    NONE {
        @Override public int getCapacity() { return 0; }
        @Override public boolean isValidMunition(ItemStack stack) { return of(stack) != this; }
    };

    HeavyAutocannonAmmoType() {
    }

    public abstract int getCapacity();

    public boolean isValidMunition(ItemStack stack) { return of(stack) == this; }

    public static HeavyAutocannonAmmoType of(ItemStack stack) {
        return stack.getItem() instanceof HeavyAutocannonAmmoItem item ? item.getType() : NONE;
    }

}
