package lv.id.bonne.vaulthunters.junkcontroller.network.packets;


import lv.id.bonne.vaulthunters.junkcontroller.interfaces.FixedScrollContainer;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class ScrollToMessage
{
    public float scroll;

    public ScrollToMessage(float scroll) {
        this.scroll = scroll;
    }

    public static void encode(ScrollToMessage msg, FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeFloat(msg.scroll);
    }


    public static ScrollToMessage decode(FriendlyByteBuf packetBuffer)
    {
        return new ScrollToMessage(packetBuffer.readFloat());
    }

    public static void handle(ScrollToMessage msg, Supplier<NetworkEvent.Context> contextSupplier)
    {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                if (sender.containerMenu instanceof FixedScrollContainer fsc) {
                    fsc.fixedScrolling$ScrollTo(msg.scroll);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
