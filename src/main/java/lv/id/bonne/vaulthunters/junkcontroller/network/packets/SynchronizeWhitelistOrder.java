//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.network.packets;


import java.util.function.Supplier;

import iskallia.vault.container.VaultCharmControllerContainer;
import javax.annotation.Nullable;
import lv.id.bonne.vaulthunters.junkcontroller.interfaces.SearchInterface;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;


public class SynchronizeWhitelistOrder
{
    private final String text;

    public SynchronizeWhitelistOrder(String text)
    {
        this.text = text;
    }


    public static void encode(SynchronizeWhitelistOrder msg, FriendlyByteBuf packetBuffer)
    {
        if (msg.text != null)
        {
            packetBuffer.writeBoolean(true);
            packetBuffer.writeUtf(msg.text);
        }
        else
        {
            packetBuffer.writeBoolean(false);
        }
    }


    public static SynchronizeWhitelistOrder decode(FriendlyByteBuf packetBuffer)
    {
        boolean write = packetBuffer.readBoolean();
        return new SynchronizeWhitelistOrder(write ? packetBuffer.readUtf() : null);
    }


    public static void handle(SynchronizeWhitelistOrder msg, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleItemSetting(msg, context.getSender()));
        context.setPacketHandled(true);
    }


    private static void handleItemSetting(SynchronizeWhitelistOrder msg, @Nullable ServerPlayer sender)
    {
        if (sender != null)
        {
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof VaultCharmControllerContainer charmContainer)
            {
                charmContainer.getWhitelist().sort(ResourceLocation::compareTo);

                if (charmContainer instanceof SearchInterface searchInterface)
                {
                    searchInterface.updateSearchQuery(msg.text);
                }
            }
        }
    }
}
