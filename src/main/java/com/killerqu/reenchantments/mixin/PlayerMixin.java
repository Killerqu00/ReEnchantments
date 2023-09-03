package com.killerqu.reenchantments.mixin;

import com.killerqu.reenchantments.main.ModEnchants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerMixin {
    @ModifyVariable(method = "addExperience(I)V", at = @At("HEAD"), argsOnly = true)
    private int multiplyExpExperiencedEnchant(int experience) {
        PlayerEntity player = ((PlayerEntity)(Object)this);
        int enchLevel = EnchantmentHelper.getLevel(ModEnchants.EXPERIENCED, player.getEquippedStack(EquipmentSlot.HEAD));
        return (int) (experience * (1.0 + enchLevel * 0.1));
    }

    @Inject(method = "onKilledOther", at = @At("TAIL"))
    public void vampirismHealOnKill(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ItemStack mainhand = player.getEquippedStack(EquipmentSlot.MAINHAND);
        player.heal(EnchantmentHelper.getLevel(ModEnchants.VAMPIRISM, mainhand));
    }

    @Inject(method = "onKilledOther", at = @At("TAIL"))
    public void midasTouchOnKill(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ItemStack mainhand = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!other.isBaby() && EnchantmentHelper.getLevel(ModEnchants.MIDASTOUCH, mainhand) == 1) {
            world.spawnEntity(new ItemEntity(world, other.getX(), other.getY(), other.getZ(), new ItemStack(Registries.ITEM.get(Identifier.tryParse("minecraft:gold_nugget")))));
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"))
    public void attack(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ItemStack mainhand = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (target instanceof LivingEntity && EnchantmentHelper.getLevel(ModEnchants.CRIPPLE, mainhand) > 0) {
            ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, EnchantmentHelper.getLevel(ModEnchants.CRIPPLE, mainhand)*20), player);
        }
    }
}
