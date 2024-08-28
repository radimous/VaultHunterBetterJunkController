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
import lv.id.bonne.vaulthunters.junkcontroller.network.JunkControllerNetwork;
import lv.id.bonne.vaulthunters.junkcontroller.network.packets.UpdateItemFromJEI;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


/**
 * The Ghost item handler for JEI
 */
public class JunkControllerGhostItemHandler implements IGhostIngredientHandler<VaultCharmControllerScreen>
{
    /**
     * The method that searches targets where drop can happen.
     *
     * @param screen The VaultCharmControllerScreen screen
     * @param ingredient The ingredient that are dropped.
     * @param doStart Boolean for doing stuff??
     * @param <I> Ingredient from JEI
     * @return List of spots where item can be dropped.
     */
    @NotNull
    public <I> List<Target<I>> getTargets(@NotNull VaultCharmControllerScreen screen,
        @NotNull I ingredient,
        boolean doStart)
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

            // Mark only slots where items can be placed.
            for (int index = 0; index < inventorySize && index < 54 && emptySlots > 0; index++)
            {
                Slot slot = menu.getSlot(36 + index);

                if (!slot.hasItem())
                {
                    emptySlots--;

                    targets.add(new Target<>()
                    {
                        @NotNull
                        public Rect2i getArea()
                        {
                            return new Rect2i(screen.getGuiLeft() + slot.x,
                                screen.getGuiTop() + slot.y,
                                17,
                                17);
                        }


                        public void accept(@NotNull I ingredient)
                        {
                            // add item to client side, so it updates top number correctly.
                            menu.getWhitelist().add(itemStack.getItem().getRegistryName());
                            // Send update information to the server
                            JunkControllerNetwork.sendToServer(new UpdateItemFromJEI(itemStack, slot.index));
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


    /**
     * Do nothing on complete.
     */
    public void onComplete()
    {
    }
}