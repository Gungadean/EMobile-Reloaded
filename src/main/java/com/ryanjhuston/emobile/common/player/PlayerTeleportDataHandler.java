package com.ryanjhuston.emobile.common.player;

import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;

public class PlayerTeleportDataHandler {

    private List<String> accepted = new ArrayList<>();
    private List<String> paccepted = new ArrayList<>();

    public PlayerTeleportDataHandler() {}

    public boolean isAccepted(String uuid) {
        return accepted.contains(uuid);
    }

    public boolean isPAccepted(String uuid) {
        return paccepted.contains(uuid);
    }

    public List<String> getAccepted() {
        return accepted;
    }

    public void addAccepted(String uuid) {
        if(!accepted.contains(uuid)) {
            accepted.add(uuid);
        }
    }

    public void removeAccepted(String uuid) {
        accepted.remove(uuid);
    }

    public List<String> getPAccepted() {
        return paccepted;
    }

    public void addPAccepted(String uuid) {
        if(!paccepted.contains(uuid)) {
            paccepted.add(uuid);
        }
    }

    public void removePAccepted(String uuid) {
        paccepted.remove(uuid);
    }

    public void setAccepted(List<String> accepted) {
        if(!accepted.isEmpty()) {
            this.accepted.clear();
        }

        this.accepted.addAll(accepted);
    }

    public void setPAccepted(List<String> paccepted) {
        if(!paccepted.isEmpty()){
            this.paccepted.clear();
        }

        this.paccepted.addAll(paccepted);
    }

    public static PlayerTeleportDataHandler createDefault() {
        return new PlayerTeleportDataHandler();
    }

    public static class PlayerDataStorage implements Capability.IStorage<PlayerTeleportDataHandler> {

        @Override
        public INBT writeNBT(Capability<PlayerTeleportDataHandler> cap, PlayerTeleportDataHandler instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT acceptedList = new ListNBT();
            ListNBT pacceptedList = new ListNBT();

            for(String part : instance.getAccepted()) {
                acceptedList.add(StringNBT.valueOf(part));
            }

            for(String part : instance.getPAccepted()) {
                pacceptedList.add(StringNBT.valueOf(part));
            }

            nbt.put("accepted", acceptedList);
            nbt.put("paccepted", pacceptedList);

            return nbt;
        }

        @Override
        public void readNBT(Capability<PlayerTeleportDataHandler> cap, PlayerTeleportDataHandler instance, Direction side, INBT nbt) {
            List<String> accepted = new ArrayList<>();
            List<String> paccepted = new ArrayList<>();

            ListNBT acceptedNBT = ((CompoundNBT)nbt).getList("accepted", 8);
            ListNBT pacceptedNBT = ((CompoundNBT)nbt).getList("paccepted", 8);

            for(INBT part : acceptedNBT) {
                accepted.add(part.getString());
            }

            for(INBT part : pacceptedNBT) {
                paccepted.add(part.getString());
            }

            instance.setAccepted(accepted);
            instance.setPAccepted(paccepted);
        }
    }
}
