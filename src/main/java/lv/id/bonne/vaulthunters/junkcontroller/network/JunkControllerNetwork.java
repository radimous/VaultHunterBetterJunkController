package lv.id.bonne.vaulthunters.junkcontroller.network;


import lv.id.bonne.vaulthunters.junkcontroller.VaultHuntersJunkController;
import lv.id.bonne.vaulthunters.junkcontroller.network.packets.TriggerSorting;
import lv.id.bonne.vaulthunters.junkcontroller.network.packets.UpdateSearchQuery;
import lv.id.bonne.vaulthunters.junkcontroller.network.packets.UpdateItemFromJEI;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;


/**
 * This class manages the network channel for this mod.
 * It is used to register and send pockets between the client and server.
 */
public class JunkControllerNetwork
{
    /**
     * The protocol version for the mod. We start with 1.
     */
    private static final String PROTOCOL_VERSION = "1";
    /**
     * The network channel for the mod.
     */
    private static SimpleChannel CHANNEL;
    /**
     * The ID for the packet.
     */
    private static int packetId = 0;

    /**
     * The ID increment method :)
     *
     * @return The incremented ID.
     */
    private static int id() {
        return packetId++;
    }

    /**
     * Registers the network channel for the mod.
     */
    public static void register()
    {
        CHANNEL = NetworkRegistry.ChannelBuilder.
            named(VaultHuntersJunkController.of("messages")).
            networkProtocolVersion(() -> PROTOCOL_VERSION).
            clientAcceptedVersions(PROTOCOL_VERSION::equals).
            serverAcceptedVersions(PROTOCOL_VERSION::equals).
            simpleChannel();

        CHANNEL.messageBuilder(UpdateItemFromJEI.class, id(), NetworkDirection.PLAY_TO_SERVER).
            decoder(UpdateItemFromJEI::decode).
            encoder(UpdateItemFromJEI::encode).
            consumer(UpdateItemFromJEI::handle).
            add();

        CHANNEL.messageBuilder(UpdateSearchQuery.class, id(), NetworkDirection.PLAY_TO_SERVER).
            decoder(UpdateSearchQuery::decode).
            encoder(UpdateSearchQuery::encode).
            consumer(UpdateSearchQuery::handle).
            add();

        CHANNEL.messageBuilder(TriggerSorting.class, id(), NetworkDirection.PLAY_TO_SERVER).
            decoder(TriggerSorting::decode).
            encoder(TriggerSorting::encode).
            consumer(TriggerSorting::handle).
            add();
    }

    /**
     * This method sends a packet to the server from client.
     *
     * @param message The pocket message to be sent.
     * @param <T>     The type of the packet message.
     */
    public static <T> void sendToServer(T message) {
        CHANNEL.sendToServer(message);
    }


    /**
     * This method sends a packet to the client from server.
     *
     * @param message The pocket message to be sent.
     * @param <T>     The type of the packet message.
     */
    public static <T> void sendToPlayer(T message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
