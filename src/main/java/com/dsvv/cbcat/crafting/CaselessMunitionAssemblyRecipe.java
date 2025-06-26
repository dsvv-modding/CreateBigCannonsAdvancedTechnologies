package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.registry.BlockRegister;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.dsvv.cbcat.registry.RecipeRegister;
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
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;

import java.util.ArrayList;
import java.util.List;

public class CaselessMunitionAssemblyRecipe extends CustomRecipe
{
    public CaselessMunitionAssemblyRecipe(ResourceLocation location) { super(location, CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack projectile = ItemStack.EMPTY;
        ItemStack powder = ItemStack.EMPTY;
        List<ItemStack> nitro = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
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
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        ItemStack projectile = ItemStack.EMPTY;
        ItemStack powder = ItemStack.EMPTY;
        List<ItemStack> nitro = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
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

        //ListTag fuzes = new ListTag();
        //for (int i = 0; i < clusterParts.size(); i++)
        //    fuzes.add(((AbstractFuzedHeavyAutocannonProjectileItem) clusterParts.get(0).getItem()).getFuze(clusterParts.get(i)).save(new CompoundTag()));

        CompoundTag resultTag = result.getOrCreateTag();
        CompoundTag projectileTag = projectile.getOrCreateTag();//.save(resultTag);

        if (projectileTag.contains("BlockEntityTag"))
            resultTag.put("BlockEntityTag", projectileTag.getCompound("BlockEntityTag"));

        //resultTag.put("SecondaryFuzes", fuzes);
        //resultTag.putString("Projectile", ExtraDataRegister.clusterPartsReverse(reference.getEntityType(ItemStack.EMPTY)));
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 4; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.CASELESS_MUNITION_ASSEMBLY.getSerializer(); }
}