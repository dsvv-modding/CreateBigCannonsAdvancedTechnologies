package com.dsvv.cbcat.cannon.medium_rocketpod;

import com.dsvv.cbcat.base.Tooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;

import javax.annotation.Nullable;
import java.util.List;

public class MediumRocketPodBlockItem<T extends Block & MediumRocketPodBlock> extends BlockItem {
    private final T rocketPodBlock;

    public MediumRocketPodBlockItem(T block, Properties properties) {
        super(block, properties);
        this.rocketPodBlock = block;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        Player player = context.getPlayer();
        AutocannonMaterial material = this.rocketPodBlock.getAutocannonMaterial();
        if (player != null && (material.properties().connectsInSurvival() || player.isCreative()))
            MediumRocketPodBlock.onPlace(context.getLevel(), context.getClickedPos());
        return result;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level level, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, level, pTooltip, pFlag);
        Tooltips.appendTextMediumRocketPod(pStack, level, pTooltip, pFlag, this.rocketPodBlock);
    }
}
