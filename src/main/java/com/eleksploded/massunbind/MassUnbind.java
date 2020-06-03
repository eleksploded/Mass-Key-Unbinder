package com.eleksploded.massunbind;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// 1.12
@Mod(modid = "massunbind")
public class MassUnbind
{
	KeyBinding[] originalBinding;
	private static Logger LOGGER;

	public static File configFile;
	public static Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		LOGGER = e.getModLog();
		File directory = e.getModConfigurationDirectory();
		configFile = new File(directory.getPath(), "massunbind.cfg");
		config = new Configuration(configFile);
		MinecraftForge.EVENT_BUS.register(this);
		originalBinding = Minecraft.getMinecraft().gameSettings.keyBindings.clone();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tick(final TickEvent e) {
		if(UnbindConfig.doReset) {

			for(KeyBinding bind : Minecraft.getMinecraft().gameSettings.keyBindings) {
				if(!ArrayUtils.contains(originalBinding, bind)) {
					LOGGER.info("Unbinding key: " + bind.getDisplayName() + " in " + bind.getKeyCategory());
					Minecraft.getMinecraft().gameSettings.setOptionKeyBinding(bind, 0);
				}
			}

			LOGGER.info("All keys unbound. Cleaning up...");
			if(originalBinding != null) {
				for(@SuppressWarnings("unused") KeyBinding key : originalBinding) {
					key = null;
				}
				originalBinding = null;
			}
			Property prop = config.get("general", "doReset", false);
			prop.set(false);
			UnbindConfig.doReset = false;
			config.save();
			LOGGER.info("Done!");
		}
	}
}
