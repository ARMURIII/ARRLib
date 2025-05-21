package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.interfaces.IPlayerTargeting;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements IPlayerTargeting {
    @Unique
    @Nullable Entity lastTarget;
    @Unique
    int targetDecayTick;

    public @Nullable Entity ARRLib$getTarget() {
        return this.lastTarget;
    }

    @Override
    public int ARRLib$getDecayTicks() {
        return targetDecayTick;
    }


    @Override
    public void ARRLib$setTarget(Entity target,int tick) {
        this.lastTarget = target;
        this.targetDecayTick = tick;
    }


    @Override
    public void ARRLib$setTarget(Entity target) {
        this.lastTarget = target;
        this.targetDecayTick = 60;
    }

    @Override
    public void ARRLib$setDecayTicks(int ticks) {
        this.targetDecayTick = ticks;
    }

    @Override
    public void ARRLib$addDecayTicks(int ticks) {
        this.targetDecayTick += ticks;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void decayTarget(CallbackInfo ci) {
        if (this.targetDecayTick > 0) {
            --this.targetDecayTick;
            if (this.targetDecayTick == 0) {
                this.ARRLib$setTarget(null,0);
            }
        }

    }
}