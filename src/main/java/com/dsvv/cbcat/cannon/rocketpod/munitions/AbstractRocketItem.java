package com.dsvv.cbcat.cannon.rocketpod.munitions;

import com.dsvv.cbcat.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class AbstractRocketItem extends Item
{
    public AbstractRocketItem(Properties pProperties) {
        super(pProperties);
    }

    public abstract AbstractRocket getAutocannonProjectile(ItemStack stack, Level level);
    public abstract EntityType<?> getEntityType(ItemStack stack);

    @Nonnull
    public AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
        return CBCMunitionPropertiesHandlers.INERT_AUTOCANNON_PROJECTILE.getPropertiesOf(this.getEntityType(itemStack)).autocannonProperties();
    }

    public abstract ItemStack getCreativeTabCartridgeItem(int fuel);/* {
        ItemStack stack = ItemRegister.HEAVY_AUTOCANNON_CARTRIDGE.asStack();
        stack.getOrCreateTag().putByte("Fuel", (byte) fuel);
        RocketCartridgeItem.writeProjectile(this.getDefaultInstance(), stack);
        return stack;
    }*/

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, ctx, tooltipComponents, isAdvanced);
        /*if (stack.getOrCreateTag().getBoolean("Tracer")) {
            CreateLang.builder("tooltip").translate(CreateBigCannons.MOD_ID + ".tracer").addTo(tooltipComponents);
        }*/
        if (stack.has(DataComponentRegistry.ROCKET_FUEL))
            tooltipComponents.add(Component.translatable("tooltip.cbc_at.rocket.fuel").withStyle(ChatFormatting.WHITE).append(Byte.toString(stack.get(DataComponentRegistry.ROCKET_FUEL))));
    }

    public boolean isTracer(ItemStack stack) {
        return stack.has(CBCDataComponents.AUTOCANNON_TRACER) ? stack.get(CBCDataComponents.AUTOCANNON_TRACER) : false;
    }

    public void setTracer(ItemStack stack, boolean isTracer) {
        stack.set(CBCDataComponents.AUTOCANNON_TRACER, isTracer);
    }
}
