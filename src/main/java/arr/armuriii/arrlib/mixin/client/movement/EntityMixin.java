package arr.armuriii.arrlib.mixin.client.movement;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.LockPlayerMovementComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value=Entity.class)
public class EntityMixin {
    @Inject(method = {"changeLookDirection"}, at = {@At(value = "HEAD")}, cancellable = true)
    private void ARRLib$BlockChangingDirection(double x, double y, CallbackInfo ci) {
        Optional<LockPlayerMovementComponent> LPMComponent = ARRLib.LOCK_MOVEMENT.maybeGet((Entity)(Object)this);
        if (LPMComponent.isPresent() && LPMComponent.get().camera) {
            ci.cancel();
        }
    }
}