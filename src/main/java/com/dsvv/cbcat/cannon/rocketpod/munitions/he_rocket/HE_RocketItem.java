package com.dsvv.cbcat.cannon.rocketpod.munitions.he_rocket;

import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractFuzedRocket;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractFuzedRocketItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.AbstractRocket;
import com.dsvv.cbcat.cannon.rocketpod.munitions.RocketCartridgeItem;
import com.dsvv.cbcat.registry.DataComponentRegistry;
import com.dsvv.cbcat.registry.EntityRegister;
import com.dsvv.cbcat.registry.ItemRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class HE_RocketItem extends AbstractFuzedRocketItem {
    public HE_RocketItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractRocket getAutocannonProjectile(ItemStack stack, Level level) {
        AbstractFuzedRocket rocket = EntityRegister.HE_ROCKET.create(level);
        if (stack.has(DataComponentRegistry.ROCKET_FUEL))
            rocket.setFuel(stack.get(DataComponentRegistry.ROCKET_FUEL));
        if (stack.has(CBCDataComponents.FUZE)) {
            ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
            ItemStack fuze = items.copyOne();
            rocket.setFuze(fuze);
        }
        return rocket;
    }

    @Override
    public EntityType<?> getEntityType(ItemStack stack) {
        return EntityRegister.HE_ROCKET.get();
    }

    public ItemStack getCreativeTabCartridgeItem(int fuel) {
        ItemStack stack = ItemRegister.HE_ROCKET_ITEM.asStack();
        stack.set(DataComponentRegistry.ROCKET_FUEL, (byte) fuel);
        RocketCartridgeItem.writeProjectile(this.getDefaultInstance(), stack);
        return stack;
    }
}
