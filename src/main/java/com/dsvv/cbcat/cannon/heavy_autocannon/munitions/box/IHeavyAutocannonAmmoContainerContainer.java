package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonAmmoType;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface IHeavyAutocannonAmmoContainerContainer extends Container {
    int AMMO_SLOT = 0;
    int TRACER_SLOT = 1;

    ItemStack getMainAmmoStack();
    ItemStack getTracerStack();

    default int getMainAmmoCapacity() {
        int remainder = Math.max(0, this.getAmmoType().getCapacity() - this.getTotalCount());
        ItemStack stack = this.getMainAmmoStack();
        return Math.min(stack.getCount() + remainder, stack.getMaxStackSize());
    }

    default int getTracerAmmoCapacity() {
        int remainder = Math.max(0, this.getAmmoType().getCapacity() - this.getTotalCount());
        ItemStack stack = this.getTracerStack();
        return Math.min(stack.getCount() + remainder, stack.getMaxStackSize());
    }

    default int getTotalCount() { return this.getMainAmmoStack().getCount() + this.getTracerStack().getCount(); }

    default HeavyAutocannonAmmoType getAmmoType() {
        HeavyAutocannonAmmoType type = HeavyAutocannonAmmoType.of(this.getMainAmmoStack());
        return type != HeavyAutocannonAmmoType.NONE ? type : HeavyAutocannonAmmoType.of(this.getTracerStack());
    }

    @Override default int getContainerSize() { return 2; }

    @Override
    default boolean isEmpty() {
        return this.getMainAmmoStack().isEmpty() && this.getTracerStack().isEmpty();
    }

    @Override
    default ItemStack getItem(int slot) {
        return switch (slot) {
            case 0 -> this.getMainAmmoStack();
            case 1 -> this.getTracerStack();
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    default boolean canPlaceItem(int index, ItemStack stack) {
        if (index != AMMO_SLOT && index != TRACER_SLOT) return false;
        HeavyAutocannonAmmoType ammoType = this.getAmmoType();
        if (!ammoType.isValidMunition(stack)) return false;
        boolean ammoSlot = index == AMMO_SLOT;
        int currentCapacity;
        if (ammoType == HeavyAutocannonAmmoType.NONE) {
            currentCapacity = HeavyAutocannonAmmoType.of(stack).getCapacity();
        } else {
            currentCapacity = ammoSlot ? this.getMainAmmoCapacity() : this.getTracerAmmoCapacity();
            currentCapacity -= this.getItem(index).getCount();
        }
        return currentCapacity > 0;
    }
}
