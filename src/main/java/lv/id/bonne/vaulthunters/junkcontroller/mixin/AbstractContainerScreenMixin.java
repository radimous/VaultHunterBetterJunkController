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


/**
 * Mixin that allows to get items from AbstractContainerScreen class.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin
{
    /**
     * imageWidth object accessor.
     */
    @Shadow
    protected int imageWidth;


    /**
     * leftPos object accessor.
     */
    @Shadow
    protected int leftPos;


    /**
     * topPos object accessor.
     */
    @Shadow
    protected int topPos;


    /**
     * The accessor for getMenu() method
     */
    @Shadow
    public abstract <T extends AbstractContainerMenu> T getMenu();


    /**
     * The injection point into  init()V method.
     */
    @Inject(method = "init()V", at = @At("TAIL"))
    protected void handleInit(CallbackInfo ci)
    {
    }
}
