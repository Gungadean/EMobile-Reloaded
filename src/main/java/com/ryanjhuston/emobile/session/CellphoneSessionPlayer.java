package com.ryanjhuston.emobile.session;

import com.ryanjhuston.emobile.item.CellphoneBaseItem;
import com.ryanjhuston.emobile.util.ServerUtils;
import com.ryanjhuston.emobile.util.TeleportUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.ServerWorldInfo;

public class CellphoneSessionPlayer extends CellphoneSessionBase{

    protected ServerPlayerEntity requestingPlayer;
    protected ServerPlayerEntity receivingPlayer;

    public CellphoneSessionPlayer(int duration, ServerPlayerEntity requestingPlayer, ServerPlayerEntity receivingPlayer) {
        super(requestingPlayer.getUniqueID().toString(), duration);
        this.requestingPlayer = requestingPlayer;
        this.receivingPlayer = receivingPlayer;

        requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.start.requesting", receivingPlayer.getDisplayName()), TextFormatting.GOLD), receivingPlayer.getUniqueID());
        receivingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.start.receiving", requestingPlayer.getDisplayName()), TextFormatting.GOLD), requestingPlayer.getUniqueID());
    }

    @Override
    public void tick() {
        if (!ServerUtils.canPlayerTeleport(this.requestingPlayer) || !ServerUtils.canPlayerTeleport(this.receivingPlayer)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isWorldTeleportAllowed(this.requestingPlayer.getServerWorld(), this.receivingPlayer.getServerWorld())) {
            requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.cancel.dimension", ((ServerWorldInfo)this.requestingPlayer.world.getWorldInfo()).getWorldName(), ((ServerWorldInfo)this.receivingPlayer.world.getWorldInfo()).getWorldName()), TextFormatting.RED), receivingPlayer.getUniqueID());
            receivingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.cancel.dimension", ((ServerWorldInfo)this.requestingPlayer.world.getWorldInfo()).getWorldName(), ((ServerWorldInfo)this.receivingPlayer.world.getWorldInfo()).getWorldName()), TextFormatting.RED), requestingPlayer.getUniqueID());
            this.invalidate();
            return;
        }

        if (this.ticks % Math.max(this.countdownSecs - 2, 1) == 0) {
            ServerUtils.spawnDiallingParticles(receivingPlayer);
            ServerUtils.spawnDiallingParticles(requestingPlayer);
        }

        super.tick();
    }

    @Override
    public void onCountdownSecond() {
        if (this.countdownSecs <= 3 || this.countdownSecs % 2 == 0) {
            this.requestingPlayer.sendMessage(new TranslationTextComponent("chat.cellphone.countdown", this.countdownSecs).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.LIGHT_PURPLE))), requestingPlayer.getUniqueID());
        }
    }

    @Override
    public void onCountdownFinished() {
        if(ServerUtils.hasCellphone(receivingPlayer)) {
            ServerUtils.sendTeleportLeaveParticles(this.requestingPlayer);
            ServerUtils.playTeleportSound(requestingPlayer);
            requestingPlayer.teleport(this.receivingPlayer.getServerWorld(), this.receivingPlayer.getPosX(), this.receivingPlayer.getPosY(), this.receivingPlayer.getPosZ(), 0, 0);
            ServerUtils.playTeleportSound(requestingPlayer);
            ServerUtils.sendTeleportArriveParticles(requestingPlayer);
            requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.success.requesting", receivingPlayer.getDisplayName().getUnformattedComponentText()), TextFormatting.GREEN), receivingPlayer.getUniqueID());
            receivingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.success.receiving", requestingPlayer.getDisplayName().getUnformattedComponentText()), TextFormatting.GREEN), requestingPlayer.getUniqueID());
        } else {
            requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.cancel.nophone", receivingPlayer.getDisplayName().getUnformattedComponentText()), TextFormatting.RED), requestingPlayer.getUniqueID());
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        CellphoneSessionsHandler.deacceptPlayer(this.receivingPlayer, this.requestingPlayer.getUniqueID().toString());
    }

    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);
        requestingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.cancel.player", canceledBy), TextFormatting.RED), receivingPlayer.getUniqueID());
        receivingPlayer.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.cancel.player", canceledBy), TextFormatting.RED), requestingPlayer.getUniqueID());
    }

    @Override
    public boolean isPlayerInSession(PlayerEntity player) {
        return player.equals(this.requestingPlayer) || player.equals(this.receivingPlayer);
    }
}
