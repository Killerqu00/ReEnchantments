package com.killerqu.reenchantments.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class ExperiencedEnchant extends Enchantment {
    public ExperiencedEnchant() {
        super(Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, new EquipmentSlot[]{EquipmentSlot.HEAD});
    }

    @Override
    public int getMinPower(int level) {
        return 19 + level;
    }

    @Override
    public int getMaxPower(int level) {
        return 25 + level;
    }

    @Override
    public int getMaxLevel() {return 5;}
}
