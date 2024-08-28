package lv.id.bonne.vaulthunters.junkcontroller.jei;

import org.jetbrains.annotations.NotNull;

import iskallia.vault.client.gui.screen.VaultCharmControllerScreen;
import lv.id.bonne.vaulthunters.junkcontroller.VaultHuntersJunkController;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;


/**
 * The JEI integration plugin.
 */
@SuppressWarnings("unused")
@JeiPlugin
public class JunkControllerPlugin implements IModPlugin
{
	/**
	 * @return The main plugin ID.
	 */
	@Override
	@NotNull
	public ResourceLocation getPluginUid()
	{
		return VaultHuntersJunkController.of("controller");
	}


	/**
	 * The GUI handler registration to allow ghost item dragging.
	 * @param registration The IGuiHandlerRegistration object.
	 */
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
		registration.addGhostIngredientHandler(VaultCharmControllerScreen.class, new JunkControllerGhostItemHandler());
	}


	/**
	 * The JEI runtime accessor.
	 * @param runtime JEI runtime object.
	 */
	@Override
	public void onRuntimeAvailable(@NotNull IJeiRuntime runtime)
	{
		JunkControllerPlugin.runtime = runtime;
	}


	/**
	 * Returns JEI runtime object.
	 * @return Jei Runtime object.
	 */
	public static IJeiRuntime getRuntime()
	{
		return JunkControllerPlugin.runtime;
	}


	/**
	 * Jei Runtime Object.
	 */
	private static IJeiRuntime runtime;
}
