package com.eleksploded.massunbind;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "massunbind")
public class UnbindConfig {
	
	@Comment("Should run a reset?")
	@RequiresMcRestart
	public static boolean doReset = true;
	
	@Mod.EventBusSubscriber(modid = "massunbind")
	private static class EventHandler {
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals("massunbind")) {
				ConfigManager.sync("massunbind", Config.Type.INSTANCE);
			}
		}
	}
}
