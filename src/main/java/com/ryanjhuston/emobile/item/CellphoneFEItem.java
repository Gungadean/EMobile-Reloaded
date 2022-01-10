package com.ryanjhuston.emobile.item;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.container.CellphoneFEContainer;
import com.ryanjhuston.emobile.common.energy.EnergyHandler;
import com.ryanjhuston.emobile.config.EMConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CellphoneFEItem extends CellphoneBaseItem {

    public CellphoneFEItem(Properties properties) {
        super(properties);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack itemStack) {
        return new TranslationTextComponent("item.emobile.cellphone.fe");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        if(!world.isRemote) {
            if(hand == Hand.MAIN_HAND) {
                if(player.getHeldItemMainhand().getItem() == this) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new CellphoneFEItem.CellphoneFEContainerProvider(this, player.getHeldItemMainhand()));
                }
            }
        }

        return ActionResult.resultPass(player.getHeldItemMainhand());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.cellphone.fe", getEnergyHandler(itemStack).getEnergyStored()));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.holdShift"));
        }
    }

    public static int getStoredEnergy(ItemStack item) {
        return item.getOrCreateTag().getInt("Energy");
    }

    public static void setStoredEnergy(ItemStack item, int energy) {
        item.getOrCreateTag().putInt("Energy", energy);
    }

    @Override
    public int getFuel(ItemStack item) {
        return getEnergyHandler(item).getEnergyStored();
    }

    @Override
    public boolean useFuel(ItemStack item) {
        return getEnergyHandler(item).useEnergy(EMConfig.feCellphoneEnergyPerUse.get());
    }

    @Override
    public void refundFuel(ItemStack item) {
        getEnergyHandler(item).addEnergy(EMConfig.feCellphoneEnergyPerUse.get());
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack item, CompoundNBT oldCap) {
        return new EnergyHandler(item);
    }

    public static EnergyHandler getEnergyHandler(ItemStack item) {
        IEnergyStorage energyStorage = item.getCapability(CapabilityEnergy.ENERGY).orElse(null);
        if (energyStorage == null || !(energyStorage instanceof EnergyHandler)) {
            EMobileMod.LOGGER.error("An unexpected ENERGY_CAPABILITY was returned.");
            return new EnergyHandler(item);
        }
        return (EnergyHandler) energyStorage;
    }

    private static class CellphoneFEContainerProvider implements INamedContainerProvider {

        private CellphoneFEItem cellphoneFE;
        private ItemStack item;

        public CellphoneFEContainerProvider(CellphoneFEItem cellphoneFE, ItemStack item) {
            this.cellphoneFE = cellphoneFE;
            this.item = item;
        }

        @Override
        public ITextComponent getDisplayName() {
            return item.getDisplayName();
        }

        @Override
        public CellphoneFEContainer createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
            return CellphoneFEContainer.createContainerServerSide(windowID, playerInventory, cellphoneFE.getEnergyHandler(item), item);
        }
    }
}
