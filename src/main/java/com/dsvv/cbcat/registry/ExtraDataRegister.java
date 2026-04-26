package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectile;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.AbstractFuzedHeavyAutocannonProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.cannons.autocannon.AutocannonBlock;
import rbasamoyai.createbigcannons.cannons.autocannon.material.AutocannonMaterial;
import rbasamoyai.createbigcannons.index.CBCAutocannonMaterials;
import rbasamoyai.createbigcannons.index.CBCBlocks;
import rbasamoyai.createbigcannons.index.CBCItems;

import java.util.HashMap;
import java.util.logging.Logger;

import static com.dsvv.cbcat.debugUtils.DebugUtils.displayCustomClientMessage;

public class ExtraDataRegister
{
    private static final HashMap<AutocannonMaterial, PartialModel> RECOIL_SPRINGS = new HashMap<>();
    private static final HashMap<AutocannonMaterial, PartialModel> VERT_RECOIL_SPRINGS = new HashMap<>();

    private static final HashMap<AutocannonMaterial, PartialModel> HEAVY_RECOIL_SPRINGS = new HashMap<>();
    private static final HashMap<AutocannonMaterial, PartialModel> HEAVY_BREECH_BLOCKS = new HashMap<>();

    private static final HashMap<String, BlockEntry<?>> ALL_CARTRIDGES = new HashMap<>();
    private static final HashMap<BlockEntry<?>, String> ALL_PROJECTILE_BLOCKS = new HashMap<>();
    private static final HashMap<String, EntityEntry<? extends AbstractFuzedHeavyAutocannonProjectile>> CLUSTER_PROJECTILES = new HashMap<>();
    private static PartialModel MEDIUM_ROCKET;
    private static final HashMap<ItemEntry<?>, ItemEntry<?>> PROJECTILE_MEDIUM_ROCKET = new HashMap<>();
    private static final HashMap<ItemEntry<?>, ItemEntry<?>> PROJECTILE_ROCKET = new HashMap<>();

    public static void register()
    {
        RECOIL_SPRINGS.put(CBCAutocannonMaterials.CAST_IRON, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/twin_autocannon/cast_iron_recoil_spring")));
        RECOIL_SPRINGS.put(CBCAutocannonMaterials.BRONZE, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/twin_autocannon/bronze_recoil_spring")));
        RECOIL_SPRINGS.put(CBCAutocannonMaterials.STEEL, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID, "block/twin_autocannon/steel_recoil_spring")));

        VERT_RECOIL_SPRINGS.put(CBCAutocannonMaterials.CAST_IRON, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/twin_autocannon/cast_iron_vert_recoil_spring")));
        VERT_RECOIL_SPRINGS.put(CBCAutocannonMaterials.BRONZE, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/twin_autocannon/bronze_vert_recoil_spring")));
        VERT_RECOIL_SPRINGS.put(CBCAutocannonMaterials.STEEL, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID, "block/twin_autocannon/steel_vert_recoil_spring")));

        HEAVY_RECOIL_SPRINGS.put(CBCAutocannonMaterials.CAST_IRON, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/heavy_autocannon/cast_iron_recoil_spring")));
        HEAVY_RECOIL_SPRINGS.put(CBCAutocannonMaterials.BRONZE, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/heavy_autocannon/bronze_recoil_spring")));
        HEAVY_RECOIL_SPRINGS.put(CBCAutocannonMaterials.STEEL, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID, "block/heavy_autocannon/steel_recoil_spring")));

        HEAVY_BREECH_BLOCKS.put(CBCAutocannonMaterials.CAST_IRON, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/heavy_autocannon/cast_iron_breechblock")));
        HEAVY_BREECH_BLOCKS.put(CBCAutocannonMaterials.BRONZE, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID,"block/heavy_autocannon/bronze_breechblock")));
        HEAVY_BREECH_BLOCKS.put(CBCAutocannonMaterials.STEEL, PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID, "block/heavy_autocannon/steel_breechblock")));

        ALL_CARTRIDGES.put("ap shot", BlockRegister.ARMOR_PIERCING_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("ap shell", BlockRegister.ARMOR_PIERCING_SHELL_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("fluid shell", BlockRegister.FLUID_SHELL_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("grapeshot", BlockRegister.GRAPESHOT_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("he shell", BlockRegister.HIGH_EXPLOSIVE_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("shrapnel shell", BlockRegister.SHRAPNEL_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("smoke shell", BlockRegister.SMOKE_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("solid shot", BlockRegister.SOLID_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("cluster shell", BlockRegister.CLUSTER_CARTRIDGE_BLOCK);
        ALL_CARTRIDGES.put("ap shot caseless", BlockRegister.ARMOR_PIERCING_CASELESS_BLOCK);
        ALL_CARTRIDGES.put("ap shell caseless", BlockRegister.ARMOR_PIERCING_SHELL_CASELESS_BLOCK);
        ALL_CARTRIDGES.put("fluid shell caseless", BlockRegister.FLUID_SHELL_CASELESS_BLOCK);
        ALL_CARTRIDGES.put("grapeshot caseless", BlockRegister.GRAPESHOT_CASELESS_BLOCK);
        ALL_CARTRIDGES.put("he shell caseless", BlockRegister.HIGH_EXPLOSIVE_CASELESS_BLOCK);
        ALL_CARTRIDGES.put("shrapnel shell caseless", BlockRegister.SHRAPNEL_CASELESS_BLOCK);
        ALL_CARTRIDGES.put("smoke shell caseless", BlockRegister.SMOKE_CASELESS_BLOCK);
        ALL_CARTRIDGES.put("solid shot caseless", BlockRegister.SOLID_CASELESS_BLOCK);
        ALL_CARTRIDGES.put("cluster shell caseless", BlockRegister.CLUSTER_CASELESS_BLOCK);

        ALL_PROJECTILE_BLOCKS.put(CBCBlocks.AP_SHOT, "ap shot");
        ALL_PROJECTILE_BLOCKS.put(CBCBlocks.AP_SHELL, "ap shell");
        ALL_PROJECTILE_BLOCKS.put(CBCBlocks.FLUID_SHELL, "fluid shell");
        ALL_PROJECTILE_BLOCKS.put(CBCBlocks.BAG_OF_GRAPESHOT, "grapeshot");
        ALL_PROJECTILE_BLOCKS.put(CBCBlocks.HE_SHELL, "he shell");
        ALL_PROJECTILE_BLOCKS.put(CBCBlocks.SHRAPNEL_SHELL, "shrapnel shell");
        ALL_PROJECTILE_BLOCKS.put(CBCBlocks.SMOKE_SHELL, "smoke shell");
        ALL_PROJECTILE_BLOCKS.put(CBCBlocks.SOLID_SHOT, "solid shot");
        ALL_PROJECTILE_BLOCKS.put(BlockRegister.CLUSTER_BLOCK, "cluster shell");

        //CLUSTER_PROJECTILES.put("", null);
        CLUSTER_PROJECTILES.put("tooltip.cbc_at.ha_smoke", EntityRegister.HA_SMOKE_PROJECTILE);
        CLUSTER_PROJECTILES.put("tooltip.cbc_at.ha_he", EntityRegister.HA_HE_PROJECTILE);
        CLUSTER_PROJECTILES.put("tooltip.cbc_at.ha_hef", EntityRegister.HA_HEF_PROJECTILE);
        CLUSTER_PROJECTILES.put("tooltip.cbc_at.ha_heat", EntityRegister.HA_HEAT_PROJECTILE);

        MEDIUM_ROCKET = PartialModel.of(new ResourceLocation(CreateBigCannons_AdvancedTechnology.MOD_ID, "item/base/medium_rocket_item"));

        PROJECTILE_MEDIUM_ROCKET.put(ItemRegister.HA_AP_ITEM, ItemRegister.MEDIUM_AP_ROCKET_ITEM);
        PROJECTILE_MEDIUM_ROCKET.put(ItemRegister.HA_HE_ITEM, ItemRegister.MEDIUM_HE_ROCKET_ITEM);
        PROJECTILE_MEDIUM_ROCKET.put(ItemRegister.HA_HEF_ITEM, ItemRegister.MEDIUM_HEF_ROCKET_ITEM);
        PROJECTILE_MEDIUM_ROCKET.put(ItemRegister.HA_HEAT_ITEM, ItemRegister.MEDIUM_HEAT_ROCKET_ITEM);

        PROJECTILE_ROCKET.put(CBCItems.AP_AUTOCANNON_ROUND, ItemRegister.AP_ROCKET_ITEM);
        PROJECTILE_ROCKET.put(CBCItems.FLAK_AUTOCANNON_ROUND, ItemRegister.FLAK_ROCKET_ITEM);
        PROJECTILE_ROCKET.put(ItemRegister.HE_ROCKET_ITEM, ItemRegister.HE_ROCKET_ITEM);
        PROJECTILE_ROCKET.put(ItemRegister.HEI_ROCKET_ITEM, ItemRegister.HEI_ROCKET_ITEM);
    }

    public static PartialModel twinAutocannonSpringFor(AutocannonMaterial mat, boolean vertical)
    {
        if (!vertical)
            return RECOIL_SPRINGS.get(mat);
        return VERT_RECOIL_SPRINGS.get(mat);
    }

    public static PartialModel heavyAutocannonSpringFor(AutocannonMaterial mat)
    {
        return HEAVY_RECOIL_SPRINGS.get(mat);
    }

    public static BlockEntry<?> getCartridge(String name) {
        return ALL_CARTRIDGES.get(name);
    }

    public static String getCartridgeReverse(Item cartridgeItem) {
        for (String key : ALL_CARTRIDGES.keySet())
            if (ALL_CARTRIDGES.get(key).is(cartridgeItem))
                return key;
        return "";
    }

    public static String getProjectile(ItemStack stack) {
        for (BlockEntry<?> key : ALL_PROJECTILE_BLOCKS.keySet())
            if (key.isIn(stack))
                return ALL_PROJECTILE_BLOCKS.get(key);
        return "";
    }

    public static BlockEntry getProjectileReverse(String name) {
        for (BlockEntry key : ALL_PROJECTILE_BLOCKS.keySet())
            if (ALL_PROJECTILE_BLOCKS.get(key).equals(name))
                return key;
        return null;
    }

    public static PartialModel heavyAutocannonBreechblockFor(AutocannonMaterial mat)
    {
        return HEAVY_BREECH_BLOCKS.get(mat);
    }

    public static EntityEntry<? extends AbstractFuzedHeavyAutocannonProjectile> clusterParts(String projectile) {
        return CLUSTER_PROJECTILES.get(projectile);
    }

    public static String clusterPartsReverse(EntityType entity) {
        for (String key : CLUSTER_PROJECTILES.keySet())
            if (CLUSTER_PROJECTILES.get(key).is(entity))
                return key;
        return "";
    }

    public static PartialModel mediumRocketModel() {
        return MEDIUM_ROCKET;
    }

    public static Item getMediumRocketForProjectile(Item haProjectile) {
        for (ItemEntry<?> itemEntry :  PROJECTILE_MEDIUM_ROCKET.keySet())
            if (itemEntry.is(haProjectile))
                return PROJECTILE_MEDIUM_ROCKET.get(itemEntry).get();
        return Items.AIR;
    }

    public static Item getRocketForProjectile(Item haProjectile) {
        for (ItemEntry<?> itemEntry :  PROJECTILE_ROCKET.keySet())
            if (itemEntry.is(haProjectile))
                return PROJECTILE_ROCKET.get(itemEntry).get();
        return Items.AIR;
    }
}
