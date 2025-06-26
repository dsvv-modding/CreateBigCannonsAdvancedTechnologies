package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.hef_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HA_HEFProjectileItem extends AbstractFuzedHeavyAutocannonProjectileItem {
    public HA_HEFProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractHeavyAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
        HA_HEFProjectile projectile = EntityRegister.HA_HEF_PROJECTILE.create(level);
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("Fuze", Tag.TAG_COMPOUND)) {
            projectile.setFuze(ItemStack.of(tag.getCompound("Fuze")));
        }
        return projectile;
    }

    @Override
    public EntityType<?> getEntityType(ItemStack stack) {
        return EntityRegister.HA_HEF_PROJECTILE.get();
    }
}
