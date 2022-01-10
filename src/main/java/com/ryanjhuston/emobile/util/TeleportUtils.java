package com.ryanjhuston.emobile.util;

import com.ryanjhuston.emobile.config.EMConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.ServerWorldInfo;

public class TeleportUtils {

    public static void teleportPlayer(ServerPlayerEntity from, PlayerEntity to) {
        teleportPlayer(from, to.getEntityWorld(), to.getPosX(), to.getPosY(), to.getPosZ());
    }

    public static void teleportPlayer(ServerPlayerEntity player, BlockPos pos) {
        teleportPlayer(player, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void teleportPlayer(ServerPlayerEntity player, double posX, double posY, double posZ) {
        player.dismount();
        if(player.isBeingRidden()) {
            player.getRidingEntity().dismount();
        }

        player.setPositionAndUpdate(posX, posY, posZ);
    }

    public static void teleportPlayer(ServerPlayerEntity player, World world, double posX, double posY, double posZ) {
        if(!player.getEntityWorld().equals(world)) {
            player.setWorld(world);
        }

        teleportPlayer(player, posX, posY, posZ);
    }

    public static boolean isWorldTeleportAllowed(World from, World to) {
        if (from.equals(to)) {
            return true;
        }

        if (!EMConfig.worldWhitelist.get()) {
            return !configContainsWorld(((ServerWorldInfo) from.getWorldInfo()).getWorldName()) && !configContainsWorld(((ServerWorldInfo) to.getWorldInfo()).getWorldName());
        } else {
            return configContainsWorld(from.getProviderName()) && configContainsWorld(to.getProviderName());
        }
    }

    public static boolean configContainsWorld(String name) {
        for (String i : EMConfig.worldBlacklist.get()) {
            if (i.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
