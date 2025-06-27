package com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.index.CBCAutocannonMaterials;

public class HeavyAutocannonQuickFireBreechRenderer extends SmartBlockEntityRenderer<HeavyAutocannonQuickFireBreechBlockEntity>
{
    public HeavyAutocannonQuickFireBreechRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(HeavyAutocannonQuickFireBreechBlockEntity breech, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = breech.getBlockState();

        if (Backend.canUseInstancing(breech.getLevel())) return;

        Direction facing = blockState.getValue(BlockStateProperties.FACING);
        Direction.Axis axis = CBCClientCommon.getRotationAxis(blockState);
        Direction blockRotation = facing.getCounterClockWise(axis);
        if (blockRotation == Direction.DOWN) blockRotation = Direction.UP;

        Quaternionf qrot;

        boolean alongFirst = blockState.getValue(HeavyAutocannonQuickFireBreechBlock.AXIS);
        if (facing.getAxis().isHorizontal() && !alongFirst) {
            Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
            qrot = Axis.of(rotDir.step()).rotationDegrees(90f);
        } else if (facing.getAxis() == Direction.Axis.X && alongFirst) {
            qrot = Axis.of(blockRotation.step()).rotationDegrees(90f);
        } else {
            qrot = Axis.of(blockRotation.step()).rotationDegrees(0);
        }

        VertexConsumer vcons = buffer.getBuffer(RenderType.solid());

        ms.pushPose();

        float progress = breech.getOpenProgress(partialTicks);
        float renderedBreechblockOffset = progress / 16.0f * 13.0f;
        Vector3f normal = blockRotation.step();
        normal.mul(renderedBreechblockOffset);

        CachedBufferer.partialFacing(getPartialModelForState(breech), blockState, blockRotation)
                .translate(normal.x(), normal.y(), normal.z())
                .rotateCentered(qrot)
                .light(light)
                .renderInto(ms, vcons);

        ms.popPose();
        ms.pushPose();

        float angle = progress * 90;
        Direction dir = facing.getCounterClockWise(blockRotation.getAxis());
        Vector3f normal1 = dir.step();
        Axis axis1 = Axis.of(normal1);

        CachedBufferer.block(AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, axis))
                .rotateCentered(axis1.rotationDegrees(angle))
                .light(light)
                .renderInto(ms, vcons);

        ms.popPose();
        ms.pushPose();

        /*CachedBuffers.partialFacing(CBCBlockPartials.QUICKFIRING_BREECH_LEVER, blockState, dir)
                .rotateCentered(axis1.rotationDegrees(angle))
                .translate(normal1)
                .light(light)
                .renderInto(ms, vcons);*/

        ms.popPose();
    }

    private static PartialModel getPartialModelForState(HeavyAutocannonQuickFireBreechBlockEntity breech) {
        return breech.getBlockState().getBlock() instanceof HeavyAutocannonBlock cBlock
                ? ExtraDataRegister.heavyAutocannonBreechblockFor(cBlock.getAutocannonMaterial())
                : ExtraDataRegister.heavyAutocannonBreechblockFor(CBCAutocannonMaterials.CAST_IRON);
    }
}
