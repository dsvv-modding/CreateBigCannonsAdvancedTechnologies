package com.dsvv.cbcat.cannon.twin_autocannon;

import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.dsvv.cbcat.registry.BlockRegister;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

import java.util.function.BiFunction;

public class TwinAutocannonBarrelBlock extends TwinAutocannonBaseBlock implements IBE<TwinAutocannonBlockEntity>, IWrenchable, MovesWithTwinAutocannonRecoilSpring, TwinAutocannonBlock
{
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    public static final EnumProperty<AutocannonBarrelBlock.AutocannonBarrelEnd> BARREL_END = EnumProperty.create("end", AutocannonBarrelBlock.AutocannonBarrelEnd.class);

    boolean isComplete = true;

    private float volumeMultiplier = 1;

    private BiFunction<Direction, AutocannonBarrelBlock.AutocannonBarrelEnd, BlockState> instanceFunc = null;

    protected TwinAutocannonBarrelBlock(Properties properties, AutocannonMaterial material, boolean vertical) {
        super(properties, material, vertical);
        this.registerDefaultState(this.defaultBlockState().setValue(BARREL_END, AutocannonBarrelBlock.AutocannonBarrelEnd.NOTHING).setValue(ASSEMBLED, false));
        this.codec = simpleCodec(this::fromSelf);
    }

    public TwinAutocannonBarrelBlock(Properties properties, AutocannonMaterial material, boolean vertical, float volumeMultiplier) {
        this(properties, material, vertical);
        this.volumeMultiplier = volumeMultiplier > 0 ? volumeMultiplier : 1;
        if (volumeMultiplier > 1)
            isComplete = false;
    }

    public TwinAutocannonBarrelBlock(Properties properties, AutocannonMaterial material, BiFunction<Direction, AutocannonBarrelBlock.AutocannonBarrelEnd, BlockState> instance, boolean vertical) {
        this(properties, material, vertical);
        instanceFunc = instance;
    }

    public TwinAutocannonBarrelBlock(Properties properties, AutocannonMaterial material, BiFunction<Direction, AutocannonBarrelBlock.AutocannonBarrelEnd, BlockState> instance, boolean vertical, float volumeMultiplier) {
        this(properties, material, vertical, volumeMultiplier);
        instanceFunc = instance;
    }

    public TwinAutocannonBarrelBlock(Properties properties, AutocannonMaterial material, boolean vertical, boolean isComplete)
    {
        this(properties, material, vertical);
        this.isComplete = isComplete;
    }

    private final MapCodec<? extends DirectionalBlock> codec;

    private TwinAutocannonBarrelBlock fromSelf(Properties properties) {
        return new TwinAutocannonBarrelBlock(properties, this.getAutocannonMaterial(), this.vertical);
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ASSEMBLED).add(BARREL_END);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Class<TwinAutocannonBlockEntity> getBlockEntityClass() {
        return TwinAutocannonBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TwinAutocannonBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.TWIN_AUTOCANNON_BLOCK_ENTITY.get();
    }

    @Override
    public CannonCastShape getCannonShape() {
        return vertical ? CannonCastingShapes.VERT_TWIN_AUTOCANNON_BARREL : CannonCastingShapes.TWIN_AUTOCANNON_BARREL;
    }

    @Override
    public boolean isBreechMechanism(BlockState state) {
        return false;
    }

    @Override
    public CannonCastShape getCannonShapeInLevel(LevelAccessor level, BlockState state, BlockPos pos) {
        return switch (state.getValue(BARREL_END)) {
            case FLANGED -> CannonCastShape.AUTOCANNON_BARREL_FLANGED;
            default -> super.getCannonShapeInLevel(level, state, pos);
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        if(!vertical)
        {
            if (volumeMultiplier == 1) {
                shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.375, 0.375, 1, 0.625), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.625, 0, 0.375, 0.875, 1, 0.625), BooleanOp.OR);
            } else {
                shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.3125, 0.4375, 1, 0.6875), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.5625, 0, 0.3125, 0.9375, 1, 0.6875), BooleanOp.OR);
            }
        }
        else
        {
            if(volumeMultiplier == 1) {
                shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.125, 0.625, 1, 0.375), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.625, 0.625, 1, 0.875), BooleanOp.OR);
            } else {
                shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.0625, 0.6875, 1, 0.4375), BooleanOp.OR);
                shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.5625, 0.6875, 1, 0.9375), BooleanOp.OR);
            }
        }
        return new AllShapes.Builder(shape).forDirectional().get(getFacing(state));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos();
        TwinAutocannonBlockEntity autocannon = this.getBlockEntity(level, pos);
        Direction facing = this.getFacing(state);

        if (!autocannon.cannonBehavior().isConnectedTo(facing)) {
            level.setBlock(context.getClickedPos(), state.cycle(BARREL_END), 3);
            TwinAutocannonBlockEntity autocannon1 = this.getBlockEntity(level, pos);
            if (autocannon1 != null) {
                boolean previouslyConnected = autocannon.cannonBehavior().isConnectedTo(facing.getOpposite());
                autocannon1.cannonBehavior().setConnectedFace(facing.getOpposite(), previouslyConnected);
                autocannon1.setLeavesItemStack(autocannon.getLeavesItemStack());
                if (level.getBlockEntity(pos.relative(facing.getOpposite())) instanceof TwinAutocannonBlockEntity autocannon2) {
                    autocannon2.cannonBehavior().setConnectedFace(facing, previouslyConnected);
                }
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override public BlockState getMovingState(BlockState original) {
        BlockState instance = instanceFunc.apply(this.getFacing(original), original.getValue(BARREL_END));
        instance.setValue(AutocannonBarrelBlock.ASSEMBLED, false).setValue(AutocannonBarrelBlock.BARREL_END, original.getValue(BARREL_END));
        return instance;
    }
    @Override public BlockState getStationaryState(BlockState original) { return original.setValue(ASSEMBLED, true); }

    public float getVolumeMultiplier() { return volumeMultiplier; }
}
