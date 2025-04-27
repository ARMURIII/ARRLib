package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.SweepingEnchantment;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getSweepingMultiplier", at = @At("RETURN"))
    private static float ARRLib$changeSweepingMultiplier(float original, LivingEntity living) {
        return (living.getAttributeValue(ARRLibEntityAttributes.SWEEPING) > 0.0  && original == 0.0f) ? SweepingEnchantment.getMultiplier(1) : original;
    }
}
