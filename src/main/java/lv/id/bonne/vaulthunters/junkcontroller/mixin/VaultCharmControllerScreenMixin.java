//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Objects;

import iskallia.vault.client.gui.screen.VaultCharmControllerScreen;
import iskallia.vault.container.VaultCharmControllerContainer;
import lv.id.bonne.vaulthunters.junkcontroller.interfaces.SearchInterface;
import lv.id.bonne.vaulthunters.junkcontroller.jei.JunkControllerPlugin;
import lv.id.bonne.vaulthunters.junkcontroller.network.JunkControllerNetwork;
import lv.id.bonne.vaulthunters.junkcontroller.network.packets.TriggerSorting;
import lv.id.bonne.vaulthunters.junkcontroller.network.packets.UpdateSearchQuery;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.Button;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;


/**
 * The mixin for adding buttons and features in Vault Junk Controller.
 */
@Mixin(VaultCharmControllerScreen.class)
public abstract class VaultCharmControllerScreenMixin extends AbstractContainerScreenMixin
{
    @Override
    protected void handleInit(CallbackInfo ci)
    {
        Position pos = new Position(this.leftPos + this.imageWidth - 20, this.topPos + 4);

        // Button for widget. I will reuse P3pp3r code because his code is one of the best :)
        // Also, I do not think this mod will be used without his sophisticated core
        this.sortButton = new Button(new Position(pos.x(), pos.y()), ButtonDefinitions.SORT, (button) ->
        {
            if (button == 0)
            {
                // Send sorting trigger on server side.
                JunkControllerNetwork.sendToServer(new TriggerSorting());
            }
        });

        ((ScreenAccessor) this).getChildren().add(this.sortButton);
        ((ScreenAccessor) this).getNarratables().add(this.sortButton);
    }


    /**
     * An injection to trigger button rendering.
     * @param matrixStack The pose matrix renderer.
     * @param mouseX Mouse X location
     * @param mouseY Mouse Y location
     * @param partialTicks Ticks
     * @param ci Call back information.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Liskallia/vault/client/gui/screen/VaultCharmControllerScreen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;II)V"))
    private void addButtonRender(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci)
    {
        if (this.sortButton != null)
        {
            // Render sort button.
            this.sortButton.render(matrixStack, mouseX, mouseY, partialTicks);
        }

        // Getting search from JEI
        String newSearchQuery = JunkControllerPlugin.getRuntime().getIngredientFilter().getFilterText();

        if (!Objects.equals(this.searchQuery, newSearchQuery))
        {
            // Update query.
            this.searchQuery = newSearchQuery;

            VaultCharmControllerContainer menu = this.getMenu();

            if (menu instanceof SearchInterface)
            {
                // Trigger search query on server side.
                JunkControllerNetwork.sendToServer(new UpdateSearchQuery(this.searchQuery));
            }
        }
    }


    /**
     * The sorting button location.
     */
    @Nullable
    @Unique
    private Button sortButton = null;

    /**
     * Client sorting search query.
     */
    @Unique
    private String searchQuery = null;
}
