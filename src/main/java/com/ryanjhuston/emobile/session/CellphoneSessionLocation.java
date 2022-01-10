package com.ryanjhuston.emobile.session;

import com.ryanjhuston.emobile.client.audio.SoundRegister;
import com.ryanjhuston.emobile.util.ServerUtils;
import com.ryanjhuston.emobile.util.TeleportUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.ServerWorldInfo;

public class CellphoneSessionLocation extends CellphoneSessionBase{

    protected ServerPlayerEntity player;
    protected String unlocalizedLocation;
    protected ServerWorld world;
    protected double posX;
    protected double posY;
    protected double posZ;

    public CellphoneSessionLocation(int duration, String unlocalizedLocation, ServerPlayerEntity player, ServerWorld world, double posX, double posY, double posZ) {
        super(player.getUniqueID().toString(), duration);
        this.unlocalizedLocation = unlocalizedLocation;
        this.player = player;
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;

        ServerUtils.sendChatToPlayer(player.getUniqueID(), ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.start.location", new TranslationTextComponent(unlocalizedLocation)), TextFormatting.GOLD));
    }

    @Override
    public void tick() {
        if (!ServerUtils.canPlayerTeleport(this.player)) {
            this.invalidate();
            return;
        } else if (!TeleportUtils.isWorldTeleportAllowed(this.player.getServerWorld(), this.world)) {
            player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.cancel.dimension", ((ServerWorldInfo)this.player.getServerWorld().getWorldInfo()).getWorldName(), ((ServerWorldInfo)this.world.getWorldInfo()).getWorldName()), TextFormatting.RED), player.getUniqueID());
            this.invalidate();
            return;
        }

        if (this.ticks % Math.max(this.countdownSecs - 2, 1) == 0) {
            ServerUtils.spawnDiallingParticles(player);
        }

        if (this.ticks % 5 == 0 && countdownSecs >= duration-1) {
            player.getServerWorld().playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundRegister.DIAL_TONE.get(), SoundCategory.MASTER, 0.5F, 0.5F);
        }

        super.tick();
    }

    @Override
    public void onCountdownSecond() {
        if (this.countdownSecs <= 3 || this.countdownSecs % 2 == 0) {
            this.player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.countdown", this.countdownSecs), TextFormatting.LIGHT_PURPLE), player.getUniqueID());
        }
    }

    @Override
    public void onCountdownFinished() {
        ServerUtils.playTeleportSound(player);
        ServerUtils.sendTeleportLeaveParticles(player);
        player.teleport(this.world, this.posX + 0.5, this.posY, this.posZ + 0.5, 0, 0);
        ServerUtils.playTeleportSound(player);
        ServerUtils.sendTeleportArriveParticles(player);
        player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.success.location", new TranslationTextComponent(unlocalizedLocation)), TextFormatting.GOLD), player.getUniqueID());
    }

    @Override
    public boolean isPlayerInSession(PlayerEntity player) {
        return this.player.equals(player);
    }

    @Override
    public void cancel(String canceledBy) {
        super.cancel(canceledBy);
        player.sendMessage(ServerUtils.setColor(new TranslationTextComponent("chat.cellphone.cancel", canceledBy), TextFormatting.RED), player.getUniqueID());
    }
}
