package com.dsvv.cbcat;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.config.CBCATConfigs;
import com.dsvv.cbcat.registry.*;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(CreateBigCannons_AdvancedTechnology.MOD_ID)
public class CreateBigCannons_AdvancedTechnology
{
    public static final String MOD_ID = "cbc_at";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, MOD_ID);

    public CreateBigCannons_AdvancedTechnology(IEventBus modEventBus, ModContainer modContainer)
    {
        //IEventBus modEventBus = NeoForge.EVENT_BUS;
        //MinecraftForge.EVENT_BUS.register(this);

        //NeoForge.EVENT_BUS.register(this); Add back when resource pack is reactivated

        REGISTRATE.registerEventListeners(modEventBus);
        RECIPE_SERIALIZER_REGISTER.register(modEventBus);
        RECIPE_TYPE_REGISTER.register(modEventBus);

        TabRegister.register(modEventBus);
        BlockRegister.register();
        ItemRegister.register();
        BlockEntityRegister.register();
        //ContraptionRegister.register();
        ExtraDataRegister.register();
        EntityRegister.register();
        MenuRegister.register();
        RecipeRegister.register();
        CBCATConfigs.registerConfigs(modContainer::registerConfig);

        modEventBus.addListener(this::onRegister);
        //modEventBus.addListener(this::addPackFinders);
    }

    /*public void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(BiomeSpecialEffects.GrassColorModifier.ColorModifier);
    }*/

    private void onRegister(RegisterEvent event){
        CannonCastingShapes.register();
        ContraptionRegister.register();
        DataComponentRegistry.register();
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemProperties.register(BlockRegister.HEAVY_AUTOCANNON_AMMO_BOX.asItem(),
                    ResourceLocation.fromNamespaceAndPath(MOD_ID, "ammo_box_filing"),
                    (stack, level, living, id) -> stack.getItem() instanceof HeavyAutocannonAmmoContainerItem && HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(stack) > 0 ? 1.0f : 0.0f);
            ItemProperties.register(BlockRegister.CREATIVE_HEAVY_AUTOCANNON_AMMO_BOX.asItem(),
                    ResourceLocation.fromNamespaceAndPath(MOD_ID,"ammo_box_filling"),
                    (stack, level, living, id) -> stack.getItem() instanceof HeavyAutocannonAmmoContainerItem && HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(stack) > 0 ? 1.0f : 0.0f);
        }
    }

    //@SubscribeEvent
    public void addPackFinders(AddPackFindersEvent event)
    {
        if (event.getPackType() == PackType.CLIENT_RESOURCES)
        {
            var resourcePath = ModList.get().getModFileById(MOD_ID).getFile().findResource("resourcepacks/green_creative_ammo_boxes");
            /*var pack = Pack.readMetaAndCreate("builtin/cbc_at", Component.literal("Green Creative Ammo Container"), false,
                    (path) -> new PathPackResources(path, false, resourcePath), PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, PackSource.BUILT_IN);
            event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));*/
        }
    }
}
