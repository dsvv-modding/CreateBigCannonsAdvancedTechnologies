package com.dsvv.cbcat.cannon.twin_autocannon.recoil_spring;

import com.dsvv.cbcat.cannon.twin_autocannon.MovesWithTwinAutocannonRecoilSpring;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBaseBlock;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBlock;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class TwinAutocannonRecoilSpringBlock extends TwinAutocannonBaseBlock implements IBE<TwinAutocannonRecoilSpringBlockEntity>, TwinAutocannonBlock, MovesWithTwinAutocannonRecoilSpring
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final NonNullFunction<Direction, BlockState> movingBlockFunction;

    public TwinAutocannonRecoilSpringBlock(Properties properties, AutocannonMaterial material, NonNullFunction<Direction, BlockState> movingBlockFunction, boolean vertical) {
        super(properties, material, vertical);
        this.movingBlockFunction = movingBlockFunction;
    }

    public TwinAutocannonRecoilSpringBlock(Properties properties, AutocannonMaterial material, NonNullFunction<Direction, BlockState> movingBlockFunction, boolean vertical, boolean isComplete)
    {
        this(properties, material, movingBlockFunction, vertical);
        this.isComplete = isComplete;
    }

    @Override
    public Class<TwinAutocannonRecoilSpringBlockEntity> getBlockEntityClass() {
        return TwinAutocannonRecoilSpringBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TwinAutocannonRecoilSpringBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.TWIN_AUTOCANNON_RECOIL_SPRING_BLOCK_ENTITY.get();
    }

    @Override
    public CannonCastShape getCannonShape() {
        return vertical ? CannonCastingShapes.VERT_TWIN_AUTOCANNON_RECOIL_SPRING : CannonCastingShapes.TWIN_AUTOCANNON_RECOIL_SPRING;
    }

    @Override
    public boolean isBreechMechanism(BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        if(!vertical) {
            shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.3125, 0.4375, 1, 0.6875), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.5625, 0, 0.3125, 0.9375, 1, 0.6875), BooleanOp.OR);
        }
        else {
            shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.0625, 0.6875, 1, 0.4375), BooleanOp.OR);
            shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.5625, 0.6875, 1, 0.9375), BooleanOp.OR);
        }
        return new AllShapes.Builder(shape).forDirectional().get(getFacing(state));
    }

    @Override
    public BlockState getMovingState(BlockState original) {
        return this.movingBlockFunction.apply(this.getFacing(original));
    }

    @Override public BlockState getStationaryState(BlockState original) { return original; }

    @Override
    public float getVolumeMultiplier() {
        return 1;
    }
}
