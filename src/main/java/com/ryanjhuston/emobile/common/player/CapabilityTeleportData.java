package com.ryanjhuston.emobile.common.player;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityTeleportData {

    @CapabilityInject(PlayerTeleportData.class)
    public static Capability<PlayerTeleportData> CAPABILITY_PLAYER_TELEPORT = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(PlayerTeleportData.class,
                new PlayerTeleportData.PlayerTeleportDataNBTStorage(),
                PlayerTeleportData::createDefault);
    }
}
