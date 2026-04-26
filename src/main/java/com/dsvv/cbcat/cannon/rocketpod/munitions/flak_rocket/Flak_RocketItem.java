package com.dsvv.cbcat.cannon.rocketpod.munitions.flak_rocket;

import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractFuzedRocket;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractFuzedRocketItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocket;
import com.dsvv.cbcat.cannon.rocketpod.munitions.RocketCartridgeItem;
import com.dsvv.cbcat.registry.EntityRegister;
import com.dsvv.cbcat.registry.ItemRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Flak_RocketItem extends AbstractFuzedRocketItem {
    public Flak_RocketItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractRocket getAutocannonProjectile(ItemStack stack, Level level) {
        AbstractFuzedRocket rocket = EntityRegister.FLAK_ROCKET.create(level);
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("fuel", Tag.TAG_BYTE)) {
            rocket.setFuel(tag.getByte("fuel"));
        }
        if (tag.contains("Fuze", Tag.TAG_COMPOUND)) {
            rocket.setFuze(ItemStack.of(tag.getCompound("Fuze")));
        }
        return rocket;
    }

    @Override
    public EntityType<?> getEntityType(ItemStack stack) {
        return EntityRegister.FLAK_ROCKET.get();
    }

    public ItemStack getCreativeTabCartridgeItem(int fuel) {
        ItemStack stack = ItemRegister.FLAK_ROCKET_ITEM.asStack();
        stack.getOrCreateTag().putByte("fuel", (byte) fuel);
        RocketCartridgeItem.writeProjectile(this.getDefaultInstance(), stack);
        return stack;
    }
}
