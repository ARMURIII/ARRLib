package arr.armuriii.arrlib.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.mialib.util.MRaycasting;

import java.util.List;
import java.util.function.Predicate;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Shadow @Final public GameOptions options;

    @Shadow @Nullable public abstract Entity getCameraEntity();

    @Shadow @Nullable public ClientWorld world;

    @Shadow @Nullable public ClientPlayerInteractionManager interactionManager;

    public MinecraftClientMixin() {
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;tick(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/hit/HitResult;)V"))
    public void ARRLib$setLastTarget(CallbackInfo ci) {
        Entity camera = this.getCameraEntity();
        if (this.player == null)
            return;
        if (camera == null)
            return;
        if (this.world == null)
            return;
        double distanceCap = 128f * 128f;
        List<Entity> raycast = MRaycasting.raycast(this.player,distanceCap,this::ARRLib$isValidTarget);

        if (!raycast.isEmpty()) {
            if (raycast.get(0) instanceof LivingEntity living) {
                this.player.ARRLib$setTarget(living,60);
            }
        }
    }

    @Inject(
            method = {"doAttack"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void ARRLib$attackTarget(CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null && this.player.ARRLib$getTarget() != null && interactionManager != null) {
            interactionManager.attackEntity(player, player.ARRLib$getTarget());
            player.swingHand(Hand.MAIN_HAND);
            cir.setReturnValue(true);
        }
    }

    @Unique
    public boolean ARRLib$isValidTarget(PlayerEntity player, Entity target) {
        if (!(target instanceof LivingEntity living)) return false;
        if (player == null) return false;
        if (target == player) return false;
        if (!player.canSee(target)) return false;
        if (living.isDead()) return false;
        if (target.isRemoved()) return false;
        if (target.isTeammate(player)) return false;
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) return false;
        if (target instanceof TameableEntity tamed && tamed.isOwner(player)) return false;
        return target.canHit();
    }
}