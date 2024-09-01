//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import lv.id.bonne.vaulthunters.junkcontroller.interfaces.FixedScrollContainer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import iskallia.vault.container.VaultCharmControllerContainer;
import iskallia.vault.world.data.VaultCharmData;
import lv.id.bonne.vaulthunters.junkcontroller.interfaces.SearchInterface;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;


/**
 * The main operational mixin. This mixin controls how Vault Charm Controller Container operates.
 */
@Mixin(VaultCharmControllerContainer.class)
public abstract class VaultCharmControllerContainerMixin implements SearchInterface, FixedScrollContainer
{
    /**
     * This mixin clones VaultCharmInventory whitelist in new filteredList that is used for displaying elements.
     * @param instance The original instance of VaultCharmInventory
     * @return The whitelist of VaultCharmInventory.
     */
    @Redirect(method = "<init>",
        at = @At(value = "INVOKE", target = "Liskallia/vault/world/data/VaultCharmData$VaultCharmInventory;getWhitelist()Ljava/util/List;"),
        remap = false)
    List<ResourceLocation> customVariableAdding(VaultCharmData.VaultCharmInventory instance)
    {
        this.filteredList.addAll(instance.getWhitelist());
        return instance.getWhitelist();
    }


    /**
     * This mixin triggers search query update on new item adding with quick move.
     * @param playerIn Player who triggers quick move
     * @param index Index from which quick move happens
     * @param cir Callback information
     */
    @Inject(method = "quickMoveStack",
        at = @At(value = "INVOKE",
            target = "Liskallia/vault/container/VaultCharmControllerContainer;updateVisibleItems()V"))
    private void injectFilterBeforeUpdate(Player playerIn, int index, CallbackInfoReturnable<ItemStack> cir)
    {
        this.updateSearchQuery(null);
    }


    /**
     * This mixin triggers search query update on clicking item in menu or adding a new one.
     * @param slotId Slot that is clicked
     * @param dragType Drag type
     * @param clickTypeIn Click type
     * @param player Player who did action
     * @param ci Callback information.
     */
    @Inject(method = "clicked",
        at = @At(value = "INVOKE",
            target = "Liskallia/vault/container/VaultCharmControllerContainer;updateVisibleItems()V"))
    private void injectFilterBeforeUpdate(int slotId,
        int dragType,
        ClickType clickTypeIn,
        Player player,
        CallbackInfo ci)
    {
        this.updateSearchQuery(null);
    }


    /**
     * This method redirects this.whiteList.size() to this.filteredList.size()
     * @param instance The original object list.
     * @return Size of filteredList
     */
    @Redirect(method = "updateVisibleItems",
        at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"),
        remap = false)
    private int redirectToFilterList(List<ResourceLocation> instance)
    {
        return this.filteredList.size();
    }


    /**
     * This method redirects this.whiteList.get(int) to this.filteredList.get(int)
     * @param instance The original object list.
     * @param i the index of element
     * @return the object from filteredList at requested index.
     */
    @Redirect(method = "updateVisibleItems",
        at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"),
        remap = false)
    private Object redirectToFilterList(List<ResourceLocation> instance, int i)
    {
        return this.filteredList.get(i);
    }


    /**
     * This is main method that performs sorting and searching objects in filtered list.
     * @param searchQuery Nullable object of search text.
     */
    @Unique
    @Override
    public void updateSearchQuery(@Nullable String searchQuery)
    {
        if (searchQuery != null)
        {
            // If query is not null, update it.
            this.searchQuery = searchQuery;
        }

        if (this.searchQuery.isEmpty() && this.filteredList.size() == this.whitelist.size())
        {
            // If query is empty, and both lists has same size, then order filteredList by whiteList
            this.filteredList.sort(Comparator.comparingInt(this.whitelist::indexOf));
            // Trigger updating visible items.
            this.updateVisibleItems();
            return;
        }

        // Now clear items in filtered list and repopulate it.
        this.filteredList.clear();

        if (this.searchQuery.isEmpty())
        {
            // Nothing to search. Add all elements back.
            this.filteredList.addAll(this.whitelist);
        }
        else
        {
            // Filter elements based on search parameter.
            this.filteredList.addAll(this.whitelist.stream().
                filter(resourceLocation -> resourceLocation.toString().contains(this.searchQuery)).
                toList());
        }

        // Trigger view update.
        this.updateVisibleItems();
    }


    /**
     * Accessor to the whitelist object.
     */
    @Shadow(remap = false)
    @Final
    private List<ResourceLocation> whitelist;


    /**
     * Accessor to the updateVisibleItems() method.
     */
    @Shadow(remap = false)
    protected abstract void updateVisibleItems();

    /**
     * List of filteredList elements.
     */
    @Unique
    private final List<ResourceLocation> filteredList = new ArrayList<>();

    /**
     * The default search query text.
     */
    @Unique
    private String searchQuery = "";


    @Shadow(remap = false) @Final private int inventorySize;
    @Shadow(remap = false) private int currentEnd;
    @Shadow(remap = false) private int currentStart;
    @Shadow(remap = false) private float scrollDelta;

    /**
     * Scrolls the container to a specified position based on a fractional value.
     * <p>
     * Calculates the scroll position within the container based on the
     * given {@code scroll} parameter, which represents a fraction of the container's
     * total size. The goal is to position the desired slot in the middle of the screen.
     * Since the container's height is even(6 rows), the method tries to position the slot
     * as close as possible to the third row.
     *
     * @param scroll A float representing the desired scroll position as a fraction
     *               of the container size. The value should be between 0 (top) and 1 (bottom).
     */
    @Unique
    public void fixedScrolling$ScrollTo(float scroll){
        // exact wanted slot - excluding 2rows from top and 3 rows from bottom
        int wantedSlot = (int)(scroll * ((float)this.inventorySize - 5*9));
        // get first slot of 3rd row
        int thirdRow = wantedSlot - wantedSlot % 9;
        // start of the first row when wanted is in 3rd row
        int newStart = Math.max(Math.min(thirdRow - 2, inventorySize - 54), 0);
        int newEnd = Math.min(newStart + 54, this.inventorySize);

        if (newStart != this.currentStart || newEnd != this.currentEnd) {
            this.currentStart = newStart;
            this.currentEnd = newEnd;
            this.updateVisibleItems();
            this.scrollDelta = scroll;
        }
    }
}
