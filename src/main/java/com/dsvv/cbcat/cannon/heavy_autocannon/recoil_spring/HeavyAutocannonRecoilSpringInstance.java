package com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.model.Model;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HeavyAutocannonRecoilSpringInstance extends AbstractBlockEntityVisual<HeavyAutocannonRecoilSpringBlockEntity> implements SimpleDynamicVisual
{
    private final Map<BlockPos, OrientedInstance> blocks = new HashMap<>();

    private boolean first = true;

    private Direction facing;

    public HeavyAutocannonRecoilSpringInstance(VisualizationContext ctx, HeavyAutocannonRecoilSpringBlockEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.blocks.clear();
        for (Map.Entry<BlockPos, BlockState> entry : this.blockEntity.toAnimate.entrySet()) {
            if (entry.getValue() == null) continue;
            this.blocks.put(entry.getKey(), this.instancerProvider().instancer(InstanceTypes.ORIENTED, Models.block(entry.getValue())).createInstance());
        }

        this.updateTransforms(partialTick);
    }

    @Override public void beginFrame(DynamicVisual.Context ctx) { this.updateTransforms(ctx.partialTick()); }

    private void updateTransforms(float partialTicks) {
        float scale = this.blockEntity.getAnimateOffset(partialTicks);

        Vector3f offs = this.facing.step();
        offs.mul((1 - scale) * -0.875f);
        offs.add(this.visualPos.getX(), this.visualPos.getY(), this.visualPos.getZ());

        for (Map.Entry<BlockPos, OrientedInstance> entry : this.blocks.entrySet()) {
            BlockPos pos1 = entry.getKey();
            entry.getValue().position(offs).translatePosition(pos1.getX(), pos1.getY(), pos1.getZ()).setChanged();
        }
    }

    @Override
    public void updateLight(float partialTicks) {
        this.relight(this.pos);
        for (Map.Entry<BlockPos, OrientedInstance> entry : this.blocks.entrySet()) {
            this.relight(this.pos.offset(entry.getKey()), entry.getValue());
        }
    }

    @Override
    protected void _delete() {
        for (OrientedInstance block : this.blocks.values()) block.delete();
    }

    private PartialModel getPartialModelForState() {
        return this.blockState.getBlock() instanceof HeavyAutocannonBlock cBlock
                ? ExtraDataRegister.heavyAutocannonSpringFor(cBlock.getAutocannonMaterial())
                : ExtraDataRegister.heavyAutocannonSpringFor(CBCAutocannonMaterials.CAST_IRON);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        for (OrientedInstance block : this.blocks.values()) consumer.accept(block);
    }
}
