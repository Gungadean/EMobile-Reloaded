package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import com.ryanjhuston.emobile.util.ServerUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCellphoneCancel {

    private String uuid;

    public MessageCellphoneCancel(String uuid) {
        this.uuid = uuid;
    }

    public static void handle(MessageCellphoneCancel msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ServerUtils.getPlayerOnServer(msg.uuid);

            if(player != null) {
                CellphoneSessionsHandler.cancelSessionsForPlayer(player);
                player.sendMessage(new TranslationTextComponent("chat.cellphone.cancel"), player.getUniqueID());
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static MessageCellphoneCancel decode(PacketBuffer buffer)
    {
        MessageCellphoneCancel message = null;
        try {
            message = new MessageCellphoneCancel(buffer.readString());
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            EMobileMod.LOGGER.warn("Exception while reading MessageCellphoneCancel: " + e);
        }
        return message;
    }

    public void encode(PacketBuffer buffer)
    {
        if (uuid == null || uuid.equals("")) return;
        buffer.writeString(uuid);
    }

/*
    private String player;

    public MessageCellphoneCancel() {
    }

    public MessageCellphoneCancel(String player) {
        this.player = player;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.player = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.player);
    }

    @Override
    public IMessage onMessage(MessageCellphoneCancel msg, MessageContext ctx) {
        EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.player);
        if (player == null) {
            return null;
        } else {
            CellphoneSessionsHandler.cancelSessionsForPlayer(player);
        }
        return null;
    }

 */
}
