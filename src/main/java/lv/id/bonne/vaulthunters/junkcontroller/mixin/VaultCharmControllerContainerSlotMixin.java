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


@Mixin(targets = "iskallia.vault.container.VaultCharmControllerContainer$VaultCharmControllerSlot")
public abstract class VaultCharmControllerContainerSlotMixin
{
    @Shadow(remap = false)
    @Final
    VaultCharmControllerContainer this$0;


    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void canTrulyPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir)
    {
        if (this$0.getInventorySize() - this$0.getWhitelist().size() <= 0)
        {
            cir.setReturnValue(false);
        }
    }
}
