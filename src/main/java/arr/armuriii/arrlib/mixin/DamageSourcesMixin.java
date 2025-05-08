package arr.armuriii.arrlib.mixin;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {

    @ModifyReturnValue(method = "playerAttack", at = @At("RETURN"))
    private DamageSource ARRLib$customDamageSource(DamageSource original, PlayerEntity attacker) {
        ItemStack stack = attacker.getMainHandStack();
        if (!stack.isEmpty()) {
            return stack.getItem().ARRLib$getCustomDamageSource(stack,attacker,original);
        }
        return original;
    }

    @ModifyReturnValue(method = "mobAttack", at = @At("RETURN"))
    private DamageSource ARRLib$customDamageSource(DamageSource original, LivingEntity attacker) {
        ItemStack stack = attacker.getMainHandStack();
        if (!stack.isEmpty()) {
            return stack.getItem().ARRLib$getCustomDamageSource(stack,attacker,original);
        }
        return original;
    }
}
