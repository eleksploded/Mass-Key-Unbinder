package com.eleksploded.massunbind;

import net.minecraftforge.common.ForgeConfigSpec;

public class UnbindConfig {
	public static final String CATEGORY_GENERAL = "general";

	static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
	public static General general = new General(builder);

	public static class General {
		public ForgeConfigSpec.BooleanValue reset;
		
		public General(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings").push(CATEGORY_GENERAL);		
			reset = builder.comment("Should keys be reset?").define("Reset", true);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec spec = builder.build();
}
