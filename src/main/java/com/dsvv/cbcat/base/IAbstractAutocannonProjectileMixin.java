package com.dsvv.cbcat.base;

import net.minecraft.core.Position;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;

public interface IAbstractAutocannonProjectileMixin {
    BallisticPropertiesComponent getBallisticComponent();
    void detonateProjectile(Position position);
}
