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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;


/**
 * This packet handles player placing item in junk controller from JEI.
 */
public class UpdateItemFromJEI
{
    /**
     * Default constructor.
     * @param stack The stack item.
     * @param slotNumber The slot index.
     */
    public UpdateItemFromJEI(ItemStack stack, int slotNumber)
    {
        this.stack = stack;
        this.slotNumber = slotNumber;
    }


    /**
     * The message encoder into packet.
     * @param msg Informational message.
     * @param packetBuffer Outgoing packet.
     */
    public static void encode(UpdateItemFromJEI msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeItem(msg.stack);
        packetBuffer.writeShort(msg.slotNumber);
    }


    /**
     * The incoming packet decoder
     * @param packetBuffer Message buffer
     * @return new UpdateItemFromJEI from incoming packet.
     */
    public static UpdateItemFromJEI decode(FriendlyByteBuf packetBuffer)
    {
        return new UpdateItemFromJEI(packetBuffer.readItem(), packetBuffer.readShort());
    }


    /**
     * The packet handler
     * @param msg Incoming packet
     * @param contextSupplier The network context
     */
    public static void handle(UpdateItemFromJEI msg, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleItemSetting(msg, context.getSender()));
        context.setPacketHandled(true);
    }


    /**
     * Add item into server side container.
     * @param msg The JEI package.
     * @param sender Player who sends that information
     */
    private static void handleItemSetting(UpdateItemFromJEI msg, @Nullable ServerPlayer sender)
    {
        if (sender != null)
        {
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof VaultCharmControllerContainer charmContainer)
            {
                if (charmContainer.getSlot(msg.slotNumber).mayPlace(msg.stack))
                {
                    // Add item to the whitelist.
                    charmContainer.getWhitelist().add(msg.stack.getItem().getRegistryName());

                    if (charmContainer instanceof SearchInterface searchInterface)
                    {
                        // Trigger search update.
                        searchInterface.updateSearchQuery(null);
                    }
                }
            }
        }
    }

    /**
     * The stack that is applied.
     */
    private final ItemStack stack;

    /**
     * The slot number in which stack is placed.
     */
    private final int slotNumber;
}
