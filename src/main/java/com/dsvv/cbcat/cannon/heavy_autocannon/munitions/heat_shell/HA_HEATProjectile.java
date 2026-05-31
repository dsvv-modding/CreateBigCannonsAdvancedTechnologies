package com.dsvv.cbcat.cannon.heavy_autocannon.munitions.heat_shell;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractHeavyAutocannonProjectile;
import com.dsvv.cbcat.registry.EntityRegister;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.autocannon.flak.FlakAutocannonProjectileProperties;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import static com.dsvv.cbcat.debugUtils.DebugUtils.displayCustomClientMessage;

public class HA_HEATProjectile extends AbstractFuzedHeavyAutocannonProjectile {
    private Vec3 velocity = new Vec3(0, 0, 0);
    private boolean spawnCopper = false;
    private Position pos = null;
    public HA_HEATProjectile(EntityType<? extends AbstractHeavyAutocannonProjectile> type, Level level) {
        super(type, level);
    }

    public void tick() {
        velocity = getOrientation().normalize();
        if(spawnCopper)
            spawnCopper();
        super.tick();
    }

    @Override
    protected void detonate(Position position) {
        FlakAutocannonProjectileProperties properties = CBCMunitionPropertiesHandlers.FLAK_AUTOCANNON.getPropertiesOf(CBCEntityTypes.FLAK_AUTOCANNON.get());
        ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
                position.y(), position.z(), properties.explosion().blockDamagePower() * 1.5f, properties.explosion().entityDamagePower() * 1.4f, false,
                CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
        pos = position;
        spawnCopper = true;
        CreateBigCannons.handleCustomExplosion(this.level(), explosion);
    }

    @Override
    public @NotNull EntityDamagePropertiesComponent getDamageProperties() {
        return new EntityDamagePropertiesComponent(
                28,
                false,
                true,
                false,
                1.75f
        );
    }

    @Override
    protected @NotNull BallisticPropertiesComponent getBallisticProperties() {
        return new BallisticPropertiesComponent(
                -0.035,
                0.012,
                true,
                1.55f,
                2.2f,
                0.85f,
                0.66f
        );
    }

    private void spawnCopper() {
        HA_CopperRay copperRay = EntityRegister.HA_HEAT_COPPER_RAY.create(level());
        if (copperRay != null) {
            copperRay.setTracer(true);
            copperRay.setPos((Vec3) pos);
            copperRay.shoot(velocity.x, velocity.y, velocity.z, 8, 0.001f);
            copperRay.xRotO = xRotO;
            copperRay.yRotO = yRotO;
            copperRay.setLifetime(5);
            level().addFreshEntity(copperRay);
        }
        spawnCopper = false;
    }
}
