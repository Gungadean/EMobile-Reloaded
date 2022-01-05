package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.item.CellphoneBaseItem;
import com.ryanjhuston.emobile.session.CellphoneSessionLocation;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import com.ryanjhuston.emobile.util.ServerUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCellphoneSpawn {

    private String uuid;

    public MessageCellphoneSpawn(String uuid) {
        this.uuid = uuid;
    }

    public static void handle(MessageCellphoneSpawn msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ServerUtils.getPlayerOnServer(msg.uuid);

            if(player != null) {
                player.getServerWorld().getSpawnPoint();

                BlockPos pos = player.getServerWorld().getSpawnPoint();

                if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                    ItemStack heldItem = player.getHeldItemMainhand();
                    if (heldItem != null && heldItem.getItem() instanceof CellphoneBaseItem) {
                        if (player.isCreative() || ((CellphoneBaseItem) heldItem.getItem()).useFuel(heldItem)) {
                            //ServerUtils.sendDiallingSound(player);
                            new CellphoneSessionLocation(8, "chat.cellphone.location.spawn", player, player.getServerWorld(), pos.getX(), pos.getY(), pos.getZ());
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static MessageCellphoneSpawn decode(PacketBuffer buffer)
    {
        MessageCellphoneSpawn message = null;
        try {
            message = new MessageCellphoneSpawn(buffer.readString());
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            EMobileMod.LOGGER.warn("Exception while reading MessageCellphoneSpawn: " + e);
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

    public MessageCellphoneSpawn() {
    }

    public MessageCellphoneSpawn(String player) {
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
    public IMessage onMessage(MessageCellphoneSpawn msg, MessageContext ctx) {
        if (EMConfig.allowTeleportSpawn) {
            EntityPlayerMP player = ServerUtils.getPlayerOnServer(msg.player);
            if (player == null) {
                return null;
            } else if (!TeleportUtils.isDimTeleportAllowed(player.dimension, 0)) {
                ServerUtils.sendChatToPlayer(player.getCommandSenderName(), String.format(StringUtils.translate("chat.cellphone.tryStart.dimension"), player.worldObj.provider.getDimensionName(), player.mcServer.worldServerForDimension(0).provider.getDimensionName()), EnumChatFormatting.RED);
            } else {
                World world = player.mcServer.worldServerForDimension(0);
                ChunkCoordinates spawn = world.getSpawnPoint();
                if (player.worldObj.provider.canRespawnHere()) {
                    world = player.worldObj;
                    spawn = world.getSpawnPoint();
                }
                if (spawn != null) {
                    spawn.posY = world.provider.getAverageGroundLevel();
                    Material mat = world.getBlock(spawn.posX, spawn.posY, spawn.posZ).getMaterial();
                    Material mat2 = world.getBlock(spawn.posX, spawn.posY + 1, spawn.posZ).getMaterial();
                    if (mat.isSolid() || mat.isLiquid() || mat2.isSolid() || mat2.isLiquid()) {
                        do {
                            mat = world.getBlock(spawn.posX, spawn.posY, spawn.posZ).getMaterial();
                            mat2 = world.getBlock(spawn.posX, spawn.posY + 1, spawn.posZ).getMaterial();
                            if (!mat.isSolid() && !mat.isLiquid() && !mat2.isSolid() && !mat2.isLiquid()) {
                                break;
                            }
                            spawn.posY++;
                        } while (mat.isSolid() || mat.isLiquid() || mat2.isSolid() || mat2.isLiquid());
                    } else {
                        do {
                            mat = world.getBlock(spawn.posX, spawn.posY - 1, spawn.posZ).getMaterial();
                            mat2 = world.getBlock(spawn.posX, spawn.posY - 2, spawn.posZ).getMaterial();
                            if ((mat.isSolid() || mat.isLiquid()) && (mat2.isSolid() || mat2.isLiquid())) {
                                break;
                            }
                            spawn.posY--;
                        } while (!mat.isSolid() && !mat.isLiquid() && !mat2.isSolid() && !mat2.isLiquid());
                    }
                    spawn.posY += 0.2D;

                    if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                        ItemStack heldItem = player.getCurrentEquippedItem();
                        if (heldItem != null && heldItem.getItem() instanceof ItemCellphone) {
                            if (player.capabilities.isCreativeMode || ((ItemCellphone) heldItem.getItem()).useFuel(heldItem, player)) {
                                ServerUtils.sendDiallingSound(player);
                                new CellphoneSessionLocation(8, "chat.cellphone.location.spawn", player, 0, spawn.posX, spawn.posY, spawn.posZ);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

 */
}
