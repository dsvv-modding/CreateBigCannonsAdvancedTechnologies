package com.dsvv.cbcat.cannon.heavy_autocannon;

import com.dsvv.cbcat.base.Tooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;

import javax.annotation.Nullable;
import java.util.List;

public class HeavyAutocannonBlockItem <T extends Block & HeavyAutocannonBlock> extends BlockItem
{
    private final T heavyAutocannonBlock;

    public HeavyAutocannonBlockItem(T block, Properties properties) {
        super(block, properties);
        this.heavyAutocannonBlock = block;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);
        Player player = context.getPlayer();
        AutocannonMaterial material = this.heavyAutocannonBlock.getAutocannonMaterial();
        if (player != null && (material.properties().connectsInSurvival() || player.isCreative()))
            HeavyAutocannonBlock.onPlace(context.getLevel(), context.getClickedPos());
        return result;
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, context, pTooltip, pFlag);
        Tooltips.appendTextHeavyAutocannon(pStack, pTooltip, pFlag, this.heavyAutocannonBlock);
    }
}
