package com.ryanjhuston.emobile.item;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.container.CellphoneItemContainer;
import com.ryanjhuston.emobile.common.inventory.ItemCapabilityProvider;
import com.ryanjhuston.emobile.common.inventory.CellphoneItemStackHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CellphonePearlItem extends CellphoneBaseItem {

    public CellphonePearlItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack itemStack) {
        return new TranslationTextComponent("item.emobile.cellphone");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        if(!world.isRemote) {
            if(hand == Hand.MAIN_HAND) {
                if(player.getHeldItemMainhand().getItem() == this) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new CellphoneItemContainerProvider(this, player.getHeldItemMainhand()));
                }
            }
        }

        return ActionResult.resultPass(player.getHeldItemMainhand());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
        if(!Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.holdShift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.cellphone.pearls", getItemStackHandler(itemStack).getStoredPearls()));
        }
    }

    @Override
    public int getFuel(ItemStack item) {
        return getItemStackHandler(item).getStoredPearls();
    }

    @Override
    public boolean useFuel(ItemStack item) {
        return getItemStackHandler(item).useFuel();
    }

    @Override
    public void refundFuel(ItemStack item) {
        getItemStackHandler(item).refundFuel();
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack item, CompoundNBT oldCap) {
        return new ItemCapabilityProvider();
    }

    private static CellphoneItemStackHandler getItemStackHandler(ItemStack item) {
        IItemHandler itemHandler = item.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(itemHandler == null || !(itemHandler instanceof CellphoneItemStackHandler)) {
            EMobileMod.LOGGER.error("An unexpected ITEM_HANDLER_CAPABILITY was returned.");
            return new CellphoneItemStackHandler(1);
        }
        return (CellphoneItemStackHandler) itemHandler;
    }

    private static class CellphoneItemContainerProvider implements INamedContainerProvider {

        private CellphonePearlItem cellphone;
        private ItemStack item;

        public CellphoneItemContainerProvider(CellphonePearlItem cellphone, ItemStack item) {
            this.cellphone = cellphone;
            this.item = item;
        }

        @Override
        public ITextComponent getDisplayName() {
            return item.getDisplayName();
        }

        @Override
        public CellphoneItemContainer createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
            return CellphoneItemContainer.createContainerServerSide(windowID, playerInventory, cellphone.getItemStackHandler(item), item);
        }
    }
}
