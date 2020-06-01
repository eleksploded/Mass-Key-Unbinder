package com.eleksploded.massunbind;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("massunbind")
public class MassUnbind
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    
    KeyBinding[] originalBinding;

    public MassUnbind() {
        MinecraftForge.EVENT_BUS.register(this);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, UnbindConfig.spec);
        
        originalBinding = Minecraft.getInstance().gameSettings.keyBindings.clone();
    }
    
    @SuppressWarnings("unused")
	@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void tick(final TickEvent e) {
    	if(!UnbindConfig.general.reset.get()) return;
    	
    	LOGGER.info("Unbinding keys...");
    	
    	for(KeyBinding bind : Minecraft.getInstance().gameSettings.keyBindings) {
    		if(!ArrayUtils.contains(originalBinding, bind)) {
    			LOGGER.info("Unbinding key: " + bind.getLocalizedName() + " in " + bind.getKeyCategory());
    			Minecraft.getInstance().gameSettings.setKeyBindingCode(bind, InputMappings.INPUT_INVALID);
    		}
    	}
    	
    	LOGGER.info("All keys unbound. Cleaning up...");
    	for(KeyBinding key : originalBinding) {
    		key = null;
    	}
    	originalBinding = null;
    	UnbindConfig.general.reset.set(false);
    	UnbindConfig.general.reset.save();
    	LOGGER.info("Done!");
    }
}
