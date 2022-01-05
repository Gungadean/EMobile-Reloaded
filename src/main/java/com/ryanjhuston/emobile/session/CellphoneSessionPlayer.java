package com.ryanjhuston.emobile.session;

import com.ryanjhuston.emobile.util.ServerUtils;
import com.ryanjhuston.emobile.util.StringUtils;
import com.ryanjhuston.emobile.util.TeleportUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.eventbus.api.Event;

import java.util.UUID;

public class CellphoneSessionPlayer extends CellphoneSessionBase{

    protected ServerPlayerEntity requestingPlayer;
    protected ServerPlayerEntity receivingPlayer;

    public CellphoneSessionPlayer(int duration, ServerPlayerEntity requestingPlayer, ServerPlayerEntity receivingPlayer) {
        super(requestingPlayer.getUniqueID().toString(), duration);
        this.requestingPlayer = requestingPlayer;
        this.receivingPlayer = receivingPlayer;

        requestingPlayer.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.start.request", TextFormatting.GOLD), receivingPlayer.getUniqueID());
        receivingPlayer.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.start.receiving", TextFormatting.GOLD), requestingPlayer.getUniqueID());
    }

    @Override
    public void tick() {
        if (!ServerUtils.canPlayerTeleport(this.requestingPlayer) || !ServerUtils.canPlayerTeleport(this.receivingPlayer)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isWorldTeleportAllowed(this.requestingPlayer.getServerWorld(), this.receivingPlayer.getServerWorld())) {
            //String msg = String.format(StringUtils.translate("chat.cellphone.cancel.dimension"), this.requestingPlayer.worldObj.provider.getDimensionName(), this.receivingPlayer.worldObj.provider.getDimensionName());
            requestingPlayer.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.cancel.dimension", TextFormatting.RED), receivingPlayer.getUniqueID());
            receivingPlayer.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.cancel.dimension", TextFormatting.RED), requestingPlayer.getUniqueID());
            this.invalidate();
            return;
        }

        if (this.ticks % Math.max(this.countdownSecs - 2, 1) == 0) {
            ServerUtils.sendDiallingParticles(this.receivingPlayer);
            ServerUtils.sendDiallingParticles(this.requestingPlayer);
        }

        super.tick();
    }

    @Override
    public void onCountdownSecond() {
        if (this.countdownSecs <= 3 || this.countdownSecs % 2 == 0) {
            this.requestingPlayer.sendMessage(new TranslationTextComponent("chat.cellphone.countdown", this.countdownSecs).setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.LIGHT_PURPLE))), UUID.fromString(""));
        }
    }

    @Override
    public void onCountdownFinished() {
        //this.requestingPlayer.worldObj.playSoundAtEntity(this.requestingPlayer, "mob.endermen.portal", 1.0F, 1.0F);
        //this.requestingPlayer.getServerWorld().playSound(this.requestingPlayer, this.requestingPlayer.getPosition(), Event.HasResult);
        ServerUtils.sendTeleportParticles(this.receivingPlayer);
        TeleportUtils.teleportPlayer(this.requestingPlayer, this.receivingPlayer);
        //this.requestingPlayer.getServerWorld().playSound(this.requestingPlayer, "mob.endermen.portal", 1.0F, 1.0F);
        ServerUtils.sendTeleportParticles(this.receivingPlayer);
        requestingPlayer.sendMessage(new TranslationTextComponent("chat.cellphone.success.request", receivingPlayer.getDisplayName().toString()), receivingPlayer.getUniqueID());
        receivingPlayer.sendMessage(new TranslationTextComponent("chat.cellphone.success.receiving", requestingPlayer.getDisplayName().toString()), requestingPlayer.getUniqueID());
    }

    @Override
    public void invalidate() {
        super.invalidate();
        CellphoneSessionsHandler.deacceptPlayer(this.receivingPlayer, this.requestingPlayer.getUniqueID().toString());
    }

    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);
        //TranslationTextComponent msg = new TranslationTextComponent("chat.cellphone.cancel.player")("chat.cellphone.cancel.player"), canceledBy);
        requestingPlayer.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.cancel.player", TextFormatting.RED), receivingPlayer.getUniqueID());
        receivingPlayer.sendMessage(ServerUtils.getTranslationTextComponent("chat.cellphone.cancel.player", TextFormatting.RED), requestingPlayer.getUniqueID());
    }

    @Override
    public boolean isPlayerInSession(PlayerEntity player) {
        return player.equals(this.requestingPlayer) || player.equals(this.receivingPlayer);
    }
}
