package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractFuzedRocketItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocketItem;
import com.dsvv.cbcat.registry.RecipeRegister;
import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;

import static com.dsvv.cbcat.registry.ExtraDataRegister.getRocketForProjectile;

public class RocketRecipe extends CustomRecipe
{
    public RocketRecipe(ResourceLocation location) { super(location, CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack round = ItemStack.EMPTY;
        ItemStack paper = ItemStack.EMPTY;
        byte power = 0;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
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
    public ItemStack assemble(CraftingContainer input, RegistryAccess access) {
        ItemStack round = ItemStack.EMPTY;
        ItemStack paper = ItemStack.EMPTY;
        byte power = 0;

        for (int i = 0; i < input.getContainerSize(); ++i) {
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

        CompoundTag tag = result.getOrCreateTag();
        if (round.getItem() instanceof AbstractFuzedRocketItem fuzedRocketItem) {
            ItemStack fuzeCopy = fuzedRocketItem.getFuze(round);
            fuzeCopy.setCount(1);
            if (result.getItem() instanceof FuzedItemMunition && !fuzeCopy.isEmpty()) {
                CompoundTag projectileTag = round.getOrCreateTag();
                projectileTag.put("Fuze", fuzeCopy.save(new CompoundTag()));
            }
        }
        tag.put("Projectile", round.save(new CompoundTag()));
        tag.putByte("fuel", power);
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 4; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.ROCKET_ASSEMBLY.getSerializer(); }
}