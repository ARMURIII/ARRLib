package arr.armuriii.arrlib.mixin.client.movement;

import arr.armuriii.arrlib.ARRLib;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(Camera.class)
public abstract class CameraMixin {
    @WrapWithCondition(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    public boolean update(Camera instance, float yaw, float pitch) {
        return ARRLib.LOCK_MOVEMENT.maybeGet(MinecraftClient.getInstance().player).isEmpty() || !ARRLib.LOCK_MOVEMENT.maybeGet(MinecraftClient.getInstance().player).get().camera;
    }
}