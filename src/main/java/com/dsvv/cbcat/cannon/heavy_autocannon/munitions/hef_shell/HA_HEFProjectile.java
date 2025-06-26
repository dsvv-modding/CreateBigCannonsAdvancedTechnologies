package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.hef_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakExplosion;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

public class HA_HEFProjectile extends AbstractFuzedHeavyAutocannonProjectile {
    public HA_HEFProjectile(EntityType<? extends AbstractHeavyAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    protected void detonate(Position position) {
        Vec3 oldDelta = this.getDeltaMovement();
        FlakAutocannonProjectileProperties properties = CBCMunitionPropertiesHandlers.FLAK_AUTOCANNON.getPropertiesOf(CBCEntityTypes.FLAK_AUTOCANNON.get());
        FlakExplosion explosion = new FlakExplosion(this.level(), null, this.indirectArtilleryFire(false), position.x(), position.y(), position.z(),
                properties.explosion().explosivePower() * 1.33f, CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
        CreateBigCannons.handleCustomExplosion(this.level(), explosion);
        CBCProjectileBurst.spawnConeBurst(this.level(), CBCEntityTypes.FLAK_BURST.get(), new Vec3(position.x(), position.y(), position.z()),
                oldDelta, properties.flakBurst().burstProjectileCount() * 8, properties.flakBurst().burstSpread() * 4);
    }

    @Override
    public @NotNull EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                35,
                false,
                true,
                false,
                1.5f
        );
    }

    @Override
    protected @NotNull BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.035,
                0.005,
                true,
                0.75f,
                0.4f,
                0.33f,
                0.7f
        );
    }
}