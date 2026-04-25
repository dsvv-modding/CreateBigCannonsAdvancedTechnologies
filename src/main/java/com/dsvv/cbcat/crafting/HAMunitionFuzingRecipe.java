package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.registry.RecipeRegister;
import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class HAMunitionFuzingRecipe extends CustomRecipe
{
    public HAMunitionFuzingRecipe() { super(CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack round = ItemStack.EMPTY;
        ItemStack fuze = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof HeavyAutocannonCartridgeItem) {
                if (!round.isEmpty()) return false;
                stack = HeavyAutocannonCartridgeItem.getProjectileStack(stack);
            }
            if (stack.getItem() instanceof FuzedItemMunition) {
                if (!round.isEmpty() || (stack.has(CBCDataComponents.FUZE) && stack.get(CBCDataComponents.FUZE).getSlots() > 0)) return false;
                round = stack;
            } else if (stack.getItem() instanceof FuzeItem) {
                if (!fuze.isEmpty()) return false;
                fuze = stack;
            } else {
                return false;
            }
        }
        return !round.isEmpty() && !fuze.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider access) {
        ItemStack round = ItemStack.EMPTY;
        ItemStack fuze = ItemStack.EMPTY;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof FuzedItemMunition || stack.getItem() instanceof HeavyAutocannonCartridgeItem) {
                if (!round.isEmpty()) return ItemStack.EMPTY;
                round = stack;
            } else if (stack.getItem() instanceof FuzeItem) {
                if (!fuze.isEmpty()) return ItemStack.EMPTY;
                fuze = stack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (round.isEmpty() || fuze.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = round.copy();
        result.setCount(1);
        ItemStack fuzeCopy = fuze.copy();
        fuzeCopy.setCount(1);
        if (result.getItem() instanceof FuzedItemMunition) {
            result.set(CBCDataComponents.FUZE, ItemContainerContents.fromItems(Lists.newArrayList(fuzeCopy)));
        } else if (result.getItem() instanceof HeavyAutocannonCartridgeItem) {
            ItemContainerContents itemContainer = result.getOrDefault(CBCDataComponents.PROJECTILE, ItemContainerContents.EMPTY);
            ItemStack projectile = itemContainer.copyOne();
            if (!projectile.isEmpty()) {
                projectile.set(CBCDataComponents.FUZE, ItemContainerContents.fromItems(Lists.newArrayList(fuzeCopy)));
                result.set(CBCDataComponents.PROJECTILE, ItemContainerContents.fromItems(Lists.newArrayList(projectile)));
            }
        }
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 2; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.HA_MUNITION_FUZING.getSerializer(); }
}
