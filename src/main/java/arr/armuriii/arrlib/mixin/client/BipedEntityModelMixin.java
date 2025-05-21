package arr.armuriii.arrlib.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> {

    @Inject(
            method = {"setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V"},
            at = @At(
                    value = "TAIL"
            )
    )
    public void ARRLib$poseModel(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        ItemStack mainStack = livingEntity.getMainHandStack();
        ItemStack offStack = livingEntity.getOffHandStack();
        BipedEntityModel<LivingEntity> model = (BipedEntityModel<LivingEntity>)(Object)this;
        if (livingEntity instanceof PlayerEntity player) {
            mainStack.getItem().ARRLib$animateModel(mainStack, player, model, player.getMainArm(), false);
            offStack.getItem().ARRLib$animateModel(offStack, player, model, player.getMainArm().getOpposite(), true);
        }
    }
}