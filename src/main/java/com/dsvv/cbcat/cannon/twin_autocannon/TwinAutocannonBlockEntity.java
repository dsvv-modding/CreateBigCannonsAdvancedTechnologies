package com.dsvv.cbcat.cannon.twin_autocannon;

import com.dsvv.cbcat.base.LeavesCannonBlockEntity;
import com.dsvv.cbcat.cannon.twin_autocannon.breech.TwinAutocannonBreechBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

import java.util.List;
import java.util.stream.Collectors;

public class TwinAutocannonBlockEntity extends SmartBlockEntity implements ITwinAutocannonBlockEntity, LeavesCannonBlockEntity
{
    private ItemCannonBehavior behavior;

    private boolean vertical;
    private ItemStack leavesItemStack = ItemStack.EMPTY;
    private boolean updateInstance = true;

    public TwinAutocannonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        vertical = ((TwinAutocannonBaseBlock)state.getBlock()).isVertical();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(this.behavior = this.makeBehavior());
        updateInstance = true;
    }

    protected ItemCannonBehavior makeBehavior() {
        return new ItemCannonBehavior(this);
    }

    @Override
    public ItemCannonBehavior cannonBehavior() {
        return this.behavior;
    }

    public boolean isVertical() { return vertical; }

    public ItemStack getLeavesItemStack() { return leavesItemStack; }

    public void setLeavesItemStack(ItemStack leavesItemStack) {
        this.leavesItemStack = leavesItemStack;
        this.leavesItemStack.setCount(1);
        this.updateInstance = true;
    }

    public boolean getUpdateInstance() {
        return this.updateInstance;
    }

    public void setUpdateInstance(boolean updateInstance) {
        this.updateInstance = updateInstance;
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider provider, boolean clientPacket) {
        super.read(tag, provider, clientPacket);
        this.leavesItemStack = ItemStack.parseOptional(provider, tag.getCompound("Leaves"));
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider provider, boolean clientPacket) {
        super.write(tag, provider, clientPacket);
        tag.put("Leaves", leavesItemStack.saveOptional(provider));
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = ITwinAutocannonBlockEntity.super.getDrops();
        if (!leavesItemStack.isEmpty())
            drops.add(leavesItemStack);
        return drops;
    }
}
