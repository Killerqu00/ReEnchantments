package com.killerqu.reenchantments;

import com.killerqu.reenchantments.enchantments.ModEnchants;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReEnchantments implements ModInitializer {
    public static final String MOD_ID = "reenchantments";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        ModEnchants.registerAll(LOGGER);
    }
}
