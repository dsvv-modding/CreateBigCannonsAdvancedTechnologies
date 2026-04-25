package com.dsvv.cbcat.cannon.heavy_autocannon.breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

import java.util.function.Consumer;


public class HeavyAutocannonBreechInstance extends AbstractBlockEntityVisual<HeavyAutocannonBreechBlockEntity> implements SimpleDynamicVisual
{
    private OrientedInstance ammoContainer;

    private Direction facing;
    private boolean isFilled = false;
    private Item magazineItem = null;

    public HeavyAutocannonBreechInstance(VisualizationContext ctx, HeavyAutocannonBreechBlockEntity blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);
        Quaternionf q = Axis.YP.rotationDegrees(this.facing.getAxis().isVertical() ? 180 : 0);

        this.ammoContainer = this.instancerProvider().instancer(InstanceTypes.ORIENTED, Models.block(this.getAmmoContainerModel())).createInstance();
        boolean flag = this.facing.getAxis().isVertical();
        Quaternionf q1;
        if (flag) {
            float f = this.facing == Direction.UP ? 90 : -90;
            q1 = Axis.ZP.rotationDegrees(f);
            q1.mul(Axis.XP.rotationDegrees(f));
        } else {
            q1 = Axis.YP.rotationDegrees(-90 - this.facing.toYRot());
            q1.mul(Axis.XP.rotationDegrees(-90));
        }
        Direction offset = flag
                ? this.facing.getCounterClockWise(Direction.Axis.Z)
                : this.facing.getClockWise(Direction.Axis.Y);
        Vector3f normal = this.facing == Direction.UP ? offset.getOpposite().step() : offset.step();
        normal.mul(10 / 16f);
        this.ammoContainer.rotation(q1).position(this.getVisualPosition()).translatePosition(normal.x(), normal.y(), normal.z());
        this.isFilled = this.isFilled();
        this.magazineItem = this.getMagazineItem();
        this.updateTransforms(partialTick);
    }

    private void updateTransforms(float partialTick) {
        float offset = this.blockEntity.getAnimateOffset(partialTick) * 0.5f;
        Vector3f normal = this.facing.getOpposite().step();
        normal.mul(offset);

        ItemStack container = this.blockEntity.getMagazine();
        this.ammoContainer.color((byte) 255, (byte) 255, (byte) 255, (byte)(container.getItem() instanceof HeavyAutocannonAmmoContainerItem ? 255 : 0));
        if (this.isFilled != this.isFilled() || this.magazineItem != this.getMagazineItem()) {
            this.updateLight(partialTick);
        }
    }

    @Override
    public void updateLight(float partialTick) {
        this.relight(this.pos, this.ammoContainer);
    }

    @Override
    protected void _delete() {
        this.ammoContainer.delete();
    }

    private BlockState getAmmoContainerModel() {
        ItemStack item = this.blockEntity.getMagazine();
        if (item == null || item.isEmpty() || !(item.getItem() instanceof HeavyAutocannonAmmoContainerItem blockItem))
            return Blocks.AIR.defaultBlockState();
        BlockState state = blockItem.getBlock().defaultBlockState();
        if (state.hasProperty(HeavyAutocannonAmmoContainerBlock.CONTAINER_STATE)) {
            state = state.setValue(HeavyAutocannonAmmoContainerBlock.CONTAINER_STATE,
                    HeavyAutocannonAmmoContainerBlock.State.getFromFilled(HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(item) > 0));
        }
        return state;
    }

    private boolean isFilled() { return HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(this.blockEntity.getMagazine()) > 0; }

    private Item getMagazineItem() {
        ItemStack stack = this.blockEntity.getMagazine();
        return stack == null || stack.isEmpty() ? null : stack.getItem();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(ammoContainer);
    }

    @Override
    public void beginFrame(DynamicVisual.Context context) {
        updateTransforms(context.partialTick());
    }
}
