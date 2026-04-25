package com.dsvv.cbcat.cannon.rocketpod.munitions;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFuzedRocketItem extends AbstractRocketItem implements FuzedItemMunition
{
    public AbstractFuzedRocketItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
        ItemStack fuze = items.copyOne();
        if (!fuze.isEmpty()) {
            CreateLang.builder("block")
                    .translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
                    .add(Component.literal(" "))
                    .add(fuze.getDisplayName().copy())
                    .addTo(tooltip);
            if (fuze.getItem() instanceof FuzeItem) {
                List<Component> subTooltip = new ArrayList<>();
                fuze.getItem().appendHoverText(fuze, ctx, subTooltip, flag);
                subTooltip.replaceAll(sibling -> Component.literal("  ").append(sibling).withStyle(ChatFormatting.GRAY));
                tooltip.addAll(subTooltip);
            }
        }
    }

    public ItemStack getFuze(ItemStack stack) {
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
        if (!items.equals(ItemContainerContents.EMPTY))
            return items.copyOne();
        return ItemStack.EMPTY;
    }
}
