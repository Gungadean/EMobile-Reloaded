package com.ryanjhuston.emobile.core.util;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.player.PlayerCapabilityProvider;
import com.ryanjhuston.emobile.common.player.PlayerTeleportDataHandler;
import com.ryanjhuston.emobile.config.EMConfig;
import com.ryanjhuston.emobile.network.PacketHandler;
import com.ryanjhuston.emobile.network.message.MessageConfigSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = EMobileMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class ForgeServerEventSubscriber {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EMobileMod.LOGGER.info("Server config settings being sent to player.");
        PacketHandler.INSTANCE.sendTo(new MessageConfigSync(EMConfig.allowTeleportPlayers.get(), EMConfig.allowTeleportHome.get(),
                EMConfig.allowTeleportSpawn.get(), EMConfig.feCellphoneMaxEnergy.get(),
                EMConfig.feCellphoneMaxInput.get(), EMConfig.feCellphoneEnergyPerUse.get()),
                ((ServerPlayerEntity)event.getPlayer()).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if(!event.isWasDeath()) return;

        PlayerTeleportDataHandler data = event.getPlayer().getCapability(PlayerCapabilityProvider.CAPABILITY_PLAYER_TELEPORT)
                .orElseThrow(() -> new IllegalStateException("PlayerDataHandler was not found. This is an error."));
        PlayerTeleportDataHandler oldData = event.getOriginal().getCapability(PlayerCapabilityProvider.CAPABILITY_PLAYER_TELEPORT)
                .orElseThrow(() -> new IllegalStateException("PlayerDataHandler was not found. This is an error."));

        data.setAccepted(oldData.getAccepted());
        data.setPAccepted(oldData.getPAccepted());
    }

    @SubscribeEvent
    public static void attachCapabilityToPlayerHandler(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof ServerPlayerEntity) {
            event.addCapability(new ResourceLocation(EMobileMod.MODID, "teleportdata"), new PlayerCapabilityProvider());
        }
    }
}