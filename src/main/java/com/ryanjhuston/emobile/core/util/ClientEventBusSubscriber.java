package com.ryanjhuston.emobile.core.util;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.client.gui.PearlCellphoneScreen;
import com.ryanjhuston.emobile.client.gui.RFCellphoneScreen;
import com.ryanjhuston.emobile.core.init.ContainerTypesInit;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EMobileMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ContainerTypesInit.CELLPHONE_ITEM_CONTAINER_TYPE.get(), PearlCellphoneScreen::new);
        ScreenManager.registerFactory(ContainerTypesInit.CELLPHONE_RF_CONTAINER_TYPE.get(), RFCellphoneScreen::new);
    }
}