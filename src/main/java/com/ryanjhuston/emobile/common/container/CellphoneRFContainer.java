package com.ryanjhuston.emobile.common.container;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.core.init.ContainerTypesInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class CellphoneRFContainer extends CellphoneBaseContainer {

    public static CellphoneRFContainer createContainerServerSide(int windowId, PlayerInventory playerInventory, ItemStack item) {
        return new CellphoneRFContainer(windowId, playerInventory, item);
    }

    public static CellphoneRFContainer createContainerClientSide(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        try {
            //Change to RF handler.
            //CellphoneItemStackHandler itemStackHandler = new CellphoneItemStackHandler(1);

            return new CellphoneRFContainer(windowId, playerInventory, ItemStack.EMPTY);
        } catch (IllegalArgumentException e) {
            EMobileMod.LOGGER.warn(e);
        }
        return null;
    }

    public CellphoneRFContainer(int windowId, PlayerInventory playerInventory, ItemStack item) {
        super(ContainerTypesInit.CELLPHONE_RF_CONTAINER_TYPE.get(), windowId);

        this.cellphone = item;

        super.createInventory(playerInventory);
    }
}
