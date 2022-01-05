package com.ryanjhuston.emobile.common.player;

import com.ryanjhuston.emobile.EMobileMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerCapabilityAttachEventHandler {

    @SubscribeEvent
    public static void attachCapabilityToPlayerHandler(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof ServerPlayerEntity) {
            event.addCapability(new ResourceLocation(EMobileMod.MODID + ":capability_provider_players"), new PlayerCapabilityProvider());
        }
    }

    //@SubscribeEvent
    public static void onPlayerDie(PlayerEvent.Clone event) {

    }
}
