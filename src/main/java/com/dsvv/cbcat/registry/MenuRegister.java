package com.dsvv.cbcat.registry;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerMenu;
import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerScreen;
import com.tterrag.registrate.util.entry.MenuEntry;

import static com.dsvv.cbcat.CreateBigCannons_AdvancedTechnology.REGISTRATE;

public class MenuRegister {
    public static final MenuEntry<HeavyAutocannonAmmoContainerMenu> HEAVY_AUTOCANNON_AMMO_BOX =
            REGISTRATE.menu("heavy_autocannon_ammo_box_menu", HeavyAutocannonAmmoContainerMenu::getClientMenu, ()-> HeavyAutocannonAmmoContainerScreen::new).register();

    public static void register() {}
}
