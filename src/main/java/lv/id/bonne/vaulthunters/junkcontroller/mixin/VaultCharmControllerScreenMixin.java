//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import iskallia.vault.client.gui.screen.VaultCharmControllerScreen;
import iskallia.vault.container.VaultCharmControllerContainer;
import javax.annotation.Nullable;
import lv.id.bonne.vaulthunters.junkcontroller.jei.JunkControllerPlugin;
import lv.id.bonne.vaulthunters.junkcontroller.interfaces.SearchInterface;
import lv.id.bonne.vaulthunters.junkcontroller.network.JunkControllerNetwork;
import lv.id.bonne.vaulthunters.junkcontroller.network.packets.SynchronizeWhitelistOrder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.Button;
import net.p3pp3rf1y.sophisticatedcore.client.gui.controls.ButtonDefinitions;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;


@Mixin(VaultCharmControllerScreen.class)
public abstract class VaultCharmControllerScreenMixin extends AbstractContainerScreenMixin
{
    @Nullable
    @Unique
    private Button sortButton = null;

    @Unique
    private String searchQuery = "";

    @Override
    protected void handleInit(CallbackInfo ci)
    {
        Position pos = new Position(this.leftPos + this.imageWidth - 20, this.topPos + 4);
        this.sortButton = new Button(new Position(pos.x(), pos.y()), ButtonDefinitions.SORT, (button) -> {
            if (button == 0) {
                VaultCharmControllerContainer menu = this.getMenu();
                menu.getWhitelist().sort(ResourceLocation::compareTo);

                if (menu instanceof SearchInterface searchInterface)
                {
                    JunkControllerNetwork.sendToServer(new SynchronizeWhitelistOrder(this.searchQuery));
                    searchInterface.updateSearchQuery(this.searchQuery);
                }

                Minecraft.getInstance().player.displayClientMessage(new TextComponent("Sorted"), true);
            }
        });

        ((ScreenAccessor) this).getChildren().add(this.sortButton);
        ((ScreenAccessor) this).getNarratables().add(this.sortButton);
    }


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Liskallia/vault/client/gui/screen/VaultCharmControllerScreen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;II)V"))
    private void addButtonRender(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci)
    {
        if (this.sortButton != null)
        {
            this.sortButton.render(matrixStack, mouseX, mouseY, partialTicks);
        }

        String newSearchQuery = JunkControllerPlugin.getRuntime().getIngredientFilter().getFilterText();

        if (!Objects.equals(this.searchQuery, newSearchQuery))
        {
            this.searchQuery = newSearchQuery;
            VaultCharmControllerContainer menu = this.getMenu();

            if (menu instanceof SearchInterface searchInterface)
            {
                JunkControllerNetwork.sendToServer(new SynchronizeWhitelistOrder(this.searchQuery));
                searchInterface.updateSearchQuery(this.searchQuery);
            }
        }
    }
}
