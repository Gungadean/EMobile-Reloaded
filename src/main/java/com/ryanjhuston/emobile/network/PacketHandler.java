package com.ryanjhuston.emobile.network;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.network.message.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

import static net.minecraftforge.fml.network.NetworkDirection.PLAY_TO_SERVER;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1.0";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(
            EMobileMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
            );

    public static void setup() {
        EMobileMod.LOGGER.info("Registering network messages");

        INSTANCE.registerMessage(0, MessageCellphoneAuthorize.class, MessageCellphoneAuthorize::encode, MessageCellphoneAuthorize::decode, MessageCellphoneAuthorize::handle, Optional.of(PLAY_TO_SERVER));
        INSTANCE.registerMessage(1, MessageCellphoneCancel.class, MessageCellphoneCancel::encode, MessageCellphoneCancel::decode, MessageCellphoneCancel::handle, Optional.of(PLAY_TO_SERVER));
        INSTANCE.registerMessage(2, MessageCellphoneHome.class, MessageCellphoneHome::encode, MessageCellphoneHome::decode, MessageCellphoneHome::handle, Optional.of(PLAY_TO_SERVER));
        //INSTANCE.registerMessage(3, MessageCellphonePlayer.class, MessageCellphonePlayer::encode, MessageCellphonePlayer::decode, MessageCellphonePlayer::handle, Optional.of(PLAY_TO_SERVER));
        INSTANCE.registerMessage(4, MessageCellphoneSpawn.class, MessageCellphoneSpawn::encode, MessageCellphoneSpawn::decode, MessageCellphoneSpawn::handle, Optional.of(PLAY_TO_SERVER));

        /*INSTANCE.registerMessage(MessageConfigSync.class, MessageConfigSync.class, 5, Side.CLIENT);
        INSTANCE.registerMessage(MessageDiallingSound.class, MessageDiallingSound.class, 6, Side.CLIENT);
        INSTANCE.registerMessage(MessageDiallingParticles.class, MessageDiallingParticles.class, 7, Side.CLIENT);
        INSTANCE.registerMessage(MessageTeleportParticles.class, MessageTeleportParticles.class, 8, Side.CLIENT);*/
    }
}
