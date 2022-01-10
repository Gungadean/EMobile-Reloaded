package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.config.EMConfig;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageConfigSync {

    private boolean allowTeleportPlayers;
    private boolean allowTeleportHome;
    private boolean allowTeleportSpawn;
    private int feCellphoneMaxEnergy;
    private int feCellphoneMaxInput;
    private int feCellphoneEnergyPerUse;

    public MessageConfigSync(boolean allowTeleportPlayers, boolean allowTeleportHome,
                             boolean allowTeleportSpawn, int feCellphoneMaxEnergy,
                             int feCellphoneMaxInput, int feCellphoneEnergyPerUse) {
        this.allowTeleportPlayers = allowTeleportPlayers;
        this.allowTeleportSpawn = allowTeleportSpawn;
        this.allowTeleportHome = allowTeleportHome;
        this.feCellphoneMaxEnergy = feCellphoneMaxEnergy;
        this.feCellphoneMaxInput = feCellphoneMaxInput;
        this.feCellphoneEnergyPerUse = feCellphoneEnergyPerUse;
    }

    public static MessageConfigSync decode(PacketBuffer buffer) {
        MessageConfigSync message = null;
        try {
            message = new MessageConfigSync(buffer.readBoolean(), buffer.readBoolean(),
                    buffer.readBoolean(), buffer.readInt(),
                    buffer.readInt(), buffer.readInt());
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            EMobileMod.LOGGER.warn("Exception while reading MessageConfigSync: " + e);
        }
        return message;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(EMConfig.allowTeleportPlayers.get());
        buffer.writeBoolean(EMConfig.allowTeleportHome.get());
        buffer.writeBoolean(EMConfig.allowTeleportSpawn.get());
        buffer.writeInt(EMConfig.feCellphoneMaxEnergy.get());
        buffer.writeInt(EMConfig.feCellphoneMaxInput.get());
        buffer.writeInt(EMConfig.feCellphoneEnergyPerUse.get());
    }

    public static void clientHandle(MessageConfigSync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            EMConfig.client_allowTeleportPlayers = msg.allowTeleportPlayers;
            EMConfig.client_allowTeleportSpawn = msg.allowTeleportSpawn;
            EMConfig.client_allowTeleportHome = msg.allowTeleportHome;
            EMConfig.client_feCellphoneMaxEnergy = msg.feCellphoneMaxEnergy;
            EMConfig.client_feCellphoneMaxInput = msg.feCellphoneMaxInput;
            EMConfig.client_feCellphoneEnergyPerUse = msg.feCellphoneEnergyPerUse;

            EMobileMod.LOGGER.info("Settings synced with server config.");
        });
        ctx.get().setPacketHandled(true);
    }
}
