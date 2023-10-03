package com.killerqu.reenchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModEnchants {
    Map<Identifier, Enchantment> ENCHANTMENTS = new HashMap<>();

    Enchantment EXPERIENCED = register("experienced", new SimpleEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD).withMinPower(level -> 19 + level).withMaxPower(level -> 25 + level).withMaxLevel(() -> 5));
    Enchantment VAMPIRISM = register("vampirism", new SimpleEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND).withMinPower(level -> 20 + (level - 1) * 5).withMaxLevel(() -> 2));
    Enchantment MIDASTOUCH = register("midas_touch", new SimpleEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND).withTreasure());
    Enchantment CRIPPLE = register("cripple", new SimpleEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND).withMinPower(level -> 15 + (level - 1) * 5).withMaxLevel(() -> 3));
    Enchantment HERMESBLESSING = register("hermes_blessing", new SimpleEnchantment(Enchantment.Rarity.COMMON, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET).withMinPower(level -> (level - 1) * 5).withMaxLevel(() -> 5));

    private static Enchantment register(String name, Enchantment enchantment) {
        ENCHANTMENTS.put(ReEnchantments.id(name), enchantment);
        return enchantment;
    }

    static void init() {
        ENCHANTMENTS.forEach((id, enchantment) -> Registry.register(Registries.ENCHANTMENT, id, enchantment));
        ReEnchantments.LOGGER.info("Enchantments loaded!");
    }

    @SuppressWarnings("unused")
    class SimpleEnchantment extends Enchantment {
        private Function<Integer, Integer> minPower = super::getMinPower;
        private Function<Integer, Integer> maxPower = super::getMaxPower;
        private Supplier<Integer> minLevel = super::getMinLevel;
        private Supplier<Integer> maxLevel = super::getMaxLevel;
        private boolean isTreasure = false;

        public SimpleEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slots) {
            super(weight, target, slots);
        }

        public SimpleEnchantment withMinPower(Function<Integer, Integer> minPower) {
            this.minPower = minPower;
            return this;
        }

        public SimpleEnchantment withMaxPower(Function<Integer, Integer> maxPower) {
            this.maxPower = maxPower;
            return this;
        }

        public SimpleEnchantment withMinLevel(Supplier<Integer> minLevel) {
            this.minLevel = minLevel;
            return this;
        }

        public SimpleEnchantment withMaxLevel(Supplier<Integer> maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }

        public SimpleEnchantment withTreasure() {
            this.isTreasure = true;
            return this;
        }

        @Override
        public int getMinPower(int level) {
            return this.minPower.apply(level);
        }

        @Override
        public int getMaxPower(int level) {
            return this.maxPower.apply(level);
        }

        @Override
        public int getMinLevel() {
            return this.minLevel.get();
        }

        @Override
        public int getMaxLevel() {
            return this.maxLevel.get();
        }

        @Override
        public boolean isTreasure() {
            return this.isTreasure;
        }
    }
}