package com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.index.CBCAutocannonMaterials;

public class HeavyAutocannonQuickFireBreechInstance extends BlockEntityInstance<HeavyAutocannonQuickFireBreechBlockEntity> implements DynamicInstance
{
    private OrientedData breechblock;
    private Direction blockRotation;

    public HeavyAutocannonQuickFireBreechInstance(MaterialManager manager, HeavyAutocannonQuickFireBreechBlockEntity blockEntity) {
        super(manager, blockEntity);
    }

    @Override
    public void init() {
        super.init();
        Direction.Axis axis = getRotationAxis(this.blockState);
        Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
        Direction blockRotation = facing.getCounterClockWise(axis);
        if (blockRotation == Direction.DOWN)
            blockRotation = Direction.UP;
        this.blockRotation = blockRotation;

        this.breechblock = this.materialManager.defaultCutout().material(Materials.ORIENTED).getModel(this.getPartialModelForState(this.blockState)).createInstance();

        boolean alongFirst = this.blockState.getValue(HeavyAutocannonQuickFireBreechBlock.AXIS);
        if (alongFirst) {
            this.breechblock.setRotation(Axis.of(new Vector3f(0, 1, 0)).rotationDegrees(90));
        }
        if (facing.getAxis().isHorizontal()) {
            Quaternionf q = Axis.of(new Vector3f(0, 1, 0)).rotationDegrees(90);
            q.rotateAxis(Mth.HALF_PI, new Vector3f(1, 0, 0));
            this.breechblock.setRotation(q);
        }
        this.transformModels();
    }

    private void transformModels() {
        float progress = this.blockEntity.getOpenProgress(AnimationTickHolder.getPartialTicks());
        BlockPos visualPos = this.getInstancePosition();

        float renderedBreechblockOffset = progress / 16.0f * 13.0f;
        Vector3f normal = this.blockRotation.getClockWise(Direction.Axis.X).step();
        normal.mul(renderedBreechblockOffset);
        this.breechblock.setPosition(visualPos).nudge(normal.x(), normal.y(), normal.z());
    }

    private PartialModel getPartialModelForState(BlockState state) {
        return state.getBlock() instanceof HeavyAutocannonBlock cBlock
                ? ExtraDataRegister.heavyAutocannonBreechblockFor(cBlock.getAutocannonMaterial())
                : ExtraDataRegister.heavyAutocannonBreechblockFor(CBCAutocannonMaterials.CAST_IRON);
    }

    @Override
    public void updateLight() {
        this.relight(this.pos, this.breechblock);
    }

    @Override
    protected void remove() {
        this.breechblock.delete();
    }

    @Override
    public void beginFrame() {
        this.transformModels();
    }

    private static Direction.Axis getRotationAxis(BlockState state) {
        boolean flag = state.getValue(HeavyAutocannonQuickFireBreechBlock.AXIS);
        return switch (state.getValue(HeavyAutocannonQuickFireBreechBlock.FACING).getAxis()) {
            case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
            case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
            case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
        };
    }
}