package com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBaseBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.IHeavyAutocannonBreech;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.IQFBreechLoadable;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.equipment.manual_loading.WormItem;

import static com.dsvv.cbcat.debugUtils.DebugUtils.displayCustomClientMessage;

public class HeavyAutocannonQuickFireBreechBlock extends HeavyAutocannonBaseBlock implements IBE<HeavyAutocannonQuickFireBreechBlockEntity>, HeavyAutocannonBlock, IHeavyAutocannonBreech
{
    //public static final DirectionProperty FACING = HeavyAutocannonBaseBlock.FACING;
    public static final BooleanProperty AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

    public HeavyAutocannonQuickFireBreechBlock(Properties properties, AutocannonMaterial material) {
        super(properties, material);
    }

    public HeavyAutocannonQuickFireBreechBlock(Properties properties, AutocannonMaterial material, boolean isComplete) {
        this(properties, material);
        this.isComplete = isComplete;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Class<HeavyAutocannonQuickFireBreechBlockEntity> getBlockEntityClass() {
        return HeavyAutocannonQuickFireBreechBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HeavyAutocannonQuickFireBreechBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.HEAVY_AUTOCANNON_QFBREECH_BLOCK_ENTITY.get();
    }

    @Override
    public CannonCastShape getCannonShape() {
        return CannonCastingShapes.HEAVY_AUTOCANNON_BREECH;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState clicked = context.getLevel().getBlockState(context.getClickedPos().relative(context.getHorizontalDirection()));
        return this.defaultBlockState().setValue(FACING, clicked.getBlock() instanceof HeavyAutocannonBlock ? context.getHorizontalDirection() : context.getHorizontalDirection().getOpposite())
                .setValue(AXIS, context.getHorizontalDirection().getAxis() == Direction.Axis.X);
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof HeavyAutocannonQuickFireBreechBlockEntity breech) {

            /*ItemStack container = breech.getMagazine();
            boolean changed = false;
            boolean tryAdd = false;
            if (!container.isEmpty()) {
                if (!level.isClientSide) {
                    tryAdd = true;
                    //breech.setMagazine(ItemStack.EMPTY);
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
                return InteractionResult.sidedSuccess(level.isClientSide);
            }*/
        }
        return super.use(state, level, pos, player, hand, result);
    }

    @Override
    public boolean onInteractWhileAssembled(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand,
                                            Level level, Contraption contraption, BlockEntity be, StructureTemplate.StructureBlockInfo info,
                                            PitchOrientedContraptionEntity entity) {
        if (!(be instanceof HeavyAutocannonQuickFireBreechBlockEntity breech)) return false;

        ItemStack stack = player.getItemInHand(interactionHand);
        Direction pushDirection = entity.getInitialOrientation();

        if (stack.isEmpty()) {
            breech.toggleOpen();
            if (breech.willBeOpen()) {
                if (!level.isClientSide) {
                    ItemStack eject = breech.extractNextInput();
                    if (eject != ItemStack.EMPTY) {
                        eject.setCount(1);
                        Vec3 normal = new Vec3(pushDirection.getOpposite().step());
                        Vec3 ejectPos = Vec3.atCenterOf(localPos).add(normal.scale(1.1));
                        Vec3 globalPos = entity.toGlobalVector(ejectPos, 0);
                        Vector3f direc = be.getBlockState().getValue(FACING).getOpposite().step().mul(0.125f);
                        ItemEntity ejectItem = new ItemEntity(level, globalPos.x, globalPos.y, globalPos.z, eject, direc.x, direc.y, direc.z);
                        ejectItem.setPickUpDelay((int) Math.floor(CBCConfigs.server().munitions.quickFiringBreechItemPickupDelay.get() * 0.8));
                        level.addFreshEntity(ejectItem);
                    }
                }
            }
        }
        else if (breech.getOpen()) {
            if (!level.isClientSide)
                if (stack.getItem() instanceof IQFBreechLoadable) {
                    if (((HeavyAutocannonQuickFireBreechBlockEntity) be).isLoaded())
                        return false;
                    ItemStack insert = stack.copy();
                    insert.setCount(1);
                    if (stack.getItem() instanceof HeavyAutocannonCartridgeItem)
                        breech.setCartridge(insert);
                    //else if (stack.getItem() instanceof AbstractHeavyAutocannonProjectileItem)
                    //    breech.setProjectile(stack);
                    //else if (stack.getItem() instanceof IHeavyAutocannonCharge)
                    //    breech.addCharge();
                    else
                        return false;
                    if (!stack.isEmpty())
                        stack.setCount(stack.getCount() - 1);
                    player.setItemInHand(interactionHand, stack);
                } else if (stack.getItem() instanceof WormItem) {
                    ItemStack eject = breech.extractNextInput();
                    if (eject != ItemStack.EMPTY) {
                        eject.setCount(1);
                        Vec3 normal = new Vec3(pushDirection.getOpposite().step());
                        Vec3 ejectPos = Vec3.atCenterOf(localPos).add(normal.scale(1.1));
                        Vec3 globalPos = entity.toGlobalVector(ejectPos, 0);
                        Vector3f direc = be.getBlockState().getValue(FACING).getOpposite().step().mul(0.125f);
                        ItemEntity ejectItem = new ItemEntity(level, globalPos.x, globalPos.y, globalPos.z, eject, direc.x, direc.y, direc.z);
                        ejectItem.setPickUpDelay((int) Math.floor(CBCConfigs.server().munitions.quickFiringBreechItemPickupDelay.get() * 0.8));
                        level.addFreshEntity(ejectItem);
                    }
                }
        }
        else
            return false;
        return true;
    }

    public boolean canConnectToSide(BlockState state, Direction face) { return this.getFacing(state) == face; }

    /*public void setSaveCharges(Level level, BlockPos pos, int saveCharges) {
        if (level.getBlockEntity(pos) instanceof HeavyAutocannonQuickFireBreechBlockEntity be) {
            be.setMaxCharges(saveCharges);
        }
    }*/
}