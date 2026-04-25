package com.dsvv.cbcat.casting;

import com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology;
import com.dsvv.cbcat.registry.BlockRegister;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class CannonCastingShapes
{
    private static final int INGOT_SIZE_MB = 90;

    public static final CannonCastShape
        MUZZLE_BRAKE = register("muzzle_brake_mould", new CannonCastShape(9 * INGOT_SIZE_MB, 15, BlockRegister.MUZZLE_BRAKE_MOULD)),
        FUME_EXTRACTOR = register("fume_extractor_mould", new CannonCastShape(9 * INGOT_SIZE_MB, 15, BlockRegister.FUME_EXTRACTOR_MOULD)),
        SILENCER = register("silencer_mould", new CannonCastShape(9 * INGOT_SIZE_MB, 14, BlockRegister.SILENCER_MOULD)),
        RIFLED_BARREL = register("rifled_barrel_mould", new CannonCastShape(9 * INGOT_SIZE_MB, 14, BlockRegister.RIFLED_BARREL_MOULD));

    public static final CannonCastShape
        TWIN_AUTOCANNON_BARREL = register("twin_autocannon_barrel_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.TWIN_AUTOCANNON_BARREL_MOULD)),
        TWIN_AUTOCANNON_RECOIL_SPRING = register("twin_autocannon_recoil_spring_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.TWIN_AUTOCANNON_RECOIL_SPRING_MOULD)),
        TWIN_AUTOCANNON_BREECH = register("twin_autocannon_breech_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.TWIN_AUTOCANNON_BREECH_MOULD)),
        TWIN_AUTOCANNON_SILENCER = register("twin_autocannon_silencer_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.TWIN_AUTOCANNON_SILENCER_MOULD)),
        TWIN_AUTOCANNON_MUZZLE_BRAKE = register("twin_autocannon_muzzle_brake_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.TWIN_AUTOCANNON_MUZZLE_BRAKE_MOULD));

    public static final CannonCastShape
        VERT_TWIN_AUTOCANNON_BARREL = register("vert_twin_autocannon_barrel_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.VERT_TWIN_AUTOCANNON_BARREL_MOULD)),
        VERT_TWIN_AUTOCANNON_RECOIL_SPRING = register("vert_twin_autocannon_recoil_spring_mould", new CannonCastShape(8 * INGOT_SIZE_MB, 6, BlockRegister.VERT_TWIN_AUTOCANNON_RECOIL_SPRING_MOULD)),
        VERT_TWIN_AUTOCANNON_BREECH = register("vert_twin_autocannon_breech_mould", new CannonCastShape(8 * INGOT_SIZE_MB, 8, BlockRegister.VERT_TWIN_AUTOCANNON_BREECH_MOULD)),
        VERT_TWIN_AUTOCANNON_SILENCER = register("vert_twin_autocannon_silencer_mould", new CannonCastShape(8 * INGOT_SIZE_MB, 6, BlockRegister.VERT_TWIN_AUTOCANNON_SILENCER_MOULD)),
        VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE = register("vert_twin_autocannon_muzzle_brake_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE_MOULD));

    public static final CannonCastShape
        HEAVY_AUTOCANNON_BARREL = register("heavy_autocannon_barrel_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.HEAVY_AUTOCANNON_BARREL_MOULD)),
        HEAVY_AUTOCANNON_MUZZLE_BRAKE = register("heavy_autocannon_muzzle_brake_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.HEAVY_AUTOCANNON_MUZZLE_BRAKE_MOULD)),
        HEAVY_AUTOCANNON_SILENCER = register("heavy_autocannon_silencer_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.HEAVY_AUTOCANNON_SILENCER_MOULD)),
        HEAVY_AUTOCANNON_RECOIL_SPRING = register("heavy_autocannon_recoil_spring_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.HEAVY_AUTOCANNON_RECOIL_SPRING_MOULD)),
        HEAVY_AUTOCANNON_BREECH = register("heavy_autocannon_breech_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.HEAVY_AUTOCANNON_BREECH_MOULD)),
        HEAVY_AUTOCANNON_QFBREECH = register("heavy_autocannon_qfbreech_mould", new CannonCastShape(6 * INGOT_SIZE_MB, 4, BlockRegister.HEAVY_AUTOCANNON_QFBREECH_MOULD));

    public static final CannonCastShape
        ROCKET_POD_RAIL = register("rocket_pod_rail_mould", new CannonCastShape(10 * INGOT_SIZE_MB, 10, BlockRegister.ROCKET_POD_RAIL_MOULD)),
        ROCKET_POD_BREECH = register("rocket_pod_breech_mould", new CannonCastShape(10 * INGOT_SIZE_MB, 10, BlockRegister.ROCKET_POD_BREECH_MOULD));

    public static final CannonCastShape
        AUTOCANNON_SILENCER = register("autocannon_silencer_mould", new CannonCastShape(3 * INGOT_SIZE_MB, 3, BlockRegister.AUTOCANNON_SILENCER_MOULD)),
        AUTOCANNON_MUZZLE_BRAKE = register("autocannon_muzzle_brake_mould", new CannonCastShape(3 * INGOT_SIZE_MB, 2, BlockRegister.AUTOCANNON_MUZZLE_BRAKE_MOULD));

    private static CannonCastShape register(String name, CannonCastShape shape)
    {
        return Registry.register(CBCRegistries.cannonCastShapes(), new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID, name), shape);
    }

    public static void register(){

    }
}
