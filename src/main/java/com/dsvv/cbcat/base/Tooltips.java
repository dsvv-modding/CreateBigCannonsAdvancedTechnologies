package com.dsvv.cbcat.base;

import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlock;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBlock;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipHelper.Palette;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;

import javax.annotation.Nullable;

import java.util.List;

import static rbasamoyai.createbigcannons.base.CBCTooltip.getPalette;

public class Tooltips {
    public static <T extends Block & TwinAutocannonBlock> void appendTextTwinAutocannon(ItemStack stack, @Nullable Level level,
                                                                                        List<Component> tooltip, TooltipFlag flag, T block) {
        boolean desc = Screen.hasShiftDown();
        addHoldShift(desc, tooltip);
        if (!desc) {
            return;
        }

        Palette palette = getPalette(level, stack);
        AutocannonMaterial material = block.getAutocannonMaterial();
        Minecraft mc = Minecraft.getInstance();
        boolean hasGoggles = GogglesItem.isWearingGoggles(mc.player);
        String rootKey = "block." + CreateBigCannons.MOD_ID + ".autocannon.tooltip";
        tooltip.add(Component.literal(I18n.get(rootKey + ".materialProperties")).withStyle(ChatFormatting.GRAY));

        int maxLength = material.properties().maxBarrelLength();
        tooltip.add(Component.literal(" " + I18n.get(rootKey + ".maxBarrelLength")).withStyle(ChatFormatting.GRAY));
        if (hasGoggles) {
            tooltip.addAll(
                    TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".maxBarrelLength.goggles", maxLength + 1),
                            palette.primary(), palette.highlight(), 2));
        } else {
            tooltip.add(getNoGogglesMeter(maxLength == 0 ? 0 : (maxLength - 1) / 2 + 1, false, true));
        }

        tooltip.add(Component.literal(" " + I18n.get(rootKey + ".weightImpact")).withStyle(ChatFormatting.GRAY));
        float weightImpact = material.properties().weight();
        if (hasGoggles) {
            tooltip.addAll(TooltipHelper.cutStringTextComponent(
                    I18n.get(rootKey + ".weightImpact.goggles", String.format("%.2f", weightImpact)), palette.primary(),
                    palette.highlight(), 2));
        } else {
            tooltip.add(getNoGogglesMeter(weightImpact < 1d ? 0 : Mth.ceil(weightImpact), true, true));
        }
    }

    private static void addHoldShift(boolean desc, List<Component> tooltip) {
        String[] holdDesc = Lang.translateDirect("tooltip.holdForDescription", "$").getString().split("\\$");
        if (holdDesc.length < 2) {
            return;
        }
        Component keyShift = Lang.translateDirect("tooltip.keyShift");
        MutableComponent tabBuilder = Component.literal("");
        tabBuilder.append(Component.literal(holdDesc[0]).withStyle(ChatFormatting.DARK_GRAY));
        tabBuilder.append(keyShift.plainCopy().withStyle(desc ? ChatFormatting.WHITE : ChatFormatting.GRAY));
        tabBuilder.append(Component.literal(holdDesc[1]).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(tabBuilder);
    }

    public static <T extends Block & HeavyAutocannonBlock> void appendTextHeavyAutocannon(ItemStack stack, @Nullable Level level,
                                                                                          List<Component> tooltip, TooltipFlag flag, T block) {
        boolean desc = Screen.hasShiftDown();
        addHoldShift(desc, tooltip);
        if (!desc) {
            return;
        }

        Palette palette = getPalette(level, stack);
        AutocannonMaterial material = block.getAutocannonMaterial();
        Minecraft mc = Minecraft.getInstance();
        boolean hasGoggles = GogglesItem.isWearingGoggles(mc.player);
        String rootKey = "block." + CreateBigCannons.MOD_ID + ".autocannon.tooltip";
        tooltip.add(Component.literal(I18n.get(rootKey + ".materialProperties")).withStyle(ChatFormatting.GRAY));

        int maxLength = material.properties().maxBarrelLength();
        tooltip.add(Component.literal(" " + I18n.get(rootKey + ".maxBarrelLength")).withStyle(ChatFormatting.GRAY));
        if (hasGoggles) {
            tooltip.addAll(
                    TooltipHelper.cutStringTextComponent(I18n.get(rootKey + ".maxBarrelLength.goggles", Math.floor(maxLength * 1.5f) + 1),
                            palette.primary(), palette.highlight(), 2));
        } else {
            tooltip.add(getNoGogglesMeter(maxLength == 0 ? 0 : ((int)Math.floor(maxLength * 1.5f) - 1) / 3 + 1, false, true));
        }

        tooltip.add(Component.literal(" " + I18n.get(rootKey + ".weightImpact")).withStyle(ChatFormatting.GRAY));
        float weightImpact = material.properties().weight() * 2;
        if (hasGoggles) {
            tooltip.addAll(TooltipHelper.cutStringTextComponent(
                    I18n.get(rootKey + ".weightImpact.goggles", String.format("%.2f", weightImpact)), palette.primary(),
                    palette.highlight(), 2));
        } else {
            tooltip.add(getNoGogglesMeter(weightImpact < 1d ? 0 : Mth.ceil(weightImpact), true, true));
        }
    }

    private static Component getNoGogglesMeter(int outOfFive, boolean invertColor, boolean canBeInvalid) {
        int value = invertColor ? 5 - outOfFive : outOfFive;
        ChatFormatting color = switch (value) {
            case 0, 1 -> ChatFormatting.RED;
            case 2, 3 -> ChatFormatting.GOLD;
            case 4, 5 -> ChatFormatting.YELLOW;
            default -> canBeInvalid ? ChatFormatting.DARK_GRAY : value < 0 ? ChatFormatting.RED : ChatFormatting.YELLOW;
        };
        return Component.literal(" " + TooltipHelper.makeProgressBar(5, outOfFive)).withStyle(color);
    }
}
