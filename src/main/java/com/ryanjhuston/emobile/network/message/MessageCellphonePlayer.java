package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.config.EMConfig;
import com.ryanjhuston.emobile.item.CellphoneBaseItem;
import com.ryanjhuston.emobile.session.CellphoneSessionPlayer;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import com.ryanjhuston.emobile.util.ServerUtils;
import com.ryanjhuston.emobile.util.TeleportUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCellphonePlayer {

    private String player;
    private String target;

    public MessageCellphonePlayer(String player, String target) {
        this.player = player;
        this.target = target;
    }

    public static void handle(MessageCellphonePlayer msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(EMConfig.allowTeleportPlayers.get()) {
                ServerPlayerEntity requestingPlayer = ServerUtils.getPlayerOnServer(msg.player);
                ServerPlayerEntity receivingPlayer = ServerUtils.getPlayerOnServer(ServerUtils.getUUIDFromName(msg.target));

                if (requestingPlayer == null) {
                } else if (receivingPlayer == null) {
                    requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.unknown", msg.target), TextFormatting.RED), requestingPlayer.getUniqueID());
                } else if (!TeleportUtils.isWorldTeleportAllowed(requestingPlayer.getServerWorld(), receivingPlayer.getServerWorld())) {
                    requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.world",
                                    ((ServerWorldInfo) requestingPlayer.getServerWorld().getWorldInfo()).getWorldName(),
                                    ((ServerWorldInfo) receivingPlayer.getServerWorld().getWorldInfo()).getWorldName()),
                            TextFormatting.RED), requestingPlayer.getUniqueID());
                } else {
                    if (!CellphoneSessionsHandler.isPlayerInSession(requestingPlayer)) {
                        if (CellphoneSessionsHandler.isPlayerAccepted(requestingPlayer, receivingPlayer)) {
                            if(ServerUtils.hasCellphone(receivingPlayer)) {
                                ItemStack heldItem = requestingPlayer.getHeldItemMainhand();
                                if (heldItem != null && heldItem.getItem() instanceof CellphoneBaseItem) {
                                    if (requestingPlayer.isCreative() || ((CellphoneBaseItem) heldItem.getItem()).useFuel(heldItem)) {
                                        ServerUtils.playDiallingSound(requestingPlayer);
                                        new CellphoneSessionPlayer(EMConfig.teleportDuration.get(), requestingPlayer, receivingPlayer);
                                    }
                                }
                            } else {
                                requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.cancel.nophone", receivingPlayer.getDisplayName().getString()), TextFormatting.RED), requestingPlayer.getUniqueID());
                            }
                        } else {
                            requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.unauthorized", receivingPlayer.getDisplayName().getString()), TextFormatting.RED), requestingPlayer.getUniqueID());
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static MessageCellphonePlayer decode(PacketBuffer buffer)
    {
        MessageCellphonePlayer message = null;
        try {
            message = new MessageCellphonePlayer(buffer.readString(), buffer.readString());
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            EMobileMod.LOGGER.warn("Exception while reading MessageCellphonePlayer: " + e);
        }
        return message;
    }

    public void encode(PacketBuffer buffer)
    {
        if (player == null || player.equals("")) return;
        if (target == null || target.equals("")) return;
        buffer.writeString(player);
        buffer.writeString(target);
    }
}
