package com.dsvv.cbcat.cannon.heavy_autocannon.breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;


public class HeavyAutocannonBreechInstance extends BlockEntityInstance<HeavyAutocannonBreechBlockEntity> implements DynamicInstance
{
    private OrientedData ammoContainer;

    private Direction facing;
    private boolean isFilled = false;
    private Item magazineItem = null;

    public HeavyAutocannonBreechInstance(MaterialManager manager, HeavyAutocannonBreechBlockEntity blockEntity) {
        super(manager, blockEntity);
    }

    @Override
    public void init() {
        super.init();
        this.facing = this.blockState.getValue(BlockStateProperties.FACING);
        Quaternionf q = Axis.YP.rotationDegrees(this.facing.getAxis().isVertical() ? 180 : 0);

        this.ammoContainer = this.materialManager.defaultCutout().material(Materials.ORIENTED).getModel(this.getAmmoContainerModel()).createInstance();
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
        this.ammoContainer.setRotation(q1).setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z());
        this.isFilled = this.isFilled();
        this.magazineItem = this.getMagazineItem();
        this.updateTransforms();
    }

    private void updateTransforms() {
        float offset = this.blockEntity.getAnimateOffset(AnimationTickHolder.getPartialTicks()) * 0.5f;
        Vector3f normal = this.facing.getOpposite().step();
        normal.mul(offset);
        //this.ejector.setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z()).setColor((byte) 255, (byte) 255, (byte) 255, (byte) 255);

        ItemStack container = this.blockEntity.getMagazine();
        this.ammoContainer.setColor((byte) 255, (byte) 255, (byte) 255, (byte)(container.getItem() instanceof HeavyAutocannonAmmoContainerItem ? 255 : 0));
        if (this.isFilled != this.isFilled() || this.magazineItem != this.getMagazineItem()) {
            //this.remove();
            //this.init();
            this.updateLight();
        }
    }

    @Override
    public void updateLight() {
        this.relight(this.pos, this.ammoContainer);
    }

    @Override
    protected void remove() {
        this.ammoContainer.delete();
    }

    private PartialModel getPartialModelForState() {
        return this.blockState.getBlock() instanceof HeavyAutocannonBlock cBlock
                ? CBCBlockPartials.autocannonEjectorFor(cBlock.getAutocannonMaterial())
                : CBCBlockPartials.CAST_IRON_AUTOCANNON_EJECTOR;
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
    public void beginFrame() {
        this.updateTransforms();
    }
}
