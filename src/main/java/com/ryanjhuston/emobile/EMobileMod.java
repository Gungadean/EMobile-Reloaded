package com.ryanjhuston.emobile;

import com.ryanjhuston.emobile.client.audio.SoundRegister;
import com.ryanjhuston.emobile.config.EMConfig;
import com.ryanjhuston.emobile.core.init.CapabilityProvidersInit;
import com.ryanjhuston.emobile.core.init.ContainerTypesInit;
import com.ryanjhuston.emobile.core.util.ClientEventBusSubscriber;
import com.ryanjhuston.emobile.core.util.ForgeServerEventSubscriber;
import com.ryanjhuston.emobile.core.util.ServerEventBusSubscriber;
import com.ryanjhuston.emobile.item.EMobileItems;
import com.ryanjhuston.emobile.network.PacketHandler;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EMobileMod.MODID)
public class EMobileMod {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "emobile";

    private IEventBus eventBus;

    public EMobileMod() {
        eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EMConfig.SPEC, "emobile.toml");

        EMobileItems.registerItems(eventBus);
        SoundRegister.register(eventBus);
        ContainerTypesInit.CONTAINER_TYPES.register(eventBus);

        eventBus.addListener(this::setup);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("E-Mobile Reloaded starting pre-initialization.");

        PacketHandler.setup();

        CapabilityProvidersInit.registerProviders();
        eventBus.register(ClientEventBusSubscriber.class);
        eventBus.register(ServerEventBusSubscriber.class);
        MinecraftForge.EVENT_BUS.register(ForgeServerEventSubscriber.class);

        MinecraftForge.EVENT_BUS.register(CellphoneSessionsHandler.class);
    }
}