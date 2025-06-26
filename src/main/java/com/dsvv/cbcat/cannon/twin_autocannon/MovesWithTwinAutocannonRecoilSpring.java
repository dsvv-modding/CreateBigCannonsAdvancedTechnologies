package com.dsvv.cbcat.cannon.twin_autocannon;

import net.minecraft.world.level.block.state.BlockState;

public interface MovesWithTwinAutocannonRecoilSpring
{
    BlockState getMovingState(BlockState original);
    BlockState getStationaryState(BlockState original);
}
