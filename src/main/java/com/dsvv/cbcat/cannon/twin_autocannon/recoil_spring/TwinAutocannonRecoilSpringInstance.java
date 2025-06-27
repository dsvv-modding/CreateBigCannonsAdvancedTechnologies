package com.dsvv.cbcat.cannon.twin_autocannon.recoil_spring;

import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBlock;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.index.CBCAutocannonMaterials;

import java.util.HashMap;
import java.util.Map;

public class TwinAutocannonRecoilSpringInstance extends BlockEntityInstance<TwinAutocannonRecoilSpringBlockEntity> implements DynamicInstance
{
    private ModelData spring;
    private final Map<BlockPos, OrientedData> blocks1 = new HashMap<>();
    private final Map<BlockPos, OrientedData> blocks2 = new HashMap<>();

    private Direction facing;

    private boolean vertical;
    private boolean firstFire = true;

    public TwinAutocannonRecoilSpringInstance(MaterialManager manager, TwinAutocannonRecoilSpringBlockEntity blockEntity) {
        super(manager, blockEntity);
    }

    @Override
    public void init() {
        super.init();
        vertical = blockEntity.isVertical();

        this.facing = this.blockState.getValue(BlockStateProperties.FACING);

        this.spring = this.materialManager.defaultSolid().material(Materials.TRANSFORMED).getModel(this.getPartialModelForState(), this.blockState, this.facing).createInstance();

        this.blocks1.clear();
        this.blocks2.clear();
        for (Map.Entry<BlockPos, BlockState> entry : this.blockEntity.toAnimate.entrySet()) {
            if (entry.getValue() == null) continue;
            this.blocks1.put(entry.getKey(), this.materialManager.defaultCutout().material(Materials.ORIENTED).getModel(entry.getValue()).createInstance());
            this.blocks2.put(entry.getKey(), this.materialManager.defaultCutout().material(Materials.ORIENTED).getModel(entry.getValue()).createInstance());
        }

        this.updateTransforms();
    }

    @Override public void beginFrame() { this.updateTransforms(); }

    private void updateTransforms() {
        firstFire = blockEntity.firstFire;
        boolean flag = this.facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
        BlockPos pos = this.instancePos.relative(this.facing.getOpposite(), flag ? 1 : 0);
        Vec3 pivot = Vec3.atLowerCornerOf(pos);
        Map.Entry<Float, Boolean> beValues = this.blockEntity.getAnimateOffset(AnimationTickHolder.getPartialTicks());
        float scale = beValues.getKey();
        firstFire = beValues.getValue();
        float f1 = scale * 0.5f + 0.5f;
        Direction.Axis axis = this.facing.getAxis();

        float fx = axis == Direction.Axis.X ? f1 : 1;
        float fy = axis == Direction.Axis.Y ? f1 : 1;
        float fz = axis == Direction.Axis.Z ? f1 : 1;

        this.spring.loadIdentity().translate(pivot);
        if (flag) {
            this.spring.rotateCentered(axis.isVertical() ? Direction.EAST : Direction.UP, Mth.PI)
                    .translate(this.facing.getOpposite().step());
        }
        this.spring.scale(fx, fy, fz);

        Vector3f offs = this.facing.step();
        //offs.mul((1 - scale) * -0.5f);
        offs.add(this.instancePos.getX(), this.instancePos.getY(), this.instancePos.getZ());
        offs.sub(facing.step());
        offs.sub(facing.step());

        Vector3f offsFiring = this.facing.step();
        offsFiring.mul((1 - scale) * -0.5f);
        offsFiring.add(this.instancePos.getX(), this.instancePos.getY(), this.instancePos.getZ());
        offsFiring.sub(facing.step());

        Vector3f offs2 = vertical ? new Vector3f(0, 0.25f, 0) : new Vector3f((facing.getAxis() == Direction.Axis.Z ? -0.25f : 0), 0, (facing.getAxis() == Direction.Axis.X ? -0.25f : 0));

        for (Map.Entry<BlockPos, OrientedData> entry : this.blocks1.entrySet()) {
            BlockPos pos1 = entry.getKey().relative(facing);
            entry.getValue().setPosition(pos1);
            if(entry != blocks1.entrySet().toArray()[0])
                entry.getValue().nudge(offs2.x, offs2.y, offs2.z);
            if(firstFire)
                entry.getValue().nudge(offsFiring.x, offsFiring.y, offsFiring.z);
            else
                entry.getValue().nudge(offs.x, offs.y, offs.z);
        }
        for (Map.Entry<BlockPos, OrientedData> entry : this.blocks2.entrySet()) {
            BlockPos pos1 = entry.getKey().relative(facing);
            entry.getValue().setPosition(pos1);
            if(entry != blocks2.entrySet().toArray()[0])
                entry.getValue().nudge(-offs2.x, -offs2.y, -offs2.z);
            if(!firstFire)
                entry.getValue().nudge(offsFiring.x, offsFiring.y, offsFiring.z);
            else
                entry.getValue().nudge(offs.x, offs.y, offs.z);
        }
    }

    @Override
    public void updateLight() {
        this.relight(this.pos, this.spring);
        for (Map.Entry<BlockPos, OrientedData> entry : this.blocks1.entrySet()) {
            this.relight(this.pos.offset(entry.getKey()), entry.getValue());
        }
        for (Map.Entry<BlockPos, OrientedData> entry : this.blocks2.entrySet()) {
            this.relight(this.pos.offset(entry.getKey()), entry.getValue());
        }
    }

    @Override
    protected void remove() {
        this.spring.delete();
        for (OrientedData block : this.blocks1.values()) block.delete();
        for (OrientedData block : this.blocks2.values()) block.delete();
    }

    private PartialModel getPartialModelForState() {
        return this.blockState.getBlock() instanceof TwinAutocannonBlock cBlock
                ? ExtraDataRegister.twinAutocannonSpringFor(cBlock.getAutocannonMaterial(), vertical)
                : ExtraDataRegister.twinAutocannonSpringFor(CBCAutocannonMaterials.CAST_IRON, vertical);
    }
}
