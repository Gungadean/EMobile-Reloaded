package com.ryanjhuston.emobile.session;

import com.ryanjhuston.emobile.common.player.CapabilityTeleportData;
import com.ryanjhuston.emobile.common.player.PlayerTeleportData;
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
        PlayerTeleportData teleportData = accepting.getCapability(CapabilityTeleportData.CAPABILITY_PLAYER_TELEPORT).orElse(null);

        if(teleportData == null) {
            throw new IllegalStateException("PlayerTeleportData was not found. This is an error.");
        }

        if(perma) {
            if(!teleportData.isPAccepted(accepted)) {
                teleportData.addPAccepted(accepted);
                return false;
            }
        } else {
            if(!teleportData.isAccepted(accepted)) {
                teleportData.addAccepted(accepted);
                return false;
            }
        }
        return false;
    }

    public static boolean deacceptPlayer(ServerPlayerEntity deaccepting, String deaccepted) {
        PlayerTeleportData teleportData = deaccepting.getCapability(CapabilityTeleportData.CAPABILITY_PLAYER_TELEPORT).orElse(null);

        if(teleportData == null) {
            throw new IllegalStateException("Error: PlayerTeleportData was not found.");
        }

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

    public static boolean isPlayerAccepted(PlayerTeleportData data, String target) {
        if(data.isAccepted(target) || data.isPAccepted(target)) {
            return true;
        } else {
            return false;
        }
    }

    public static void cancelSessionsForPlayer(ServerPlayerEntity player) {
        sessions.remove(player.getUniqueID().toString());
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
