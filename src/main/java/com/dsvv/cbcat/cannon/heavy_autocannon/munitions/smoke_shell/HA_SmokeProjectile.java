package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.smoke_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeEmitterEntity;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.smoke_shell.SmokeShellProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class HA_SmokeProjectile extends AbstractFuzedHeavyAutocannonProjectile {
    public HA_SmokeProjectile(EntityType<? extends AbstractHeavyAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void detonate(Position position) {
        SmokeShellProperties properties = CBCMunitionPropertiesHandlers.SMOKE_SHELL.getPropertiesOf(CBCEntityTypes.SMOKE_SHELL.get());
        SmokeExplosion explosion = new SmokeExplosion(this.level(), null, position.x(), position.y(), position.z(), 1.75f,
                Level.ExplosionInteraction.NONE);
        CreateBigCannons.handleCustomExplosion(this.level(), explosion);
        SmokeEmitterEntity smoke = CBCEntityTypes.SMOKE_EMITTER.create(this.level());
        smoke.setPos(new Vec3(position.x(), position.y(), position.z()));
        smoke.setDuration((int)(properties.smokeDuration() * 0.75f));
        smoke.setSize(properties.smokeScale() * 0.3f);
        this.level().addFreshEntity(smoke);
    }

    @Override
    public @NotNull EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                25,
                false,
                true,
                false,
                1
        );
    }

    @Override
    protected @NotNull BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.035,
                0.005,
                true,
                0.75f,
                0.5f,
                0.8f,
                0.7f
        );
    }
}
