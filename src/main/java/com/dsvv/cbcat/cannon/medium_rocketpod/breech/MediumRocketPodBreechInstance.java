package com.dsvv.cbcat.cannon.medium_rocketpod.breech;

import com.dsvv.cbcat.registry.ExtraDataRegister;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Consumer;


public class MediumRocketPodBreechInstance extends AbstractBlockEntityVisual<MediumRocketPodBreechBlockEntity> implements SimpleDynamicVisual
{
    private final OrientedInstance[] rockets;
    private byte oldRocketPos = 0;

    public MediumRocketPodBreechInstance(VisualizationContext ctx, MediumRocketPodBreechBlockEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
        Direction.Axis axis = getRotationAxis(this.blockState);
        Direction facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.rockets = new OrientedInstance[4];
        for (int i = 0; i < rockets.length; i++) {
            this.rockets[i] = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(getPartialModelForState(this.blockState))).createInstance();

            boolean alongFirst = this.blockState.getValue(MediumRocketPodBreechBlock.FACING).getAxis() == Direction.Axis.X;
            BlockPos visualPos = this.getVisualPosition();
            if (alongFirst) {
                this.rockets[i].rotateYDegrees(-90f);
                this.rockets[i].rotateZDegrees(45f);
                this.rockets[i].position(visualPos).translatePosition(0, i > 1 ? -0.315f : 0.185f, (i % 2) == 0 ? -0.3175f : 0.3175f).setChanged();
            } else {
                this.rockets[i].rotateYDegrees(180);
                this.rockets[i].rotateZDegrees(45f);
                this.rockets[i].position(visualPos).translatePosition((i % 2) == 0 ? 0.3175f : -0.3175f, i > 1 ? -0.315f : 0.185f, 0).setChanged();
            }
            if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE)
                this.rockets[i].rotateTo(Direction.NORTH, Direction.UP);
            else
                this.rockets[i].rotateTo(Direction.SOUTH, Direction.UP);
            this.rockets[i].setVisible(false);
        }
        this.transformModels();
    }

    private void transformModels() {
        for (int i = 0; i < rockets.length; i++) {
            boolean rocketState = this.blockEntity.isSlotUsed(i);
            //byte comp = (byte)Math.pow(2, i);
            //if ((oldRocketPos & comp) != comp) {
                this.rockets[i].setVisible(rocketState);
                this.rockets[i].setChanged();
            //    oldRocketPos ^= comp;
            //}
        }
    }

    private PartialModel getPartialModelForState(BlockState state) {
        return ExtraDataRegister.mediumRocketModel();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        for (int i = 0; i < rockets.length; i++)
            consumer.accept(rockets[i]);
    }

    @Override
    public void updateLight(float v) {
        for (int i = 0; i < rockets.length; i++)
            this.relight(this.pos, this.rockets[i]);
    }

    @Override
    protected void _delete() {
        for (int i = 0; i < rockets.length; i++)
            this.rockets[i].delete();
    }

    @Override
    public void beginFrame(DynamicVisual.Context context) {
        this.transformModels();
    }

    private static Direction.Axis getRotationAxis(BlockState state) {
        return switch (state.getValue(MediumRocketPodBreechBlock.FACING).getAxis()) {
            case X, Y -> Direction.Axis.Z;
            case Z -> Direction.Axis.Y;
        };
    }
}