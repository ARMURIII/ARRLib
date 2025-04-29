package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.Immunity.DamageImmunityComponent;
import arr.armuriii.arrlib.cca.Immunity.EffectImmunityComponent;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {

    @ModifyReturnValue(method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder ARRLib$addAttribute(DefaultAttributeContainer.Builder original) {
        return original.add(ARRLibEntityAttributes.SWEEPING,0.0);
    }

    /*@ModifyReturnValue(method = "canHaveStatusEffect", at = @At("RETURN"))
    private boolean ARRLib$EffectImmunity(boolean original, StatusEffectInstance effectInstance) {
        LivingEntity living = (LivingEntity) (Object)this;
        if (living instanceof PlayerEntity player) {
            Optional<EffectImmunityComponent> EComponent = ARRLib.EFFECT_IMMUNITY.maybeGet(player);
            if (EComponent.isEmpty()) return original;
            for (StatusEffect effect : EComponent.get().getImmunity()) {
                if (effect == effectInstance.getEffectType()) {
                    return false;
                }
            }
        }
        return original;
    }*/
}
