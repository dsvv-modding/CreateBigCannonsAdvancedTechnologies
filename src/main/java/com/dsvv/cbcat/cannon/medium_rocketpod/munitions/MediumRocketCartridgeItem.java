package com.dsvv.cbcat.cannon.medium_rocketpod.munitions;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonRoundItem;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MediumRocketCartridgeItem extends Item implements MediumRocketItem {
    public MediumRocketCartridgeItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        ItemStack round = getProjectileStack(stack);
        if (!round.isEmpty()) {
            tooltip.add(Component.translatable("item.minecraft.crossbow.projectile").append(" ").append(round.getDisplayName()));
                tooltip.add(Component.translatable("tooltip.cbc_at.heavy_autocannon.charged").withStyle(ChatFormatting.WHITE).append(Component.translatable("tooltip.cbc_at.heavy_autocannon.charged.weak").withStyle(ChatFormatting.DARK_GREEN)));
            if (round.getItem() instanceof AutocannonRoundItem) {
                List<Component> subTooltip = new ArrayList<>();
                round.getItem().appendHoverText(round, level, subTooltip, flag);
                for (int i = 0; i < subTooltip.size(); ++i) {
                    subTooltip.set(i, Component.literal("  ").append(subTooltip.get(i)).withStyle(ChatFormatting.GRAY));
                }
                tooltip.addAll(subTooltip);
            }
        }
    }

    @Override public MediumRocketType getType() { return MediumRocketType.NORMAL; }

    @Override
    @Nullable
    public AbstractMediumRocket getAutocannonProjectile(ItemStack stack, Level level) {
        ItemStack projectileStack = getProjectileStack(stack);
        return projectileStack.getItem() instanceof AbstractMediumRocketItem projectileItem ? projectileItem.getAutocannonProjectile(projectileStack, level) : null;
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
        return hasProjectile(stack) ? ItemStack.of(stack.getOrCreateTag().getCompound("Projectile")) : ItemStack.EMPTY;
    }

    public static boolean hasProjectile(ItemStack stack) {
        return stack.getOrCreateTag().contains("Projectile", Tag.TAG_COMPOUND);
    }

    public static void writeProjectile(ItemStack round, ItemStack cartridge) {
        if (round.getItem() instanceof AutocannonRoundItem && cartridge.getItem() instanceof MediumRocketCartridgeItem  ) {
            cartridge.getOrCreateTag().put("Projectile", round.save(new CompoundTag()));
        }
    }

    @Override
    public boolean isTracer(ItemStack stack) {
        return hasProjectile(stack) && getProjectileStack(stack).getOrCreateTag().getBoolean("Tracer");
    }

    @Override
    public void setTracer(ItemStack stack, boolean value) {
        if (!hasProjectile(stack)) return;
        CompoundTag tag = stack.getOrCreateTag().getCompound("Projectile");
        if (!tag.contains("tag", Tag.TAG_COMPOUND)) tag.put("tag", new CompoundTag());
        tag.getCompound("tag").putBoolean("Tracer", value);
    }
}
