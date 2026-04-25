package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.registry.DataComponentRegistry;
import com.dsvv.cbcat.registry.RecipeRegister;
import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

import static com.dsvv.cbcat.registry.ExtraDataRegister.getRocketForProjectile;

public class RocketRecipe extends CustomRecipe
{
    public RocketRecipe() { super(CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack round = ItemStack.EMPTY;
        ItemStack paper = ItemStack.EMPTY;
        byte power = 0;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof AutocannonRoundItem) {
                if (!round.isEmpty()) return false;
                round = stack;
            } else if (ItemStack.isSameItem(stack, CBCItems.PACKED_GUNPOWDER.asStack())) {
                power += 54;
            } else if (ItemStack.isSameItem(stack, Items.GUNPOWDER.getDefaultInstance())) {
                power += 18;
            } else if (ItemStack.isSameItem(stack, CBCItems.GUNPOWDER_PINCH.asStack())) {
                power += 2;
            } else if (ItemStack.isSameItem(stack, Items.PAPER.getDefaultInstance())) {
                if (paper.isEmpty())
                    paper = stack;
                else
                    return false;
            } else
            {
                return false;
            }
        }
        return !round.isEmpty() && power > 0 && power <= 75 && !paper.isEmpty();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        ItemStack round = ItemStack.EMPTY;
        ItemStack paper = ItemStack.EMPTY;
        byte power = 0;

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof AutocannonRoundItem) {
                if (!round.isEmpty()) return ItemStack.EMPTY;
                round = stack;
            } else if (ItemStack.isSameItem(stack, CBCItems.PACKED_GUNPOWDER.asStack())) {
                power += 54;
            } else if (ItemStack.isSameItem(stack, Items.GUNPOWDER.getDefaultInstance())) {
                power += 18;
            } else if (ItemStack.isSameItem(stack, CBCItems.GUNPOWDER_PINCH.asStack())) {
                power += 2;
            } else if (ItemStack.isSameItem(stack, Items.PAPER.getDefaultInstance())) {
                if (paper.isEmpty())
                    paper = stack;
                else
                    return ItemStack.EMPTY;
            } else
            {
                return ItemStack.EMPTY;
            }
        }

        if (power <= 0 || power > 75 || round.isEmpty() || paper.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = getRocketForProjectile(round.getItem()).getDefaultInstance();
        result.setCount(1);

        if (round.has(CBCDataComponents.FUZE))
            result.set(CBCDataComponents.FUZE, round.get(CBCDataComponents.FUZE));

        result.set(CBCDataComponents.PROJECTILE, ItemContainerContents.fromItems(Lists.newArrayList(round)));
        result.set(DataComponentRegistry.ROCKET_FUEL, power);
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 4; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.ROCKET_ASSEMBLY.getSerializer(); }
}