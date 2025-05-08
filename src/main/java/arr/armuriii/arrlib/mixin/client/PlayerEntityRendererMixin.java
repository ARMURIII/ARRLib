package arr.armuriii.arrlib.mixin.client;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.DiscardPlayerComponent;
import arr.armuriii.arrlib.interfaces.IPlayerEntityRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;


@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin implements IPlayerEntityRenderer {

    @Unique
    private boolean slim = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void ARRLib$getSlimness(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        this.slim = slim;
    }

    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    private void ARRLib$hideWhenDiscarded(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null) {
            Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(abstractClientPlayerEntity);
            if (DPComponent.isPresent() && DPComponent.get().isDiscarded()) {
                if (!MinecraftClient.getInstance().options.getPerspective().isFirstPerson()) {
                    ci.cancel();
                }
                if (isInFOV(MinecraftClient.getInstance().player, abstractClientPlayerEntity, 100)) {
                    ci.cancel();
                }
            }
        }
    }

    @Override
    public boolean ARRLib$isSlim() {
        return this.slim;
    }

    @Unique
    private static boolean isInFOV(Entity watcher, Entity invisible, float FOV) {
        double fov = Math.toRadians(FOV);
        Vec3d vec3dFacing = watcher.getRotationVec(1.0f).normalize();
        Vec3d vec3dToInvisible = invisible.getPos().subtract(watcher.getPos()).normalize();
        double angle = Math.acos(vec3dFacing.dotProduct(vec3dToInvisible));
        return angle <= Math.cos(fov / 2);
    }
}