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

public class SilencerBlock extends BigCannonBaseBlock implements IBE<SilencerBlockEntity>, SaveAssemble
{
    public boolean complete = true;
    boolean builtUp;
    static VoxelShaper buildUpShaper = new AllShapes.Builder(makeBuiltUpShape()).forDirectional();
    static VoxelShaper shaper = new AllShapes.Builder(makeShape()).forDirectional();
    public SilencerBlock(Properties properties, BigCannonMaterial material, boolean... builtUp)
    {
        super(properties, material);
        this.builtUp = builtUp.length > 0 && builtUp[0];
    }
    public SilencerBlock(Properties properties, BigCannonMaterial material, boolean isComplete, boolean... builtUp)
    {
        this(properties, material, builtUp);
        complete = isComplete;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }


    public static VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0.1875, 0.125, 0.875, 0.4375, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.125, 0.875, 0.125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.1875, 0.8125, 0.1875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.125, 0.875, 1, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.4375, 0.1875, 0.8125, 0.5, 0.8125), BooleanOp.OR);

        return shape;
    }

    public static VoxelShape makeBuiltUpShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.125, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.0625, 0.9375, 0.4375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.5, 0.0625, 0.9375, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.125, 0.125, 0.875, 0.1875, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.4375, 0.125, 0.875, 0.5, 0.875), BooleanOp.OR);

        return shape;
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context)
    {
        return builtUp ? buildUpShaper.get(getFacing(state)) : shaper.get(getFacing(state));
    }

    @Override
    public BigCannonEnd getDefaultOpeningType()
    {
        return BigCannonEnd.OPEN;
    }

    @Override
    public CannonCastShape getCannonShape()
    {
        return CannonCastingShapes.SILENCER;
    }

    @Override
    public boolean isComplete(BlockState blockState)
    {
        return complete;
    }

    @Override
    public Class<SilencerBlockEntity> getBlockEntityClass()
    {
        return SilencerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SilencerBlockEntity> getBlockEntityType()
    {
        return BlockEntityRegister.SILENCER_BLOCK_ENTITY.get();
    }

    public CustomPropellantContext applyBarrelPhysic(CustomPropellantContext propCtx)
    {
        CustomPropellantContext newCtx = new CustomPropellantContext();
        newCtx.explosionGas = propCtx.explosionGas - 0.4f;
        newCtx.drag = propCtx.drag + 0.04f * (propCtx.getVelocity() * propCtx.getVelocity());
        newCtx.recoil = propCtx.recoil * 0.45f* propCtx.explosionGas;
        newCtx.spread = propCtx.spread * 0.9f;
        newCtx.smokeScale = propCtx.smokeScale * 0.9f;
        newCtx.stress = propCtx.stress + 0.5f;
        newCtx.volume = propCtx.volume * 0.4f;
        return newCtx;
    }

    public boolean canConnectToSide(BlockState state, Direction face) { return this.getFacing(state).getAxis() == face.getAxis(); }// || this.getFacing(state) == face.getOpposite(); }
}
