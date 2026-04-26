package com.dsvv.cbcat.cannon.rocketpod.breech;

import com.dsvv.cbcat.cannon.FumeExtractorBlock;
import com.dsvv.cbcat.cannon.rocketpod.IRocketPodBreech;
import com.dsvv.cbcat.cannon.rocketpod.RocketPodBaseBlock;
import com.dsvv.cbcat.cannon.rocketpod.RocketPodBlock;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

import static rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock.writeAndSyncSingleBlockData;

public class RocketPodBreechBlock extends RocketPodBaseBlock implements IBE<RocketPodBreechBlockEntity>, RocketPodBlock, IRocketPodBreech {
    public static final DirectionProperty FACING = RocketPodBaseBlock.FACING;

    public RocketPodBreechBlock(Properties properties, AutocannonMaterial material) {
        super(properties, material);
        codec = simpleCodec(this::fromSelf);
    }

    public RocketPodBreechBlock(Properties properties, AutocannonMaterial material, boolean isComplete) {
        this(properties, material);
        this.isComplete = isComplete;
    }

    private final MapCodec<? extends DirectionalBlock> codec;

    private RocketPodBreechBlock fromSelf(Properties properties) {
        return new RocketPodBreechBlock(properties, this.getAutocannonMaterial());
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    @Override
    public Class<RocketPodBreechBlockEntity> getBlockEntityClass() {
        return RocketPodBreechBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RocketPodBreechBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.ROCKET_POD_BREECH_BLOCK_ENTITY.get();
    }

    @Override
    public CannonCastShape getCannonShape() {
        return CannonCastingShapes.ROCKET_POD_BREECH;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState clicked = context.getLevel().getBlockState(context.getClickedPos().relative(context.getHorizontalDirection()));
        return this.defaultBlockState().setValue(FACING, clicked.getBlock() instanceof RocketPodBlock ? context.getHorizontalDirection() : context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean isBreechMechanism(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.1875, 1, 1, 0.8125), BooleanOp.OR);
        return new AllShapes.Builder(shape).forDirectional().get(getFacing(state));
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.getBlockEntity(pos) instanceof RocketPodBreechBlockEntity breech) {
            boolean changed = breech.addToInputBuffer(stack);
            if (changed) {
                breech.notifyUpdate();
                ItemStack newStack = stack.copy();
                newStack.setCount(newStack.getCount() - 1);
                player.setItemInHand(hand, newStack);
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, result);
    }

    @Override
    public boolean onInteractWhileAssembled(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand,
                                            Level level, Contraption contraption, BlockEntity be,
                                            StructureTemplate.StructureBlockInfo info, PitchOrientedContraptionEntity entity) {
        if (!(be instanceof RocketPodBreechBlockEntity breech)) return false;

        ItemStack stack = player.getItemInHand(interactionHand);
        boolean changed = breech.addToInputBuffer(stack);
        if (changed && !level.isClientSide) {
            writeAndSyncSingleBlockData(be, info, entity, contraption);
            ItemStack newStack = stack.copy();
            newStack.setCount(newStack.getCount() - 1);
            player.setItemInHand(interactionHand, newStack);
        }
        return changed;
    }

    public boolean canConnectToSide(BlockState state, Direction face) { return this.getFacing(state) == face; }
}
