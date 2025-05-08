package arr.armuriii.arrlib.mixin.client;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.DiscardPlayerComponent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
    private static void ARRLib$hideDiscardedHitBoxes(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {
            Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(player);
            if (DPComponent.isPresent() && DPComponent.get().isDiscarded()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
    private static void ARRLib$hideDiscardedShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, float opacity, float tickDelta, WorldView world, float radius, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) {
            Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(player);
            if (DPComponent.isPresent() && DPComponent.get().isDiscarded()) {
                ci.cancel();
            }
        }
    }
}
