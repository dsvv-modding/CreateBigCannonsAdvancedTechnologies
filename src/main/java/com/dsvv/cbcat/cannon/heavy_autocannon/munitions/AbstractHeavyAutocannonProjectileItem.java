package com.dsvv.cbcat.cannon.heavy_autocannon.munitions;

import com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology;
import com.dsvv.cbcat.registry.DataComponentRegistry;
import com.dsvv.cbcat.registry.ItemRegister;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractHeavyAutocannonProjectileItem extends Item
{
    public AbstractHeavyAutocannonProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    public abstract AbstractHeavyAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level);
    public abstract EntityType<?> getEntityType(ItemStack stack);

    @Nonnull
    public AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
        return CBCMunitionPropertiesHandlers.INERT_AUTOCANNON_PROJECTILE.getPropertiesOf(this.getEntityType(itemStack)).autocannonProperties();
    }

    public ItemStack getCreativeTabCartridgeItem(boolean strong) {
        ItemStack stack = ItemRegister.HEAVY_AUTOCANNON_CARTRIDGE.asStack();
        HeavyAutocannonCartridgeItem.writeProjectile(this.getDefaultInstance(), stack);
        stack.set(DataComponentRegistry.HA_STRONG_ROUND, strong);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, ctx, tooltipComponents, isAdvanced);
        if (stack.has(CBCDataComponents.TRACER)) {
            CreateLang.builder("tooltip").translate(CreateBigCannons.MOD_ID + ".tracer").addTo(tooltipComponents);
        }
        if (stack.has(DataComponentRegistry.HA_STRONG_ROUND)) {
            LangBuilder builder = CreateLang.builder("tooltip").translate(CreateBigCannons_AdvancedTechnology.MOD_ID + ".heavy_autocannon.charged");
            if (stack.get(DataComponentRegistry.HA_STRONG_ROUND))
                builder.add(CreateLang.builder("tooltip").translate(CreateBigCannons_AdvancedTechnology.MOD_ID + "heavy_autocannon.charged.strong"));
            else
                builder.add(CreateLang.builder("tooltip").translate(CreateBigCannons_AdvancedTechnology.MOD_ID + "heavy_autocannon.charged.weak"));
            builder.addTo(tooltipComponents);
        }
    }

    public ItemStack getFuze(ItemStack stack) {
        ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
        if (!items.equals(ItemContainerContents.EMPTY))
            return items.copyOne();
        return ItemStack.EMPTY;
    }

    public boolean isTracer(ItemStack stack) {
        return stack.has(CBCDataComponents.AUTOCANNON_TRACER) ? stack.get(CBCDataComponents.AUTOCANNON_TRACER) : false;
    }

    public void setTracer(ItemStack stack, boolean isTracer) {
        stack.set(CBCDataComponents.AUTOCANNON_TRACER, isTracer);
    }
}
