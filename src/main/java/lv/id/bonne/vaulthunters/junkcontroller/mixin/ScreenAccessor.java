//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;


/**
 * The accessor for screen class.
 */
@Mixin(Screen.class)
public interface ScreenAccessor
{
    /**
     * @return children element accessor.
     */
    @Accessor("children")
    List<GuiEventListener> getChildren();


    /**
     * @return narratables element accessor.
     */
    @Accessor("narratables")
    List<NarratableEntry> getNarratables();
}
