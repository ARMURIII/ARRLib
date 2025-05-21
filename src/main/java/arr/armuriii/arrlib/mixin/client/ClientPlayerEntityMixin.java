package arr.armuriii.arrlib.mixin.client;

import arr.armuriii.arrlib.init.ARRLibPackets;
import arr.armuriii.arrlib.interfaces.IPlayerTargeting;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements IPlayerTargeting {
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
        if (target != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(target.getId());
            ClientPlayNetworking.send(ARRLibPackets.TARGETING_PACKET, buf);
        }
    }


    @Override
    public void ARRLib$setTarget(Entity target) {
        this.lastTarget = target;
        this.targetDecayTick = 60;
        if (target != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(target.getId());
            ClientPlayNetworking.send(ARRLibPackets.TARGETING_PACKET, buf);
        }
    }

    @Override
    public void ARRLib$setDecayTicks(int ticks) {
        this.targetDecayTick = ticks;
    }

    @Override
    public void ARRLib$addDecayTicks(int ticks) {
        this.targetDecayTick += ticks;
    }

    @Inject(
            method = {"tick"},
            at = {@At("TAIL")}
    )
    private void decayTarget(CallbackInfo ci) {
        if (this.targetDecayTick > 0) {
            --this.targetDecayTick;
            if (this.targetDecayTick == 0) {
                this.ARRLib$setTarget(null,0);
            }
        }

    }
}
