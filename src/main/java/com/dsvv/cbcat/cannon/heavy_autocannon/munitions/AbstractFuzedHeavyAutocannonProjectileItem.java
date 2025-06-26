package com.dsvv.cbcat.cannon.heavy_autocannon.munitions;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.FuzedItemMunition;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFuzedHeavyAutocannonProjectileItem extends AbstractHeavyAutocannonProjectileItem implements FuzedItemMunition
{
    public AbstractFuzedHeavyAutocannonProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        CompoundTag tag = stack.getOrCreateTag();
        ItemStack fuze =
                tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Fuze")) : ItemStack.EMPTY;
        if (!fuze.isEmpty()) {
            CreateLang.builder("block")
                    .translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
                    .add(Component.literal(" "))
                    .add(fuze.getDisplayName().copy())
                    .addTo(tooltip);
            if (fuze.getItem() instanceof FuzeItem) {
                List<Component> subTooltip = new ArrayList<>();
                fuze.getItem().appendHoverText(fuze, level, subTooltip, flag);
                subTooltip.replaceAll(sibling -> Component.literal("  ").append(sibling).withStyle(ChatFormatting.GRAY));
                tooltip.addAll(subTooltip);
            }
        }
    }

    public ItemStack getFuze(ItemStack stack) {
        if (stack.getOrCreateTag().contains("Fuze"))
            return ItemStack.of(stack.getTag().getCompound("Fuze"));
        return ItemStack.EMPTY;
    }
}
