package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.cca.EntityEnchantmentComponent;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.SweepingEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getSweepingMultiplier", at = @At("RETURN"))
    private static float ARRLib$changeSweepingMultiplier(float original, LivingEntity living) {
        return (living.getAttributeValue(ARRLibEntityAttributes.SWEEPING) > 0.0  && original == 0.0f) ? SweepingEnchantment.getMultiplier(1) : original;
    }

    @ModifyReturnValue(method = "getEquipmentLevel", at = @At("RETURN"))
    private static int ARRLib$addPlayerEnchantment(int original, Enchantment enchantment, LivingEntity entity) {
        if (original > 0) return original;
        EntityEnchantmentComponent PEComponent = EntityEnchantmentComponent.get(entity);
        if (PEComponent.getEnchantment().anyMatch(PEnchantment -> PEnchantment == enchantment))
            return enchantment.getMaxLevel();
        return original;
    }

    @ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
    private static int ARRLib$addPlayerEnchantment(int original, Enchantment enchantment, ItemStack stack) {
        if (original > 0) return original;
        if (stack.getHolder() != null && stack.getHolder() instanceof LivingEntity entity) {
            EntityEnchantmentComponent PEComponent = EntityEnchantmentComponent.get(entity);
            if (PEComponent.getEnchantment().anyMatch(PEnchantment -> PEnchantment == enchantment))
                return enchantment.getMaxLevel();
        }
        return original;
    }

    @Inject(method = "onUserDamaged", at = @At("TAIL"))
    private static void ARRLib$getUserPE(LivingEntity user, Entity attacker, CallbackInfo ci, @Local EnchantmentHelper.Consumer consumer) {
            EntityEnchantmentComponent PEComponent = EntityEnchantmentComponent.get(user);
            for (Enchantment enchantment : PEComponent.getEnchantment().toList()) {
                consumer.accept(enchantment,enchantment.getMaxLevel());
            }

    }

    @Inject(method = "onTargetDamaged", at = @At("TAIL"))
    private static void ARRLib$getTargetPE(LivingEntity user, Entity target, CallbackInfo ci, @Local EnchantmentHelper.Consumer consumer) {
        EntityEnchantmentComponent PEComponent = EntityEnchantmentComponent.get(user);
        for (Enchantment enchantment : PEComponent.getEnchantment().toList()) {
            consumer.accept(enchantment,enchantment.getMaxLevel());
        }
    }
}
