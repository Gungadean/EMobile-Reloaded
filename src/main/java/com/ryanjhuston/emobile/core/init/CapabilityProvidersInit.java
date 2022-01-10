package com.ryanjhuston.emobile.core.init;

import com.ryanjhuston.emobile.common.player.PlayerTeleportDataHandler;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityProvidersInit {

    public static void registerProviders() {
        CapabilityManager.INSTANCE.register(PlayerTeleportDataHandler.class,
                new PlayerTeleportDataHandler.PlayerDataStorage(),
                PlayerTeleportDataHandler::createDefault);
    }
}