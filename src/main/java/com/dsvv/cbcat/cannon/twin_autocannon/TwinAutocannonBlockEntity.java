package com.dsvv.cbcat.cannon.twin_autocannon;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

import java.util.List;

public class TwinAutocannonBlockEntity extends SmartBlockEntity implements ITwinAutocannonBlockEntity
{
    private ItemCannonBehavior behavior;

    private boolean vertical;

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
}
