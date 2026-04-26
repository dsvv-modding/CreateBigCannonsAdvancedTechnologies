package com.dsvv.cbcat.base;

import net.minecraft.world.item.ItemStack;

public interface LeavesCannonBlockEntity {
    ItemStack getLeavesItemStack();

    void setLeavesItemStack(ItemStack leavesItemStack);

    boolean getUpdateInstance();
    void setUpdateInstance(boolean value);
}
