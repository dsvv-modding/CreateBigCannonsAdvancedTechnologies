package com.dsvv.cbcat.cluster_munition;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;

public class FuzedClusterProjectileBlockEntity extends FuzedBlockEntity {
    private ItemStack[] fuzes = new ItemStack[] { ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY };
    private String projectile = "";

    public FuzedClusterProjectileBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setFuzes(ItemStack[] fuzes) {
        if (fuzes != null)
            this.fuzes = fuzes;
    }

    public ItemStack[] getFuzes() {
        return fuzes;
    }

    public void setProjectile(String projectile) {
        if (projectile != null)
            this.projectile = projectile;
    }

    public String getProjectile() {
        return projectile;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag fuzeTags = new ListTag();
        for (int i = 0; i < fuzes.length; i++)
            fuzeTags.add(fuzes[i].save(new CompoundTag()));
        tag.put("SecondaryFuzes", fuzeTags);
        tag.putString("Projectile", projectile);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ListTag fuzeTags = tag.getList("SecondaryFuzes", 10);
        fuzes = new ItemStack[fuzeTags.size()];
        for (int i = 0; i < fuzeTags.size(); i++)
            fuzes[i] = ItemStack.of(fuzeTags.getCompound(i));
        projectile = tag.getString("Projectile");
    }
}