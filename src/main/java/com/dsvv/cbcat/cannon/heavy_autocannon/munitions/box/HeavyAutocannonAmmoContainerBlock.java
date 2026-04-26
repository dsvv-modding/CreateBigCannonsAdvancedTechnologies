package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonAmmoType;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.dsvv.cbcat.registry.MenuRegister;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Locale;

public class HeavyAutocannonAmmoContainerBlock extends Block implements IWrenchable, SimpleWaterloggedBlock, IBE<HeavyAutocannonAmmoContainerBlockEntity> {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<State> CONTAINER_STATE = EnumProperty.create("state", State.class);

    public HeavyAutocannonAmmoContainerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(CONTAINER_STATE, State.EMPTY)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AXIS).add(CONTAINER_STATE).add(WATERLOGGED);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean waterlogged = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(AXIS, context.getHorizontalDirection().getAxis())
                .setValue(WATERLOGGED, waterlogged);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(AXIS) == Direction.Axis.X)
            return Shapes.box(0.0625, 0, 0.3125, 0.9325, 1, 0.6875);
        return Shapes.box(0.3125, 0, 0.0625, 0.6875, 1, 0.9325);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof HeavyAutocannonAmmoContainerBlockEntity be) {
            if (stack.hasCustomHoverName()) be.setCustomName(stack.getHoverName());
            be.setMainAmmoDirect(HeavyAutocannonAmmoContainerItem.getMainAmmoStack(stack));
            be.setTracersDirect(HeavyAutocannonAmmoContainerItem.getTracerAmmoStack(stack));
            be.setSpacing(HeavyAutocannonAmmoContainerItem.getTracerSpacing(stack));
            be.updateBlockState(state, true);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof HeavyAutocannonAmmoContainerBlockEntity be && player.isCreative() && be.canDropInCreative()) {
            ItemStack stack = new ItemStack(this.asItem());
            be.saveToItem(stack);
            if (be.hasCustomName()) stack.setHoverName(be.getCustomName());

            ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof HeavyAutocannonAmmoContainerBlockEntity) {
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof HeavyAutocannonAmmoContainerBlockEntity be) be.saveToItem(itemStack);
        return itemStack;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof HeavyAutocannonAmmoContainerBlockEntity be) {
            if (player instanceof ServerPlayer sPlayer) {
                MenuRegister.HEAVY_AUTOCANNON_AMMO_BOX.open(sPlayer, be.getDisplayName(), be, buf -> {
                    buf.writeBoolean(!be.isCreativeContainer());
                    buf.writeVarInt(be.getSpacing());
                    buf.writeBoolean(true);
                    buf.writeBlockPos(pos);
                });
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof HeavyAutocannonAmmoContainerBlockEntity be)) return 0;
        HeavyAutocannonAmmoType type = be.getAmmoType();
        if (type == HeavyAutocannonAmmoType.NONE) return 0;
        if (be.isCreativeContainer()) return be.getTotalCount() == 0 ? 0 : 15;
        return (int) Math.floor((float) be.getTotalCount() / (float) type.getCapacity() * 15f);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        if (level.getBlockEntity(pos) instanceof HeavyAutocannonAmmoContainerBlockEntity be) be.recheckOpen();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction face, BlockState otherState, LevelAccessor level, BlockPos pos, BlockPos otherPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, face, otherState, level, pos, otherPos);
    }

    @Override public Class<HeavyAutocannonAmmoContainerBlockEntity> getBlockEntityClass() { return HeavyAutocannonAmmoContainerBlockEntity.class; }
    @Override public BlockEntityType<? extends HeavyAutocannonAmmoContainerBlockEntity> getBlockEntityType() { return BlockEntityRegister.HEAVY_AUTOCANNON_AMMO_BOX_BLOCK_ENTITY.get(); }

    public enum State implements StringRepresentable {
        EMPTY,
        FILLED;

        private final String id = this.name().toLowerCase(Locale.ROOT);

        @Override public String getSerializedName() { return this.id; }

        public static State getFromFilled(boolean filled) { return filled ? FILLED : EMPTY; }
    }
}
