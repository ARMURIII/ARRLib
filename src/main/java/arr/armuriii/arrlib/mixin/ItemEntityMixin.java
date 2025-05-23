package arr.armuriii.arrlib.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getStack();

    @Inject(method = "tick",at = @At("TAIL"))
    private void ARRLib$ItemTick(CallbackInfo ci) {
        this.getStack().getItem().ARRLib$entityTick();
        ItemEntity entity = (ItemEntity)(Object)this;
        if (ARRLib$isImportant(entity)) {
            entity.setNoGravity(true);
            if (entity.getY() <= entity.getWorld().getBottomY()) {
                entity.setVelocity(0,1,0);
            }else {
                entity.setVelocity(entity.getVelocity().x,-0.1,entity.getVelocity().z);
            }
        }
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V",ordinal = 1))
    private boolean ARRLib$despawn(ItemEntity instance) {
        return !ARRLib$isImportant(instance);
    }

    @Inject(method = "damage",at = @At("HEAD"), cancellable = true)
    private void ARRLib$invuln(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity entity = (ItemEntity)(Object)this;
        if (ARRLib$isImportant(entity))
            cir.setReturnValue(false);
        if (getStack().getItem().ARRLib$isExplosionProof() && source.isIn(DamageTypeTags.IS_EXPLOSION))
            cir.setReturnValue(false);
    }

    @Unique
    private boolean ARRLib$isImportant(ItemEntity entity) {
        return entity.getStack().getItem().ARRLib$isImportantItemEntity(entity);
    }
}
