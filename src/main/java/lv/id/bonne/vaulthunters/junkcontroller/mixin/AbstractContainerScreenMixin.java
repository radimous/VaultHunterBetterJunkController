//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;


@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin
{
    @Shadow
    protected int imageWidth;


    @Shadow
    protected int leftPos;


    @Shadow
    protected int topPos;


    @Shadow
    public abstract <T extends AbstractContainerMenu> T getMenu();


    @Inject(method = "init()V", at = @At("TAIL"))
    protected void handleInit(CallbackInfo ci)
    {
    }
}
