package com.dsvv.cbcat.cannon.twin_autocannon;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.*;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public class TwinAutocannonInstance extends AbstractBlockEntityVisual<TwinAutocannonBlockEntity> implements SimpleDynamicVisual
{
    private TransformedInstance leavesInstance;
    private Direction facing;

    public TwinAutocannonInstance(VisualizationContext ctx, TwinAutocannonBlockEntity blockEntity, float partialTicks) {
        super(ctx, blockEntity, partialTicks);

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);

        if (blockEntity.getLeavesItemStack().getItem() instanceof BlockItem blockItem)
            leavesInstance = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.block(blockItem.getBlock().defaultBlockState()))
                .createInstance();
        else
            leavesInstance = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.block(Blocks.AIR.defaultBlockState()))
                .createInstance();
        leavesInstance.setChanged();
        this.updateTransforms(partialTicks);
    }

    @Override public void beginFrame(Context ctx) { this.updateTransforms(ctx.partialTick()); }

    private void updateTransforms(float partialTicks) {
        if (blockEntity.getUpdateInstance()) {
            ItemStack leavesStack = blockEntity.getLeavesItemStack();
            if (leavesStack.isEmpty()) {
                leavesInstance.setVisible(false);
                leavesInstance.setChanged();
            } else if (leavesStack.getItem() instanceof BlockItem leavesBlockItem) {
                leavesInstance.delete();
                leavesInstance = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.block(leavesBlockItem.getBlock().defaultBlockState()))
                        .createInstance();
                leavesInstance.setVisible(true);
            }
            blockEntity.setUpdateInstance(false);
        }
        BlockPos pos = this.getVisualPosition();
        Direction.Axis axis = this.facing.getAxis();
        float x = axis == Direction.Axis.X ? -0.005f : 0;
        float y = axis == Direction.Axis.Y ? -0.005f : 0;
        float z = axis == Direction.Axis.Z ? -0.005f : 0;
        Vec3 leavesOffset = new Vec3(x, y, z).scale(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? -1 : 1);
        Vec3 pivot = Vec3.atLowerCornerOf(pos).add(leavesOffset);
        if (Math.abs(leavesInstance.pose.get(3, 0) - pivot.x) < 0.01 && Math.abs(leavesInstance.pose.get(3, 1) - pivot.y) < 0.01 && Math.abs(leavesInstance.pose.get(3, 2) - pivot.z) < 0.01)
            return;
        leavesInstance.setIdentityTransform().translate(pivot);
        leavesInstance.setChanged();
        updateLight(partialTicks);
    }

    @Override
    public void updateLight(float partialTicks) {
        this.relight(this.pos, this.leavesInstance);
    }

    @Override
    protected void _delete() {
        this.leavesInstance.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(leavesInstance);
    }
}
