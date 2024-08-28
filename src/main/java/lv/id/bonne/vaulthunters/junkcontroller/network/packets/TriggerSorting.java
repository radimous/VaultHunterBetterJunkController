//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.network.packets;


import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

import iskallia.vault.container.VaultCharmControllerContainer;
import lv.id.bonne.vaulthunters.junkcontroller.interfaces.SearchInterface;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;


/**
 * The packet that should trigger item sorting from client to the server.
 */
public class TriggerSorting
{
    /**
     * The packet encoder.
     * @param msg Message that need to be sent.
     * @param packetBuffer outgoing message buffer.
     */
    public static void encode(TriggerSorting msg, FriendlyByteBuf packetBuffer)
    {
        // Empty pocket
    }


    /**
     * Message decoder into new UpdateSearchQuery message.
     * @param packetBuffer incoming message packet
     * @return incoming UpdateSearchQuery
     */
    public static TriggerSorting decode(FriendlyByteBuf packetBuffer)
    {
        return new TriggerSorting();
    }


    /**
     * The packet handler
     * @param msg Incoming packet
     * @param contextSupplier The network context
     */
    public static void handle(TriggerSorting msg, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleSorting(msg, context.getSender()));
        context.setPacketHandled(true);
    }


    /**
     * The search query update handler.
     * @param msg Incoming packet
     * @param sender The player who send the packet.
     */
    private static void handleSorting(TriggerSorting msg, @Nullable ServerPlayer sender)
    {
        if (sender != null)
        {
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof VaultCharmControllerContainer charmContainer)
            {
                // Reorder whitelist on server side.
                charmContainer.getWhitelist().sort(ResourceLocation::compareTo);

                if (charmContainer instanceof SearchInterface searchInterface)
                {
                    // Update the list based on reordered list
                    searchInterface.updateSearchQuery(null);
                }
            }
        }
    }
}
