package com.dsvv.cbcat.cannon.rocketpod.munitions;

import net.minecraft.world.item.ItemStack;

public enum RocketType {
    NORMAL {
        @Override public int getCapacity() { return 64; }
    },
    NONE {
        @Override public int getCapacity() { return 0; }
        @Override public boolean isValidMunition(ItemStack stack) { return of(stack) != this; }
    };

    RocketType() {
    }

    public abstract int getCapacity();

    public boolean isValidMunition(ItemStack stack) { return of(stack) == this; }

    public static RocketType of(ItemStack stack) {
        return stack.getItem() instanceof RocketItem item ? item.getType() : NONE;
    }
}
