package com.dsvv.cbcat.crafting;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.registry.BlockRegister;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.dsvv.cbcat.registry.ItemRegister;
import com.dsvv.cbcat.registry.RecipeRegister;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITagManager;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;

import java.util.ArrayList;
import java.util.List;

public class ClusterMunitionAssemblyRecipe extends CustomRecipe
{
    public ClusterMunitionAssemblyRecipe(ResourceLocation location) { super(location, CraftingBookCategory.MISC); }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack slab = ItemStack.EMPTY;
        ItemStack powder = ItemStack.EMPTY;
        List<ItemStack> clusterParts = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
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
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        ItemStack slab = ItemStack.EMPTY;
        ItemStack powder = ItemStack.EMPTY;
        List<ItemStack> clusterParts = new ArrayList<>();

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
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

        ItemStack result = BlockRegister.CLUSTER_BLOCK.asStack(1);//ItemStack.EMPTY;
        /*if (reference == ItemRegister.HA_HE_ITEM.asItem())
            result = BlockRegister.HIGH_EXPLOSIVE_CLUSTER_BLOCK.asStack(1);
        else if (reference == ItemRegister.HA_SMOKE_ITEM.asItem())
            result = BlockRegister.SMOKE_CLUSTER_BLOCK.asStack(1);
        else*/
            //return result;

        ListTag fuzes = new ListTag();
        for (int i = 0; i < clusterParts.size(); i++)
            fuzes.add(((AbstractFuzedHeavyAutocannonProjectileItem) clusterParts.get(0).getItem()).getFuze(clusterParts.get(i)).save(new CompoundTag()));

        CompoundTag baseTag = result.getOrCreateTag();
        if (!baseTag.contains("BlockEntityTag"))
            baseTag.put("BlockEntityTag", new CompoundTag());
        CompoundTag resultTag = baseTag.getCompound("BlockEntityTag");
        resultTag.put("SecondaryFuzes", fuzes);
        resultTag.putString("Projectile", ExtraDataRegister.clusterPartsReverse(reference.getEntityType(ItemStack.EMPTY)));
        /*CompoundTag tag = new CompoundTag();//result.getOrCreateTag();
        if (result.getItem() instanceof FuzedItemMunition && !fuzeCopy.isEmpty()) {
            CompoundTag projectileTag = round.getOrCreateTag();
            projectileTag.put("Fuze", fuzeCopy.save(new CompoundTag()));
        }
        tag.put("Projectile", round.save(new CompoundTag()));
        tag.putBoolean("Strong", strong);*/
        return result;
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 6; }
    @Override public RecipeSerializer<?> getSerializer() { return RecipeRegister.CLUSTER_MUNITION_ASSEMBLY.getSerializer(); }
}
