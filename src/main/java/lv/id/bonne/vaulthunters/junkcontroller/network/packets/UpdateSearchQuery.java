//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.network.packets;


import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

import lv.id.bonne.vaulthunters.junkcontroller.interfaces.SearchInterface;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;


/**
 * The packet that should trigger item synchronizing from client to the server.
 */
public class UpdateSearchQuery
{
    /**
     * @param text The search message.
     */
    public UpdateSearchQuery(@Nullable String text)
    {
        this.text = text;
    }


    /**
     * The packet encoder.
     * @param msg Message that need to be sent.
     * @param packetBuffer outgoing message buffer.
     */
    public static void encode(UpdateSearchQuery msg, FriendlyByteBuf packetBuffer)
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


    /**
     * Message decoder into new UpdateSearchQuery message.
     * @param packetBuffer incoming message packet
     * @return incoming UpdateSearchQuery
     */
    public static UpdateSearchQuery decode(FriendlyByteBuf packetBuffer)
    {
        boolean write = packetBuffer.readBoolean();
        return new UpdateSearchQuery(write ? packetBuffer.readUtf() : null);
    }


    /**
     * The packet handler
     * @param msg Incoming packet
     * @param contextSupplier The network context
     */
    public static void handle(UpdateSearchQuery msg, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleSearchQuery(msg, context.getSender()));
        context.setPacketHandled(true);
    }


    /**
     * The search query update handler.
     * @param msg Incoming packet
     * @param sender The player who send the packet.
     */
    private static void handleSearchQuery(UpdateSearchQuery msg, @Nullable ServerPlayer sender)
    {
        if (sender != null)
        {
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof SearchInterface searchInterface)
            {
                // Update the list based on search query.
                searchInterface.updateSearchQuery(msg.text);
            }
        }
    }


    /**
     * The new search query that is sent to server
     */
    private final String text;
}
