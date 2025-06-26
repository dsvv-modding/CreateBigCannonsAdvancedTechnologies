package com.dsvv.cbcat.cannon.heavy_autocannon;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;

import java.util.List;

public class HeavyAutocannonBlockEntity extends SmartBlockEntity implements IHeavyAutocannonBlockEntity
{
    private ItemCannonBehavior behavior;

    public HeavyAutocannonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
