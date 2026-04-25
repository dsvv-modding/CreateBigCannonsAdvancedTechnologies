package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.registry.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;

import java.util.ArrayList;
import java.util.List;

public class ClusterMunitionAssemblyRecipe extends CustomRecipe
{
    public ClusterMunitionAssemblyRecipe() { super(CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack slab = ItemStack.EMPTY;
        ItemStack powder = ItemStack.EMPTY;
        List<ItemStack> clusterParts = new ArrayList<>();

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SlabBlock slabBlock && new ItemStack(slabBlock).is(ItemTags.WOODEN_SLABS)) {
                if (!slab.isEmpty()) return false;
                slab = stack;
            } else if (ItemStack.isSameItem(stack, Items.GUNPOWDER.getDefaultInstance())) {
                if (!powder.isEmpty()) return false;
                powder = stack;
            } else if (stack.getItem() instanceof AbstractHeavyAutocannonProjectileItem) {
                clusterParts.add(stack);
            }else
            {
                return false;
            }
        }
        return !slab.isEmpty() && !powder.isEmpty() && clusterParts.size() > 3;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider access) {
        ItemStack slab = ItemStack.EMPTY;
        ItemStack powder = ItemStack.EMPTY;
        List<ItemStack> clusterParts = new ArrayList<>();

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty())
                continue;

            if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SlabBlock slabBlock && new ItemStack(slabBlock).is(ItemTags.WOODEN_SLABS)) {
                if (!slab.isEmpty()) return ItemStack.EMPTY;
                slab = stack;
            } else if (ItemStack.isSameItem(stack, Items.GUNPOWDER.getDefaultInstance())) {
                if (!powder.isEmpty()) return ItemStack.EMPTY;
                powder = stack;
            } else if (stack.getItem() instanceof AbstractFuzedHeavyAutocannonProjectileItem) {
                clusterParts.add(stack);
            } else
            {
                return ItemStack.EMPTY;
            }
        }

        if (slab.isEmpty() || powder.isEmpty() || clusterParts.size() < 4) return ItemStack.EMPTY;

        AbstractFuzedHeavyAutocannonProjectileItem reference = (AbstractFuzedHeavyAutocannonProjectileItem) clusterParts.get(0).getItem();
        for (int i = 0; i < clusterParts.size(); i++)
            if (!clusterParts.get(i).getItem().equals(reference))
                return ItemStack.EMPTY;

        ItemStack result = BlockRegister.CLUSTER_BLOCK.asStack(1);

        ItemContainerContents fuzeContainer = ItemContainerContents.fromItems(clusterParts);
        result.set(DataComponentRegistry.CLUSTER_FUZES, fuzeContainer);
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 6; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.CLUSTER_MUNITION_ASSEMBLY.getSerializer(); }
}
