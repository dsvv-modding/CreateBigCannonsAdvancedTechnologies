package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology;
import com.dsvv.cbcat.cannon.FumeExtractorBlock;
import com.dsvv.cbcat.cannon.MuzzleBrakeBlock;
import com.dsvv.cbcat.cannon.RifledBarrelBlock;
import com.dsvv.cbcat.cannon.SilencerBlock;
import com.dsvv.cbcat.cannon.autocannon.SpecialAutocannonBarrel;
import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBarrelBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.HeavyAutocannonBlockItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.breech.HeavyAutocannonBreechBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.qf_breech.HeavyAutocannonQuickFireBreechBlock;
import com.dsvv.cbcat.cannon.heavy_autocannon.recoil_spring.HeavyAutocannonRecoilSpringBlock;
import com.dsvv.cbcat.cannon.medium_rocketpod.MediumRocketPodBarrelBlock;
import com.dsvv.cbcat.cannon.medium_rocketpod.breech.MediumRocketPodBreechBlock;
import com.dsvv.cbcat.cannon.rocketpod.RocketPodBarrelBlock;
import com.dsvv.cbcat.cannon.rocketpod.breech.RocketPodBreechBlock;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBarrelBlock;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBaseBlock;
import com.dsvv.cbcat.cannon.twin_autocannon.TwinAutocannonBlockItem;
import com.dsvv.cbcat.cannon.twin_autocannon.breech.TwinAutocannonBreechBlock;
import com.dsvv.cbcat.cannon.twin_autocannon.recoil_spring.TwinAutocannonRecoilSpringBlock;
import com.dsvv.cbcat.cartridge.*;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.cluster_munition.FuzedClusterProjectileBlock;
import com.dsvv.cbcat.cluster_munition.FuzedClusterProjectileBlockItem;
import com.simibubi.create.AllTags;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBarrelBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlockItem;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlockItem;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastMouldBlock;
import rbasamoyai.createbigcannons.index.*;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidShellBlockItem;

import static com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology.REGISTRATE;

public class BlockRegister
{
    public static final BlockEntry<MuzzleBrakeBlock> CAST_IRON_MUZZLE_BRAKE = REGISTRATE.block("cast_iron_muzzle_brake", p -> new MuzzleBrakeBlock(p, CBCBigCannonMaterials.CAST_IRON))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MuzzleBrakeBlock> BRONZE_MUZZLE_BRAKE = REGISTRATE.block("bronze_muzzle_brake", p -> new MuzzleBrakeBlock(p, CBCBigCannonMaterials.BRONZE))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MuzzleBrakeBlock> STEEL_MUZZLE_BRAKE = REGISTRATE.block("steel_muzzle_brake", p -> new MuzzleBrakeBlock(p, CBCBigCannonMaterials.STEEL))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();
    public static final BlockEntry<MuzzleBrakeBlock> NETHERSTEEL_MUZZLE_BRAKE = REGISTRATE.block("nethersteel_muzzle_brake", p -> new MuzzleBrakeBlock(p, CBCBigCannonMaterials.NETHERSTEEL))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();

    public static final BlockEntry<FumeExtractorBlock> CAST_IRON_FUME_EXTRACTOR = REGISTRATE.block("cast_iron_fume_extractor", p -> new FumeExtractorBlock(p, CBCBigCannonMaterials.CAST_IRON))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<FumeExtractorBlock> BRONZE_FUME_EXTRACTOR = REGISTRATE.block("bronze_fume_extractor", p -> new FumeExtractorBlock(p, CBCBigCannonMaterials.BRONZE))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<FumeExtractorBlock> STEEL_FUME_EXTRACTOR = REGISTRATE.block("steel_fume_extractor", p -> new FumeExtractorBlock(p, CBCBigCannonMaterials.STEEL))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();
    public static final BlockEntry<FumeExtractorBlock> NETHERSTEEL_FUME_EXTRACTOR = REGISTRATE.block("nethersteel_fume_extractor", p -> new FumeExtractorBlock(p, CBCBigCannonMaterials.NETHERSTEEL))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();

    public static final BlockEntry<SilencerBlock> CAST_IRON_SILENCER = REGISTRATE.block("cast_iron_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.CAST_IRON))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<SilencerBlock> BRONZE_SILENCER = REGISTRATE.block("bronze_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.BRONZE))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<SilencerBlock> STEEL_SILENCER = REGISTRATE.block("steel_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.STEEL))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();
    public static final BlockEntry<SilencerBlock> NETHERSTEEL_SILENCER = REGISTRATE.block("nethersteel_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.NETHERSTEEL))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();

    public static final BlockEntry<SilencerBlock> BUILT_UP_STEEL_SILENCER = REGISTRATE.block("built_up_steel_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.STEEL, new boolean[]{true}))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();
    public static final BlockEntry<SilencerBlock> BUILT_UP_NETHERSTEEL_SILENCER = REGISTRATE.block("built_up_nethersteel_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.NETHERSTEEL, new boolean[]{true}))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();

    public static final BlockEntry<RifledBarrelBlock> CAST_IRON_RIFLED_BARREL = REGISTRATE.block("cast_iron_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.CAST_IRON))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<RifledBarrelBlock> BRONZE_RIFLED_BARREL = REGISTRATE.block("bronze_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.BRONZE))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<RifledBarrelBlock> STEEL_RIFLED_BARREL = REGISTRATE.block("steel_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.STEEL))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();
    public static final BlockEntry<RifledBarrelBlock> NETHERSTEEL_RIFLED_BARREL = REGISTRATE.block("nethersteel_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.NETHERSTEEL))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();

    public static final BlockEntry<RifledBarrelBlock> BUILT_UP_STEEL_RIFLED_BARREL = REGISTRATE.block("built_up_steel_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.STEEL, new boolean[]{true}))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();
    public static final BlockEntry<RifledBarrelBlock> BUILT_UP_NETHERSTEEL_RIFLED_BARREL = REGISTRATE.block("built_up_nethersteel_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.NETHERSTEEL, new boolean[]{true}))
            .item(BigCannonBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock())
            .register();

    public static final BlockEntry<RocketPodBarrelBlock> CAST_IRON_ROCKET_POD_BARREL = REGISTRATE.block("cast_iron_rocket_pod_rail", p -> new RocketPodBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<RocketPodBarrelBlock> BRONZE_ROCKET_POD_BARREL = REGISTRATE.block("bronze_rocket_pod_rail", p -> new RocketPodBarrelBlock(p, CBCAutocannonMaterials.BRONZE))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<RocketPodBarrelBlock> STEEL_ROCKET_POD_BARREL = REGISTRATE.block("steel_rocket_pod_rail", p -> new RocketPodBarrelBlock(p, CBCAutocannonMaterials.STEEL))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<RocketPodBreechBlock> CAST_IRON_ROCKET_POD_BREECH = REGISTRATE.block("cast_iron_rocket_pod_breech", p -> new RocketPodBreechBlock(p, CBCAutocannonMaterials.CAST_IRON))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<RocketPodBreechBlock> BRONZE_ROCKET_POD_BREECH = REGISTRATE.block("bronze_rocket_pod_breech", p -> new RocketPodBreechBlock(p, CBCAutocannonMaterials.BRONZE))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<RocketPodBreechBlock> STEEL_ROCKET_POD_BREECH = REGISTRATE.block("steel_rocket_pod_breech", p -> new RocketPodBreechBlock(p, CBCAutocannonMaterials.STEEL))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();

    /*public static final BlockEntry<MediumRocketPodBarrelBlock> CAST_IRON_MEDIUM_ROCKET_POD_BARREL = REGISTRATE.block("cast_iron_medium_rocket_pod_rail", p -> new MediumRocketPodBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBarrelBlock> BRONZE_MEDIUM_ROCKET_POD_BARREL = REGISTRATE.block("bronze_medium_rocket_pod_rail", p -> new MediumRocketPodBarrelBlock(p, CBCAutocannonMaterials.BRONZE))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBarrelBlock> STEEL_MEDIUM_ROCKET_POD_BARREL = REGISTRATE.block("steel_medium_rocket_pod_rail", p -> new MediumRocketPodBarrelBlock(p, CBCAutocannonMaterials.STEEL))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBreechBlock> CAST_IRON_MEDIUM_ROCKET_POD_BREECH = REGISTRATE.block("cast_iron_medium_rocket_pod_breech", p -> new MediumRocketPodBreechBlock(p, CBCAutocannonMaterials.CAST_IRON))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBreechBlock> BRONZE_MEDIUM_ROCKET_POD_BREECH = REGISTRATE.block("bronze_medium_rocket_pod_breech", p -> new MediumRocketPodBreechBlock(p, CBCAutocannonMaterials.BRONZE))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBreechBlock> STEEL_MEDIUM_ROCKET_POD_BREECH = REGISTRATE.block("steel_medium_rocket_pod_breech", p -> new MediumRocketPodBreechBlock(p, CBCAutocannonMaterials.STEEL))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();*/
    public static final BlockEntry<MediumRocketPodBarrelBlock> WROUGHT_IRON_MEDIUM_ROCKET_RAIL = REGISTRATE.block("wrought_iron_medium_rocket_rail", p -> new MediumRocketPodBarrelBlock(p, CBCAutocannonMaterials.STEEL))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBreechBlock> WROUGHT_IRON_MEDIUM_ROCKET_RAIL_BREECH = REGISTRATE.block("wrought_iron_medium_rocket_rail_breech", p -> new MediumRocketPodBreechBlock(p, CBCAutocannonMaterials.STEEL))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
/*
    public static final BlockEntry<RevolverBarrelBlock> CAST_IRON_REVOLVER_BARREL = REGISTRATE.block("cast_iron_revolver_barrel", p -> new RevolverBarrelBlock(p, CBCBigCannonMaterials.CAST_IRON))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())

            .register();
    public static final BlockEntry<RevolverBarrelBlock> BRONZE_REVOLVER_BARREL = REGISTRATE.block("bronze_revolver_barrel", p -> new RevolverBarrelBlock(p, CBCBigCannonMaterials.BRONZE))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<RevolverBarrelBlock> STEEL_REVOLVER_BARREL = REGISTRATE.block("steel_revolver_barrel", p -> new RevolverBarrelBlock(p, CBCBigCannonMaterials.STEEL))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();

    public static final BlockEntry<RevolverDrumBlock> REVOLVER_DRUM = REGISTRATE.block("revolver_drum", RevolverDrumBlock::new)
            .item(RevolverDrumItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .register();*/

    public static final BlockEntry<ProjectileCartridgeBlock> ARMOR_PIERCING_CARTRIDGE_BLOCK = REGISTRATE.block("armor_piercing_cartridge", p -> new ProjectileCartridgeBlock(p, CBCEntityTypes.AP_SHOT, "ap shot"))
            .item(ProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FuzedProjectileCartridgeBlock> ARMOR_PIERCING_SHELL_CARTRIDGE_BLOCK = REGISTRATE.block("armor_piercing_shell_cartridge", p -> new FuzedProjectileCartridgeBlock(p, CBCEntityTypes.AP_SHELL, "ap shell"))
            .item(FuzedProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FluidProjectileCartridgeBlock> FLUID_SHELL_CARTRIDGE_BLOCK = REGISTRATE.block("fluid_shell_cartridge", p -> new FluidProjectileCartridgeBlock(p, "fluid shell"))
            .item(FluidProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<ProjectileCartridgeBlock> GRAPESHOT_CARTRIDGE_BLOCK = REGISTRATE.block("grapeshot_cartridge", p -> new ProjectileCartridgeBlock(p, CBCEntityTypes.BAG_OF_GRAPESHOT, "grapeshot"))
            .item(ProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FuzedProjectileCartridgeBlock> HIGH_EXPLOSIVE_CARTRIDGE_BLOCK = REGISTRATE.block("high_explosive_cartridge", p -> new FuzedProjectileCartridgeBlock(p, CBCEntityTypes.HE_SHELL, "he shell"))
            .item(FuzedProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FuzedProjectileCartridgeBlock> SHRAPNEL_CARTRIDGE_BLOCK = REGISTRATE.block("shrapnel_cartridge", p -> new FuzedProjectileCartridgeBlock(p, CBCEntityTypes.SHRAPNEL_SHELL, "shrapnel shell"))
            .item(FuzedProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FuzedProjectileCartridgeBlock> SMOKE_CARTRIDGE_BLOCK = REGISTRATE.block("smoke_cartridge", p -> new FuzedProjectileCartridgeBlock(p, CBCEntityTypes.SMOKE_SHELL, "smoke shell"))
            .item(FuzedProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<ProjectileCartridgeBlock> SOLID_CARTRIDGE_BLOCK = REGISTRATE.block("solid_cartridge", p -> new ProjectileCartridgeBlock(p, CBCEntityTypes.SHOT, "solid shot"))
            .item(ProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<ClusterProjectileCartridgeBlock> CLUSTER_CARTRIDGE_BLOCK = REGISTRATE.block("cluster_cartridge", p -> new ClusterProjectileCartridgeBlock(p, "cluster shell", true))
            .item(FuzedClusterProjectileBlockItem::new).build()///*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();

    public static final BlockEntry<ProjectileCartridgeBlock> ARMOR_PIERCING_CASELESS_BLOCK = REGISTRATE.block("armor_piercing_caseless", p -> new ProjectileCartridgeBlock(p, CBCEntityTypes.AP_SHOT, "ap shot caseless", false))
            .item(ProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FuzedProjectileCartridgeBlock> ARMOR_PIERCING_SHELL_CASELESS_BLOCK = REGISTRATE.block("armor_piercing_shell_caseless", p -> new FuzedProjectileCartridgeBlock(p, CBCEntityTypes.AP_SHELL, "ap shell caseless", false))
            .item(FuzedProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FluidProjectileCartridgeBlock> FLUID_SHELL_CASELESS_BLOCK = REGISTRATE.block("fluid_shell_caseless", p -> new FluidProjectileCartridgeBlock(p, "fluid shell caseless", false))
            .item(FluidProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<ProjectileCartridgeBlock> GRAPESHOT_CASELESS_BLOCK = REGISTRATE.block("grapeshot_caseless", p -> new ProjectileCartridgeBlock(p, CBCEntityTypes.BAG_OF_GRAPESHOT, "grapeshot caseless", false))
            .item(ProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FuzedProjectileCartridgeBlock> HIGH_EXPLOSIVE_CASELESS_BLOCK = REGISTRATE.block("high_explosive_caseless", p -> new FuzedProjectileCartridgeBlock(p, CBCEntityTypes.HE_SHELL, "he shell caseless", false))
            .item(FuzedProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FuzedProjectileCartridgeBlock> SHRAPNEL_CASELESS_BLOCK = REGISTRATE.block("shrapnel_caseless", p -> new FuzedProjectileCartridgeBlock(p, CBCEntityTypes.SHRAPNEL_SHELL, "shrapnel shell caseless", false))
            .item(FuzedProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<FuzedProjectileCartridgeBlock> SMOKE_CASELESS_BLOCK = REGISTRATE.block("smoke_caseless", p -> new FuzedProjectileCartridgeBlock(p, CBCEntityTypes.SMOKE_SHELL, "smoke shell caseless", false))
            .item(FuzedProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<ProjectileCartridgeBlock> SOLID_CASELESS_BLOCK = REGISTRATE.block("solid_caseless", p -> new ProjectileCartridgeBlock(p, CBCEntityTypes.SHOT, "solid shot caseless", false))
            .item(ProjectileCartridgeBlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();
    public static final BlockEntry<ClusterProjectileCartridgeBlock> CLUSTER_CASELESS_BLOCK = REGISTRATE.block("cluster_caseless", p -> new ClusterProjectileCartridgeBlock(p, "cluster shell caseless", false))
            .item(FuzedClusterProjectileBlockItem::new).build()///*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();

    public static final BlockEntry<FuzedClusterProjectileBlock> CLUSTER_BLOCK = REGISTRATE.block("cluster", p -> new FuzedClusterProjectileBlock(p))
            .item(FuzedClusterProjectileBlockItem::new).build()///*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .register();

    public static final BlockEntry<TwinAutocannonBarrelBlock> CAST_IRON_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("cast_iron_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::getCastIronBarrel, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> BRONZE_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("bronze_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::getBronzeBarrel, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> STEEL_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("steel_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::getSteelBarrel, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> CAST_IRON_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("cast_iron_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::castIronTwinAutocannonBarrel, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> BRONZE_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("bronze_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::bronzeTwinAutocannonBarrel, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> STEEL_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("steel_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::steelTwinAutocannonBarrel, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> CAST_IRON_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("cast_iron_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> BRONZE_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("bronze_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> STEEL_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("steel_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> CAST_IRON_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("cast_iron_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::getCastIronSilencer, false, 0.5f))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> BRONZE_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("bronze_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::getBronzeSilencer, false, 0.5f))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> STEEL_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("steel_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::getSteelSilencer, false, 0.5f))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> CAST_IRON_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("cast_iron_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::getCastIronMuzzleBrake, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> BRONZE_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("bronze_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::getBronzeMuzzleBrake, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> STEEL_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("steel_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::getSteelMuzzleBrake, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();

    public static final BlockEntry<TwinAutocannonBarrelBlock> CAST_IRON_VERT_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("cast_iron_vert_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::getCastIronBarrel, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> BRONZE_VERT_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("bronze_vert_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::getBronzeBarrel, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> STEEL_VERT_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("steel_vert_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::getSteelBarrel, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> CAST_IRON_VERT_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("cast_iron_vert_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::castIronVertTwinAutocannonBarrel, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> BRONZE_VERT_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("bronze_vert_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::bronzeVertTwinAutocannonBarrel, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> STEEL_VERT_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("steel_vert_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::steelVertTwinAutocannonBarrel, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> CAST_IRON_VERT_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("cast_iron_vert_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.CAST_IRON, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> BRONZE_VERT_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("bronze_vert_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.BRONZE, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> STEEL_VERT_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("steel_vert_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.STEEL, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> CAST_IRON_VERT_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("cast_iron_vert_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::getCastIronSilencer, true, 0.5f))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> BRONZE_VERT_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("bronze_vert_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::getBronzeSilencer, true, 0.5f))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> STEEL_VERT_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("steel_vert_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::getSteelSilencer, true, 0.5f))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> CAST_IRON_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("cast_iron_vert_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::getCastIronMuzzleBrake, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> BRONZE_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("bronze_vert_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::getBronzeMuzzleBrake, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> STEEL_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("steel_vert_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::getSteelMuzzleBrake, true))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();

    public static final BlockEntry<SpecialAutocannonBarrel> CAST_IRON_AUTOCANNON_SILENCER = REGISTRATE.block("cast_iron_autocannon_silencer", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.CAST_IRON, 0.5f))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> BRONZE_AUTOCANNON_SILENCER = REGISTRATE.block("bronze_autocannon_silencer", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.BRONZE, 0.5f))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> STEEL_AUTOCANNON_SILENCER = REGISTRATE.block("steel_autocannon_silencer", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.STEEL, 0.5f))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> CAST_IRON_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("cast_iron_autocannon_muzzle_brake", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.CAST_IRON))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> BRONZE_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("bronze_autocannon_muzzle_brake", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.BRONZE))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> STEEL_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("steel_autocannon_muzzle_brake", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.STEEL))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();

    public static final BlockEntry<HeavyAutocannonBarrelBlock> CAST_IRON_HEAVY_AUTOCANNON_BARREL = REGISTRATE.block("cast_iron_heavy_autocannon_barrel", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> BRONZE_HEAVY_AUTOCANNON_BARREL = REGISTRATE.block("bronze_heavy_autocannon_barrel", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> STEEL_HEAVY_AUTOCANNON_BARREL = REGISTRATE.block("steel_heavy_autocannon_barrel", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    /*public static final BlockEntry<HeavyAutocannonBarrelBlock> CAST_IRON_HEAVY_AUTOCANNON_CHAMBER = REGISTRATE.block("cast_iron_heavy_autocannon_chamber", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, true))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> BRONZE_HEAVY_AUTOCANNON_CHAMBER = REGISTRATE.block("bronze_heavy_autocannon_chamber", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, true))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> STEEL_HEAVY_AUTOCANNON_CHAMBER = REGISTRATE.block("steel_heavy_autocannon_chamber", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, true))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();*/
    public static final BlockEntry<HeavyAutocannonRecoilSpringBlock> CAST_IRON_HEAVY_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("cast_iron_heavy_autocannon_recoil_spring", p -> new HeavyAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::castIronHeavyAutocannonBarrel))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonRecoilSpringBlock> BRONZE_HEAVY_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("bronze_heavy_autocannon_recoil_spring", p -> new HeavyAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::bronzeHeavyAutocannonBarrel))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonRecoilSpringBlock> STEEL_HEAVY_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("steel_heavy_autocannon_recoil_spring", p -> new HeavyAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::steelHeavyAutocannonBarrel))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> CAST_IRON_HEAVY_AUTOCANNON_BREECH = REGISTRATE.block("cast_iron_heavy_autocannon_breech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.CAST_IRON))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> BRONZE_HEAVY_AUTOCANNON_BREECH = REGISTRATE.block("bronze_heavy_autocannon_breech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.BRONZE))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> STEEL_HEAVY_AUTOCANNON_BREECH = REGISTRATE.block("steel_heavy_autocannon_breech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.STEEL))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonQuickFireBreechBlock> CAST_IRON_HEAVY_AUTOCANNON_QFBREECH = REGISTRATE.block("cast_iron_heavy_autocannon_qfbreech", p -> new HeavyAutocannonQuickFireBreechBlock(p, CBCAutocannonMaterials.CAST_IRON))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonQuickFireBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonQuickFireBreechBlock> BRONZE_HEAVY_AUTOCANNON_QFBREECH = REGISTRATE.block("bronze_heavy_autocannon_qfbreech", p -> new HeavyAutocannonQuickFireBreechBlock(p, CBCAutocannonMaterials.BRONZE))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonQuickFireBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonQuickFireBreechBlock> STEEL_HEAVY_AUTOCANNON_QFBREECH = REGISTRATE.block("steel_heavy_autocannon_qfbreech", p -> new HeavyAutocannonQuickFireBreechBlock(p, CBCAutocannonMaterials.STEEL))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonQuickFireBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> CAST_IRON_HEAVY_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("cast_iron_heavy_autocannon_muzzle_brake", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> BRONZE_HEAVY_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("bronze_heavy_autocannon_muzzle_brake", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> STEEL_HEAVY_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("steel_heavy_autocannon_muzzle_brake", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> CAST_IRON_HEAVY_AUTOCANNON_SILENCER = REGISTRATE.block("cast_iron_heavy_autocannon_silencer", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false, 0.6f))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> BRONZE_HEAVY_AUTOCANNON_SILENCER = REGISTRATE.block("bronze_heavy_autocannon_silencer", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false, 0.6f))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> STEEL_HEAVY_AUTOCANNON_SILENCER = REGISTRATE.block("steel_heavy_autocannon_silencer", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false, 0.6f))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();

    public static final BlockEntry<HeavyAutocannonAmmoContainerBlock> HEAVY_AUTOCANNON_AMMO_BOX = REGISTRATE.block("heavy_autocannon_ammo_box", HeavyAutocannonAmmoContainerBlock::new)
            //.item(HeavyAutocannonAmmoContainerItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .properties(p -> p.mapColor(MapColor.COLOR_GREEN))
            .properties(p -> p.strength(0.0f, 2.5f))
            .properties(p -> p.sound(SoundType.CHAIN))
            .properties(p -> p.noOcclusion())
            .transform(autocannonAmmoContainer(false))
            .register();
    public static final BlockEntry<HeavyAutocannonAmmoContainerBlock> CREATIVE_HEAVY_AUTOCANNON_AMMO_BOX = REGISTRATE.block("creative_heavy_autocannon_ammo_box", HeavyAutocannonAmmoContainerBlock::new)
            //.item(HeavyAutocannonAmmoContainerItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .properties(p -> p.mapColor(MapColor.COLOR_MAGENTA))
            .properties(p -> p.strength(0.0f, 2.5f))
            .properties(p -> p.sound(SoundType.CHAIN))
            .properties(p -> p.noOcclusion())
            .transform(autocannonAmmoContainer(true))
            .register();

    public static final BlockEntry<CannonCastMouldBlock> MUZZLE_BRAKE_MOULD = REGISTRATE.block("muzzle_brake_cast_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(2, 0, 2, 14, 12, 14), Block.box(1, 2, 1, 15, 16, 15)), () -> CannonCastingShapes.MUZZLE_BRAKE))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("muzzle_brake"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> FUME_EXTRACTOR_MOULD = REGISTRATE.block("fume_extractor_cast_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(2, 0, 2, 14, 16, 14), Block.box(1.5f, 2, 1.5f, 14.5, 14, 14.5f), Block.box(1, 2.5, 1, 15, 13.5f, 15)), () -> CannonCastingShapes.FUME_EXTRACTOR))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("fume_extractor"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> SILENCER_MOULD = REGISTRATE.block("silencer_cast_mould", p -> new CannonCastMouldBlock(p, Block.box(2, 0, 2, 14, 16, 14), () -> CannonCastingShapes.SILENCER))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("silencer"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> RIFLED_BARREL_MOULD = REGISTRATE.block("rifled_barrel_cast_mould", p -> new CannonCastMouldBlock(p, Block.box(2, 0, 2, 14, 16, 14), () -> CannonCastingShapes.RIFLED_BARREL))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("rifled_barrel"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> TWIN_AUTOCANNON_BARREL_MOULD = REGISTRATE.block("twin_autocannon_barrel_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(2, 0, 6, 6, 16, 10), Block.box(10, 0, 6, 14, 16, 10)), () -> CannonCastingShapes.TWIN_AUTOCANNON_BARREL))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("twin_autocannon_barrel"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> TWIN_AUTOCANNON_RECOIL_SPRING_MOULD = REGISTRATE.block("twin_autocannon_recoil_spring_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(1, 0, 5, 7, 16, 11), Block.box(9, 0, 5, 15, 16, 11)), () -> CannonCastingShapes.TWIN_AUTOCANNON_RECOIL_SPRING))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("twin_autocannon_recoil_spring"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> TWIN_AUTOCANNON_BREECH_MOULD = REGISTRATE.block("twin_autocannon_breech_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(8, 0, 4, 16, 16, 12), Block.box(0, 0, 4, 8, 16, 12)), () -> CannonCastingShapes.TWIN_AUTOCANNON_BREECH))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("twin_autocannon_breech"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> TWIN_AUTOCANNON_SILENCER_MOULD = REGISTRATE.block("twin_autocannon_silencer_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(1, 0, 5, 7, 16, 11), Block.box(9, 0, 5, 15, 16, 11)), () -> CannonCastingShapes.TWIN_AUTOCANNON_SILENCER))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("twin_autocannon_silencer"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> TWIN_AUTOCANNON_MUZZLE_BRAKE_MOULD = REGISTRATE.block("twin_autocannon_muzzle_brake_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(2, 0, 6, 6, 16, 10), Block.box(10, 0, 6, 14, 16, 10)), () -> CannonCastingShapes.TWIN_AUTOCANNON_MUZZLE_BRAKE))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("twin_autocannon_muzzle_brake"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> VERT_TWIN_AUTOCANNON_BARREL_MOULD = REGISTRATE.block("vert_twin_autocannon_barrel_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(6, 0, 2, 10, 16, 6), Block.box(6, 0, 10, 10, 16, 14)), () -> CannonCastingShapes.VERT_TWIN_AUTOCANNON_BARREL))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("vert_twin_autocannon_barrel"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> VERT_TWIN_AUTOCANNON_RECOIL_SPRING_MOULD = REGISTRATE.block("vert_twin_autocannon_recoil_spring_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(5, 0, 1, 11, 16, 7), Block.box(5, 0, 9, 11, 16, 15)), () -> CannonCastingShapes.VERT_TWIN_AUTOCANNON_RECOIL_SPRING))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("vert_twin_autocannon_recoil_spring"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> VERT_TWIN_AUTOCANNON_BREECH_MOULD = REGISTRATE.block("vert_twin_autocannon_breech_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(4, 0, 8, 12, 16, 16), Block.box(4, 0, 0, 12, 16, 8)), () -> CannonCastingShapes.VERT_TWIN_AUTOCANNON_BREECH))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("vert_twin_autocannon_breech"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> VERT_TWIN_AUTOCANNON_SILENCER_MOULD = REGISTRATE.block("vert_twin_autocannon_silencer_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(5, 0, 1, 11, 16, 7), Block.box(5, 0, 9, 11, 16, 15)), () -> CannonCastingShapes.VERT_TWIN_AUTOCANNON_SILENCER))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("vert_twin_autocannon_silencer"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE_MOULD = REGISTRATE.block("vert_twin_autocannon_muzzle_brake_mould", p -> new CannonCastMouldBlock(p, Shapes.or(Block.box(6, 0, 2, 10, 16, 6), Block.box(6, 0, 10, 10, 16, 14)), () -> CannonCastingShapes.VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("vert_twin_autocannon_muzzle_brake"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> AUTOCANNON_SILENCER_MOULD = REGISTRATE.block("autocannon_silencer_mould", p -> new CannonCastMouldBlock(p, Block.box(5, 0, 5, 11, 16, 11), () -> CannonCastingShapes.AUTOCANNON_SILENCER))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("autocannon_silencer"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> AUTOCANNON_MUZZLE_BRAKE_MOULD = REGISTRATE.block("autocannon_muzzle_brake_mould", p -> new CannonCastMouldBlock(p, Block.box(6, 0, 6, 10, 16, 10), () -> CannonCastingShapes.AUTOCANNON_MUZZLE_BRAKE))
            //.item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(castMould("autocannon_muzzle_brake"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> ROCKET_POD_RAIL_MOULD = REGISTRATE.block("rocket_pod_rail_mould", p -> new CannonCastMouldBlock(p, Block.box(0, 0, 3, 16, 16, 13), () -> CannonCastingShapes.ROCKET_POD_RAIL))
            .transform(castMould("rocket_pod_rail"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> ROCKET_POD_BREECH_MOULD = REGISTRATE.block("rocket_pod_breech_mould", p -> new CannonCastMouldBlock(p, Block.box(0, 0, 3, 16, 16, 13), () -> CannonCastingShapes.ROCKET_POD_BREECH))
            .transform(castMould("rocket_pod_breech"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> HEAVY_AUTOCANNON_BARREL_MOULD = REGISTRATE.block("heavy_autocannon_barrel_mould", p -> new CannonCastMouldBlock(p, Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875), () -> CannonCastingShapes.HEAVY_AUTOCANNON_BARREL))
            .transform(castMould("heavy_autocannon_barrel"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> HEAVY_AUTOCANNON_MUZZLE_BRAKE_MOULD = REGISTRATE.block("heavy_autocannon_muzzle_brake_mould", p -> new CannonCastMouldBlock(p, Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.687575), () -> CannonCastingShapes.HEAVY_AUTOCANNON_MUZZLE_BRAKE))
            .transform(castMould("heavy_autocannon_muzzle_brake"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> HEAVY_AUTOCANNON_SILENCER_MOULD = REGISTRATE.block("heavy_autocannon_silencer_mould", p -> new CannonCastMouldBlock(p, Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75), () -> CannonCastingShapes.HEAVY_AUTOCANNON_SILENCER))
            .transform(castMould("heavy_autocannon_silencer"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> HEAVY_AUTOCANNON_RECOIL_SPRING_MOULD = REGISTRATE.block("heavy_autocannon_recoil_spring_mould", p -> new CannonCastMouldBlock(p, Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75), () -> CannonCastingShapes.HEAVY_AUTOCANNON_RECOIL_SPRING))
            .transform(castMould("heavy_autocannon_recoil_spring"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> HEAVY_AUTOCANNON_BREECH_MOULD = REGISTRATE.block("heavy_autocannon_breech_mould", p -> new CannonCastMouldBlock(p, Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125), () -> CannonCastingShapes.HEAVY_AUTOCANNON_BREECH))
            .transform(castMould("heavy_autocannon_breech"))
            .register();
    public static final BlockEntry<CannonCastMouldBlock> HEAVY_AUTOCANNON_QFBREECH_MOULD = REGISTRATE.block("heavy_autocannon_qfbreech_mould", p -> new CannonCastMouldBlock(p, Shapes.box(0.1875, 0, 0.1875, 0.8125, 1, 0.8125), () -> CannonCastingShapes.HEAVY_AUTOCANNON_QFBREECH))
            .transform(castMould("heavy_autocannon_qfbreech"))
            .register();

    public static final BlockEntry<MuzzleBrakeBlock> UNBORED_CAST_IRON_MUZZLE_BRAKE = REGISTRATE.block("unbored_cast_iron_muzzle_brake", p -> new MuzzleBrakeBlock(p, CBCBigCannonMaterials.CAST_IRON, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<MuzzleBrakeBlock> UNBORED_BRONZE_MUZZLE_BRAKE = REGISTRATE.block("unbored_bronze_muzzle_brake", p -> new MuzzleBrakeBlock(p, CBCBigCannonMaterials.BRONZE, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<MuzzleBrakeBlock> UNBORED_STEEL_MUZZLE_BRAKE = REGISTRATE.block("unbored_steel_muzzle_brake", p -> new MuzzleBrakeBlock(p, CBCBigCannonMaterials.STEEL, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock(false))
            .register();
    public static final BlockEntry<MuzzleBrakeBlock> UNBORED_NETHERSTEEL_MUZZLE_BRAKE = REGISTRATE.block("unbored_nethersteel_muzzle_brake", p -> new MuzzleBrakeBlock(p, CBCBigCannonMaterials.NETHERSTEEL, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock(false))
            .register();

    public static final BlockEntry<FumeExtractorBlock> UNBORED_CAST_IRON_FUME_EXTRACTOR = REGISTRATE.block("unbored_cast_iron_fume_extractor", p -> new FumeExtractorBlock(p, CBCBigCannonMaterials.CAST_IRON, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<FumeExtractorBlock> UNBORED_BRONZE_FUME_EXTRACTOR = REGISTRATE.block("unbored_bronze_fume_extractor", p -> new FumeExtractorBlock(p, CBCBigCannonMaterials.BRONZE, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<FumeExtractorBlock> UNBORED_STEEL_FUME_EXTRACTOR = REGISTRATE.block("unbored_steel_fume_extractor", p -> new FumeExtractorBlock(p, CBCBigCannonMaterials.STEEL, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock(false))
            .register();
    public static final BlockEntry<FumeExtractorBlock> UNBORED_NETHERSTEEL_FUME_EXTRACTOR = REGISTRATE.block("unbored_nethersteel_fume_extractor", p -> new FumeExtractorBlock(p, CBCBigCannonMaterials.NETHERSTEEL, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock(false))
            .register();

    public static final BlockEntry<SilencerBlock> UNBORED_CAST_IRON_SILENCER = REGISTRATE.block("unbored_cast_iron_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.CAST_IRON, false, new boolean[]{false}))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<SilencerBlock> UNBORED_BRONZE_SILENCER = REGISTRATE.block("unbored_bronze_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.BRONZE, false, new boolean[]{false}))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<SilencerBlock> UNBORED_STEEL_SILENCER = REGISTRATE.block("unbored_steel_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.STEEL, false, new boolean[]{false}))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock(false))
            .register();
    public static final BlockEntry<SilencerBlock> UNBORED_NETHERSTEEL_SILENCER = REGISTRATE.block("unbored_nethersteel_silencer", p -> new SilencerBlock(p, CBCBigCannonMaterials.NETHERSTEEL, false, new boolean[]{false}))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock(false))
            .register();

    public static final BlockEntry<RifledBarrelBlock> UNBORED_CAST_IRON_RIFLED_BARREL = REGISTRATE.block("unbored_cast_iron_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.CAST_IRON, false, new boolean[]{false}))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<RifledBarrelBlock> UNBORED_BRONZE_RIFLED_BARREL = REGISTRATE.block("unbored_bronze_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.BRONZE, false, new boolean[]{false}))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<RifledBarrelBlock> UNBORED_STEEL_RIFLED_BARREL = REGISTRATE.block("unbored_steel_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.STEEL, false, new boolean[]{false}))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock(false))
            .register();
    public static final BlockEntry<RifledBarrelBlock> UNBORED_NETHERSTEEL_RIFLED_BARREL = REGISTRATE.block("unbored_nethersteel_rifled_barrel", p -> new RifledBarrelBlock(p, CBCBigCannonMaterials.NETHERSTEEL, false, new boolean[]{false}))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(strongCannonBlock(false))
            .register();

    public static final BlockEntry<RocketPodBarrelBlock> UNBORED_CAST_IRON_ROCKET_POD_BARREL = REGISTRATE.block("unbored_cast_iron_rocket_pod_rail", p -> new RocketPodBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<RocketPodBarrelBlock> UNBORED_BRONZE_ROCKET_POD_BARREL = REGISTRATE.block("unbored_bronze_rocket_pod_rail", p -> new RocketPodBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<RocketPodBarrelBlock> UNBORED_STEEL_ROCKET_POD_BARREL = REGISTRATE.block("unbored_steel_rocket_pod_rail", p -> new RocketPodBarrelBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<RocketPodBreechBlock> UNBORED_CAST_IRON_ROCKET_POD_BREECH = REGISTRATE.block("unbored_cast_iron_rocket_pod_breech", p -> new RocketPodBreechBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<RocketPodBreechBlock> UNBORED_BRONZE_ROCKET_POD_BREECH = REGISTRATE.block("unbored_bronze_rocket_pod_breech", p -> new RocketPodBreechBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<RocketPodBreechBlock> UNBORED_STEEL_ROCKET_POD_BREECH = REGISTRATE.block("unbored_steel_rocket_pod_breech", p -> new RocketPodBreechBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(BlockItem::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();

    /*public static final BlockEntry<MediumRocketPodBarrelBlock> UNBORED_CAST_IRON_MEDIUM_ROCKET_POD_BARREL = REGISTRATE.block("unbored_cast_iron_medium_rocket_pod_rail", p -> new MediumRocketPodBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBarrelBlock> UNBORED_BRONZE_MEDIUM_ROCKET_POD_BARREL = REGISTRATE.block("unbored_bronze_medium_rocket_pod_rail", p -> new MediumRocketPodBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBarrelBlock> UNBORED_STEEL_MEDIUM_ROCKET_POD_BARREL = REGISTRATE.block("unbored_steel_medium_rocket_pod_rail", p -> new MediumRocketPodBarrelBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBreechBlock> UNBORED_CAST_IRON_MEDIUM_ROCKET_POD_BREECH = REGISTRATE.block("unbored_cast_iron_medium_rocket_pod_breech", p -> new MediumRocketPodBreechBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBreechBlock> UNBORED_BRONZE_MEDIUM_ROCKET_POD_BREECH = REGISTRATE.block("unbored_bronze_medium_rocket_pod_breech", p -> new MediumRocketPodBreechBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<MediumRocketPodBreechBlock> UNBORED_STEEL_MEDIUM_ROCKET_POD_BREECH = REGISTRATE.block("unbored_steel_medium_rocket_pod_breech", p -> new MediumRocketPodBreechBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(BlockItem::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock())
            .register();*/

    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_CAST_IRON_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("unbored_cast_iron_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_BRONZE_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("unbored_bronze_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_STEEL_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("unbored_steel_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> UNBORED_CAST_IRON_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_cast_iron_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::castIronTwinAutocannonBarrel, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> UNBORED_BRONZE_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_bronze_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::bronzeTwinAutocannonBarrel, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> UNBORED_STEEL_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_steel_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::steelTwinAutocannonBarrel, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> UNBORED_CAST_IRON_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("unbored_cast_iron_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.CAST_IRON, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> UNBORED_BRONZE_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("unbored_bronze_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.BRONZE, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> UNBORED_STEEL_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("unbored_steel_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.STEEL, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();

    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_CAST_IRON_VERT_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("unbored_cast_iron_vert_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_BRONZE_VERT_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("unbored_bronze_vert_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_STEEL_VERT_TWIN_AUTOCANNON_BARREL = REGISTRATE.block("unbored_steel_vert_twin_autocannon_barrel", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> UNBORED_CAST_IRON_VERT_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_cast_iron_vert_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::castIronVertTwinAutocannonBarrel, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> UNBORED_BRONZE_VERT_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_bronze_vert_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::bronzeVertTwinAutocannonBarrel, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonRecoilSpringBlock> UNBORED_STEEL_VERT_TWIN_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_steel_vert_twin_autocannon_recoil_spring", p -> new TwinAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::steelVertTwinAutocannonBarrel, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> UNBORED_CAST_IRON_VERT_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("unbored_cast_iron_vert_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.CAST_IRON, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> UNBORED_BRONZE_VERT_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("unbored_bronze_vert_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.BRONZE, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBreechBlock> UNBORED_STEEL_VERT_TWIN_AUTOCANNON_BREECH = REGISTRATE.block("unbored_steel_vert_twin_autocannon_breech", p -> new TwinAutocannonBreechBlock(p, CBCAutocannonMaterials.STEEL, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_CAST_IRON_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_cast_iron_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false, 5))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_BRONZE_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_bronze_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false, 5))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_STEEL_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_steel_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false, 5))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_CAST_IRON_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_cast_iron_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_BRONZE_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_bronze_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_STEEL_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_steel_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_CAST_IRON_VERT_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_cast_iron_vert_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, true, 5))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_BRONZE_VERT_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_bronze_vert_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, true, 5))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_STEEL_VERT_TWIN_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_steel_vert_twin_autocannon_silencer", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, true, 5f))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_CAST_IRON_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_cast_iron_vert_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_BRONZE_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_bronze_vert_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<TwinAutocannonBarrelBlock> UNBORED_STEEL_VERT_TWIN_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_steel_vert_twin_autocannon_muzzle_brake", p -> new TwinAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, true, false))
            .item(TwinAutocannonBlockItem<TwinAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();

    public static final BlockEntry<SpecialAutocannonBarrel> UNBORED_CAST_IRON_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_cast_iron_autocannon_silencer", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.CAST_IRON, 5))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> UNBORED_BRONZE_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_bronze_autocannon_silencer", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.BRONZE, 5))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> UNBORED_STEEL_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_steel_autocannon_silencer", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.STEEL, 5))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> UNBORED_CAST_IRON_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_cast_iron_autocannon_muzzle_brake", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> UNBORED_BRONZE_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_bronze_autocannon_muzzle_brake", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.BRONZE, false))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<SpecialAutocannonBarrel> UNBORED_STEEL_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_steel_autocannon_muzzle_brake", p -> new SpecialAutocannonBarrel(p, CBCAutocannonMaterials.STEEL, false))
            .item(AutocannonBlockItem<SpecialAutocannonBarrel>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();

    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_CAST_IRON_HEAVY_AUTOCANNON_BARREL = REGISTRATE.block("unbored_cast_iron_heavy_autocannon_barrel", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_BRONZE_HEAVY_AUTOCANNON_BARREL = REGISTRATE.block("unbored_bronze_heavy_autocannon_barrel", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_STEEL_HEAVY_AUTOCANNON_BARREL = REGISTRATE.block("unbored_steel_heavy_autocannon_barrel", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    /*public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_CAST_IRON_HEAVY_AUTOCANNON_CHAMBER = REGISTRATE.block("unbored_cast_iron_heavy_autocannon_chamber", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, true, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_BRONZE_HEAVY_AUTOCANNON_CHAMBER = REGISTRATE.block("unbored_bronze_heavy_autocannon_chamber", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, true, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_STEEL_HEAVY_AUTOCANNON_CHAMBER = REGISTRATE.block("unbored_steel_heavy_autocannon_chamber", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, true, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new).tab(TabRegister.SIMPLE_TAB.getKey()).build()
            .transform(cannonBlock(false))
            .register();*/
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_CAST_IRON_HEAVY_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_cast_iron_heavy_autocannon_muzzle_brake", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_BRONZE_HEAVY_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_bronze_heavy_autocannon_muzzle_brake", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_STEEL_HEAVY_AUTOCANNON_MUZZLE_BRAKE = REGISTRATE.block("unbored_steel_heavy_autocannon_muzzle_brake", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_CAST_IRON_HEAVY_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_cast_iron_heavy_autocannon_silencer", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.CAST_IRON, false, 5))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_BRONZE_HEAVY_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_bronze_heavy_autocannon_silencer", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.BRONZE, false, 5))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonBarrelBlock> UNBORED_STEEL_HEAVY_AUTOCANNON_SILENCER = REGISTRATE.block("unbored_steel_heavy_autocannon_silencer", p -> new HeavyAutocannonBarrelBlock(p, CBCAutocannonMaterials.STEEL, false, 5))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBarrelBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock())
            .register();
    public static final BlockEntry<HeavyAutocannonRecoilSpringBlock> UNBORED_CAST_IRON_HEAVY_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_cast_iron_heavy_autocannon_recoil_spring", p -> new HeavyAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.CAST_IRON, BlockRegister::castIronVertTwinAutocannonBarrel, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonRecoilSpringBlock> UNBORED_BRONZE_HEAVY_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_bronze_heavy_autocannon_recoil_spring", p -> new HeavyAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.BRONZE, BlockRegister::bronzeHeavyAutocannonBarrel, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonRecoilSpringBlock> UNBORED_STEEL_HEAVY_AUTOCANNON_RECOIL_SPRING = REGISTRATE.block("unbored_steel_heavy_autocannon_recoil_spring", p -> new HeavyAutocannonRecoilSpringBlock(p, CBCAutocannonMaterials.STEEL, BlockRegister::steelHeavyAutocannonBarrel, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonRecoilSpringBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> UNBORED_CAST_IRON_HEAVY_AUTOCANNON_BREECH = REGISTRATE.block("unbored_cast_iron_heavy_autocannon_breech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> UNBORED_BRONZE_HEAVY_AUTOCANNON_BREECH = REGISTRATE.block("unbored_bronze_heavy_autocannon_breech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> UNBORED_STEEL_HEAVY_AUTOCANNON_BREECH = REGISTRATE.block("unbored_steel_heavy_autocannon_breech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> UNBORED_CAST_IRON_HEAVY_AUTOCANNON_QFBREECH = REGISTRATE.block("unbored_cast_iron_heavy_autocannon_qfbreech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.CAST_IRON, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> UNBORED_BRONZE_HEAVY_AUTOCANNON_QFBREECH = REGISTRATE.block("unbored_bronze_heavy_autocannon_qfbreech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.BRONZE, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();
    public static final BlockEntry<HeavyAutocannonBreechBlock> UNBORED_STEEL_HEAVY_AUTOCANNON_QFBREECH = REGISTRATE.block("unbored_steel_heavy_autocannon_qfbreech", p -> new HeavyAutocannonBreechBlock(p, CBCAutocannonMaterials.STEEL, false))
            .item(HeavyAutocannonBlockItem<HeavyAutocannonBreechBlock>::new)/*.tab(TabRegister.SIMPLE_TAB.getKey())*/.build()
            .transform(cannonBlock(false))
            .register();

    public static void register() {}

    public static BlockState castIronTwinAutocannonBarrel(Direction facing) { return CAST_IRON_TWIN_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }
    public static BlockState bronzeTwinAutocannonBarrel(Direction facing) { return BRONZE_TWIN_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }
    public static BlockState steelTwinAutocannonBarrel(Direction facing) { return STEEL_TWIN_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }

    public static BlockState castIronVertTwinAutocannonBarrel(Direction facing) { return CAST_IRON_VERT_TWIN_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }
    public static BlockState bronzeVertTwinAutocannonBarrel(Direction facing) { return BRONZE_VERT_TWIN_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }
    public static BlockState steelVertTwinAutocannonBarrel(Direction facing) { return STEEL_VERT_TWIN_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }

    public static BlockState castIronHeavyAutocannonBarrel(Direction facing) { return CAST_IRON_HEAVY_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }
    public static BlockState bronzeHeavyAutocannonBarrel(Direction facing) { return BRONZE_HEAVY_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }
    public static BlockState steelHeavyAutocannonBarrel(Direction facing) { return STEEL_HEAVY_AUTOCANNON_BARREL.getDefaultState().setValue(TwinAutocannonBaseBlock.FACING, facing); }

    public static BlockState getCastIronSilencer(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return CAST_IRON_AUTOCANNON_SILENCER.getDefaultState().setValue(SpecialAutocannonBarrel.FACING, f); }
    public static BlockState getBronzeSilencer(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return BRONZE_AUTOCANNON_SILENCER.getDefaultState().setValue(SpecialAutocannonBarrel.FACING, f); }
    public static BlockState getSteelSilencer(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return STEEL_AUTOCANNON_SILENCER.getDefaultState().setValue(SpecialAutocannonBarrel.FACING, f); }

    public static BlockState getCastIronMuzzleBrake(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return CAST_IRON_AUTOCANNON_MUZZLE_BRAKE.getDefaultState().setValue(SpecialAutocannonBarrel.FACING, f); }
    public static BlockState getBronzeMuzzleBrake(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return BRONZE_AUTOCANNON_MUZZLE_BRAKE.getDefaultState().setValue(SpecialAutocannonBarrel.FACING, f); }
    public static BlockState getSteelMuzzleBrake(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return STEEL_AUTOCANNON_MUZZLE_BRAKE.getDefaultState().setValue(SpecialAutocannonBarrel.FACING, f); }

    public static BlockState getCastIronBarrel(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return CBCBlocks.CAST_IRON_AUTOCANNON_BARREL.getDefaultState().setValue(AutocannonBarrelBlock.FACING, f).setValue(AutocannonBarrelBlock.BARREL_END, e); }
    public static BlockState getBronzeBarrel(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return CBCBlocks.BRONZE_AUTOCANNON_BARREL.getDefaultState().setValue(AutocannonBarrelBlock.FACING, f).setValue(AutocannonBarrelBlock.BARREL_END, e); }
    public static BlockState getSteelBarrel(Direction f, AutocannonBarrelBlock.AutocannonBarrelEnd e) { return CBCBlocks.STEEL_AUTOCANNON_BARREL.getDefaultState().setValue(AutocannonBarrelBlock.FACING, f).setValue(AutocannonBarrelBlock.BARREL_END, e); }

    private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBlock(){
        return cannonBlock(true);
    }

    private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> cannonBlock(boolean canPassTrough) {
        NonNullUnaryOperator<BlockBuilder<T, P>> transform = b -> b.properties(p -> p.strength(5.0f, 6.0f))
                .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                .properties(p -> p.requiresCorrectToolForDrops())
                //.properties(p -> p.destroyTime(0.2f))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .tag(BlockTags.NEEDS_IRON_TOOL);
        return canPassTrough ? transform.andThen(b -> b.tag(CBCTags.CBCBlockTags.DRILL_CAN_PASS_THROUGH)) : transform;
    }

    private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> strongCannonBlock() {
        return strongCannonBlock(true);
    }

    private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> strongCannonBlock(boolean canPassThrough) {
        NonNullUnaryOperator<BlockBuilder<T, P>> transform = b -> b.properties(p -> p.strength(50.0f, 1200.0f))
                .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                .properties(p -> p.requiresCorrectToolForDrops())
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .tag(BlockTags.NEEDS_DIAMOND_TOOL);
        return canPassThrough ? transform.andThen(b -> b.tag(CBCTags.CBCBlockTags.DRILL_CAN_PASS_THROUGH)) : transform;
    }

    public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> castMould(String size) {
        ResourceLocation baseLoc = ResourceLocation.fromNamespaceAndPath(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/cast_mould/" + size + "_mould");
        ResourceLocation sandLoc = ResourceLocation.fromNamespaceAndPath(CreateBigCannons.MOD_ID, "block/casting_sand");
        return b -> b.properties(p -> p.mapColor(MapColor.PODZOL))
                .properties(p -> p.strength(2.0f, 3.0f))
                .properties(p -> p.sound(SoundType.WOOD))
                .properties(p -> p.noOcclusion())
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                .addLayer(() -> RenderType::solid)
                .blockstate((c, p) -> p.getMultipartBuilder(c.get())
                        .part()
                        .modelFile(p.models().getExistingFile(baseLoc))
                        .addModel()
                        .end()
                        .part()
                        .modelFile(p.models().getExistingFile(sandLoc))
                        .addModel()
                        .condition(CannonCastMouldBlock.SAND, true)
                        .end())
                .item()
                /*.tab(TabRegister.SIMPLE_TAB.getKey())*/
                .model((c, p) -> p.getBuilder(c.getName()).parent(p.getExistingFile(baseLoc)))
                .build();
    }

    public static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> autocannonAmmoContainer(boolean isCreative) {
        String root = isCreative ? "creative": "regular";
        return b -> b.addLayer(() -> RenderType::cutoutMipped)
                .blockstate((c, p) -> p.getVariantBuilder(c.get())
                        .forAllStatesExcept(state -> {
                            HeavyAutocannonAmmoContainerBlock.State containerState = state.getValue(HeavyAutocannonAmmoContainerBlock.CONTAINER_STATE);
                            String suffix = switch (containerState) {
                                case EMPTY -> "_empty";
                                case FILLED -> "_filled";
                            };
                            ResourceLocation loc = p.modLoc("block/autocannon_ammo_containers/" + root + suffix);
                            Direction.Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models().getExistingFile(loc))
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED))
                .loot((t, c) -> {
                    CopyComponentsFunction.Builder func = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                            .include(CBCDataComponents.AMMO)
                            .include(CBCDataComponents.TRACER)
                            .include(CBCDataComponents.TRACER_SPACING);
                    if (isCreative)
                        func = func.include(CBCDataComponents.CURRENT_INDEX);
                    t.add(c, LootTable.lootTable()
                            .withPool(t.applyExplosionCondition(c, LootPool.lootPool()
                                            .add(LootItem.lootTableItem(c))
                                            .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))
                                    .apply(func)));
                })
                .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                .item(HeavyAutocannonAmmoContainerItem::new)
                .properties(p -> p.stacksTo(1))
                .properties(p -> isCreative ? p.rarity(Rarity.EPIC) : p)
                .tag(CBCTags.CBCItemTags.AUTOCANNON_AMMO_CONTAINERS)
                .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/autocannon_ammo_containers/" + root)))
                /*.tab(TabRegister.SIMPLE_TAB.getKey())*/
                .build();
    }
}