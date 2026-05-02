package com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.index.CBCAutocannonMaterials;

import java.util.function.Consumer;

public class HeavyAutocannonQuickFireBreechInstance extends AbstractBlockEntityVisual<HeavyAutocannonQuickFireBreechBlockEntity> implements SimpleDynamicVisual
{
    private final OrientedInstance breechblock;

    public HeavyAutocannonQuickFireBreechInstance(VisualizationContext ctx, HeavyAutocannonQuickFireBreechBlockEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
        Direction facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.breechblock = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(getPartialModelForState(this.blockState)))
                .createInstance();

        if (!(blockState.getBlock() instanceof HeavyAutocannonQuickFireBreechBlock haBreechBlock) || !haBreechBlock.isComplete(blockState)) return;

        boolean alongFirst = this.blockState.getValue(HeavyAutocannonQuickFireBreechBlock.AXIS);
        if (alongFirst) {
            this.breechblock.rotateYDegrees(90f);
        }
        if (facing.getAxis().isHorizontal()) {
            this.breechblock.rotateTo(Direction.NORTH, Direction.UP);
        }
        this.transformModels(partialTick);
    }

    private void transformModels(float partialTick) {
        if (!(blockState.getBlock() instanceof HeavyAutocannonQuickFireBreechBlock haBreechBlock) || !haBreechBlock.isComplete(blockState)) return;

        float progress = this.blockEntity.getOpenProgress(partialTick);
        BlockPos visualPos = this.getVisualPosition();

        float renderedBreechblockOffset = progress / 16.0f * -14.5f;
        Vector3f normal = Direction.UP.step();
        normal.mul(renderedBreechblockOffset);
        this.breechblock.position(visualPos).translatePosition(normal.x(), normal.y(), normal.z()).setChanged();
    }

    private PartialModel getPartialModelForState(BlockState state) {
        return state.getBlock() instanceof HeavyAutocannonBlock cBlock
                ? ExtraDataRegister.heavyAutocannonBreechblockFor(cBlock.getAutocannonMaterial())
                : ExtraDataRegister.heavyAutocannonBreechblockFor(CBCAutocannonMaterials.CAST_IRON);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(breechblock);
    }

    @Override
    public void updateLight(float v) {
        this.relight(this.pos, this.breechblock);
    }

    @Override
    protected void _delete() {
        this.breechblock.delete();
    }

    @Override
    public void beginFrame(DynamicVisual.Context context) {
        this.transformModels(context.partialTick());
    }
}