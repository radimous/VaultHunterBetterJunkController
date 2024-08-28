package lv.id.bonne.vaulthunters.junkcontroller;


import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import lv.id.bonne.vaulthunters.junkcontroller.network.JunkControllerNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


/**
 * The main class for Vault Hunters Better Junk Controller mod.
 */
@Mod(VaultHuntersJunkController.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VaultHuntersJunkController
{
    /**
     * The main class initialization.
     */
    public VaultHuntersJunkController()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }


    /**
     * Initializes mod events.
     *
     * @param event the FMLCommonSetupEvent event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void setupCommon(FMLCommonSetupEvent event)
    {
        event.enqueueWork(JunkControllerNetwork::register);
    }


    /**
     * This method returns resource location of current mod.
     * @param name The resource name.
     * @return The resource location for current name.
     */
    public static ResourceLocation of(String name)
    {
        return new ResourceLocation(MODID, name);
    }


    /**
     * The logger for this mod.
     */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * The mod id
     */
    public static final String MODID = "vault_hunters_better_junk_controller";
}
