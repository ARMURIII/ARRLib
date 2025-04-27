package arr.armuriii.arrlib.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    /*@Inject(method = "getArmPose", at = @At(value = "HEAD"), cancellable = true)
    private static void ARRLib$changeArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof CustomArmPoseItem armPoseItem) {
            BipedEntityModel.ArmPose pose = armPoseItem.getArmPose(itemStack, player, hand);
            if (pose != null) {
                cir.setReturnValue(pose);
            }
        }
    }*/
}