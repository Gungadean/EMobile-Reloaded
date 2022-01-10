package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.config.EMConfig;
import com.ryanjhuston.emobile.item.CellphoneBaseItem;
import com.ryanjhuston.emobile.session.CellphoneSessionLocation;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import com.ryanjhuston.emobile.util.ServerUtils;
import com.ryanjhuston.emobile.util.TeleportUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCellphoneSpawn {

    private String uuid;

    public MessageCellphoneSpawn(String uuid) {
        this.uuid = uuid;
    }

    public static void handle(MessageCellphoneSpawn msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(EMConfig.allowTeleportSpawn.get()) {
                ServerPlayerEntity player = ServerUtils.getPlayerOnServer(msg.uuid);

                if (player != null) {
                    ServerWorld world = player.getServer().getWorlds().iterator().next();

                    BlockPos pos = world.getSpawnPoint();

                    if(!TeleportUtils.isWorldTeleportAllowed(player.getServerWorld(), world)) {
                        player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.world",
                                        ((ServerWorldInfo) player.getServerWorld().getWorldInfo()).getWorldName(),
                                        ((ServerWorldInfo) world.getWorldInfo()).getWorldName()),
                                TextFormatting.RED), player.getUniqueID());
                        return;
                    }

                    if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                        ItemStack heldItem = player.getHeldItemMainhand();
                        if (heldItem != null && heldItem.getItem() instanceof CellphoneBaseItem) {
                            if (player.isCreative() || ((CellphoneBaseItem) heldItem.getItem()).useFuel(heldItem)) {
                                ServerUtils.playDiallingSound(player);
                                new CellphoneSessionLocation(EMConfig.teleportDuration.get(), "chat.cellphone.location.spawn", player, world, pos.getX(), pos.getY(), pos.getZ());
                            }
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
}
