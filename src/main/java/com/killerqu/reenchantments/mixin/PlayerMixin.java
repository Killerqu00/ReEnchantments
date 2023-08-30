package com.killerqu.reenchantments.mixin;

import com.killerqu.reenchantments.enchantments.ModEnchants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Debug(export = true)
@Mixin(PlayerEntity.class)
public class PlayerMixin {
    @ModifyVariable(method = "addExperience(I)V", at = @At("HEAD"), argsOnly = true)
    private int multiplyExpExperiencedEnchant(int experience) {
        System.out.println("ITWORKS");
        PlayerEntity player = ((PlayerEntity)(Object)this);
        int enchLevel = EnchantmentHelper.getLevel(ModEnchants.EXPERIENCED, player.getEquippedStack(EquipmentSlot.HEAD));
        return (int) (experience * (1.0 + enchLevel * 0.1));
    }
}
