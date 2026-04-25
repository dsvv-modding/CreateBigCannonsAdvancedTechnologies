package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.registry.RecipeRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;

public class HAMunitionUnfuzingRecipe extends CustomRecipe {
    public HAMunitionUnfuzingRecipe() {
        super(CraftingBookCategory.MISC);
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        ItemStack target = ItemStack.EMPTY;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (!target.isEmpty()) return false;

            if (stack.getItem() instanceof HeavyAutocannonCartridgeItem)
                stack = HeavyAutocannonCartridgeItem.getProjectileStack(stack);
            if (stack.getItem() instanceof FuzedItemMunition) {
                if (!stack.has(CBCDataComponents.FUZE)) return false;
                target = stack;
            } else {
                return false;
            }
        }
        return !target.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider registryAccess) {
        ItemStack target = ItemStack.EMPTY;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (!target.isEmpty()) return ItemStack.EMPTY;

            if (stack.getItem() instanceof HeavyAutocannonCartridgeItem)
                stack = HeavyAutocannonCartridgeItem.getProjectileStack(stack);
            if (stack.getItem() instanceof FuzedItemMunition) {
                if (!stack.has(CBCDataComponents.FUZE)) return ItemStack.EMPTY;
                target = stack;
            } else {
                return ItemStack.EMPTY;
            }
        }
        ItemContainerContents item = target.get(CBCDataComponents.FUZE);
        return item.copyOne();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
        NonNullList<ItemStack> result = super.getRemainingItems(container);
        int sz = container.size();

        for (int i = 0; i < sz; ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.getItem() instanceof FuzedItemMunition) {
                if (stack.has(CBCDataComponents.FUZE)) {
                    ItemStack copy = stack.copy();
                    copy.set(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
                    copy.setCount(1);
                    result.set(i, copy);
                }
                break;
            } else if (stack.getItem() instanceof HeavyAutocannonCartridgeItem) {
                ItemStack cartridgeRound = HeavyAutocannonCartridgeItem.getProjectileStack(stack);
                if (cartridgeRound.getItem() instanceof FuzedItemMunition && cartridgeRound.has(CBCDataComponents.FUZE)) {
                    ItemStack copyRound = cartridgeRound.copy();
                    copyRound.set(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
                    copyRound.setCount(1);
                    ItemStack newStack = new ItemStack(stack.getItem());
                    HeavyAutocannonCartridgeItem.writeProjectile(copyRound, newStack);
                    result.set(i, newStack);
                }
                break;
            }
        }
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.HA_MUNITION_UNFUZING.getSerializer(); }

}
