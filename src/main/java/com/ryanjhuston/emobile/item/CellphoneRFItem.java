package com.ryanjhuston.emobile.item;

import com.ryanjhuston.emobile.EMobileMod;
import com.ryanjhuston.emobile.common.container.CellphoneRFContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CellphoneRFItem extends Item {

    private int energy;

    public CellphoneRFItem(Properties properties) {
        super(properties);

        properties.maxStackSize(1);
        properties.group(EMobileItemGroup.EMOBILE_GROUP);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack itemStack) {
        return new TranslationTextComponent("item.emobile.cellphone.rf");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        if(!world.isRemote) {
            if(hand == Hand.MAIN_HAND) {
                if(player.getHeldItemMainhand().getItem() == this) {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new CellphoneRFItem.CellphoneRFContainerProvider(this, player.getHeldItemMainhand()));
                }
            }
        }

        return ActionResult.resultPass(player.getHeldItemMainhand());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag tooltipFlag) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.holdShift"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.cellphone.pearls"));
        }
    }

    private static class CellphoneRFContainerProvider implements INamedContainerProvider {

        private CellphoneRFItem cellphoneRF;
        private ItemStack item;

        public CellphoneRFContainerProvider(CellphoneRFItem cellphoneRF, ItemStack item) {
            this.cellphoneRF = cellphoneRF;
            this.item = item;
        }

        @Override
        public ITextComponent getDisplayName() {
            return item.getDisplayName();
        }

        @Override
        public CellphoneRFContainer createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
            CellphoneRFContainer container = CellphoneRFContainer.createContainerServerSide(windowID, playerInventory, item);
            return container;
        }
    }
}
