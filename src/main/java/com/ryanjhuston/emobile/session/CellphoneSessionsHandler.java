package com.ryanjhuston.emobile.session;

import com.ryanjhuston.emobile.common.player.PlayerCapabilityProvider;
import com.ryanjhuston.emobile.common.player.PlayerTeleportDataHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class CellphoneSessionsHandler {

    public static Map<String, CellphoneSessionBase> sessions = new HashMap<>();

    public static void addSession(String uuid, CellphoneSessionBase session) {
        sessions.put(uuid, session);
    }

    public static void clearSessions() {
        sessions.clear();
    }

    public static boolean isPlayerInSession(ServerPlayerEntity player) {
        return sessions.containsKey(player.getUniqueID().toString());
    }

    public static boolean acceptPlayer(ServerPlayerEntity accepting, String accepted, boolean perma) {
        PlayerTeleportDataHandler teleportData = accepting.getCapability(PlayerCapabilityProvider.CAPABILITY_PLAYER_TELEPORT)
                .orElseThrow(() -> new IllegalStateException("PlayerDataHandler was not found. This is an error."));

        if(perma) {
            if(!teleportData.isPAccepted(accepted)) {
                if(teleportData.isAccepted(accepted)) {
                    teleportData.removeAccepted(accepted);
                }
                teleportData.addPAccepted(accepted);
                return true;
            }
        } else {
            if(!teleportData.isAccepted(accepted) && !teleportData.isPAccepted(accepted)) {
                teleportData.addAccepted(accepted);
                return true;
            }
        }
        return false;
    }

    public static boolean deacceptPlayer(ServerPlayerEntity deaccepting, String deaccepted) {
        PlayerTeleportDataHandler teleportData = deaccepting.getCapability(PlayerCapabilityProvider.CAPABILITY_PLAYER_TELEPORT)
                .orElseThrow(() -> new IllegalStateException("PlayerDataHandler was not found. This is an error."));

        if(!teleportData.isAccepted(deaccepted) && !teleportData.isPAccepted(deaccepted)) {
            return false;
        }

        if(teleportData.isAccepted(deaccepted)) {
            teleportData.removeAccepted(deaccepted);
        }

        if(teleportData.isPAccepted(deaccepted)) {
            teleportData.removePAccepted(deaccepted);
        }
        return true;
    }

    public static boolean isPlayerAccepted(ServerPlayerEntity player, ServerPlayerEntity target) {
        PlayerTeleportDataHandler teleportData = target.getCapability(PlayerCapabilityProvider.CAPABILITY_PLAYER_TELEPORT)
                .orElseThrow(() -> new IllegalStateException("PlayerDataHandler was not found. This is an error."));
        return isPlayerAccepted(teleportData, player.getUniqueID().toString());
    }

    public static boolean isPlayerAccepted(PlayerTeleportDataHandler data, String target) {
        return !data.isAccepted(target) && !data.isPAccepted(target);
    }

    public static void cancelSessionsForPlayer(ServerPlayerEntity player) {
        sessions.get(player.getUniqueID().toString()).cancel(player.getUniqueID().toString());
    }

    @SubscribeEvent
    public static void tickEnd(final TickEvent.ServerTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END) {
            Iterator<Map.Entry<String, CellphoneSessionBase>> itr = sessions.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, CellphoneSessionBase> set = itr.next();
                CellphoneSessionBase session = set.getValue();
                session.tick();
                if (!session.isValid()) {
                    itr.remove();
                }
            }
        }
    }
}
