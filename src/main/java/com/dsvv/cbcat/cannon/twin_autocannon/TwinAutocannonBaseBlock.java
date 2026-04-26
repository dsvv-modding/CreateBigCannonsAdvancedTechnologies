package com.dsvv.cbcat.cannon.twin_autocannon;

import com.dsvv.cbcat.base.LeavesCannonBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.Tags;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;

public abstract class TwinAutocannonBaseBlock extends DirectionalBlock implements TwinAutocannonBlock, SimpleWaterloggedBlock
{
    private final AutocannonMaterial material;
    protected final boolean vertical;
    protected boolean isComplete = true;

    protected TwinAutocannonBaseBlock(Properties properties, AutocannonMaterial material, boolean vertical) {
        super(properties.pushReaction(PushReaction.NORMAL));
        this.material = material;
        this.vertical = vertical;
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override public AutocannonMaterial getAutocannonMaterial() { return this.material; }
    @Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override public BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
    @Override public BlockState mirror(BlockState state, Mirror mirror) { return state.setValue(FACING, mirror.mirror(state.getValue(FACING))); }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction face, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, face, otherState, level, pos, otherPos);
    }
    public boolean canConnectToSide(BlockState state, Direction face, boolean isVertical) { return (this.getFacing(state) == face || this.getFacing(state) == face.getOpposite()) && isVertical == vertical; }

    public boolean canConnectToSide(BlockState state, Direction face) { return canConnectToSide(state, face, vertical); }

    public boolean isVertical() { return vertical; }

    public boolean isComplete(BlockState state) {
        return isComplete;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof LeavesCannonBlockEntity leavesBE))
            return super.use(state, level, pos, player, hand, hit);
        if (stack.is(Tags.Items.SHEARS) && !leavesBE.getLeavesItemStack().isEmpty()) {
            ItemStack leaves = leavesBE.getLeavesItemStack();
            leaves.setCount(1);
            ItemEntity dropEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), leaves);
            level.addFreshEntity(dropEntity);
            leavesBE.setLeavesItemStack(ItemStack.EMPTY);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (leavesBE.getLeavesItemStack().isEmpty() && stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof LeavesBlock) {
            leavesBE.setLeavesItemStack(stack.copy());
            stack.setCount(stack.getCount() - 1);
            player.setItemInHand(hand, stack);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }
}
