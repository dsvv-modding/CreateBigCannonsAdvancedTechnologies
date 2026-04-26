package com.dsvv.cbcat.cartridge;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockEntity;

import java.util.List;

public class ProjectileCartridgeBlockItem extends BlockItem implements IProjectileCartridgeBlockItem
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
        IProjectileCartridgeBlockItem.appendCartridgePropellantPowerText(stack, ctx, tooltipComponents, isAdvanced);
        CBCTooltip.appendMuzzleVelocityText(stack, ctx, tooltipComponents, isAdvanced, this.cartridgeBlock);
        CBCTooltip.appendPropellantStressText(stack, ctx, tooltipComponents, isAdvanced, this.cartridgeBlock);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);

        Level level = context.getLevel();
        if (!level.isClientSide && level.getBlockEntity(context.getClickedPos()) instanceof BigCartridgeBlockEntity cart) {
            cart.setPower(IProjectileCartridgeBlockItem.getPower(context.getItemInHand()));
            cart.setChanged();
        }

        return result;
    }
}
