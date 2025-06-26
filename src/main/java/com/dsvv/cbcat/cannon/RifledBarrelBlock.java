package com.dsvv.cbcat.cannon;

import com.dsvv.cbcat.base.CustomPropellantContext;
import com.dsvv.cbcat.base.SaveAssemble;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBaseBlock;
import rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class RifledBarrelBlock extends BigCannonBaseBlock implements IBE<RifledBarrelBlockEntity>, SaveAssemble
{
    public boolean complete = true;
    boolean builtUp;
    static VoxelShaper builtUpShaper = new AllShapes.Builder(box(1, 0, 1, 15, 16, 15)).forDirectional();
    static VoxelShaper shaper = new AllShapes.Builder(box(2, 0, 2, 14, 16, 14)).forDirectional();

    public RifledBarrelBlock(Properties properties, BigCannonMaterial material, boolean... builtUp)
    {
        super(properties, material);
        this.builtUp = builtUp.length > 0 && builtUp[0];
    }
    public RifledBarrelBlock(Properties properties, BigCannonMaterial material, boolean isComplete, boolean... builtUp)
    {
        this(properties, material, builtUp);
        complete = isComplete;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context)
    {
        return builtUp ? builtUpShaper.get(getFacing(state)) : shaper.get(getFacing(state));
    }

    @Override
    public BigCannonEnd getDefaultOpeningType() {
        return BigCannonEnd.OPEN;
    }

    @Override
    public CannonCastShape getCannonShape() {
        return CannonCastingShapes.RIFLED_BARREL;
    }

    @Override
    public boolean isComplete(BlockState blockState) {
        return complete;
    }

    @Override
    public Class<RifledBarrelBlockEntity> getBlockEntityClass() {
        return RifledBarrelBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RifledBarrelBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.RIFLED_BARREL_BLOCK_ENTITY.get();
    }

    public CustomPropellantContext applyBarrelPhysic(@NotNull CustomPropellantContext propCtx)
    {
        CustomPropellantContext newCtx = new CustomPropellantContext();
        newCtx.explosionGas = propCtx.explosionGas - 0.35f;
        newCtx.drag = propCtx.drag + 0.08f * (propCtx.getVelocity() * propCtx.getVelocity());
        newCtx.recoil = propCtx.recoil;
        newCtx.spread = propCtx.spread * 0.45f;
        newCtx.smokeScale = propCtx.smokeScale;
        newCtx.stress = propCtx.stress;
        newCtx.volume = propCtx.volume;
        return newCtx;
    }

    public boolean canConnectToSide(BlockState state, Direction face) { return this.getFacing(state).getAxis() == face.getAxis(); }// || this.getFacing(state) == face.getOpposite(); }
}
