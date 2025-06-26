package com.dsvv.cbcat.cannon.twin_autocannon.breech;

import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBreechBlockEntity;
import com.mojang.math.Axis;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlock;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;

import java.util.function.Consumer;

public class TwinAutocannonBreechInstance extends AbstractBlockEntityVisual<TwinAutocannonBreechBlockEntity> implements SimpleDynamicVisual
{
    //private OrientedData ejector;
    private OrientedInstance seat;
    private OrientedInstance ammoContainer;
    private DyeColor seatColor;
    //private final OrientedData shell;

    private Direction facing;
    private boolean isFilled = false;
    private Item magazineItem = null;

    public TwinAutocannonBreechInstance(VisualizationContext ctx, TwinAutocannonBreechBlockEntity blockEntity, float partialTicks) {
        super(ctx, blockEntity, partialTicks);

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.seatColor = this.blockEntity.getSeatColor();

        this.seat = this.instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCBlockPartials.autocannonSeatFor(this.seatColor))).createInstance();
        this.ammoContainer = this.instancerProvider().instancer(InstanceTypes.ORIENTED, Models.block(getAmmoContainerModel())).createInstance();

        boolean flag = this.facing.getAxis().isVertical();
        Quaternionf q1;
        if (flag) {
            float f = this.facing == Direction.UP ? 90 : -90;
            q1 = Axis.ZP.rotationDegrees(f);
            q1.mul(Axis.XP.rotationDegrees(f));
        } else {
            q1 = Axis.YP.rotationDegrees(-90 - this.facing.toYRot());
        }
        Direction offset = flag
                ? this.facing.getCounterClockWise(Direction.Axis.Z)
                : this.facing.getClockWise(Direction.Axis.Y);
        Vector3f normal = this.facing == Direction.UP ? offset.getOpposite().step() : offset.step();
        normal.mul(10 / 16f);
        this.ammoContainer.rotation(q1).position(this.getVisualPosition()).translatePosition(normal.x(), normal.y(), normal.z());
        this.isFilled = this.isFilled();
        this.magazineItem = this.getMagazineItem();

        this.seat.rotation(q1.rotateY((float) Math.PI / 2f)).position(this.getVisualPosition());

        this.updateTransforms(partialTicks);
    }

    @Override public void beginFrame(Context ctx) { this.updateTransforms(ctx.partialTick()); }

    private void updateTransforms(float partialTicks) {
        if (this.blockState.getValue(TwinAutocannonBreechBlock.HANDLE))
            this.seat.setVisible(this.seatColor != null);
        else
            this.seat.setVisible(false);

        if (this.seatColor != this.blockEntity.getSeatColor())
            this.seatColor = this.blockEntity.getSeatColor();

        ItemStack container = this.blockEntity.getMagazine();
        this.ammoContainer.setVisible(container.getItem() instanceof AutocannonAmmoContainerItem);
        if (this.isFilled != this.isFilled() || this.magazineItem != this.getMagazineItem() || this.seatColor != this.blockEntity.getSeatColor()) {
            //this._delete();
            //this.init();
            this.updateLight(partialTicks);
        }

        seat.setChanged();
        ammoContainer.setChanged();
    }

    @Override
    public void updateLight(float partialTicks) {
        this.relight(this.pos, this.seat);
        this.relight(this.pos, this.ammoContainer);
    }

    @Override
    protected void _delete() {
        this.seat.delete();
        this.ammoContainer.delete();
    }

    private BlockState getAmmoContainerModel() {
        ItemStack item = this.blockEntity.getMagazine();
        if (item == null || item.isEmpty() || !(item.getItem() instanceof AutocannonAmmoContainerItem blockItem))
            return Blocks.AIR.defaultBlockState();
        BlockState state = blockItem.getBlock().defaultBlockState();
        if (state.hasProperty(AutocannonAmmoContainerBlock.CONTAINER_STATE)) {
            state = state.setValue(AutocannonAmmoContainerBlock.CONTAINER_STATE,
                    AutocannonAmmoContainerBlock.State.getFromFilled(AutocannonAmmoContainerItem.getTotalAmmoCount(item) > 0));
        }
        return state;
    }

    private boolean isFilled() { return AutocannonAmmoContainerItem.getTotalAmmoCount(this.blockEntity.getMagazine()) > 0; }

    private Item getMagazineItem() {
        ItemStack stack = this.blockEntity.getMagazine();
        return stack == null || stack.isEmpty() ? null : stack.getItem();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(ammoContainer);
        consumer.accept(seat);
    }
}
