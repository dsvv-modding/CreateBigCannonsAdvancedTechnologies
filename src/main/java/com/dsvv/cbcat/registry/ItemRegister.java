package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.cannon.autocannon.munitions.apds.AutocannonAPDSProjectileItem;
import com.dsvv.cbcat.cannon.autocannon.munitions.apdsfs.AutocannonAPDSFSProjectileItem;
import com.dsvv.cbcat.cannon.autocannon.munitions.he.AutocannonHEProjectileItem;
import com.dsvv.cbcat.cannon.autocannon.munitions.hei.AutocannonHEIProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.ap_shot.HA_APProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.HeavyAutocannonCartridgeItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.apds_shot.HA_APDSProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.apdsfs.HA_APDSFSProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.he_shell.HA_HEProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.heat_shell.HA_HEATProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.hef_shell.HA_HEFProjectileItem;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.smoke_shell.HA_SmokeProjectileItem;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.medium_ap_rocket.APMediumRocketItem;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.medium_he_rocket.HEMediumRocketItem;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.medium_heat_rocket.HEATMediumRocketItem;
import com.dsvv.cbcat.cannon.medium_rocketpod.munitions.medium_hef_rocket.HEFMediumRocketItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.RocketCartridgeItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.ap_rocket.AP_RocketItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.flak_rocket.Flak_RocketItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.he_rocket.HE_RocketItem;
import com.dsvv.cbcat.cannon.rocketpod.munitions.hei_rocket.HEI_RocketItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import rbasamoyai.createbigcannons.CBCTags;

import static com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology.REGISTRATE;

public class ItemRegister
{
    //public static final ItemEntry<HeavyAutocannonPowderCharge> HEAVY_AUTOCANNON_POWDER_CHARGE = REGISTRATE.item("heavy_autocannon_powder_charge", HeavyAutocannonPowderCharge::new)
    //        .register();
    public static final ItemEntry<AP_RocketItem> AP_ROCKET_ITEM = REGISTRATE.item("ap_rocket_item", AP_RocketItem::new)
            ////.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<Flak_RocketItem> FLAK_ROCKET_ITEM = REGISTRATE.item("flak_rocket_item", Flak_RocketItem::new)
            ////.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HE_RocketItem> HE_ROCKET_ITEM = REGISTRATE.item("he_rocket_item", HE_RocketItem::new)
            ////.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HEI_RocketItem> HEI_ROCKET_ITEM = REGISTRATE.item("hei_rocket_item", HEI_RocketItem::new)
            ////.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();

    public static final ItemEntry<APMediumRocketItem> MEDIUM_AP_ROCKET_ITEM = REGISTRATE.item("medium_ap_rocket_item", APMediumRocketItem::new)
            ////.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HEMediumRocketItem> MEDIUM_HE_ROCKET_ITEM = REGISTRATE.item("medium_he_rocket_item", HEMediumRocketItem::new)
            ////.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HEFMediumRocketItem> MEDIUM_HEF_ROCKET_ITEM = REGISTRATE.item("medium_hef_rocket_item", HEFMediumRocketItem::new)
            ////.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HEATMediumRocketItem> MEDIUM_HEAT_ROCKET_ITEM = REGISTRATE.item("medium_heat_rocket_item", HEATMediumRocketItem::new)
            ////.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();

    public static final ItemEntry<HeavyAutocannonCartridgeItem> HEAVY_AUTOCANNON_CARTRIDGE = REGISTRATE.item("heavy_autocannon_cartridge", HeavyAutocannonCartridgeItem::new)
            .tag(CBCTags.CBCItemTags.AUTOCANNON_CARTRIDGES)
            .model((c, p) -> {})
            .register();
    public static final ItemEntry<HeavyAutocannonCartridgeItem> HEAVY_AUTOCANNON_EMPTY_CARTRIDGE = REGISTRATE.item("heavy_autocannon_empty_cartridge", HeavyAutocannonCartridgeItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_CARTRIDGES)
            .model((c, p) -> {})
            .register();

    public static final ItemEntry<HA_APProjectileItem> HA_AP_ITEM = REGISTRATE.item("ha_ap_item", HA_APProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HA_APDSProjectileItem> HA_APDS_ITEM = REGISTRATE.item("ha_apds_item", HA_APDSProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HA_APDSFSProjectileItem> HA_APDSFS_ITEM = REGISTRATE.item("ha_apdsfs_item", HA_APDSFSProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HA_HEProjectileItem> HA_HE_ITEM = REGISTRATE.item("ha_he_item", HA_HEProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HA_HEFProjectileItem> HA_HEF_ITEM = REGISTRATE.item("ha_hef_item", HA_HEFProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HA_HEATProjectileItem> HA_HEAT_ITEM = REGISTRATE.item("ha_heat_item", HA_HEATProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<HA_SmokeProjectileItem> HA_SMOKE_ITEM = REGISTRATE.item("ha_smoke_item", HA_SmokeProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();

    public static final ItemEntry<AutocannonAPDSProjectileItem> APDS_ITEM = REGISTRATE.item("apds_item", AutocannonAPDSProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<AutocannonAPDSFSProjectileItem> APDSFS_ITEM = REGISTRATE.item("apdsfs_item", AutocannonAPDSFSProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<AutocannonHEProjectileItem> HE_ITEM = REGISTRATE.item("he_item", AutocannonHEProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();
    public static final ItemEntry<AutocannonHEIProjectileItem> HEI_ITEM = REGISTRATE.item("hei_item", AutocannonHEIProjectileItem::new)
            //.tab(TabRegister.SIMPLE_TAB.getKey())
            .tag(CBCTags.CBCItemTags.AUTOCANNON_ROUNDS)
            .register();

    public static void register() {}
}
