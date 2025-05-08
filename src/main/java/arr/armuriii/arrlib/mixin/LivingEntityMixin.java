package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.Immunity.EffectImmunityComponent;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {

    @ModifyReturnValue(method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder ARRLib$addAttribute(DefaultAttributeContainer.Builder original) {
        return original.add(ARRLibEntityAttributes.SWEEPING,0.0);
    }

    @ModifyReturnValue(method = "canHaveStatusEffect", at = @At("RETURN"))
    private boolean ARRLib$EffectImmunity(boolean original, StatusEffectInstance effectInstance) {
        LivingEntity living = (LivingEntity) (Object)this;
            Optional<EffectImmunityComponent> EComponent = ARRLib.EFFECT_IMMUNITY.maybeGet(living);
            if (EComponent.isEmpty()) return original;
            for (StatusEffect effect : EComponent.get().getImmunity().toList())
                if (effect == effectInstance.getEffectType())
                    return false;
        return original;
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void ARRLib$EffectImmunity(StatusEffectInstance instance, Entity source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity living = (LivingEntity) (Object)this;
        if (living.getWorld().isClient()) cir.setReturnValue(false);
    }

    /*@ModifyArg(method = "getMainHandStack",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private EquipmentSlot ARRLib$Test(EquipmentSlot slot) {
        if (this.hasStatusEffect(StatusEffects.UNLUCK)) {
            return EquipmentSlot.OFFHAND;
        }
        return slot;
    }

    @ModifyArg(method = "getOffHandStack",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"))
    private EquipmentSlot ARRLib$Test2(EquipmentSlot slot) {
        if (this.hasStatusEffect(StatusEffects.UNLUCK)) {
            return EquipmentSlot.MAINHAND;
        }
        return slot;
    }*/


}
