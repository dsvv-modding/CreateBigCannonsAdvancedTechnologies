package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.heat_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;

public class HA_HEATProjectileItem extends AbstractFuzedHeavyAutocannonProjectileItem {
    public HA_HEATProjectileItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public AbstractHeavyAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
        HA_HEATProjectile projectile = EntityRegister.HA_HEAT_PROJECTILE.create(level);
        if (stack.has(CBCDataComponents.FUZE)) {
            ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
            ItemStack fuze = items.copyOne();
            projectile.setFuze(fuze);
        }
        return projectile;
    }

    @Override
    public EntityType<?> getEntityType(ItemStack stack) {
        return EntityRegister.HA_HEAT_PROJECTILE.get();
    }
}
