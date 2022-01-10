package com.ryanjhuston.emobile.util;

import com.mojang.authlib.GameProfile;
import com.ryanjhuston.emobile.client.audio.SoundRegister;
import com.ryanjhuston.emobile.item.CellphoneBaseItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Random;
import java.util.UUID;

public class ServerUtils {

    private static Random rand = new Random();

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

    public static ITextComponent setColor(TranslationTextComponent component, TextFormatting color) {
        return component.setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(color)));
    }

    public static ITextComponent setFormat(TranslationTextComponent component, TextFormatting format) {
        return component.setStyle(Style.EMPTY.setFormatting(format));
    }

    public static ITextComponent setColor(StringTextComponent component, TextFormatting color) {
        return component.setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(color)));
    }

    public static ITextComponent setFormat(StringTextComponent component, TextFormatting format) {
        return component.setStyle(Style.EMPTY.setFormatting(format));
    }

    public static void playDiallingSound(ServerPlayerEntity player) {
        player.getServerWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundRegister.DIAL_TONE.get(), SoundCategory.MASTER, 0.5F, 0.5F);
    }

    public static void playTeleportSound(ServerPlayerEntity player) {
        playTeleportSound(player, player.getPosX(), player.getPosY(), player.getPosZ());
    }

    public static void playTeleportSound(ServerPlayerEntity player, double posX, double posY, double posZ) {
        player.getServerWorld().playSound(null, posX, posY, posZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    public static void spawnDiallingParticles(ServerPlayerEntity player) {
        spawnDiallingParticles(player.getServerWorld(), player.getPosX(), player.getPosY(), player.getPosZ());
    }

    public static void spawnDiallingParticles(ServerWorld world, double posX, double posY, double posZ) {
        for(int i = 0; i <= 15; i++) {
            double velX = rand.nextGaussian();
            double velY = rand.nextGaussian();
            double velZ = rand.nextGaussian();

            world.spawnParticle(ParticleTypes.PORTAL, posX, posY, posZ, 1, velX, velY, velZ, 1);
        }
    }

    public static void sendTeleportLeaveParticles(ServerPlayerEntity player) {
        sendTeleportLeaveParticles(player.getServerWorld(), player.getPosX(), player.getPosY(), player.getPosZ());
    }

    public static void sendTeleportLeaveParticles(ServerWorld world, double posX, double posY, double posZ) {
        world.playEvent(2003, new BlockPos(posX, posY + 1.5, posZ), 0);
    }

    public static void sendTeleportArriveParticles(ServerPlayerEntity player) {
        sendTeleportArriveParticles(player.getServerWorld(), player.getPosX(), player.getPosZ(), player.getPosZ());
    }

    public static void sendTeleportArriveParticles(ServerWorld world, double posX, double posY, double posZ) {

    }

    public static boolean hasCellphone(ServerPlayerEntity player) {
        for(ItemStack item : player.inventory.mainInventory) {
            if(item.getItem() instanceof CellphoneBaseItem) {
                return true;
            }
        }

        for(ItemStack item : player.inventory.offHandInventory) {
            if(item.getItem() instanceof CellphoneBaseItem) {
                return true;
            }
        }
        return false;
    }
}