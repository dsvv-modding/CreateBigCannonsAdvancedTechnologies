package com.dsvv.cbcat.cannon.rocketpod;

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

public class RocketPodBarrelBlock extends RocketPodBaseBlock implements IBE<RocketPodBlockEntity>, IWrenchable, RocketPodBlock {
    public static final BooleanProperty ASSEMBLED = BooleanProperty.create("assembled");
    
    public RocketPodBarrelBlock(Properties properties, AutocannonMaterial material) {
        super(properties, material);
        this.registerDefaultState(this.defaultBlockState().setValue(ASSEMBLED, false));
        this.codec = simpleCodec(this::fromSelf);
    }

    public RocketPodBarrelBlock(Properties properties, AutocannonMaterial material, boolean isComplete)
    {
        this(properties, material);
        this.isComplete = isComplete;
    }

    private final MapCodec<? extends DirectionalBlock> codec;

    private RocketPodBarrelBlock fromSelf(Properties properties) {
        return new RocketPodBarrelBlock(properties, this.getAutocannonMaterial());
    }

    @Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ASSEMBLED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Class<RocketPodBlockEntity> getBlockEntityClass() {
        return RocketPodBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RocketPodBlockEntity> getBlockEntityType() {
        return BlockEntityRegister.ROCKET_POD_BARREL_BLOCK_ENTITY.get();
    }

    @Override
    public CannonCastShape getCannonShape() {
        return CannonCastingShapes.ROCKET_POD_RAIL;
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
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.1875, 1, 1, 0.8125), BooleanOp.OR);
        
        return new AllShapes.Builder(shape).forDirectional().get(getFacing(state));
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockPos pos = context.getClickedPos();
        RocketPodBlockEntity autocannon = this.getBlockEntity(level, pos);
        Direction facing = this.getFacing(state);

        if (!autocannon.cannonBehavior().isConnectedTo(facing)) {
            //level.setBlock(context.getClickedPos(), state.cycle(BARREL_END), 3);
            RocketPodBlockEntity autocannon1 = this.getBlockEntity(level, pos);
            if (autocannon1 != null) {
                boolean previouslyConnected = autocannon.cannonBehavior().isConnectedTo(facing.getOpposite());
                autocannon1.cannonBehavior().setConnectedFace(facing.getOpposite(), previouslyConnected);
                if (level.getBlockEntity(pos.relative(facing.getOpposite())) instanceof RocketPodBlockEntity autocannon2) {
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
}
