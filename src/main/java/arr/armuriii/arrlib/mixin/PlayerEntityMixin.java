package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Debug(export = true)
@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin {

    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item ARRLib$addAttribute(Item original) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        return player.getAttributeValue(ARRLibEntityAttributes.SWEEPING) > 0.0 ? Items.WOODEN_SWORD : original;
    }

    @ModifyArgs(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
    private void ARRLib$modifySweepingBox(Args args) {
        PlayerEntity player = (PlayerEntity) (Object)this;
        double sweeping = player.getAttributeValue(ARRLibEntityAttributes.SWEEPING);
        args.set(0, sweeping);
        args.set(1, sweeping/4);
        args.set(2, sweeping);
    }

    @ModifyArgs(method = "spawnSweepAttackParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
    private void ARRLib$addAttribute(Args args) {
        PlayerEntity player = (PlayerEntity)(Object)this;
        ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND);
        args.set(0,itemStack.getItem().ARRLib$getSweepAttackParticle(itemStack,player));
    }
}
