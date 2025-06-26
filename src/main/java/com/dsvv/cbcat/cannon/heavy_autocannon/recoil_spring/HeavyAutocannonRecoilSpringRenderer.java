package com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.registry.ExtraDataRegister;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Vector3f;

import java.util.Map;

public class HeavyAutocannonRecoilSpringRenderer extends SmartBlockEntityRenderer<HeavyAutocannonRecoilSpringBlockEntity>
{
    public HeavyAutocannonRecoilSpringRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(HeavyAutocannonRecoilSpringBlockEntity spring, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(spring, partialTicks, ms, buffer, light, overlay);
        if (VisualizationManager.supportsVisualization(spring.getLevel())) return;

        BlockState state = spring.getBlockState();
        Direction facing = state.getValue(BlockStateProperties.FACING);
        SuperByteBuffer ejectorBuf = CachedBuffers.partialFacing(getPartialModelForState(state), state, facing);

        Direction.Axis axis = facing.getAxis();

        float scale = spring.getAnimateOffset(partialTicks);
        float f1 = scale * 0.5f + 0.5f;

        float fx = axis == Direction.Axis.X ? f1 : 1;
        float fy = axis == Direction.Axis.Y ? f1 : 1;
        float fz = axis == Direction.Axis.Z ? f1 : 1;

        ms.pushPose();

        if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
            ejectorBuf.rotateCentered(Mth.PI, axis.isVertical() ? Direction.EAST : Direction.UP);
            //.translate(facing.getOpposite().step());
        }
        ejectorBuf.scale(fx, fy, fz)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));

        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        Vector3f normal = facing.step();
        normal.mul((1 - scale) * -0.5f);
        for (Map.Entry<BlockPos, BlockState> entry : spring.toAnimate.entrySet()) {
            if (entry.getValue() == null) continue;
            ms.pushPose();
            BlockPos pos = entry.getKey();
            ms.translate(pos.getX() + normal.x(), pos.getY() + normal.y(), pos.getZ() + normal.z());
            brd.renderSingleBlock(entry.getValue(), ms, buffer, light, OverlayTexture.NO_OVERLAY);
            ms.popPose();
        }

        ms.popPose();
    }

    private static PartialModel getPartialModelForState(BlockState state) {
        return state.getBlock() instanceof HeavyAutocannonBlock cBlock
                ? ExtraDataRegister.heavyAutocannonSpringFor(cBlock.getAutocannonMaterial())
                : null;//ExtraDataRegister.twinAutocannonSpringFor(CBCAutocannonMaterials.CAST_IRON, false);
    }
}