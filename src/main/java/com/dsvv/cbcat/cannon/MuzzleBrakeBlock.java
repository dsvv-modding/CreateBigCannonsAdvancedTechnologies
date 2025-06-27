package com.dsvv.cbcat.cannon;

import com.dsvv.cbcat.base.CustomPropellantContext;
import com.dsvv.cbcat.base.SaveAssemble;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class MuzzleBrakeBlock extends BigCannonBaseBlock implements IBE<MuzzleBrakeBlockEntity>, SaveAssemble
{
    public boolean complete = true;
    VoxelShaper shaper;
    public MuzzleBrakeBlock(Properties properties, BigCannonMaterial material)
    {
        super(properties, material);
        shaper = new AllShapes.Builder(makeShape()).forDirectional();
    }

    public MuzzleBrakeBlock(Properties properties, BigCannonMaterial material, boolean isComplete)
    {
        this(properties, material);
        complete = isComplete;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.25, 0.875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.0625, 0.9375, 0.875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.0625, 0.75, 0.875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.75, 0.75, 0.875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.875, 0.125, 0.875, 1, 0.875), BooleanOp.OR);

        return shape;
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context)
    {
        return shaper.get(getFacing(state));
    }

    @Override
    public CannonCastShape getCannonShape()
    {
        return CannonCastingShapes.MUZZLE_BRAKE;
    }

    @Override
    public BigCannonEnd getDefaultOpeningType()
    {
        return BigCannonEnd.OPEN;
    }

    @Override
    public boolean isComplete(BlockState blockState)
    {
        return complete;
    }

    @Override
    public Class<MuzzleBrakeBlockEntity> getBlockEntityClass() {
        return MuzzleBrakeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MuzzleBrakeBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.MUZZLE_BRAKE_BLOCK_ENTITY.get();
    }

    public Direction getFacing()
    {
        return this.getFacing(defaultBlockState());
    }

    public CustomPropellantContext applyBarrelPhysic(CustomPropellantContext propCtx)
    {
        CustomPropellantContext newCtx = new CustomPropellantContext();
        newCtx.explosionGas = (propCtx.explosionGas - 0.32f) * 0.45f;
        newCtx.drag = propCtx.drag + 0.00025f * (propCtx.getVelocity() * propCtx.getVelocity());
        newCtx.recoil = propCtx.recoil * (0.4f / Math.max(propCtx.explosionGas, 0.8f));
        newCtx.spread = propCtx.spread * 0.95f;
        newCtx.smokeScale = propCtx.smokeScale;
        newCtx.stress = propCtx.stress;
        newCtx.volume = propCtx.volume;
        return newCtx;
    }

    public boolean canConnectToSide(BlockState state, Direction face) { return this.getFacing(state).getAxis() == face.getAxis(); }// || this.getFacing(state) == face.getOpposite(); }
}
