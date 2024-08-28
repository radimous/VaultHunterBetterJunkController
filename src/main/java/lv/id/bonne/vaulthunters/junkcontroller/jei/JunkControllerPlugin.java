package lv.id.bonne.vaulthunters.junkcontroller.jei;

import org.jetbrains.annotations.NotNull;

import iskallia.vault.client.gui.screen.VaultCharmControllerScreen;
import lv.id.bonne.vaulthunters.junkcontroller.VaultHuntersJunkController;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;


@SuppressWarnings("unused")
@JeiPlugin
public class JunkControllerPlugin implements IModPlugin
{

	@Override
	@NotNull
	public ResourceLocation getPluginUid()
	{
		return VaultHuntersJunkController.of("controller");
	}


	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration)
	{
		registration.addGhostIngredientHandler(VaultCharmControllerScreen.class, new JunkControllerGhostItemHandler());
	}

	@Override
	public void onRuntimeAvailable(@NotNull IJeiRuntime runtime)
	{
		JunkControllerPlugin.runtime = runtime;
	}

	public static IJeiRuntime getRuntime()
	{
		return JunkControllerPlugin.runtime;
	}


	private static IJeiRuntime runtime;
}
