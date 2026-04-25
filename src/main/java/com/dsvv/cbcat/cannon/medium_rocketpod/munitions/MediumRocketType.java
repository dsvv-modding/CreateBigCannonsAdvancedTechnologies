package com.dsvv.cbcat.cannon.medium_rocketpod.munitions;

import net.minecraft.world.item.ItemStack;

public enum MediumRocketType {
    NORMAL {
        @Override public int getCapacity() { return 64; }
    },
    NONE {
        @Override public int getCapacity() { return 0; }
        @Override public boolean isValidMunition(ItemStack stack) { return of(stack) != this; }
    };

    MediumRocketType() {
    }

    public abstract int getCapacity();

    public boolean isValidMunition(ItemStack stack) { return of(stack) == this; }

    public static MediumRocketType of(ItemStack stack) {
        return stack.getItem() instanceof MediumRocketItem item ? item.getType() : NONE;
    }
}
