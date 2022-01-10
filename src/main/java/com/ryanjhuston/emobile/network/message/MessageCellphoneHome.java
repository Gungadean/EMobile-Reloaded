package com.ryanjhuston.emobile.network.message;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.config.EMConfig;
import com.ryanjhuston.emobile.item.CellphoneBaseItem;
import com.ryanjhuston.emobile.session.CellphoneSessionLocation;
import com.ryanjhuston.emobile.session.CellphoneSessionsHandler;
import com.ryanjhuston.emobile.util.ServerUtils;
import com.ryanjhuston.emobile.util.TeleportUtils;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCellphoneHome {

    private String uuid;

    public MessageCellphoneHome(String uuid) {
        this.uuid = uuid;
    }

    public static void handle(MessageCellphoneHome msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(EMConfig.allowTeleportHome.get()) {
                ServerPlayerEntity player = ServerUtils.getPlayerOnServer(msg.uuid);

                if (player != null) {
                    ServerWorld world = player.getServer().getWorlds().iterator().next();
                    BlockPos bed = player.func_241140_K_();

                    if (!TeleportUtils.isWorldTeleportAllowed(player.getServerWorld(), world)) {
                        player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.world",
                                        ((ServerWorldInfo) player.getServerWorld().getWorldInfo()).getWorldName(),
                                        ((ServerWorldInfo) world.getWorldInfo()).getWorldName()),
                                TextFormatting.RED), player.getUniqueID());
                        return;
                    }

                    if (bed != null) {
                        if (world.getBlockState(bed).getBlock() instanceof BedBlock) {
                            if (!CellphoneSessionsHandler.isPlayerInSession(player)) {
                                ItemStack heldItem = player.getHeldItemMainhand();
                                if (heldItem != null && heldItem.getItem() instanceof CellphoneBaseItem) {
                                    if (player.isCreative() || ((CellphoneBaseItem) heldItem.getItem()).useFuel(heldItem)) {
                                        ServerUtils.playDiallingSound(player);
                                        BedBlock bedBlock = (BedBlock) world.getBlockState(bed).getBlock();

                                        Vector3d respawn = bedBlock.getRespawnPosition(world.getBlockState(bed), EntityType.PLAYER, world, bed, 0, player).get();

                                        new CellphoneSessionLocation(EMConfig.teleportDuration.get(), "chat.cellphone.location.home", player, world, respawn.x + 0.5, respawn.y, respawn.z + 0.5);
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.bedmissing.1"), TextFormatting.RED), player.getUniqueID());
                            player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.bedmissing.2"), TextFormatting.RED), player.getUniqueID());
                        }
                    } else {
                        player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.bedmissing.1"), TextFormatting.RED), player.getUniqueID());
                        player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.tryStart.bedmissing.2"), TextFormatting.RED), player.getUniqueID());
                    }
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
}
