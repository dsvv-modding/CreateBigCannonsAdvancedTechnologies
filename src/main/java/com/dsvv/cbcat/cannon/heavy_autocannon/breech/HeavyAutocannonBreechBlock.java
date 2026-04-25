package com.dsvv.cbcat.cannon.heavy_autocannon.breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBaseBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.IHeavyAutocannonBreech;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;

import static rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock.writeAndSyncSingleBlockData;

public class HeavyAutocannonBreechBlock extends HeavyAutocannonBaseBlock implements IBE<HeavyAutocannonBreechBlockEntity>, HeavyAutocannonBlock, IHeavyAutocannonBreech
{
    public static final DirectionProperty FACING = HeavyAutocannonBaseBlock.FACING;
    public HeavyAutocannonBreechBlock(Properties properties, AutocannonMaterial material) {
        super(properties, material);
        this.codec = simpleCodec(this::fromSelf);
    }

    public HeavyAutocannonBreechBlock(Properties properties, AutocannonMaterial material, boolean isComplete) {
        this(properties, material);
        this.isComplete = isComplete;
    }

    private final MapCodec<? extends DirectionalBlock> codec;

    private HeavyAutocannonBreechBlock fromSelf(Properties properties) {
        return new HeavyAutocannonBreechBlock(properties, this.getAutocannonMaterial());
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    @Override
    public Class<HeavyAutocannonBreechBlockEntity> getBlockEntityClass() {
        return HeavyAutocannonBreechBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HeavyAutocannonBreechBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.HEAVY_AUTOCANNON_BREECH_BLOCK_ENTITY.get();
    }

    @Override
    public CannonCastShape getCannonShape() {
        return CannonCastingShapes.HEAVY_AUTOCANNON_BREECH;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState clicked = context.getLevel().getBlockState(context.getClickedPos().relative(context.getHorizontalDirection()));
        return this.defaultBlockState().setValue(FACING, clicked.getBlock() instanceof HeavyAutocannonBlock ? context.getHorizontalDirection() : context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean isBreechMechanism(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125), BooleanOp.OR);
        return new AllShapes.Builder(shape).forDirectional().get(getFacing(state));
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.getBlockEntity(pos) instanceof HeavyAutocannonBreechBlockEntity breech) {

            ItemStack container = breech.getMagazine();
            boolean changed = false;
            boolean tryAdd = false;
            if (!container.isEmpty()) {
                if (!level.isClientSide) {
                    tryAdd = true;
                    breech.setMagazine(ItemStack.EMPTY);
                }
                changed = true;
            }
            if (stack.getItem() instanceof HeavyAutocannonAmmoContainerItem) {
                if (!level.isClientSide) {
                    breech.setMagazine(stack);
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    CBCSoundEvents.PLACE_AUTOCANNON_AMMO_CONTAINER.playOnServer(level, pos);
                }
                changed = true;
            }

            if (tryAdd && !player.addItem(container)) {
                Vec3 spawnLoc = Vec3.atCenterOf(pos);
                ItemEntity dropEntity = new ItemEntity(level, spawnLoc.x, spawnLoc.y, spawnLoc.z, container);
                level.addFreshEntity(dropEntity);
            }
            if (changed) {
                breech.notifyUpdate();
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, result);
    }

    @Override
    public boolean onInteractWhileAssembled(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand,
                                            Level level, Contraption contraption, BlockEntity be,
                                            StructureTemplate.StructureBlockInfo info, PitchOrientedContraptionEntity entity) {
        if (!(be instanceof HeavyAutocannonBreechBlockEntity breech)) return false;

        ItemStack stack = player.getItemInHand(interactionHand);
        ItemStack container = breech.getMagazine();
        Vec3 globalPos = entity.toGlobalVector(Vec3.atCenterOf(localPos), 0);

        boolean insertingContainer = stack.getItem() instanceof HeavyAutocannonAmmoContainerItem;
        boolean canRemove = insertingContainer || stack.isEmpty() && (HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(container) == 0 || player.isShiftKeyDown());

        boolean changed = false;
        boolean tryAdd = false;
        if (!container.isEmpty() && canRemove) {
            if (!level.isClientSide) {
                tryAdd = true;
                breech.setMagazine(ItemStack.EMPTY);
            }
            changed = true;
        }
        if (breech.getMagazine().isEmpty() && insertingContainer) {
            if (!level.isClientSide) {
                breech.setMagazine(stack);
                player.setItemInHand(interactionHand, ItemStack.EMPTY);
                CBCSoundEvents.PLACE_AUTOCANNON_AMMO_CONTAINER.playOnServer(level, BlockPos.containing(globalPos));
            }
            changed = true;
        }

        if (tryAdd && !player.addItem(container)) {
            ItemEntity dropEntity = new ItemEntity(level, globalPos.x, globalPos.y, globalPos.z, container);
            level.addFreshEntity(dropEntity);
        }
        if (changed && !level.isClientSide) writeAndSyncSingleBlockData(be, info, entity, contraption);
        return changed;
    }

    public boolean canConnectToSide(BlockState state, Direction face) { return this.getFacing(state) == face; }
}
