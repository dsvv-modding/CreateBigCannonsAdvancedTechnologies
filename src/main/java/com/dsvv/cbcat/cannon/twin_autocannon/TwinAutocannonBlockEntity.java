package com.dsvv.cbcat.cannon.twin_autocannon;

import com.dsvv.cbcat.base.LeavesCannonBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

import java.util.List;

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
        this.setChanged();
        this.updateInstance = true;
    }

    public boolean getUpdateInstance() {
        return this.updateInstance;
    }

    public void setUpdateInstance(boolean updateInstance) {
        this.updateInstance = updateInstance;
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.leavesItemStack = ItemStack.of(tag.getCompound("Leaves"));
        this.updateInstance = true;
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.put("Leaves", leavesItemStack.save(new CompoundTag()));
    }

    @Override
    public List<ItemStack> getDrops() {
        List<ItemStack> drops = ITwinAutocannonBlockEntity.super.getDrops();
        if (!leavesItemStack.isEmpty())
            drops.add(leavesItemStack);
        return drops;
    }
}
