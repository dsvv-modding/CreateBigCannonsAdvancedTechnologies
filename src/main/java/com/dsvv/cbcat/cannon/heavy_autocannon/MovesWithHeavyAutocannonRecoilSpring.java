package com.dsvv.cbcat.cannon.heavy_autocannon;

import net.minecraft.world.level.block.state.BlockState;

public interface MovesWithHeavyAutocannonRecoilSpring
{
    BlockState getMovingState(BlockState original);
    BlockState getStationaryState(BlockState original);
}