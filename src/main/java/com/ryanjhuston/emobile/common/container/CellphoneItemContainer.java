package com.ryanjhuston.emobile.common.container;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.inventory.CellphoneItemStackHandler;
import com.ryanjhuston.emobile.core.init.ContainerTypesInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

public class CellphoneItemContainer extends CellphoneBaseContainer {

    public static CellphoneItemContainer createContainerServerSide(int windowId, PlayerInventory playerInventory, final CellphoneItemStackHandler itemStackHandler, ItemStack item) {
        return new CellphoneItemContainer(windowId, playerInventory, itemStackHandler, item);
    }

    public static CellphoneItemContainer createContainerClientSide(int windowId, PlayerInventory playerInventory, PacketBuffer data) {
        try {
            CellphoneItemStackHandler itemStackHandler = new CellphoneItemStackHandler(1);

            return new CellphoneItemContainer(windowId, playerInventory, itemStackHandler, ItemStack.EMPTY);
        } catch (IllegalArgumentException e) {
            EMobileMod.LOGGER.warn(e);
        }
        return null;
    }

    public CellphoneItemContainer(final int windowId, final PlayerInventory playerInventory, final CellphoneItemStackHandler itemStackHandler, ItemStack item) {
        super(ContainerTypesInit.CELLPHONE_ITEM_CONTAINER_TYPE.get(), windowId);

        this.cellphone = item;
        this.itemStackHandler = itemStackHandler;

        super.createInventory(playerInventory);

        //Cellphone Inventory
        this.addSlot(new SlotItemHandler(this.itemStackHandler, 0, 152, 8));
    }

    @Override
    public void detectAndSendChanges() {
        if (itemStackHandler.isDirty()) {
            CompoundNBT nbt = cellphone.getOrCreateTag();
            int dirtyCounter = nbt.getInt("dirtyCounter");
            nbt.putInt("dirtyCounter", dirtyCounter + 1);
            cellphone.setTag(nbt);
        }
        super.detectAndSendChanges();
    }
}
