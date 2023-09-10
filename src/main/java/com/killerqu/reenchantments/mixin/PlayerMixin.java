package com.killerqu.reenchantments.mixin;

import com.killerqu.reenchantments.main.ModEnchants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.UUID;

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
            world.spawnEntity(new ItemEntity(world, other.getX(), other.getY(), other.getZ(), new ItemStack(Registry.ITEM.get(Identifier.tryParse("minecraft:gold_nugget")))));
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void crippleAttack(Entity target, CallbackInfo ci, float f, float g, float h, boolean bl, boolean bl2, int i, boolean bl3, boolean bl4, double d, float j, boolean bl5) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ItemStack mainhand = player.getEquippedStack(EquipmentSlot.MAINHAND);
        if (target instanceof LivingEntity && EnchantmentHelper.getLevel(ModEnchants.CRIPPLE, mainhand) > 0 && bl3) {
            ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, EnchantmentHelper.getLevel(ModEnchants.CRIPPLE, mainhand)*20), player);
        }
    }

    @ModifyArgs(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setMovementSpeed(F)V"))
    public void setMovementSpeedWithHermesBlessing(Args args) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);
        int level = EnchantmentHelper.getLevel(ModEnchants.HERMESBLESSING, boots);
        EntityAttributeInstance playerSpeed = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        final UUID HERMES_SPEED_BOOST_UUID = UUID.fromString("e24c0784-5bc1-45b6-8334-794fa02163d5");
        final EntityAttributeModifier HERMES_SPEED_BOOST = new EntityAttributeModifier(HERMES_SPEED_BOOST_UUID, "Hermes Blessing speed boost", level*0.08, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        if (player.getHealth() == player.getMaxHealth() && level > 0 && playerSpeed.getModifier(HERMES_SPEED_BOOST_UUID) == null) {
            playerSpeed.addTemporaryModifier(HERMES_SPEED_BOOST);
        }
        if (playerSpeed.getModifier(HERMES_SPEED_BOOST_UUID) != null && (player.getHealth() != player.getMaxHealth() || level == 0)){
            playerSpeed.removeModifier(HERMES_SPEED_BOOST);
        }
    }
}
