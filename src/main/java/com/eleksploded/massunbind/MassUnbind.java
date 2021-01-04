package com.eleksploded.massunbind;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.eleksploded.eleklib.config.Config;
import com.eleksploded.eleklib.config.ConfigBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("massunbind")
public class MassUnbind
{
	// Directly reference a log4j logger.
	private static final Logger LOGGER = LogManager.getLogger();

	KeyBinding[] originalBinding;
	Config config;
	@SuppressWarnings("resource")
	public MassUnbind() {
		MinecraftForge.EVENT_BUS.register(this);

		config = ConfigBuilder.builder()
				.addBool("doReset", true, "Set to true to reset all modded keys on launch")
				.build("massunbind", ModConfig.Type.CLIENT);

		originalBinding = Minecraft.getInstance().gameSettings.keyBindings.clone();
	}

	boolean ran = false;
	@SuppressWarnings({"resource" })
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void tick(final TickEvent e) {
		if(!ran) {
			ran = true;
			if(!config.getBool("doReset")) return;

			LOGGER.info("Unbinding keys...");

			for(KeyBinding bind : Minecraft.getInstance().gameSettings.keyBindings) {
				if(!ArrayUtils.contains(originalBinding, bind)) {
					LOGGER.info("Unbinding key: " + bind.getTranslationKey() + " in " + bind.getKeyCategory());
					Minecraft.getInstance().gameSettings.setKeyBindingCode(bind, InputMappings.INPUT_INVALID);
				}
			}

			LOGGER.info("All keys unbound. Cleaning up...");
			originalBinding = null;
			config.writeBool("doReset", false);
			LOGGER.info("Done!");
		}
	}
}