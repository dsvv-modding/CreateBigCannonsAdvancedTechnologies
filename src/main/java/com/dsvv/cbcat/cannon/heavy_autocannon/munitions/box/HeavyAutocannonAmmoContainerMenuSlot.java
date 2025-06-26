package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonAmmoType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class HeavyAutocannonAmmoContainerMenuSlot extends Slot {
    private final IHeavyAutocannonAmmoContainerContainer ammoContainer;
    private final boolean isCreative;

    public HeavyAutocannonAmmoContainerMenuSlot(IHeavyAutocannonAmmoContainerContainer container, int slot, int x, int y, boolean isCreative) {
        super(container, slot, x, y);
        this.ammoContainer = container;
        this.isCreative = isCreative;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        HeavyAutocannonAmmoType placeType = HeavyAutocannonAmmoType.of(stack);
        HeavyAutocannonAmmoType type = this.ammoContainer.getAmmoType();
        return placeType != HeavyAutocannonAmmoType.NONE && type == HeavyAutocannonAmmoType.NONE ||
                placeType == type && this.ammoContainer.getTotalCount() < type.getCapacity();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        if (isCreative) return 1;
        HeavyAutocannonAmmoType ctType = this.ammoContainer.getAmmoType();
        if (ctType == HeavyAutocannonAmmoType.NONE) return HeavyAutocannonAmmoType.of(stack).getCapacity();
        int buf = Math.max(ctType.getCapacity() - this.ammoContainer.getTotalCount(), 0);
        ItemStack item = this.ammoContainer.getItem(this.getContainerSlot());
        return Math.min(item.getCount() + buf, item.getMaxStackSize());
    }
}