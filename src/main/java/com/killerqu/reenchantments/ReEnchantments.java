package com.killerqu.reenchantments;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReEnchantments implements ModInitializer {
    public static final String MOD_ID = "reenchantments";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModEnchants.init();
    }

    public static Identifier id(String name) {
        return new Identifier(MOD_ID, name);
    }
}