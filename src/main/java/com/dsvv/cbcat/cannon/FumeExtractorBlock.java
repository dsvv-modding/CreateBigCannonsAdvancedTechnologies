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

public class FumeExtractorBlock extends BigCannonBaseBlock implements IBE<FumeExtractorBlockEntity>, SaveAssemble
{
    public boolean complete = true;
    VoxelShaper shaper;
    public FumeExtractorBlock(Properties properties, BigCannonMaterial material)
    {
        super(properties, material);
        shaper = new AllShapes.Builder(makeShape()).forDirectional();
    }

    public FumeExtractorBlock(Properties properties, BigCannonMaterial material, boolean isComplete)
    {
        this(properties, material);
        complete = isComplete;
    }

    public VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.125, 0.875, 1, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.09375, 0.125, 0.09375, 0.90625, 0.8125, 0.90625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.15625, 0.0625, 0.9375, 0.78125, 0.9375), BooleanOp.OR);

        return shape;
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context)
    {
        return shaper.get(getFacing(state));
    }

    @Override
    public BigCannonEnd getDefaultOpeningType()
    {
        return BigCannonEnd.OPEN;
    }

    @Override
    public CannonCastShape getCannonShape()
    {
        return CannonCastingShapes.FUME_EXTRACTOR;
    }

    @Override
    public boolean isComplete(BlockState blockState)
    {
        return complete;
    }

    @Override
    public Class<FumeExtractorBlockEntity> getBlockEntityClass()
    {
        return FumeExtractorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FumeExtractorBlockEntity> getBlockEntityType()
    {
        return BlockEntityRegister.FUME_EXTRACTOR_BLOCK_ENTITY.get();
    }

    public CustomPropellantContext applyBarrelPhysic(CustomPropellantContext propCtx) {
        CustomPropellantContext newCtx = new CustomPropellantContext();
        newCtx.explosionGas = propCtx.explosionGas - 0.45f;
        newCtx.drag = propCtx.drag + 0.06f * (propCtx.getVelocity() * propCtx.getVelocity());
        newCtx.recoil = propCtx.recoil;
        newCtx.spread = propCtx.spread * 0.8f;
        newCtx.smokeScale = propCtx.smokeScale * 0.33f;
        newCtx.stress = propCtx.stress;
        newCtx.volume = propCtx.volume;
        return newCtx;
    }

    public boolean canConnectToSide(BlockState state, Direction face) { return this.getFacing(state).getAxis() == face.getAxis(); }// || this.getFacing(state) == face.getOpposite(); }
}
