//
// Created by BONNe
// Copyright - 2024
//


package lv.id.bonne.vaulthunters.junkcontroller.network.packets;


import org.lwjgl.system.CallbackI;
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


    public static void encode(UpdateItemFromJEI msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeItem(msg.stack);
        packetBuffer.writeShort(msg.slotNumber);
    }


    public static UpdateItemFromJEI decode(FriendlyByteBuf packetBuffer)
    {
        return new UpdateItemFromJEI(packetBuffer.readItem(), packetBuffer.readShort());
    }


    public static void handle(UpdateItemFromJEI msg, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handleItemSetting(msg, context.getSender()));
        context.setPacketHandled(true);
    }


    private static void handleItemSetting(UpdateItemFromJEI msg, @Nullable ServerPlayer sender)
    {
        if (sender != null)
        {
            AbstractContainerMenu container = sender.containerMenu;

            if (container instanceof VaultCharmControllerContainer charmContainer)
            {
                if (charmContainer.getSlot(msg.slotNumber).mayPlace(msg.stack))
                {
                    charmContainer.getSlot(msg.slotNumber).set(msg.stack);
                    charmContainer.getWhitelist().add(msg.stack.getItem().getRegistryName());

                    if (charmContainer instanceof SearchInterface searchInterface)
                    {
                        searchInterface.updateSearchQuery(null);
                        searchInterface.updateVisibleItems();
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
