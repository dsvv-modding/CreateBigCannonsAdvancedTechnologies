package com.dsvv.cbcat.cannon.rocketpod;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

import java.util.List;

public class RocketPodBlockEntity extends SmartBlockEntity implements IRocketPodBlockEntity
{
    private ItemCannonBehavior behavior;

    public RocketPodBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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
}
