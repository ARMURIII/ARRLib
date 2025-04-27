package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
abstract class LivingEntityMixin {

    @ModifyReturnValue(method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder ARRLib$addAttribute(DefaultAttributeContainer.Builder original) {
        return original.add(ARRLibEntityAttributes.SWEEPING,0.0);
    }
}
