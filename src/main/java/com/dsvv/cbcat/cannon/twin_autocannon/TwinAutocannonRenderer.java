package com.dsvv.cbcat.cannon.twin_autocannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class TwinAutocannonRenderer  extends SmartBlockEntityRenderer<TwinAutocannonBlockEntity> {
    public TwinAutocannonRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(TwinAutocannonBlockEntity barrel, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(barrel, partialTicks, ms, buffer, light, overlay);
        if (VisualizationManager.supportsVisualization(barrel.getLevel())) return;

        if (!barrel.getLeavesItemStack().isEmpty() && barrel.getLeavesItemStack().getItem() instanceof BlockItem leavesBlockItem) {
            BlockState state = barrel.getBlockState();
            Direction facing = state.getValue(BlockStateProperties.FACING);
            Direction.Axis axis = facing.getAxis();

            BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();

            ms.pushPose();
            float x = axis == Direction.Axis.X ? -0.005f : 0;
            float y = axis == Direction.Axis.Y ? -0.005f : 0;
            float z = axis == Direction.Axis.Z ? -0.005f : 0;
            Vec3 leavesOffset = new Vec3(x, y, z).scale(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? -1 : 1);
            ms.translate(leavesOffset.x(), leavesOffset.y(), leavesOffset.z());
            brd.renderSingleBlock(leavesBlockItem.getBlock().defaultBlockState(), ms, buffer, light, OverlayTexture.NO_OVERLAY);
            ms.popPose();
        }
    }
}
