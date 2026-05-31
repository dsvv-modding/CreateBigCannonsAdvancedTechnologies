package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.he_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class HA_HEProjectile extends AbstractFuzedHeavyAutocannonProjectile {

    public HA_HEProjectile(EntityType<? extends AbstractHeavyAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    public @NotNull EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                27,
                false,
                true,
                false,
                1.825f
        );
    }

    @Override
    protected @NotNull BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.03,
                0.013,
                true,
                1.6f,
                2.3f,
                0.8f,
                0.66f
        );
    }

    protected void detonate(Position position) {
        FlakAutocannonProjectileProperties properties = CBCMunitionPropertiesHandlers.FLAK_AUTOCANNON.getPropertiesOf(CBCEntityTypes.FLAK_AUTOCANNON.get());
        ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
                position.y(), position.z(), properties.explosion().blockDamagePower() * 1.66f, properties.explosion().entityDamagePower() * 1.66f, false,
                CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
        CreateBigCannons.handleCustomExplosion(this.level(), explosion);
    }
}