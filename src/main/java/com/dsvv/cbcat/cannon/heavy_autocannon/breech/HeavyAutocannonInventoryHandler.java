package com.dsvv.cbcat.cannon.heavy_autocannon.breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonAmmoItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.Deque;

public record HeavyAutocannonInventoryHandler(HeavyAutocannonBreechBlockEntity breech) implements IItemHandler {
    @Override
    public int getSlots() {
        return 2;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        Deque<ItemStack> inputBuffer = breech.getInputBuffer();
        return switch (slot) {
            case 0 -> breech.getOutputBuffer();
            case 1 -> breech.isInputFull() && !inputBuffer.isEmpty() ? inputBuffer.peekLast() : ItemStack.EMPTY;
            default -> ItemStack.EMPTY;
        };
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot != 1 || !this.isItemValid(slot, stack) || breech.isInputFull()) return stack;
        int maxCount = Math.min(breech.getQueueLimit() - breech.getInputBuffer().size(), stack.getCount());
        if (!simulate) {
            for (int i = 0; i < maxCount; ++i) {
                breech.getInputBuffer().add(ItemHandlerHelper.copyStackWithSize(stack, 1));
            }
        }
        return stack.getCount() == maxCount ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - maxCount);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0) return ItemStack.EMPTY;
        return switch (slot) {
            case 0 ->
                    simulate ? ItemHandlerHelper.copyStackWithSize(breech.getOutputBuffer(), 1) : breech.getOutputBuffer().split(1);
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stack.getItem() instanceof HeavyAutocannonAmmoItem;
    }
}
