package com.dsvv.cbcat.cannon.medium_rocketpod.breech;

import com.dsvv.cbcat.registry.BlockRegister;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionfc;

public class MediumRocketPodBreechRenderer extends SmartBlockEntityRenderer<MediumRocketPodBreechBlockEntity>
{
    public MediumRocketPodBreechRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(MediumRocketPodBreechBlockEntity breech, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = breech.getBlockState();
        if (VisualizationManager.supportsVisualization(breech.getLevel())) return;

        /*Direction facing = blockState.getValue(BlockStateProperties.FACING);
        Direction.Axis axis = CBCClientCommon.getRotationAxis(blockState);
        Direction blockRotation = facing.getCounterClockWise(axis);
        if (blockRotation == Direction.DOWN) blockRotation = Direction.UP;

        Quaternionf qrot;

        boolean alongFirst = blockState.getValue(MediumRocketPodBreechBlock.FACING).getAxis() == Direction.Axis.X;
        if (facing.getAxis().isHorizontal() && !alongFirst) {
            Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
            qrot = Axis.of(rotDir.step()).rotationDegrees(90f);
        } else if (facing.getAxis() == Direction.Axis.X && alongFirst) {
            qrot = Axis.of(blockRotation.step()).rotationDegrees(90f);
        } else {
            qrot = Axis.of(blockRotation.step()).rotationDegrees(0);
        }

        VertexConsumer vcons = buffer.getBuffer(RenderType.solid());

        ms.pushPose();*/

        //float progress = breech.getOpenProgress(partialTicks);
        //float renderedBreechblockOffset = progress / 16.0f * 13.0f;
        //Vector3f normal = blockRotation.step();
        //normal.mul(renderedBreechblockOffset);

        /*CachedBuffers.partialFacing(getPartialModelForState(breech), blockState, blockRotation)
                .translate(normal.x(), normal.y(), normal.z())
                .rotateCentered(qrot)
                .light(light)
                .renderInto(ms, vcons);

        ms.popPose();
        ms.pushPose();

        //float angle = progress * 90;
        Direction dir = facing.getCounterClockWise(blockRotation.getAxis());
        Vector3f normal1 = dir.step();
        Axis axis1 = Axis.of(normal1);

        CachedBuffers.block(AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, axis))
                //.rotateCentered(axis1.rotationDegrees(angle))
                .light(light)
                .renderInto(ms, vcons);

        ms.popPose();
        ms.pushPose();

        /*CachedBuffers.partialFacing(CBCBlockPartials.QUICKFIRING_BREECH_LEVER, blockState, dir)
                .rotateCentered(axis1.rotationDegrees(angle))
                .translate(normal1)
                .light(light)
                .renderInto(ms, vcons);*/

        //ms.popPose();
        for (int i = 0; i < breech.getQueueLimit(); i++)
        {
            if (breech.isSlotUsed(i))
            {
                Quaternionfc quaternion;
                quaternion = Axis.ZP.rotationDegrees(45);
                CachedBuffers.block(getPartialModelForState(breech))
                    .translate(0, i > 1 ? -0.315f : 0.185f, (i % 2) == 0 ? -0.3175f : 0.3175f)
                    .rotateCentered(quaternion)
                    .light(light)
                    .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
            }
        }
    }

    private static BlockState getPartialModelForState(MediumRocketPodBreechBlockEntity breech) {
        return BlockRegister.CLUSTER_CASELESS_BLOCK.getDefaultState();
    }
}
