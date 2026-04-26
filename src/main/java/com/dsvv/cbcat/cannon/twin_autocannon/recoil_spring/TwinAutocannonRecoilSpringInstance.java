package com.dsvv.cbcat.cannon.twin_autocannon.recoil_spring;

import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBlock;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.index.CBCAutocannonMaterials;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TwinAutocannonRecoilSpringInstance extends AbstractBlockEntityVisual<TwinAutocannonRecoilSpringBlockEntity> implements SimpleDynamicVisual
{
    private TransformedInstance spring;
    private TransformedInstance leavesInstance;
    private final Map<BlockPos, OrientedInstance> blocks1 = new HashMap<>();
    private final Map<BlockPos, OrientedInstance> blocks2 = new HashMap<>();

    private Direction facing;

    private boolean vertical;
    private boolean firstFire = true;

    public TwinAutocannonRecoilSpringInstance(VisualizationContext ctx, TwinAutocannonRecoilSpringBlockEntity blockEntity, float partialTicks) {
        super(ctx, blockEntity, partialTicks);
        vertical = blockEntity.isVertical();

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.spring = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(getPartialModelForState(), this.facing))
                .createInstance();

        this.blocks1.clear();
        this.blocks2.clear();
        for (Map.Entry<BlockPos, BlockState> entry : this.blockEntity.toAnimate.entrySet()) {
            if (entry.getValue() == null) continue;
            this.blocks1.put(entry.getKey(), this.instancerProvider().instancer(InstanceTypes.ORIENTED, Models.block(entry.getValue())).createInstance());
            this.blocks2.put(entry.getKey(), this.instancerProvider().instancer(InstanceTypes.ORIENTED, Models.block(entry.getValue())).createInstance());
        }

        if (blockEntity.getLeavesItemStack().getItem() instanceof BlockItem blockItem)
            leavesInstance = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.block(blockItem.getBlock().defaultBlockState()))
                    .createInstance();
        else
            leavesInstance = this.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.block(Blocks.AIR.defaultBlockState()))
                    .createInstance();
        leavesInstance.setChanged();

        this.updateTransforms(partialTicks);
    }

    @Override public void beginFrame(DynamicVisual.Context ctx) { this.updateTransforms(ctx.partialTick()); }

    private void updateTransforms(float partialTicks) {
        firstFire = blockEntity.firstFire;
        boolean flag = this.facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
        BlockPos pos = this.getVisualPosition().relative(this.facing.getOpposite(), flag ? 1 : 0);
        Vec3 pivot = Vec3.atLowerCornerOf(pos);
        Map.Entry<Float, Boolean> beValues = this.blockEntity.getAnimateOffset(partialTicks);
        float scale = beValues.getKey();
        firstFire = beValues.getValue();
        float f1 = scale * 0.5f + 0.5f;
        Direction.Axis axis = this.facing.getAxis();

        float fx = axis == Direction.Axis.X ? f1 : 1;
        float fy = axis == Direction.Axis.Y ? f1 : 1;
        float fz = axis == Direction.Axis.Z ? f1 : 1;

        this.spring.setIdentityTransform().translate(pivot);
        if (flag) {
            this.spring.rotateCentered(Mth.PI, axis.isVertical() ? Direction.EAST : Direction.UP)
                    .translate(this.facing.getOpposite().step());
        }
        this.spring.scale(fx, fy, fz).setChanged();


        Vector3f offs = this.facing.step();
        //offs.mul((1 - scale) * -0.5f);
        offs.add(this.getVisualPosition().getX(), this.getVisualPosition().getY(), this.getVisualPosition().getZ());
        offs.sub(facing.step());
        offs.sub(facing.step());

        Vector3f offsFiring = this.facing.step();
        offsFiring.mul((1 - scale) * -0.5f);
        offsFiring.add(this.getVisualPosition().getX(), this.getVisualPosition().getY(), this.getVisualPosition().getZ());
        offsFiring.sub(facing.step());

        Vector3f offs2 = vertical ? new Vector3f(0, 0.25f, 0) : new Vector3f((facing.getAxis() == Direction.Axis.Z ? -0.25f : 0), 0, (facing.getAxis() == Direction.Axis.X ? -0.25f : 0));

        for (Map.Entry<BlockPos, OrientedInstance> entry : this.blocks1.entrySet()) {
            BlockPos pos1 = entry.getKey().relative(facing);
            entry.getValue().position(pos1);
            if(entry != blocks1.entrySet().toArray()[0])
                entry.getValue().translatePosition(offs2.x, offs2.y, offs2.z);
            if(firstFire)
                entry.getValue().translatePosition(offsFiring.x, offsFiring.y, offsFiring.z);
            else
                entry.getValue().translatePosition(offs.x, offs.y, offs.z);
        }
        for (Map.Entry<BlockPos, OrientedInstance> entry : this.blocks2.entrySet()) {
            BlockPos pos1 = entry.getKey().relative(facing);
            entry.getValue().position(pos1);
            if(entry != blocks2.entrySet().toArray()[0])
                entry.getValue().translatePosition(-offs2.x, -offs2.y, -offs2.z);
            if(!firstFire)
                entry.getValue().translatePosition(offsFiring.x, offsFiring.y, offsFiring.z);
            else
                entry.getValue().translatePosition(offs.x, offs.y, offs.z);
            entry.getValue().setChanged();
        }

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
        float x = axis == Direction.Axis.X ? -0.005f : 0;
        float y = axis == Direction.Axis.Y ? -0.005f : 0;
        float z = axis == Direction.Axis.Z ? -0.005f : 0;
        Vec3 leavesOffset = new Vec3(x, y, z).scale(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? -1 : 1);
        Vec3 leavesPivot = Vec3.atLowerCornerOf(this.getVisualPosition()).add(leavesOffset);
        if (Math.abs(leavesInstance.pose.get(3, 0) - leavesPivot.x) < 0.01 && Math.abs(leavesInstance.pose.get(3, 1) - leavesPivot.y) < 0.01 && Math.abs(leavesInstance.pose.get(3, 2) - leavesPivot.z) < 0.01)
            return;
        leavesInstance.setIdentityTransform().translate(leavesPivot);
        leavesInstance.setChanged();
        updateLight(partialTicks);
    }

    @Override
    public void updateLight(float partialTicks) {
        this.relight(this.pos, this.spring);
        this.relight(this.pos, this.leavesInstance);
        for (Map.Entry<BlockPos, OrientedInstance> entry : this.blocks1.entrySet()) {
            this.relight(this.pos.offset(entry.getKey()), entry.getValue());
        }
        for (Map.Entry<BlockPos, OrientedInstance> entry : this.blocks2.entrySet()) {
            this.relight(this.pos.offset(entry.getKey()), entry.getValue());
        }
    }

    @Override
    protected void _delete() {
        this.spring.delete();
        leavesInstance.delete();
        for (OrientedInstance block : this.blocks1.values()) block.delete();
        for (OrientedInstance block : this.blocks2.values()) block.delete();
    }

    private PartialModel getPartialModelForState() {
        return this.blockState.getBlock() instanceof TwinAutocannonBlock cBlock
                ? ExtraDataRegister.twinAutocannonSpringFor(cBlock.getAutocannonMaterial(), vertical)
                : ExtraDataRegister.twinAutocannonSpringFor(CBCAutocannonMaterials.CAST_IRON, vertical);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(spring);
        consumer.accept(leavesInstance);
        //for (OrientedInstance block : this.blocks1.values()) consumer.accept(block);
        //for (OrientedInstance block : this.blocks2.values()) consumer.accept(block);
    }
}
