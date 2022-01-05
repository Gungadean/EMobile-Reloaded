package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.util.ServerUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCellphoneHome {

    private String uuid;

    public MessageCellphoneHome(String uuid) {
        this.uuid = uuid;
    }

    public static void handle(MessageCellphoneHome msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ServerUtils.getPlayerOnServer(msg.uuid);

            if(player != null) {
                BlockPos bed = player.getBedPosition().orElse(null);
                //Need to figure out how to get bed position world
                if(bed != null) {

                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static MessageCellphoneHome decode(PacketBuffer buffer)
    {
        MessageCellphoneHome message = null;
        try {
            message = new MessageCellphoneHome(buffer.readString());
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            EMobileMod.LOGGER.warn("Exception while reading MessageCellphoneHome: " + e);
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

    public MessageCellphoneHome() {
    }

    public MessageCellphoneHome(String player) {
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
    public IMessage onMessage(MessageCellphoneHome msg, MessageContext ctx) {
        if (EMConfig.allowTeleportHome) {
            EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.player);
            if (player == null) {
                return null;
            } else {
                ChunkCoordinates bed = player.getBedLocation(player.dimension);
                World world = player.worldObj;
                if (bed == null && player.dimension != 0) {
                    if (TeleportUtils.isDimTeleportAllowed(player.dimension, 0)) {
                        bed = player.getBedLocation(0);
                        world = player.mcServer.worldServerForDimension(0);
                    } else {
                        ServerUtils.sendChatToPlayer(player.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), player.worldObj.provider.getDimensionName(), player.mcServer.worldServerForDimension(0).provider.getDimensionName()), EnumChatFormatting.RED);
                        return null;
                    }
                }
                if (bed != null) {
                    Block bedBlock = world.getBlock(bed.posX, bed.posY, bed.posZ);
                    if (bedBlock != null && !(bedBlock instanceof BlockBed) && !EMConfig.bedBlocks.contains(bedBlock.getClass().getName())) {
                        bed = null;
                    }
                }
                if (bed != null) {
                    bed = world.getBlock(bed.posX, bed.posY, bed.posZ).getBedSpawnPosition(world, bed.posX, bed.posY, bed.posZ, player);
                }
                if (bed != null) {
                    if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                        ItemStack heldItem = player.getCurrentEquippedItem();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (player.capabilities.isCreativeMode || ((ItemCellphone) heldItem.getItem()).useFuel(heldItem, player)) {
                                ServerUtils.sendDiallingSound(player);
                                new CellphoneSessionLocation(8, "chat.cellphone.location.home", player, 0, bed.posX, bed.posY, bed.posZ);
                            }
                        }
                    }
                } else {
                    ServerUtils.sendChatToPlayer(player.getCommandSenderName(), StringUtils.translate("chat.cellphone.tryStart.bedmissing.1"), EnumChatFormatting.RED);
                    ServerUtils.sendChatToPlayer(player.getCommandSenderName(), StringUtils.translate("chat.cellphone.tryStart.bedmissing.2"), EnumChatFormatting.RED);
                }
            }
        }

        return null;
    }

 */
}
