package com.killerqu.reenchantments.main;

import com.killerqu.reenchantments.enchantments.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;

public class ModEnchants {

    public static final Enchantment EXPERIENCED = register("experienced", new ExperiencedEnchant());
    public static final Enchantment VAMPIRISM = register("vampirism", new VampirismEnchant());
    public static final Enchantment MIDASTOUCH = register("midas_touch", new MidasTouchEnchant());
    public static final Enchantment CRIPPLE = register("cripple", new CrippleEnchant());
    public static final Enchantment HERMESBLESSING = register("hermes_blessing", new HermesBlessingEnchant());

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registry.ENCHANTMENT, new Identifier(ReEnchantments.MOD_ID, name), enchantment);
    }

    public static void registerAll(Logger logger) {
        logger.info("Enchantments loaded!");
    }
}
