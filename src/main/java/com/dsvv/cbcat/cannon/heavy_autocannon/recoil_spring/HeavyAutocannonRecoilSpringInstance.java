package com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class HeavyAutocannonRecoilSpringInstance extends BlockEntityInstance<HeavyAutocannonRecoilSpringBlockEntity> implements DynamicInstance
{
    private final Map<BlockPos, OrientedData> blocks = new HashMap<>();

    private Direction facing;

    public HeavyAutocannonRecoilSpringInstance(MaterialManager manager, HeavyAutocannonRecoilSpringBlockEntity blockEntity) {
        super(manager, blockEntity);
    }

    @Override
    public void init() {
        super.init();
        this.facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.blocks.clear();
        for (Map.Entry<BlockPos, BlockState> entry : this.blockEntity.toAnimate.entrySet()) {
            if (entry.getValue() == null) continue;
            this.blocks.put(entry.getKey(), this.materialManager.defaultCutout().material(Materials.ORIENTED).getModel(entry.getValue()).createInstance());
        }

        this.updateTransforms();
    }

    @Override public void beginFrame() { this.updateTransforms(); }

    private void updateTransforms() {
        float scale = this.blockEntity.getAnimateOffset(AnimationTickHolder.getPartialTicks());

        Vector3f offs = this.facing.step();
        offs.mul((1 - scale) * -0.875f);
        offs.add(this.instancePos.getX(), this.instancePos.getY(), this.instancePos.getZ());

        for (Map.Entry<BlockPos, OrientedData> entry : this.blocks.entrySet()) {
            BlockPos pos1 = entry.getKey();
            entry.getValue().setPosition(offs).nudge(pos1.getX(), pos1.getY(), pos1.getZ());
        }
    }

    @Override
    public void updateLight() {
        super.updateLight();
        for (Map.Entry<BlockPos, OrientedData> entry : this.blocks.entrySet()) {
            this.relight(this.pos.offset(entry.getKey()), entry.getValue());
        }
    }

    @Override
    protected void remove() {
        for (OrientedData block : this.blocks.values()) block.delete();
    }
}
