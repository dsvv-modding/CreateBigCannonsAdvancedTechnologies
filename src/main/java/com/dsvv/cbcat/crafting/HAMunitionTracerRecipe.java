package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonAmmoItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.registry.RecipeRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;

public class HAMunitionTracerRecipe extends CustomRecipe {
    public HAMunitionTracerRecipe() { super(CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        ItemStack round = ItemStack.EMPTY;
        ItemStack tracer = ItemStack.EMPTY;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof AbstractHeavyAutocannonProjectileItem) {
                if (!round.isEmpty() || stack.has(CBCDataComponents.AUTOCANNON_TRACER)) return false;
                round = stack;
            } else if (stack.getItem() instanceof HeavyAutocannonCartridgeItem item) {
                if (!round.isEmpty() || item.isTracer(stack)) return false;
                round = stack;
            } else if (CBCItems.TRACER_TIP.isIn(stack)) {
                if (!tracer.isEmpty()) return false;
                tracer = stack;
            } else {
                return false;
            }
        }

        return !round.isEmpty() && !tracer.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider access) {
        ItemStack round = ItemStack.EMPTY;
        ItemStack tracer = ItemStack.EMPTY;

        for (int i = 0; i < container.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof AbstractHeavyAutocannonProjectileItem || stack.getItem() instanceof HeavyAutocannonAmmoItem) {
                if (!round.isEmpty()) return ItemStack.EMPTY;
                round = stack;
            } else if (CBCItems.TRACER_TIP.isIn(stack)) {
                if (!tracer.isEmpty()) return ItemStack.EMPTY;
                tracer = stack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (round.isEmpty() || tracer.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = round.copy();
        result.setCount(1);
        /*if (result.getItem() instanceof AbstractHeavyAutocannonProjectileItem) {
            result.getOrCreateTag().putBoolean("Tracer", true);
        } else if (result.getItem() instanceof HeavyAutocannonAmmoItem item) {
            item.setTracer(result, true);
        }*/
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 2; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.HA_MUNITION_TRACER.getSerializer(); }
}
