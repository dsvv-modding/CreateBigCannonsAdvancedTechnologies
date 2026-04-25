package com.dsvv.cbcat.cartridge;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

import java.util.List;

import static rbasamoyai.createbigcannons.base.CBCTooltip.getPalette;

public class ProjectileCartridgeBlockItem extends BlockItem
{
    private final ProjectileCartridgeBlock cartridgeBlock;

    public ProjectileCartridgeBlockItem(ProjectileCartridgeBlock block, Properties properties)
    {
        super(block, properties);
        this.cartridgeBlock = block;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, ctx, tooltipComponents, isAdvanced);
        appendCartridgePropellantPowerText(stack, ctx, tooltipComponents, isAdvanced, this.cartridgeBlock);
        CBCTooltip.appendMuzzleVelocityText(stack, ctx, tooltipComponents, isAdvanced, this.cartridgeBlock);
        CBCTooltip.appendPropellantStressText(stack, ctx, tooltipComponents, isAdvanced, this.cartridgeBlock);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);

        Level level = context.getLevel();
        if (!level.isClientSide && level.getBlockEntity(context.getClickedPos()) instanceof BigCartridgeBlockEntity cart) {
            cart.setPower(getPower(context.getItemInHand()));
            cart.setChanged();
        }

        return result;
    }

    public static int getMaximumPowerLevels() {
        return CBCMunitionPropertiesHandlers.BIG_CARTRIDGE.getPropertiesOf(CBCBlocks.BIG_CARTRIDGE.get()).maxPowerLevels() - 1;
    }

    public static int getPower(ItemStack stack) {
        return stack.has(CBCDataComponents.POWER) ? stack.get(CBCDataComponents.POWER) : 0;
    }

    public static ItemStack getWithPower(int power, BlockEntry<? extends IProjectileCartridgeBlock> block)
    {
        ItemStack stack = block.asStack();
        stack.set(CBCDataComponents.POWER, power);
        return stack;
    }

    public static void appendCartridgePropellantPowerText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag,
                                                          ProjectileCartridgeBlock propellant) {
        if (!Screen.hasShiftDown()) return;
        FontHelper.Palette palette = getPalette();
        String key = "block." + CreateBigCannons.MOD_ID + ".propellant.tooltip.power";
        tooltip.add(Component.literal(I18n.get(key)).withStyle(ChatFormatting.GRAY));
        int min = getPower(stack);
        int max = getMaximumPowerLevels();
        tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key + ".value", min, max), palette.primary(), palette.highlight(), 1));
    }
}
