package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.smoke_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HA_SmokeProjectileItem extends AbstractFuzedHeavyAutocannonProjectileItem {
    public HA_SmokeProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractHeavyAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
        HA_SmokeProjectile projectile = EntityRegister.HA_SMOKE_PROJECTILE.create(level);
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("Fuze", Tag.TAG_COMPOUND)) {
            projectile.setFuze(ItemStack.of(tag.getCompound("Fuze")));
        }
        return projectile;
    }

    @Override
    public EntityType<?> getEntityType(ItemStack stack) {
        return EntityRegister.HA_SMOKE_PROJECTILE.get();
    }
}
