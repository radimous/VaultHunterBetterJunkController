//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;


@Mixin(AbstractContainerMenu.class)
public interface AbstractContainerMenuAccessor
{
    @Accessor
    NonNullList<ItemStack> getLastSlots();
}
