package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.cannon.autocannon.munitions.apds.AutocannonAPDSProjectile;
import com.dsvv.cbcat.cannon.autocannon.munitions.apdsfs.AutocannonAPDSFSProjectile;
import com.dsvv.cbcat.cannon.autocannon.munitions.he.AutocannonHEProjectile;
import com.dsvv.cbcat.cannon.autocannon.munitions.hei.AutocannonHEIProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.ap_shot.HA_APProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonProjectileRenderer;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.apds_shot.HA_APDSProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.apdsfs.HA_APDSFSProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.he_shell.HA_HEProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.heat_shell.HA_CopperRay;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.heat_shell.HA_HEATProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.hef_shell.HA_HEFProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.smoke_shell.HA_SmokeProjectile;
import com.dsvv.cbcat.cluster_munition.FuzedClusterProjectile;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.ritchiesprojectilelib.RPLTags;

import static com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology.REGISTRATE;

public class EntityRegister
{
    public static final EntityEntry<HA_APProjectile> HA_AP_PROJECTILE = REGISTRATE.entity("ha_ap_projectile", HA_APProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.4f, 0.4f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> HeavyAutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<HA_APDSProjectile> HA_APDS_PROJECTILE = REGISTRATE.entity("ha_apds_projectile", HA_APDSProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.4f, 0.4f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> HeavyAutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<HA_APDSFSProjectile> HA_APDSFS_PROJECTILE = REGISTRATE.entity("ha_apdsfs_projectile", HA_APDSFSProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.4f, 0.4f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> HeavyAutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<HA_HEProjectile> HA_HE_PROJECTILE = REGISTRATE.entity("ha_he_projectile", HA_HEProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.4f, 0.4f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> HeavyAutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<HA_HEFProjectile> HA_HEF_PROJECTILE = REGISTRATE.entity("ha_hef_projectile", HA_HEFProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.4f, 0.4f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> HeavyAutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<HA_HEATProjectile> HA_HEAT_PROJECTILE = REGISTRATE.entity("ha_heat_projectile", HA_HEATProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.4f, 0.4f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> HeavyAutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<HA_SmokeProjectile> HA_SMOKE_PROJECTILE = REGISTRATE.entity("ha_smoke_projectile", HA_SmokeProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.4f, 0.4f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> HeavyAutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();

    public static final EntityEntry<HA_CopperRay> HA_HEAT_COPPER_RAY = REGISTRATE.entity("ha_heat_copper_ray", HA_CopperRay::new, MobCategory.MISC)
            .properties(c -> c.sized(0.4f, 0.4f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> HeavyAutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();

    public static final EntityEntry<AutocannonAPDSProjectile> APDS_PROJECTILE = REGISTRATE.entity("apds_projectile", AutocannonAPDSProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.2f, 0.2f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> AutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<AutocannonAPDSFSProjectile> APDSFS_PROJECTILE = REGISTRATE.entity("apdsfs_projectile", AutocannonAPDSFSProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.2f, 0.2f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> AutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<AutocannonHEProjectile> HE_PROJECTILE = REGISTRATE.entity("he_projectile", AutocannonHEProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.2f, 0.2f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> AutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();
    public static final EntityEntry<AutocannonHEIProjectile> HEI_PROJECTILE = REGISTRATE.entity("hei_projectile", AutocannonHEIProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.2f, 0.2f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> AutocannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();

    public static final EntityEntry<FuzedClusterProjectile> CLUSTER_PROJECTILE = REGISTRATE.entity("cluster_projectile", FuzedClusterProjectile::new, MobCategory.MISC)
            .properties(c -> c.sized(0.8f, 0.8f).fireImmune().updateInterval(1).setShouldReceiveVelocityUpdates(false).setTrackingRange(16))
            .renderer(() -> BigCannonProjectileRenderer::new)
            .tag(RPLTags.PRECISE_MOTION)
            .register();

    public static void register() {}
}
