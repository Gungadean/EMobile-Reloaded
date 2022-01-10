package com.ryanjhuston.emobile.common.container;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.energy.EnergyHandler;
import com.ryanjhuston.emobile.core.init.ContainerTypesInit;
import com.ryanjhuston.emobile.item.CellphoneFEItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class CellphoneFEContainer extends CellphoneBaseContainer {

    public EnergyHandler energyHandler;

    public static CellphoneFEContainer createContainerServerSide(int windowId, PlayerInventory playerInventory, EnergyHandler energyHandler, ItemStack item) {
        return new CellphoneFEContainer(windowId, playerInventory, energyHandler, item);
    }

    public static CellphoneFEContainer createContainerClientSide(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        try {
            EnergyHandler energyHandler = CellphoneFEItem.getEnergyHandler(playerInventory.getCurrentItem());

            return new CellphoneFEContainer(windowId, playerInventory, energyHandler, playerInventory.getCurrentItem());
        } catch (IllegalArgumentException e) {
            EMobileMod.LOGGER.warn(e);
        }
        return null;
    }

    public CellphoneFEContainer(int windowId, PlayerInventory playerInventory, EnergyHandler energyHandler, ItemStack item) {
        super(ContainerTypesInit.CELLPHONE_RF_CONTAINER_TYPE.get(), windowId);

        this.cellphone = item;
        this.energyHandler = energyHandler;

        super.createInventory(playerInventory);
    }
}
