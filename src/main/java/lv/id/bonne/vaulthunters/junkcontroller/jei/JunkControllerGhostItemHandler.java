//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.jei;


import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

import iskallia.vault.client.gui.screen.VaultCharmControllerScreen;
import iskallia.vault.container.VaultCharmControllerContainer;
import lv.id.bonne.vaulthunters.junkcontroller.interfaces.SearchInterface;
import lv.id.bonne.vaulthunters.junkcontroller.network.JunkControllerNetwork;
import lv.id.bonne.vaulthunters.junkcontroller.network.packets.UpdateItemFromJEI;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public class JunkControllerGhostItemHandler implements IGhostIngredientHandler<VaultCharmControllerScreen>
{
    @NotNull
    public <I>  List<Target<I>> getTargets(@NotNull VaultCharmControllerScreen screen, @NotNull I ingredient, boolean doStart)
    {
        List<Target<I>> targets = new ArrayList<>();

        if (ingredient instanceof ItemStack itemStack)
        {
            VaultCharmControllerContainer menu = screen.getMenu();

            if (menu.getWhitelist().contains(itemStack.getItem().getRegistryName()))
            {
                // Do nothing as item is already listed.
                return targets;
            }

            int inventorySize = menu.getInventorySize();
            int emptySlots = inventorySize - menu.getWhitelist().size();

            for (int index = 0; index < inventorySize && index < 54 && emptySlots > 0; index++)
            {
                Slot slot = menu.getSlot(36 + index);

                if (!slot.hasItem())
                {
                    emptySlots--;

                    final Rect2i bounds = new Rect2i(screen.getGuiLeft() + slot.x, screen.getGuiTop() + slot.y, 17, 17);

                    targets.add(new Target<>()
                    {
                        @NotNull
                        public Rect2i getArea()
                        {
                            return bounds;
                        }


                        public void accept(@NotNull I ingredient)
                        {
                            JunkControllerNetwork.sendToServer(new UpdateItemFromJEI(itemStack, slot.index));
                            menu.getWhitelist().add(itemStack.getItem().getRegistryName());

                            if (menu instanceof SearchInterface searchInterface)
                            {
                                searchInterface.updateSearchQuery(null);
                            }
                        }
                    });
                }
            }

            return targets;
        }
        else
        {
            return targets;
        }
    }


    public void onComplete() {
    }
}

