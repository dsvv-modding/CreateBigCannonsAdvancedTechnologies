package com.dsvv.cbcat.cannon.medium_rocketpod.munitions;

import com.dsvv.cbcat.registry.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import java.util.List;

public abstract class AbstractMediumRocketItem extends Item
{
    private byte fuel;
    public AbstractMediumRocketItem(Properties pProperties) {
        super(pProperties);
    }

    public abstract AbstractMediumRocket getAutocannonProjectile(ItemStack stack, Level level);
    public abstract EntityType<?> getEntityType(ItemStack stack);

    public AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
        return CBCMunitionPropertiesHandlers.INERT_AUTOCANNON_PROJECTILE.getPropertiesOf(this.getEntityType(itemStack)).autocannonProperties();
    }

    public abstract ItemStack getCreativeTabCartridgeItem(int fuel); /*{
        ItemStack stack = ItemRegister.HEAVY_AUTOCANNON_CARTRIDGE.asStack();
        MediumRocketCartridgeItem.writeProjectile(this.getDefaultInstance(), stack);
        stack.getOrCreateTag().putByte("Fuel", (byte) fuel);
        return stack;
    }*/

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, ctx, tooltipComponents, isAdvanced);
        if (stack.has(DataComponentRegistry.ROCKET_FUEL))
            tooltipComponents.add(Component.translatable("tooltip.cbc_at.rocket.fuel").withStyle(ChatFormatting.WHITE).append(Byte.toString(stack.get(DataComponentRegistry.ROCKET_FUEL))));
        /*if (stack.getOrCreateTag().getByte("Fuel")) {
            CreateLang.builder("tooltip").translate(CreateBigCannons.MOD_ID + ".tracer").addTo(tooltipComponents);
        }*/
    }

    /*public boolean isTracer(ItemStack stack) {
        return stack.getOrCreateTag().contains("Tracer") ? stack.getTag().getBoolean("Tracer") : false;
    }

    public void setTracer(ItemStack stack, boolean isTracer) {
        stack.getOrCreateTag().putBoolean("Tracer", isTracer);
    }*/
}
