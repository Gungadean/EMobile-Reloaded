package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.item.CellphoneBaseItem;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import com.ryanjhuston.emobile.util.ServerUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;
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
                if(!CellphoneSessionsHandler.isPlayerInSession(player)) {
                    player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.nocancel"), TextFormatting.RED), player.getUniqueID());
                } else {
                    CellphoneSessionsHandler.cancelSessionsForPlayer(player);

                    ItemStack heldItem = player.getHeldItemMainhand();
                    if (heldItem != null && heldItem.getItem() instanceof CellphoneBaseItem) {
                        if (!player.isCreative()) {
                            ((CellphoneBaseItem) heldItem.getItem()).refundFuel(heldItem);
                        }
                    }
                }
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
}
