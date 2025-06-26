package com.dsvv.cbcat.cannon;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlockEntity;

public class SilencerBlockEntity extends BigCannonBlockEntity
{
    public SilencerBlockEntity(BlockEntityType<? extends BigCannonBlockEntity> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }
}
