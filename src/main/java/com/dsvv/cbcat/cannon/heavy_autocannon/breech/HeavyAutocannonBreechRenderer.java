package com.dsvv.cbcat.cannon.heavy_autocannon.breech;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import rbasamoyai.createbigcannons.index.CBCBlockPartials;

public class HeavyAutocannonBreechRenderer extends SmartBlockEntityRenderer<HeavyAutocannonBreechBlockEntity>
{
    public HeavyAutocannonBreechRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(HeavyAutocannonBreechBlockEntity breech, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(breech, partialTicks, ms, buffer, light, overlay);
        if (VisualizationManager.supportsVisualization(breech.getLevel())) return;

        BlockState state = breech.getBlockState();
        Direction facing = state.getValue(HeavyAutocannonBreechBlock.FACING);

        ms.pushPose();

        Vector3f normal = facing.step();
        normal.mul(breech.getAnimateOffset(partialTicks) * -0.5f);

        ItemStack container = breech.getMagazine();
        if (container.getItem() instanceof HeavyAutocannonAmmoContainerItem) {
            boolean flag = facing.getAxis().isVertical();
            Quaternionf q1;
            /*if (flag) {
                float f = facing == Direction.UP ? 90 : -90;
                q1 = Axis.ZP.rotationDegrees(f);
                q1.mul(Axis.XP.rotationDegrees(f));
            } else {
                q1 = Axis.YP.rotationDegrees(-90 - facing.toYRot());
                q1.mul(facing.getAxis() == Direction.Axis.X ? Axis.XP.rotationDegrees(90) : Axis.ZP.rotationDegrees(90));
            }*/

            if (flag) {
                float f = facing == Direction.UP ? 90 : -90;
                q1 = Axis.ZP.rotationDegrees(f);
                q1.mul(Axis.XP.rotationDegrees(f));
            } else {
                q1 = Axis.YP.rotationDegrees(-90 - facing.toYRot());
                q1.mul(Axis.XP.rotationDegrees(-90));
            }

            Direction offset = flag
                    ? facing.getCounterClockWise(Direction.Axis.Z)
                    : facing.getClockWise(Direction.Axis.Y);
            normal = facing == Direction.UP ? offset.getOpposite().step() : offset.step();
            normal.mul(10 / 16f);

            CachedBuffers.block(getAmmoContainerModel(container))
                    .translate(normal)
                    .rotateCentered(q1)
                    .light(light)
                    .renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        }

        ms.popPose();
    }

    private static BlockState getAmmoContainerModel(ItemStack stack) {
        BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
        if (state.hasProperty(HeavyAutocannonAmmoContainerBlock.CONTAINER_STATE)) {
            state = state.setValue(HeavyAutocannonAmmoContainerBlock.CONTAINER_STATE,
                    HeavyAutocannonAmmoContainerBlock.State.getFromFilled(HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(stack) > 0));
        }
        return state;
    }
}
