package com.dsvv.cbcat;

import com.dsvv.cbcat.cannon.heavy_autocannon.munitions.box.HeavyAutocannonAmmoContainerItem;
import com.dsvv.cbcat.casting.CannonCastingShapes;
import com.dsvv.cbcat.config.CBCATConfigs;
import com.dsvv.cbcat.registry.*;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.resource.PathPackResources;
import org.slf4j.Logger;

@Mod(CreateBigCannons_AdvancedTechnology.MOD_ID)
public class CreateBigCannons_AdvancedTechnology
{
    public static final String MOD_ID = "cbc_at";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    private static final Logger LOGGER = LogUtils.getLogger();
    public CreateBigCannons_AdvancedTechnology() 
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        REGISTRATE.registerEventListeners(modEventBus);

        TabRegister.register(modEventBus);
        BlockRegister.register();
        ItemRegister.register();
        BlockEntityRegister.register();
        //ContraptionRegister.register();
        ExtraDataRegister.register();
        EntityRegister.register();
        MenuRegister.register();
        RecipeRegister.register();
        CBCATConfigs.registerConfigs(ModLoadingContext.get()::registerConfig);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(this::addPackFinders);
    }

    private void onRegister(RegisterEvent event){
        CannonCastingShapes.register();
        ContraptionRegister.register();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemProperties.register(BlockRegister.HEAVY_AUTOCANNON_AMMO_BOX.asItem(),
                    new ResourceLocation("cbc_at:ammo_box_filling"),
                    (stack, level, living, id) -> stack.getItem() instanceof HeavyAutocannonAmmoContainerItem && HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(stack) > 0 ? 1.0f : 0.0f);
            ItemProperties.register(BlockRegister.CREATIVE_HEAVY_AUTOCANNON_AMMO_BOX.asItem(),
                    new ResourceLocation("cbc_at:ammo_box_filling"),
                    (stack, level, living, id) -> stack.getItem() instanceof HeavyAutocannonAmmoContainerItem && HeavyAutocannonAmmoContainerItem.getTotalAmmoCount(stack) > 0 ? 1.0f : 0.0f);
        }
    }

    @SubscribeEvent
    public void addPackFinders(AddPackFindersEvent event)
    {
        if (event.getPackType() == PackType.CLIENT_RESOURCES)
        {
            var resourcePath = ModList.get().getModFileById(MOD_ID).getFile().findResource("resourcepacks/green_creative_ammo_boxes");
            var pack = Pack.readMetaAndCreate("builtin/cbc_at", Component.literal("Green Creative Ammo Container"), false,
                    (path) -> new PathPackResources(path, false, resourcePath), PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, PackSource.BUILT_IN);
            event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
        }
    }
}
