package com.dsvv.cbcat.mixin;

import com.dsvv.cbcat.base.IAbstractAutocannonProjectileMixin;
import net.minecraft.core.Position;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;

@Mixin(AbstractCannonProjectile.class)
public abstract class AbstractAutocannonProjectileMixin implements IAbstractAutocannonProjectileMixin {

    public BallisticPropertiesComponent getBallisticComponent() {
        return this.getBallisticProperties();
    }

    public void detonateProjectile(Position position) {
        this.detonate(position);
    }

    //@Shadow(remap = false)
    protected abstract void detonate(Position position);

    @Shadow(remap = false)
    protected abstract BallisticPropertiesComponent getBallisticProperties();
}
