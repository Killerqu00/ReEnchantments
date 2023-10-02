package com.killerqu.reenchantments.mixin;

import com.killerqu.reenchantments.ModEnchants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
public abstract class PlayerEntityMixin extends LivingEntity {
    @Unique private static final UUID HERMES_SPEED_BOOST_UUID = UUID.fromString("e24c0784-5bc1-45b6-8334-794fa02163d5");

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "addExperience(I)V", at = @At("HEAD"), argsOnly = true)
    private int reEnchantments$multiplyExpExperiencedEnchant(int experience) {
        return (int) (experience * (1.0 + EnchantmentHelper.getLevel(ModEnchants.EXPERIENCED, this.getEquippedStack(EquipmentSlot.HEAD)) * 0.1));
    }

    @Inject(method = "onKilledOther", at = @At("TAIL"))
    public void reEnchantments$vampirismHealOnKill(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        this.heal(EnchantmentHelper.getLevel(ModEnchants.VAMPIRISM, this.getEquippedStack(EquipmentSlot.MAINHAND)));
    }

    @Inject(method = "onKilledOther", at = @At("TAIL"))
    public void reEnchantments$midasTouchOnKill(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        if (!other.isBaby() && EnchantmentHelper.getLevel(ModEnchants.MIDASTOUCH, this.getEquippedStack(EquipmentSlot.MAINHAND)) == 1) {
            world.spawnEntity(new ItemEntity(world, other.getX(), other.getY(), other.getZ(), new ItemStack(Registries.ITEM.get(Identifier.tryParse("minecraft:gold_nugget")))));
        }
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void reEnchantments$crippleAttack(Entity target, CallbackInfo ci, float f, float g, float h, boolean bl, boolean bl2, int i, boolean crit, boolean bl4, double d, float j, boolean bl5) {
        if (crit) {
            var level = EnchantmentHelper.getLevel(ModEnchants.CRIPPLE, this.getEquippedStack(EquipmentSlot.MAINHAND));
            if (level > 0 && target instanceof LivingEntity living) {
                living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, level * 20), this);
            }
        }
    }

    @ModifyArgs(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setMovementSpeed(F)V"))
    public void reEnchantments$setMovementSpeedWithHermesBlessing(Args args) {
        var level = EnchantmentHelper.getLevel(ModEnchants.HERMESBLESSING, this.getEquippedStack(EquipmentSlot.FEET));
        var playerSpeed = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (playerSpeed != null) {
            var hermesBoost = new EntityAttributeModifier(HERMES_SPEED_BOOST_UUID, "Hermes Blessing speed boost", level * 0.08, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
            if (this.getHealth() == this.getMaxHealth() && level > 0 && playerSpeed.getModifier(HERMES_SPEED_BOOST_UUID) == null) {
                playerSpeed.addTemporaryModifier(hermesBoost);
            } else if (playerSpeed.getModifier(HERMES_SPEED_BOOST_UUID) != null && (this.getHealth() != this.getMaxHealth() || level == 0)) {
                playerSpeed.removeModifier(hermesBoost);
            }
        }
    }
}