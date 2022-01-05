package com.ryanjhuston.emobile.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class MessageDiallingSound {
/*
    public int entityID;

    public MessageDiallingSound() {
    }

    public MessageDiallingSound(int entityID) {
        this.entityID = entityID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityID);
    }

    @Override
    public IMessage onMessage(MessageDiallingSound msg, MessageContext ctx) {
        Entity entity = FMLClientHandler.instance().getClient().theWorld.getEntityByID(msg.entityID);
        if (entity instanceof EntityPlayer) {
            EMobile.proxy.playDiallingSound((EntityPlayer) entity);
        }
        return null;
    }

 */
}
