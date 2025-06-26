package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.heat_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HA_HEATProjectileItem extends AbstractFuzedHeavyAutocannonProjectileItem {
    public HA_HEATProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractHeavyAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
        HA_HEATProjectile projectile = EntityRegister.HA_HEAT_PROJECTILE.create(level);
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("Fuze", Tag.TAG_COMPOUND)) {
            projectile.setFuze(ItemStack.of(tag.getCompound("Fuze")));
        }
        return projectile;
    }

    @Override
    public EntityType<?> getEntityType(ItemStack stack) {
        return EntityRegister.HA_HEAT_PROJECTILE.get();
    }
}
