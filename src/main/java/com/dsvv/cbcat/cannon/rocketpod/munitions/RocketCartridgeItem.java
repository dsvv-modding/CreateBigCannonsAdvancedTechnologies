package com.dsvv.cbcat.cannon.rocketpod.munitions;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RocketCartridgeItem  extends Item implements RocketItem {
    public RocketCartridgeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        ItemStack round = getProjectileStack(stack);
        if (!round.isEmpty()) {
            tooltip.add(Component.translatable("item.minecraft.crossbow.projectile").append(" ").append(round.getDisplayName()));
                tooltip.add(Component.translatable("tooltip.cbc_at.heavy_autocannon.charged").withStyle(ChatFormatting.WHITE).append(Component.translatable("tooltip.cbc_at.heavy_autocannon.charged.weak").withStyle(ChatFormatting.DARK_GREEN)));
            if (round.getItem() instanceof AutocannonRoundItem) {
                List<Component> subTooltip = new ArrayList<>();
                round.getItem().appendHoverText(round, ctx, subTooltip, flag);
                for (int i = 0; i < subTooltip.size(); ++i) {
                    subTooltip.set(i, Component.literal("  ").append(subTooltip.get(i)).withStyle(ChatFormatting.GRAY));
                }
                tooltip.addAll(subTooltip);
            }
        }
    }

    @Override public RocketType getType() { return RocketType.NORMAL; }

    @Override
    @Nullable
    public AbstractRocket getAutocannonProjectile(ItemStack stack, Level level) {
        ItemStack projectileStack = getProjectileStack(stack);
        return projectileStack.getItem() instanceof AbstractRocketItem projectileItem ? projectileItem.getAutocannonProjectile(projectileStack, level) : null;
    }

    @Nullable
    @Override
    public EntityType<?> getEntityType(ItemStack stack) {
        ItemStack projectileStack = getProjectileStack(stack);
        return projectileStack.getItem() instanceof AutocannonRoundItem projectileItem ? projectileItem.getEntityType(projectileStack) : null;
    }

    @Override
    public AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
        ItemStack projectileStack = getProjectileStack(itemStack);
        return projectileStack.getItem() instanceof AutocannonRoundItem roundItem ? roundItem.getAutocannonProperties(itemStack) :
                AutocannonProjectilePropertiesComponent.DEFAULT;
    }

    public static ItemStack getProjectileStack(ItemStack stack) {
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.PROJECTILE, ItemContainerContents.EMPTY);
        return items.copyOne();
    }

    public static boolean hasProjectile(ItemStack stack) {
        return stack.has(CBCDataComponents.PROJECTILE);
    }

    public static void writeProjectile(ItemStack round, ItemStack cartridge) {
        if (round.getItem() instanceof AutocannonRoundItem && cartridge.getItem() instanceof RocketCartridgeItem) {
            cartridge.set(CBCDataComponents.PROJECTILE, ItemContainerContents.fromItems(Lists.newArrayList(round)));
        }
    }

    @Override
    public boolean isTracer(ItemStack stack) {
        return hasProjectile(stack) && getProjectileStack(stack).getOrDefault(CBCDataComponents.AUTOCANNON_TRACER, false);
    }

    @Override
    public void setTracer(ItemStack stack, boolean value) {
        if (!hasProjectile(stack))
            return;
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.PROJECTILE, ItemContainerContents.EMPTY);
        ItemStack projectile = items.copyOne();
        projectile.set(CBCDataComponents.AUTOCANNON_TRACER, value);
        stack.set(CBCDataComponents.PROJECTILE, ItemContainerContents.fromItems(Lists.newArrayList(projectile)));
    }
}
