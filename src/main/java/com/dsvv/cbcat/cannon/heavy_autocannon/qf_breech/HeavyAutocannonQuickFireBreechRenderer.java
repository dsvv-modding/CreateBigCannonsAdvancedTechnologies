package com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
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

        if (VisualizationManager.supportsVisualization(breech.getLevel())) return;

        Direction facing = blockState.getValue(BlockStateProperties.FACING);
        Direction.Axis axis = CBCClientCommon.getRotationAxis(blockState);
        Direction blockRotation = facing.getCounterClockWise(axis);
        if (blockRotation == Direction.DOWN) blockRotation = Direction.UP;

        Quaternionf qrot;

        boolean alongFirst = blockState.getValue(HeavyAutocannonQuickFireBreechBlock.AXIS);
        if (!alongFirst) {
            Direction rotDir = facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? Direction.WEST : Direction.EAST;
            qrot = Axis.of(rotDir.step()).rotationDegrees(90f);
        } else {
            qrot = Axis.of(blockRotation.step()).rotationDegrees(90f);
        }

        qrot.rotateAxis((float) -Math.PI / 2, new Vector3f(0, 1, 0));

        VertexConsumer vcons = buffer.getBuffer(RenderType.solid());

        ms.pushPose();

        float progress = breech.getOpenProgress(partialTicks);
        float renderedBreechblockOffset = progress / 16.0f * -14.5f;
        Vector3f normal = Direction.UP.step();
        normal.mul(renderedBreechblockOffset);

        CachedBuffers.partialFacing(getPartialModelForState(breech), blockState, blockRotation)
                .translate(normal.x(), normal.y(), normal.z())
                .rotateCentered(qrot)
                .light(light)
                .renderInto(ms, vcons);

        ms.popPose();
    }

    private static PartialModel getPartialModelForState(HeavyAutocannonQuickFireBreechBlockEntity breech) {
        return breech.getBlockState().getBlock() instanceof HeavyAutocannonBlock cBlock
                ? ExtraDataRegister.heavyAutocannonBreechblockFor(cBlock.getAutocannonMaterial())
                : ExtraDataRegister.heavyAutocannonBreechblockFor(CBCAutocannonMaterials.CAST_IRON);
    }
}
