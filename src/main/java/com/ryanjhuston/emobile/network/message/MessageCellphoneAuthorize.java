package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import com.ryanjhuston.emobile.util.ServerUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.w3c.dom.Text;

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
            boolean perma = msg.target.startsWith("p:") || msg.target.startsWith("P:");
            boolean unaccept = msg.target.startsWith("!");
            String authorizedName = msg.target.replaceFirst("p:", "").replaceFirst("P:", "").replaceFirst("!", "");

            ServerPlayerEntity player = ServerUtils.getPlayerOnServer(msg.player);

            if(player == null) {
                return;
            }

            UUID targetUUID = ServerUtils.getUUIDFromName(authorizedName);

            if(targetUUID == null) {
                player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.authorize.unknown", authorizedName), TextFormatting.RED), player.getUniqueID());
                return;
            }

            ServerPlayerEntity target = ServerUtils.getPlayerOnServer(targetUUID);

            if(player.equals(target)) {
                player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.authorize.self"), TextFormatting.RED), player.getUniqueID());
                return;
            }

            if (!unaccept) {
                if (CellphoneSessionsHandler.acceptPlayer(player, targetUUID.toString(), perma)) {
                    player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.authorize.success" + (perma ? ".perma" : ""), authorizedName), TextFormatting.GREEN), player.getUniqueID());
                    player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.authorize.success" + (perma ? ".perma" : "") + ".other", player.getDisplayName()), TextFormatting.GREEN), player.getUniqueID());
                } else {
                    ServerUtils.sendChatToPlayers(player.getUniqueID(), targetUUID, ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.authorize.already", authorizedName), TextFormatting.RED));
                }
            } else {
                if (CellphoneSessionsHandler.deacceptPlayer(player, targetUUID.toString())) {
                    ServerUtils.sendChatToPlayers(player.getUniqueID(), targetUUID, ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.unauthorize.success", authorizedName), TextFormatting.GREEN));
                } else {
                    ServerUtils.sendChatToPlayers(player.getUniqueID(), targetUUID, ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.unauthorize.already", authorizedName), TextFormatting.RED));
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
}