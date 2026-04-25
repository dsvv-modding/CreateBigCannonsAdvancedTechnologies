package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.cannon.*;
import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.breech.HeavyAutocannonBreechBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech.HeavyAutocannonQuickFireBreechBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech.HeavyAutocannonQuickFireBreechInstance;
import com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech.HeavyAutocannonQuickFireBreechRenderer;
import com.dsvv.cbcat.cannon.heavy_autocannon.breech.HeavyAutocannonBreechInstance;
import com.dsvv.cbcat.cannon.heavy_autocannon.breech.HeavyAutocannonBreechRenderer;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring.HeavyAutocannonRecoilSpringBlockEntity;
import com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring.HeavyAutocannonRecoilSpringInstance;
import com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring.HeavyAutocannonRecoilSpringRenderer;
import com.dsvv.cbcat.cannon.medium_rocketpod.MediumRocketPodBlockEntity;
import com.dsvv.cbcat.cannon.medium_rocketpod.breech.MediumRocketPodBreechBlockEntity;
import com.dsvv.cbcat.cannon.medium_rocketpod.breech.MediumRocketPodBreechInstance;
import com.dsvv.cbcat.cannon.rocketpod.RocketPodBlockEntity;
import com.dsvv.cbcat.cannon.rocketpod.breech.RocketPodBreechBlockEntity;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBlockEntity;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonInstance;
import com.dsvv.cbcat.cannon.twin_autocannon.breech.TwinAutocannonBreechBlockEntity;
import com.dsvv.cbcat.cannon.twin_autocannon.breech.TwinAutocannonBreechInstance;
import com.dsvv.cbcat.cannon.twin_autocannon.breech.TwinAutocannonBreechRenderer;
import com.dsvv.cbcat.cannon.twin_autocannon.recoil_spring.TwinAutocannonRecoilSpringBlockEntity;
import com.dsvv.cbcat.cannon.twin_autocannon.recoil_spring.TwinAutocannonRecoilSpringInstance;
import com.dsvv.cbcat.cannon.twin_autocannon.recoil_spring.TwinAutocannonRecoilSpringRenderer;
import com.dsvv.cbcat.cartridge.ProjectileCartridgeBlockEntity;
import com.dsvv.cbcat.cluster_munition.FuzedClusterProjectileBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellBlockEntity;

import static com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology.REGISTRATE;

public class BlockEntityRegister
{
    public static final BlockEntityEntry<MuzzleBrakeBlockEntity> MUZZLE_BRAKE_BLOCK_ENTITY = REGISTRATE.blockEntity("muzzle_brake_be", MuzzleBrakeBlockEntity::new)
            .validBlocks(BlockRegister.CAST_IRON_MUZZLE_BRAKE, BlockRegister.BRONZE_MUZZLE_BRAKE, BlockRegister.STEEL_MUZZLE_BRAKE, BlockRegister.NETHERSTEEL_MUZZLE_BRAKE)
            .register();

    public static final BlockEntityEntry<FumeExtractorBlockEntity> FUME_EXTRACTOR_BLOCK_ENTITY = REGISTRATE.blockEntity("fume_extractor_be", FumeExtractorBlockEntity::new)
            .validBlocks(BlockRegister.CAST_IRON_FUME_EXTRACTOR, BlockRegister.BRONZE_FUME_EXTRACTOR, BlockRegister.STEEL_FUME_EXTRACTOR, BlockRegister.NETHERSTEEL_FUME_EXTRACTOR)
            .register();

    public static final BlockEntityEntry<SilencerBlockEntity> SILENCER_BLOCK_ENTITY = REGISTRATE.blockEntity("silencer_be", SilencerBlockEntity::new)
            .validBlocks(BlockRegister.CAST_IRON_SILENCER, BlockRegister.BRONZE_SILENCER, BlockRegister.STEEL_SILENCER, BlockRegister.NETHERSTEEL_SILENCER,
                    BlockRegister.BUILT_UP_STEEL_SILENCER, BlockRegister.BUILT_UP_NETHERSTEEL_SILENCER)
            .register();

    public static final BlockEntityEntry<RifledBarrelBlockEntity> RIFLED_BARREL_BLOCK_ENTITY = REGISTRATE.blockEntity("rifled_barrel_be", RifledBarrelBlockEntity::new)
            .validBlocks(BlockRegister.CAST_IRON_RIFLED_BARREL, BlockRegister.BRONZE_RIFLED_BARREL, BlockRegister.STEEL_RIFLED_BARREL, BlockRegister.NETHERSTEEL_RIFLED_BARREL,
                    BlockRegister.BUILT_UP_STEEL_RIFLED_BARREL, BlockRegister.BUILT_UP_NETHERSTEEL_RIFLED_BARREL)
            .register();

    public static final BlockEntityEntry<RocketPodBlockEntity> ROCKET_POD_BARREL_BLOCK_ENTITY = REGISTRATE.blockEntity("rocket_pod_barrel_be", RocketPodBlockEntity::new)
            .validBlocks(BlockRegister.CAST_IRON_ROCKET_POD_BARREL, BlockRegister.BRONZE_ROCKET_POD_BARREL, BlockRegister.STEEL_ROCKET_POD_BARREL)
            .register();

    public static final BlockEntityEntry<RocketPodBreechBlockEntity> ROCKET_POD_BREECH_BLOCK_ENTITY = REGISTRATE.blockEntity("rocket_pod_breech_be", RocketPodBreechBlockEntity::new)
            .validBlocks(BlockRegister.CAST_IRON_ROCKET_POD_BREECH, BlockRegister.BRONZE_ROCKET_POD_BREECH, BlockRegister.STEEL_ROCKET_POD_BREECH)
            .register();

    public static final BlockEntityEntry<MediumRocketPodBlockEntity> MEDIUM_ROCKET_POD_BARREL_BLOCK_ENTITY = REGISTRATE.blockEntity("medium_rocket_rail_be", MediumRocketPodBlockEntity::new)
            .validBlocks(/*BlockRegister.CAST_IRON_MEDIUM_ROCKET_POD_BARREL, BlockRegister.BRONZE_MEDIUM_ROCKET_POD_BARREL, BlockRegister.STEEL_MEDIUM_ROCKET_POD_BARREL*/ BlockRegister.WROUGHT_IRON_MEDIUM_ROCKET_RAIL)
            .register();

    public static final BlockEntityEntry<MediumRocketPodBreechBlockEntity> MEDIUM_ROCKET_POD_BREECH_BLOCK_ENTITY = REGISTRATE.blockEntity("medium_rocket_rail_breech_be", MediumRocketPodBreechBlockEntity::new)
            .visual(() -> MediumRocketPodBreechInstance::new)
            //.renderer(() -> MediumRocketPodBreechRenderer::new)
            .validBlocks(/*BlockRegister.CAST_IRON_MEDIUM_ROCKET_POD_BREECH, BlockRegister.BRONZE_MEDIUM_ROCKET_POD_BREECH, BlockRegister.STEEL_MEDIUM_ROCKET_POD_BREECH*/ BlockRegister.WROUGHT_IRON_MEDIUM_ROCKET_RAIL_BREECH)
            .register();

    public static final BlockEntityEntry<ProjectileCartridgeBlockEntity> PROJECTILE_CARTRIDGE_BLOCK_ENTITY = REGISTRATE.blockEntity("projectile_cartridge_be", ProjectileCartridgeBlockEntity::new)
            .validBlocks(BlockRegister.ARMOR_PIERCING_CARTRIDGE_BLOCK, BlockRegister.ARMOR_PIERCING_SHELL_CARTRIDGE_BLOCK, BlockRegister.FLUID_SHELL_CARTRIDGE_BLOCK,
                    BlockRegister.GRAPESHOT_CARTRIDGE_BLOCK, BlockRegister.HIGH_EXPLOSIVE_CARTRIDGE_BLOCK, BlockRegister.SHRAPNEL_CARTRIDGE_BLOCK, BlockRegister.SMOKE_CARTRIDGE_BLOCK,
                    BlockRegister.SOLID_CARTRIDGE_BLOCK, BlockRegister.ARMOR_PIERCING_CASELESS_BLOCK, BlockRegister.ARMOR_PIERCING_SHELL_CASELESS_BLOCK,
                    BlockRegister.FLUID_SHELL_CASELESS_BLOCK, BlockRegister.GRAPESHOT_CASELESS_BLOCK, BlockRegister.HIGH_EXPLOSIVE_CASELESS_BLOCK, BlockRegister.SHRAPNEL_CASELESS_BLOCK,
                    BlockRegister.SMOKE_CASELESS_BLOCK, BlockRegister.SOLID_CASELESS_BLOCK)
            .register();

    public static final BlockEntityEntry<FluidShellBlockEntity> FLUID_SHELL_CARTRIDGE_BLOCK_ENTITY = REGISTRATE.blockEntity("fluid_shell_cartridge_be", FluidShellBlockEntity::new)
            .validBlocks(BlockRegister.FLUID_SHELL_CASELESS_BLOCK, BlockRegister.FLUID_SHELL_CARTRIDGE_BLOCK)
            .register();

    public static final BlockEntityEntry<TwinAutocannonBlockEntity> TWIN_AUTOCANNON_BLOCK_ENTITY = REGISTRATE.blockEntity("twin_autocannon_be", TwinAutocannonBlockEntity::new)
            .visual(() -> TwinAutocannonInstance::new)
            .validBlocks(BlockRegister.CAST_IRON_TWIN_AUTOCANNON_BARREL, BlockRegister.BRONZE_TWIN_AUTOCANNON_BARREL, BlockRegister.STEEL_TWIN_AUTOCANNON_BARREL,
                    BlockRegister.CAST_IRON_VERT_TWIN_AUTOCANNON_BARREL, BlockRegister.BRONZE_VERT_TWIN_AUTOCANNON_BARREL, BlockRegister.STEEL_VERT_TWIN_AUTOCANNON_BARREL,
                    BlockRegister.CAST_IRON_TWIN_AUTOCANNON_SILENCER, BlockRegister.BRONZE_TWIN_AUTOCANNON_SILENCER, BlockRegister.STEEL_TWIN_AUTOCANNON_SILENCER,
                    BlockRegister.CAST_IRON_VERT_TWIN_AUTOCANNON_SILENCER, BlockRegister.BRONZE_VERT_TWIN_AUTOCANNON_SILENCER, BlockRegister.STEEL_VERT_TWIN_AUTOCANNON_SILENCER,
                    BlockRegister.CAST_IRON_TWIN_AUTOCANNON_MUZZLE_BRAKE, BlockRegister.BRONZE_TWIN_AUTOCANNON_MUZZLE_BRAKE, BlockRegister.STEEL_TWIN_AUTOCANNON_MUZZLE_BRAKE,
                    BlockRegister.CAST_IRON_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE, BlockRegister.BRONZE_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE, BlockRegister.STEEL_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE)
            .register();

    public static final BlockEntityEntry<TwinAutocannonRecoilSpringBlockEntity> TWIN_AUTOCANNON_RECOIL_SPRING_BLOCK_ENTITY = REGISTRATE.blockEntity("twin_autocannon_recoil_spring_be", TwinAutocannonRecoilSpringBlockEntity::new)
            .visual(() -> TwinAutocannonInstance::new)
            .visual(() -> TwinAutocannonRecoilSpringInstance::new)
            .renderer(() -> TwinAutocannonRecoilSpringRenderer::new)
            .validBlocks(BlockRegister.CAST_IRON_TWIN_AUTOCANNON_RECOIL_SPRING, BlockRegister.BRONZE_TWIN_AUTOCANNON_RECOIL_SPRING, BlockRegister.STEEL_TWIN_AUTOCANNON_RECOIL_SPRING,
                    BlockRegister.CAST_IRON_VERT_TWIN_AUTOCANNON_RECOIL_SPRING, BlockRegister.BRONZE_VERT_TWIN_AUTOCANNON_RECOIL_SPRING, BlockRegister.STEEL_VERT_TWIN_AUTOCANNON_RECOIL_SPRING)
            .register();

    public static final BlockEntityEntry<TwinAutocannonBreechBlockEntity> TWIN_AUTOCANNON_BREECH_BLOCK_ENTITY = REGISTRATE.blockEntity("twin_autocannon_breech_be", TwinAutocannonBreechBlockEntity::new)
            .visual(() -> TwinAutocannonBreechInstance::new)
            .renderer(() -> TwinAutocannonBreechRenderer::new)
            .validBlocks(BlockRegister.CAST_IRON_TWIN_AUTOCANNON_BREECH, BlockRegister.BRONZE_TWIN_AUTOCANNON_BREECH, BlockRegister.STEEL_TWIN_AUTOCANNON_BREECH,
                    BlockRegister.CAST_IRON_VERT_TWIN_AUTOCANNON_BREECH, BlockRegister.BRONZE_VERT_TWIN_AUTOCANNON_BREECH, BlockRegister.STEEL_VERT_TWIN_AUTOCANNON_BREECH)
            .register();

    public static final BlockEntityEntry<HeavyAutocannonBlockEntity> HEAVY_AUTOCANNON_BLOCK_ENTITY = REGISTRATE.blockEntity("heavy_autocannon_be", HeavyAutocannonBlockEntity::new)
            .validBlocks(BlockRegister.CAST_IRON_HEAVY_AUTOCANNON_BARREL, BlockRegister.BRONZE_HEAVY_AUTOCANNON_BARREL, BlockRegister.STEEL_HEAVY_AUTOCANNON_BARREL,
                    BlockRegister.CAST_IRON_HEAVY_AUTOCANNON_MUZZLE_BRAKE, BlockRegister.BRONZE_HEAVY_AUTOCANNON_MUZZLE_BRAKE, BlockRegister.STEEL_HEAVY_AUTOCANNON_MUZZLE_BRAKE,
                    BlockRegister.CAST_IRON_HEAVY_AUTOCANNON_SILENCER, BlockRegister.BRONZE_HEAVY_AUTOCANNON_SILENCER, BlockRegister.STEEL_HEAVY_AUTOCANNON_SILENCER)
            .register();

    public static final BlockEntityEntry<HeavyAutocannonRecoilSpringBlockEntity> HEAVY_AUTOCANNON_RECOIL_SPRING_BLOCK_ENTITY = REGISTRATE.blockEntity("heavy_autocannon_recoil_spring_be", HeavyAutocannonRecoilSpringBlockEntity::new)
            .visual(() -> HeavyAutocannonRecoilSpringInstance::new)
            .renderer(() -> HeavyAutocannonRecoilSpringRenderer::new)
            .validBlocks(BlockRegister.CAST_IRON_HEAVY_AUTOCANNON_RECOIL_SPRING, BlockRegister.BRONZE_HEAVY_AUTOCANNON_RECOIL_SPRING, BlockRegister.STEEL_HEAVY_AUTOCANNON_RECOIL_SPRING)
            .register();

    public static final BlockEntityEntry<HeavyAutocannonBreechBlockEntity> HEAVY_AUTOCANNON_BREECH_BLOCK_ENTITY = REGISTRATE.blockEntity("heavy_autocannon_breech_be", HeavyAutocannonBreechBlockEntity::new)
            .visual(() -> HeavyAutocannonBreechInstance::new)
            .renderer(() -> HeavyAutocannonBreechRenderer::new)
            .validBlocks(BlockRegister.CAST_IRON_HEAVY_AUTOCANNON_BREECH, BlockRegister.BRONZE_HEAVY_AUTOCANNON_BREECH, BlockRegister.STEEL_HEAVY_AUTOCANNON_BREECH)
            .register();

    public static final BlockEntityEntry<HeavyAutocannonQuickFireBreechBlockEntity> HEAVY_AUTOCANNON_QFBREECH_BLOCK_ENTITY = REGISTRATE.blockEntity("heavy_autocannon_qf_breech_be", HeavyAutocannonQuickFireBreechBlockEntity::new)
            .visual(() -> HeavyAutocannonQuickFireBreechInstance::new)
            .renderer(() -> HeavyAutocannonQuickFireBreechRenderer::new)
            .validBlocks(BlockRegister.CAST_IRON_HEAVY_AUTOCANNON_QFBREECH, BlockRegister.BRONZE_HEAVY_AUTOCANNON_QFBREECH, BlockRegister.STEEL_HEAVY_AUTOCANNON_QFBREECH)
            .register();

    public static final BlockEntityEntry<HeavyAutocannonAmmoContainerBlockEntity> HEAVY_AUTOCANNON_AMMO_BOX_BLOCK_ENTITY = REGISTRATE.blockEntity("heavy_autocannon_ammo_box_be", HeavyAutocannonAmmoContainerBlockEntity::new)
            .validBlocks(BlockRegister.HEAVY_AUTOCANNON_AMMO_BOX, BlockRegister.CREATIVE_HEAVY_AUTOCANNON_AMMO_BOX)
            .register();
/*
    public static final BlockEntityEntry<RevolverBarrelBlockEntity> REVOLVER_BARREL_BLOCK_ENTITY = REGISTRATE.blockEntity("revolver_barrel_be", RevolverBarrelBlockEntity::new)
            .renderer(() -> RevolverBarrelRenderer::new)
            .validBlocks(BlockRegister.CAST_IRON_REVOLVER_BARREL, BlockRegister.BRONZE_REVOLVER_BARREL, BlockRegister.STEEL_REVOLVER_BARREL)
            .register();

    public static final BlockEntityEntry<RevolverDrumBlockEntity> REVOLVER_DRUM_BLOCK_ENTITY = REGISTRATE.blockEntity("revolver_drum_be", RevolverDrumBlockEntity::new)
            .validBlocks(BlockRegister.REVOLVER_DRUM)
            .register();*/

    public static final BlockEntityEntry<FuzedClusterProjectileBlockEntity> FUZED_CLUSTER_PROJECTILE_BLOCK_ENTITY = REGISTRATE.blockEntity("fuzed_cluster_be", FuzedClusterProjectileBlockEntity::new)
            .validBlocks(BlockRegister.CLUSTER_BLOCK, BlockRegister.CLUSTER_CASELESS_BLOCK, BlockRegister.CLUSTER_CARTRIDGE_BLOCK)
            .register();

    public static void register() {}
}
