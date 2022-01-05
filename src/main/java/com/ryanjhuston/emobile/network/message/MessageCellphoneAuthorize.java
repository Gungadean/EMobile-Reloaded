package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import com.ryanjhuston.emobile.util.ServerUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.Color;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageCellphoneAuthorize {

    private String player;
    private String target;

    public MessageCellphoneAuthorize(String player, String target) {
        this.player = player;
        this.target = target;
    }

    public static void handle(MessageCellphoneAuthorize msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            boolean perma = msg.target.startsWith("p:");
            boolean unaccept = msg.target.startsWith("!");
            String authorizedName = msg.target.replaceFirst("p:", "").replaceFirst("!", "");

            ServerPlayerEntity player = ServerUtils.getPlayerOnServer(msg.player);

            if(player == null) {
                return;
            }

            UUID targetUUID = ServerUtils.getUUIDFromName(authorizedName);

            if(targetUUID == null) {
                player.sendMessage(new TranslationTextComponent("chat.cellphone.authorize.unknown", authorizedName).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))), player.getUniqueID());
                return;
            }

            ServerPlayerEntity target = ServerUtils.getPlayerOnServer(targetUUID);

            if(player.equals(target)) {
                player.sendMessage(new TranslationTextComponent("chat.cellphone.authorize.self").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))), player.getUniqueID());
                return;
            }

            if (!unaccept) {
                if (CellphoneSessionsHandler.acceptPlayer(player, targetUUID.toString(), perma)) {
                    ServerUtils.sendChatToPlayers(player.getUniqueID(), targetUUID, new TranslationTextComponent("chat.cellphone.authorize.success" + (perma ? ".perma" : ""), authorizedName));
                } else {
                    ServerUtils.sendChatToPlayers(player.getUniqueID(), targetUUID, new TranslationTextComponent("chat.cellphone.authorize.already", authorizedName));
                }
            } else {
                if (CellphoneSessionsHandler.deacceptPlayer(player, targetUUID.toString())) {
                    ServerUtils.sendChatToPlayers(player.getUniqueID(), targetUUID, new TranslationTextComponent("chat.cellphone.unauthorize.success", authorizedName));
                } else {
                    ServerUtils.sendChatToPlayers(player.getUniqueID(), targetUUID, new TranslationTextComponent("chat.cellphone.unauthorize.already", authorizedName));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static MessageCellphoneAuthorize decode(PacketBuffer buffer)
    {
        MessageCellphoneAuthorize message = null;
        try {
            message = new MessageCellphoneAuthorize(buffer.readString(), buffer.readString());
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            EMobileMod.LOGGER.warn("Exception while reading MessageCellphoneAuthorize: " + e);
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

/*
    private String accepting;
    private String accepted;

    public MessageCellphoneAuthorize(String accepting, String accepted) {
        this.accepting = accepting;
        this.accepted = accepted;
    }

    public static void handle(MessageCellphoneAuthorize msg, MessagePassingQueue.Supplier<NetworkEvent.Context> ctx) {
        if (EMConfig.allowTeleportPlayers) {
            ctx.get().enqueueWork(() -> {
                        boolean perma = msg.accepted.startsWith("p:");
                        boolean unaccept = msg.accepted.startsWith("!");
                        String accepted = msg.accepted.replaceFirst("p:", "").replaceFirst("!", "");

                        ServerPlayerEntity acceptingPlayer = ServerUtils.getPlayerOnServer(msg.accepting);
                        ServerPlayerEntity acceptedPlayer = ServerUtils.getPlayerOnServer(accepted);
                        if (acceptingPlayer == null) {
                            return null;
                        } else if (acceptedPlayer == null) {
                            ServerUtils.sendChatToPlayer(acceptingPlayer.getUniqueID(), new TranslationTextComponent("chat.cellphone.authorize.unknown").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))));
                        } else if (acceptingPlayer.equals(acceptedPlayer)) {
                            ServerUtils.sendChatToPlayer(acceptingPlayer.getUniqueID(), new TranslationTextComponent("chat.cellphone.authorize.self").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))));
                        } else {
                            if (!unaccept) {
                                if (CellphoneSessionsHandler.acceptPlayer(acceptingPlayer, acceptedPlayer, perma)) {
                                    ServerUtils.sendChatToPlayers(acceptingPlayer.getUniqueID(), acceptedPlayer.getUniqueID(), new TranslationTextComponent("chat.cellphone.authorize.success" + (perma ? ".perma" : "")));
                                } else {
                                    ServerUtils.sendChatToPlayers(acceptingPlayer.getUniqueID(), acceptedPlayer.getUniqueID(), new TranslationTextComponent("chat.cellphone.authorize.already"));
                                }
                            } else {
                                if (CellphoneSessionsHandler.deacceptPlayer(acceptingPlayer, acceptedPlayer, true)) {
                                    ServerUtils.sendChatToPlayers(acceptingPlayer.getUniqueID(), acceptedPlayer.getUniqueID(), new TranslationTextComponent("chat.cellphone.unauthorize.success"));
                                } else {
                                    ServerUtils.sendChatToPlayers(acceptingPlayer.getUniqueID(), acceptedPlayer.getUniqueID(), new TranslationTextComponent("chat.cellphone.unauthorize.already"));
                                }
                            }
                        }
                    }
            );
        }
    }
 */
}