package com.dsvv.cbcat.cannon.heavy_autocannon;

import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.registry.BlockEntityRegister;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class HeavyAutocannonBarrelBlock extends HeavyAutocannonBaseBlock implements IBE<HeavyAutocannonBlockEntity>, IWrenchable, MovesWithHeavyAutocannonRecoilSpring, HeavyAutocannonBlock
{
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    //public static final EnumProperty<HeavyAutocannonBarrelEnd> BARREL_END = EnumProperty.create("end", HeavyAutocannonBarrelEnd.class);

    boolean chamber = false;
    boolean isComplete = true;

    private float volumeMultiplier = 1;

    public HeavyAutocannonBarrelBlock(Properties properties, AutocannonMaterial material, boolean isChamber) {
        super(properties, material);
        this.chamber = isChamber;
        this.registerDefaultState(this.defaultBlockState().setValue(ASSEMBLED, false));
        this.codec = simpleCodec(this::fromSelf);
    }

    public HeavyAutocannonBarrelBlock(Properties properties, AutocannonMaterial material, boolean isChamber, boolean isComplete)
    {
        this(properties, material, isChamber);
        this.isComplete = isComplete;
    }

    public HeavyAutocannonBarrelBlock(Properties properties, AutocannonMaterial material, boolean isChamber, float volumeMultiplier) {
        this(properties, material, isChamber);
        this.volumeMultiplier = volumeMultiplier > 0 ? volumeMultiplier : 1;
        if (this.volumeMultiplier > 1)
            isComplete = false;
    }

    private final MapCodec<? extends DirectionalBlock> codec;

    private HeavyAutocannonBarrelBlock fromSelf(Properties properties) {
        return new HeavyAutocannonBarrelBlock(properties, this.getAutocannonMaterial(), this.chamber);
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ASSEMBLED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Class<HeavyAutocannonBlockEntity> getBlockEntityClass() {
        return HeavyAutocannonBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HeavyAutocannonBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.HEAVY_AUTOCANNON_BLOCK_ENTITY.get();
    }

    @Override
    public CannonCastShape getCannonShape() {
        return CannonCastingShapes.HEAVY_AUTOCANNON_BARREL;
    }

    @Override
    public boolean isBreechMechanism(BlockState state) {
        return false;
    }

    @Override
    public CannonCastShape getCannonShapeInLevel(LevelAccessor level, BlockState state, BlockPos pos) {
       return super.getCannonShapeInLevel(level, state, pos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        if (volumeMultiplier == 1) {
            shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875), BooleanOp.OR);
        } else {
            shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75), BooleanOp.OR);
        }
        return new AllShapes.Builder(shape).forDirectional().get(getFacing(state));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos();
        HeavyAutocannonBlockEntity autocannon = this.getBlockEntity(level, pos);
        Direction facing = this.getFacing(state);

        if (!autocannon.cannonBehavior().isConnectedTo(facing)) {
            //level.setBlock(context.getClickedPos(), state.cycle(BARREL_END), 3);
            HeavyAutocannonBlockEntity autocannon1 = this.getBlockEntity(level, pos);
            if (autocannon1 != null) {
                boolean previouslyConnected = autocannon.cannonBehavior().isConnectedTo(facing.getOpposite());
                autocannon1.cannonBehavior().setConnectedFace(facing.getOpposite(), previouslyConnected);
                if (level.getBlockEntity(pos.relative(facing.getOpposite())) instanceof HeavyAutocannonBlockEntity autocannon2) {
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

    @Override public BlockState getMovingState(BlockState original) { return original.setValue(ASSEMBLED, false); }
    @Override public BlockState getStationaryState(BlockState original) { return original.setValue(ASSEMBLED, true); }


    public float getVolumeMultiplier()
    {
        return volumeMultiplier;
    }

    public boolean isChamber() { return chamber; }
}
