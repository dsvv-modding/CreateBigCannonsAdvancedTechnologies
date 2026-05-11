package com.dsvv.cbcat.cartridge;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;

import javax.annotation.Nullable;
import java.util.List;

import static rbasamoyai.createbigcannons.base.CBCTooltip.getPalette;

public interface IProjectileCartridgeBlockItem {
    static int getMaximumPowerLevels(ItemStack stack) {
        if (stack.getItem() instanceof IProjectileCartridgeBlockItem cartridgeBlockItem)
            if (cartridgeBlockItem.getBlock() instanceof ProjectileCartridge block)
                return block.getMaximumPowerLevels();
        return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(CBCBlocks.BIG_CARTRIDGE.get()).maxPowerLevels();
    }

    static int getPower(ItemStack stack) {
        if (stack.getItem() instanceof IProjectileCartridgeBlockItem cartridgeBlockItem)
            if (cartridgeBlockItem.getBlock() instanceof ProjectileCartridge block)
                return block.allowsMultipleCharges() ? 1 : 3;
        return stack.getOrCreateTag().getInt("Power");
    }

    static ItemStack getWithPower(int power, BlockEntry<? extends IProjectileCartridgeBlock> blockEntry) {
        ItemStack stack = blockEntry.asStack();
        stack.getOrCreateTag().putInt("Power", power);
        return stack;
    }

    static void appendCartridgePropellantPowerText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!Screen.hasShiftDown()) return;
        FontHelper.Palette palette = getPalette(level, stack);
        String key = "block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.power";
        tooltip.add(Component.literal(I18n.get(key)).withStyle(ChatFormatting.GRAY));
        int min = getPower(stack);
        int max = getMaximumPowerLevels(stack);
        tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", min, max), palette.primary(), palette.highlight(), 1));
    }

    Block getBlock();
}
