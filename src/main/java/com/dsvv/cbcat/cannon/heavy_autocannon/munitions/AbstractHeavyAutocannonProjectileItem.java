package com.dsvv.cbcat.cannon.heavy_autocannon.munitions;

import com.dsvv.cbcat.registry.ItemRegister;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractHeavyAutocannonProjectileItem extends Item
{
    public AbstractHeavyAutocannonProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    public abstract AbstractHeavyAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level);
    public abstract EntityType<?> getEntityType(ItemStack stack);

    public @NotNull AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
        return CBCMunitionPropertiesHandlers.INERT_AUTOCANNON_PROJECTILE.getPropertiesOf(this.getEntityType(itemStack)).autocannonProperties();
    }

    public ItemStack getCreativeTabCartridgeItem(boolean strong) {
        ItemStack stack = ItemRegister.HEAVY_AUTOCANNON_CARTRIDGE.asStack();
        stack.getOrCreateTag().putBoolean("Strong", strong);
        HeavyAutocannonCartridgeItem.writeProjectile(this.getDefaultInstance(), stack);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (stack.getOrCreateTag().getBoolean("Tracer")) {
            Lang.builder("tooltip").translate(CreateBigCannons.MOD_ID + ".tracer").addTo(tooltipComponents);
        }
    }

    public ItemStack getFuze(ItemStack stack) {
        if (stack.getOrCreateTag().contains("Fuze"))
            return ItemStack.of(stack.getTag().getCompound("Fuze"));
        return ItemStack.EMPTY;
    }

    public boolean isTracer(ItemStack stack) {
        return stack.getOrCreateTag().contains("Tracer") ? stack.getTag().getBoolean("Tracer") : false;
    }

    public void setTracer(ItemStack stack, boolean isTracer) {
        stack.getOrCreateTag().putBoolean("Tracer", isTracer);
    }
}
