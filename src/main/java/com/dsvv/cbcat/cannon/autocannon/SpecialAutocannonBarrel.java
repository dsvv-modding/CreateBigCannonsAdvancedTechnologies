package com.dsvv.cbcat.cannon.autocannon;

import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.simibubi.create.AllShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlockEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;

public class SpecialAutocannonBarrel extends AutocannonBarrelBlock {

    boolean isComplete = true;
    float volumeMultiplier = 1;

    public SpecialAutocannonBarrel(BlockBehaviour.Properties properties, AutocannonMaterial material) {
        super(properties, material);
    }

    public SpecialAutocannonBarrel(BlockBehaviour.Properties properties, AutocannonMaterial material, boolean isComplete) {
        super(properties, material);
        this.isComplete = isComplete;
    }

    public SpecialAutocannonBarrel(BlockBehaviour.Properties properties, AutocannonMaterial material, float volumeMultiplier) {
        super(properties, material);
        this.volumeMultiplier = volumeMultiplier > 0 ? volumeMultiplier : 1;
        if (this.volumeMultiplier > 1)
            isComplete = false;
    }

    @Override public BlockEntityType<? extends AutocannonBlockEntity> getBlockEntityType() { return BlockEntityRegister.SPECIAL_AUTOCANNON_BLOCK_ENTITY.get(); }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (volumeMultiplier == 1)
            return AllShapes.FOUR_VOXEL_POLE.get(this.getFacing(state).getAxis());
        return AllShapes.SIX_VOXEL_POLE.get(this.getFacing(state).getAxis());
    }

    public float getVolumeMultiplier(){
        return volumeMultiplier;
    }

    @Override
    public boolean isComplete(BlockState state) {
        return isComplete;
    }
}