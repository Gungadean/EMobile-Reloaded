package com.ryanjhuston.emobile.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class ServerUtils {

    public static ServerPlayerEntity getPlayerOnServer(UUID uuid) {
        return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByUUID(uuid);
    }

    public static ServerPlayerEntity getPlayerOnServer(String uuid) {
        return getPlayerOnServer(UUID.fromString(uuid));
    }

    public static UUID getUUIDFromName(String name) {
        GameProfile profile = ServerLifecycleHooks.getCurrentServer().getPlayerProfileCache().getGameProfileForUsername(name);

        if(profile == null) {
            return null;
        }
        return profile.getId();
    }

    public static boolean isPlayerConnected(ServerPlayerEntity player) {
        return player.getServer().getPlayerList().getPlayers().contains(player);
    }

    public static boolean canPlayerTeleport(ServerPlayerEntity player) {
        return player != null && isPlayerConnected(player) && player.isAlive();
    }

    public static void sendGlobalChat(String chat) {
        ServerLifecycleHooks.getCurrentServer().sendMessage(new StringTextComponent(chat), UUID.fromString(""));
    }

    public static void sendChatToPlayers(UUID p1, UUID p2, ITextComponent chat) {
        ServerPlayerEntity player = getPlayerOnServer(p1);
        if (player != null) {
            player.sendMessage(chat, p1);
        }

        player = getPlayerOnServer(p2);

        if (player != null) {
            player.sendMessage(chat, p2);
        }
    }

    public static void sendChatToPlayer(UUID uuid, ITextComponent chat) {
        ServerPlayerEntity player = getPlayerOnServer(uuid);
        if (player != null) {
            player.sendMessage(chat, uuid);
        }
    }

    public static void sendChatToPlayer(UUID uuid, String chat, TextFormatting color) {
        sendChatToPlayer(uuid, new StringTextComponent(chat).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(color))));
    }

    public static void sendChatToPlayer(UUID uuid, String chat) {
        sendChatToPlayer(uuid, chat, TextFormatting.WHITE);
    }

    public static ITextComponent getTranslationTextComponent(String key, TextFormatting color) {
        return new TranslationTextComponent(key).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(color)));
    }

    public static void sendDiallingSound(PlayerEntity player) {
        //PacketHandler.INSTANCE.sendToServer(new MessageDiallingSound(player.getPosX(), player.getPosY() + 0.8D, player.getPosZ()));
    }

    public static void sendDiallingParticles(PlayerEntity player) {
        //PacketHandler.INSTANCE.sendToServer(new MessageDiallingParticles(player.getPosX(), player.getPosY() + 0.8D, player.getPosZ()));
    }

    public static void sendDiallingParticles(World world, int posX, int posY, int posZ) {
        //PacketHandler.INSTANCE.sendToServer(new MessageDiallingParticles(posX + 0.5D, posY + 0.5D, posZ + 0.5D));
    }

    public static void sendTeleportParticles(PlayerEntity player) {
        //PacketHandler.INSTANCE.sendToServer(new MessageTeleportParticles(player.getPosX(), player.getPosY() + 0.8D, player.getPosZ()),
        //        new PacketDistributor.TargetPoint(player.getEntityWorld(), player.getPosX(), player.getPosY(), player.getPosZ(), 256));
    }
}