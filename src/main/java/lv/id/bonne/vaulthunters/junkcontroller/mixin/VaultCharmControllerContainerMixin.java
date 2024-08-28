//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import org.spongepowered.asm.mixin.*;
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
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;


@Mixin(VaultCharmControllerContainer.class)
public abstract class VaultCharmControllerContainerMixin implements SearchInterface
{
    @Shadow(remap = false)
    @Final
    private List<ResourceLocation> whitelist;

    @Shadow(remap = false)
    @Final
    private int inventorySize;

    @Shadow(remap = false)
    private int currentStart;

    @Shadow(remap = false)
    public Container visibleItems;

    @Unique
    private final List<ResourceLocation> filteredList = new ArrayList<>();

    private String searchQuery = "";

    @Redirect(method = "<init>",
        at = @At(value = "INVOKE", target = "Liskallia/vault/world/data/VaultCharmData$VaultCharmInventory;getWhitelist()Ljava/util/List;"),
        remap = false)
    List<ResourceLocation> customVariableAdding(VaultCharmData.VaultCharmInventory instance)
    {
        this.filteredList.addAll(instance.getWhitelist());
        return instance.getWhitelist();
    }


    @Inject(method = "quickMoveStack",
        at = @At(value = "INVOKE",
            target = "Liskallia/vault/container/VaultCharmControllerContainer;updateVisibleItems()V"))
    private void injectFilterBeforeUpdate(Player playerIn, int index, CallbackInfoReturnable<ItemStack> cir)
    {
        this.updateSearchQuery(null);
    }

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
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void updateVisibleItems()
    {
        for(int i = 0; i < this.inventorySize && i < 54; ++i) {
            int whitelistIndex = this.currentStart + i;

            if (whitelistIndex >= this.filteredList.size()) {
                this.visibleItems.setItem(i, ItemStack.EMPTY);
                ((AbstractContainerMenuAccessor) this).getLastSlots().set(i, ItemStack.EMPTY);
            } else {
                ResourceLocation id = this.filteredList.get(whitelistIndex);
                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(id));
                this.visibleItems.setItem(i, stack);

                ((AbstractContainerMenuAccessor) this).getLastSlots().add(i, ItemStack.EMPTY);
            }
        }
    }


    @Unique
    @Override
    public void updateSearchQuery(String searchQuery)
    {
        if (searchQuery != null)
        {
            this.searchQuery = searchQuery;
        }

        if (this.searchQuery.isEmpty() && this.filteredList.size() == this.whitelist.size())
        {
            this.filteredList.sort(Comparator.comparingInt(this.whitelist::indexOf));
            this.updateVisibleItems();
            return;
        }

        this.filteredList.clear();

        if (this.searchQuery.isEmpty())
        {
            this.filteredList.addAll(this.whitelist);
        }
        else
        {
            this.filteredList.addAll(this.whitelist.stream().
                filter(resourceLocation -> resourceLocation.toString().contains(this.searchQuery)).
                toList());
        }

        this.updateVisibleItems();
    }
}
