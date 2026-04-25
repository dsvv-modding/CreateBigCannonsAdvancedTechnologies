package com.dsvv.cbcat.cluster_munition;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;

public class FuzedClusterProjectileBlockEntity extends FuzedBlockEntity {
    private ItemStack[] secondaryFuzes = new ItemStack[] { ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY };
    private String projectile = "";

    public FuzedClusterProjectileBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setSecondaryFuzes(ItemStack[] secondaryFuzes) {
        if (secondaryFuzes != null)
            this.secondaryFuzes = secondaryFuzes;
    }

    public ItemStack[] getSecondaryFuzes() {
        return secondaryFuzes;
    }

    public void setProjectile(String projectile) {
        if (projectile != null)
            this.projectile = projectile;
    }

    public String getProjectile() {
        return projectile;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ListTag fuzeTags = new ListTag();
        for (int i = 0; i < secondaryFuzes.length; i++)
            fuzeTags.add(secondaryFuzes[i].saveOptional(provider));
        tag.put("SecondaryFuzes", fuzeTags);
        tag.putString("Projectile", projectile);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        ListTag fuzeTags = tag.getList("SecondaryFuzes", 10);
        secondaryFuzes = new ItemStack[fuzeTags.size()];
        for (int i = 0; i < fuzeTags.size(); i++)
            secondaryFuzes[i] = ItemStack.parseOptional(provider, fuzeTags.getCompound(i));
        projectile = tag.getString("Projectile");
    }
}