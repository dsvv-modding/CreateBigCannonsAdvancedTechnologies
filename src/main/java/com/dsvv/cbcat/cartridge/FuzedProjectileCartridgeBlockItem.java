package com.dsvv.cbcat.cartridge;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockEntity;

public class FuzedProjectileCartridgeBlockItem extends FuzedProjectileBlockItem
{
    private final FuzedProjectileCartridgeBlock cartridgeBlock;

    public FuzedProjectileCartridgeBlockItem(FuzedProjectileCartridgeBlock block, Properties properties)
    {
        super(block, properties);
        this.cartridgeBlock = block;
    }
    /*
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        CBCTooltip.appendBigCartridgePropellantPowerText(stack, level, tooltipComponents, isAdvanced, this.cartridgeBlock);
        CBCTooltip.appendMuzzleVelocityText(stack, level, tooltipComponents, isAdvanced, this.cartridgeBlock);
        CBCTooltip.appendPropellantStressText(stack, level, tooltipComponents, isAdvanced, this.cartridgeBlock);
    }
    */
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
        return stack.getOrCreateTag().getInt("Power");
    }

    public static ItemStack getWithPower(int power, BlockEntry<? extends ProjectileCartridgeBlock> block)
    {
        ItemStack stack = block.asStack();
        stack.getOrCreateTag().putInt("Power", power);
        return stack;
    }
}