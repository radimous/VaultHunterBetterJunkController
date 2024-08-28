//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import iskallia.vault.container.VaultCharmControllerContainer;
import net.minecraft.world.item.ItemStack;


/**
 * The mixin for changing how VaultCharmControllerSlot operates.
 */
@Mixin(targets = "iskallia.vault.container.VaultCharmControllerContainer$VaultCharmControllerSlot")
public abstract class VaultCharmControllerContainerSlotMixin
{
    /**
     * Main class accessor
     */
    @Shadow(remap = false)
    @Final
    VaultCharmControllerContainer this$0;


    /**
     * Add extra condition for mayPlace to prevent adding item if there are no free spots left.
     * @param stack Stack that need to be added.
     * @param cir Callback information
     */
    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void canTrulyPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
    {
        // If whitelist and inventory size has same value, then stop inserting new items.
        if (this$0.getInventorySize() - this$0.getWhitelist().size() <= 0)
        {
            cir.setReturnValue(false);
        }
    }
}
