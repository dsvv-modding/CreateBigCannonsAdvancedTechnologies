package com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring;

import com.dsvv.cbcat.cannon.heavy_autocannon.MovesWithHeavyAutocannonRecoilSpring;
import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBaseBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class HeavyAutocannonRecoilSpringBlock extends HeavyAutocannonBaseBlock implements IBE<HeavyAutocannonRecoilSpringBlockEntity>, MovesWithHeavyAutocannonRecoilSpring, HeavyAutocannonBlock
{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final NonNullFunction<Direction, BlockState> movingBlockFunction;

    public HeavyAutocannonRecoilSpringBlock(Properties properties, AutocannonMaterial material, NonNullFunction<Direction, BlockState> movingBlockFunction) {
        super(properties, material);
        this.movingBlockFunction = movingBlockFunction;
        this.codec = simpleCodec(this::fromSelf);
    }

    public HeavyAutocannonRecoilSpringBlock(Properties properties, AutocannonMaterial material, NonNullFunction<Direction, BlockState> movingBlockFunction, boolean isComplete)
    {
        this(properties, material, movingBlockFunction);
        this.isComplete = isComplete;
    }

    private final MapCodec<? extends DirectionalBlock> codec;

    private HeavyAutocannonRecoilSpringBlock fromSelf(Properties properties) {
        return new HeavyAutocannonRecoilSpringBlock(properties, this.getAutocannonMaterial(), this.movingBlockFunction);
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    @Override
    public Class<HeavyAutocannonRecoilSpringBlockEntity> getBlockEntityClass() {
        return HeavyAutocannonRecoilSpringBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HeavyAutocannonRecoilSpringBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.HEAVY_AUTOCANNON_RECOIL_SPRING_BLOCK_ENTITY.get();
    }

    @Override
    public CannonCastShape getCannonShape() {
        return CannonCastingShapes.HEAVY_AUTOCANNON_RECOIL_SPRING;
    }

    @Override
    public boolean isBreechMechanism(BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75), BooleanOp.OR);
        return new AllShapes.Builder(shape).forDirectional().get(getFacing(state));
    }

    @Override
    public BlockState getMovingState(BlockState original) {
        return this.movingBlockFunction.apply(this.getFacing(original));
    }

    @Override public BlockState getStationaryState(BlockState original) { return original; }
}
