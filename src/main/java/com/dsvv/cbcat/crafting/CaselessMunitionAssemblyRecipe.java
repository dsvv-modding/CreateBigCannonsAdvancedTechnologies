package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.registry.BlockRegister;
import com.dsvv.cbcat.registry.DataComponentRegistry;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.dsvv.cbcat.registry.RecipeRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;

import java.util.ArrayList;
import java.util.List;

public class CaselessMunitionAssemblyRecipe extends CustomRecipe
{
    public CaselessMunitionAssemblyRecipe() { super(CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack projectile = ItemStack.EMPTY;
        ItemStack powder = ItemStack.EMPTY;
        List<ItemStack> nitro = new ArrayList<>();

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof ProjectileBlockItem) {
                if (!projectile.isEmpty()) return false;
                projectile = stack;
            } else if (ItemStack.isSameItem(stack, CBCBlocks.POWDER_CHARGE.asStack())) {
                if (!powder.isEmpty()) return false;
                powder = stack;
            } else if (ItemStack.isSameItem(stack, CBCItems.NITROPOWDER.asStack())) {
                if (nitro.size() >= 3) return false;
                nitro.add(stack);
            } else {
                return false;
            }
        }
        return !projectile.isEmpty() && (powder.isEmpty() ^ nitro.isEmpty());
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider access) {
        ItemStack projectile = ItemStack.EMPTY;
        ItemStack powder = ItemStack.EMPTY;
        List<ItemStack> nitro = new ArrayList<>();

        for (int i = 0; i < input.size(); ++i) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.getItem() instanceof ProjectileBlockItem) {
                if (!projectile.isEmpty()) return ItemStack.EMPTY;
                projectile = stack;
            } else if (ItemStack.isSameItem(stack, CBCBlocks.POWDER_CHARGE.asStack())) {
                if (!powder.isEmpty()) return ItemStack.EMPTY;
                powder = stack;
            } else if (ItemStack.isSameItem(stack, CBCItems.NITROPOWDER.asStack())) {
                if (nitro.size() >= 3) return ItemStack.EMPTY;
                nitro.add(stack);
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (projectile.isEmpty() || (powder.isEmpty() == nitro.size() < 3)) return ItemStack.EMPTY;
        ItemStack result = ExtraDataRegister.getCartridge(ExtraDataRegister.getProjectile(projectile) + (powder.isEmpty() ? " caseless" : "")).asStack(1);//BlockRegister.CLUSTER_BLOCK.asStack(1);//ItemStack.EMPTY;

        if (projectile.has(CBCDataComponents.FUZE))
            result.set(CBCDataComponents.FUZE, projectile.get(CBCDataComponents.FUZE));

        if (projectile.has(CBCDataComponents.FLUID_CONTENT))
            result.set(CBCDataComponents.FLUID_CONTENT, projectile.get(CBCDataComponents.FLUID_CONTENT));

        if (projectile.has(DataComponentRegistry.CLUSTER_FUZES))
            result.set(DataComponentRegistry.CLUSTER_FUZES, projectile.get(DataComponentRegistry.CLUSTER_FUZES));
        if (projectile.has(DataComponentRegistry.CLUSTER_PROJECTILE))
            result.set(DataComponentRegistry.CLUSTER_PROJECTILE, projectile.get(DataComponentRegistry.CLUSTER_PROJECTILE));

        //ListTag fuzes = new ListTag();
        //for (int i = 0; i < clusterParts.size(); i++)
        //    fuzes.add(((AbstractFuzedHeavyAutocannonProjectileItem) clusterParts.get(0).getItem()).getFuze(clusterParts.get(i)).save(new CompoundTag()));

        /*CompoundTag resultTag = result.getOrCreateTag();
        CompoundTag projectileTag = projectile.getOrCreateTag();//.save(resultTag);

        if (projectileTag.contains("BlockEntityTag"))
            resultTag.put("BlockEntityTag", projectileTag.getCompound("BlockEntityTag"));*/

        //resultTag.put("SecondaryFuzes", fuzes);
        //resultTag.putString("Projectile", ExtraDataRegister.clusterPartsReverse(reference.getEntityType(ItemStack.EMPTY)));
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 4; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.CASELESS_MUNITION_ASSEMBLY.getSerializer(); }
}