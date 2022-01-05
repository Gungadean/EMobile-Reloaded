package com.ryanjhuston.emobile.common.player;

import net.minecraft.nbt.*;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;

public class PlayerTeleportData {

    private List<String> accepted = new ArrayList<>();
    private List<String> paccepted = new ArrayList<>();

    public PlayerTeleportData() {}

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
        this.accepted = accepted;
    }

    public void setPAccepted(List<String> paccepted) {
        this.paccepted = paccepted;
    }

    public static PlayerTeleportData createDefault() {
        return new PlayerTeleportData();
    }

    public static class PlayerTeleportDataNBTStorage implements Capability.IStorage<PlayerTeleportData> {

        @Override
        public INBT writeNBT(Capability<PlayerTeleportData> cap, PlayerTeleportData instance, Direction side) {
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
        public void readNBT(Capability<PlayerTeleportData> cap, PlayerTeleportData instance, Direction side, INBT nbt) {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;

            List<String> accepted = new ArrayList<>();
            List<String> paccepted = new ArrayList<>();

            ListNBT acceptedNBT = compoundNBT.getList("accepted", 0);
            ListNBT pacceptedNBT = compoundNBT.getList("paccepted", 0);

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
