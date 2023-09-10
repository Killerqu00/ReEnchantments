package com.killerqu.reenchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class HermesBlessingEnchant extends Enchantment {
    public HermesBlessingEnchant() {
        super(Rarity.COMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @Override
    public int getMinPower(int level) {
        return (level - 1) * 5;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}
