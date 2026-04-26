package com.dsvv.cbcat.cartridge;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;

import java.util.List;

import static rbasamoyai.createbigcannons.base.CBCTooltip.getPalette;

public interface IProjectileCartridgeBlockItem {
    static int getMaximumPowerLevels() {
        return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(CBCBlocks.BIG_CARTRIDGE.get()).maxPowerLevels() - 1;
    }

    static int getPower(ItemStack stack) {
        return stack.has(CBCDataComponents.POWER) ? stack.get(CBCDataComponents.POWER) : 0;
    }

    static ItemStack getWithPower(int power, BlockEntry<? extends IProjectileCartridgeBlock> block)
    {
        ItemStack stack = block.asStack();
        stack.set(CBCDataComponents.POWER, power);
        return stack;
    }

    static void appendCartridgePropellantPowerText(ItemStack stack, Item.TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        if (!Screen.hasShiftDown()) return;
        FontHelper.Palette palette = getPalette();
        String key = "block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.power";
        tooltip.add(Component.literal(I18n.get(key)).withStyle(ChatFormatting.GRAY));
        int min = getPower(stack);
        int max = getMaximumPowerLevels();
        tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", min, max), palette.primary(), palette.highlight(), 1));
    }
}
