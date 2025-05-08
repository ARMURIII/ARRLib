package arr.armuriii.arrlib.mixin.client;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.DiscardPlayerComponent;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {super(world, pos, yaw, gameProfile);}

    @ModifyReturnValue(method = "getSkinTexture", at = @At("RETURN"))
    public Identifier ARRLib$changeSkin(Identifier original) {
        Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(this);
        if (DPComponent.isPresent() && DPComponent.get().isDiscarded()) {
            return ARRLib.MOD_HELPER.id("textures/entity/player/slim/discarded.png");
        }
        return original;
    }

    @ModifyReturnValue(method = "getCapeTexture", at = @At("RETURN"))
    public Identifier ARRLib$changeCape(Identifier original) {
        Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(this);
        if (DPComponent.isPresent() && DPComponent.get().isDiscarded()) {
            return ARRLib.MOD_HELPER.id("textures/entity/player/cape/discarded.png");
        }
        return original;
    }

    @ModifyReturnValue(method = "getModel", at = @At("RETURN"))
    public String ARRLib$changeModel(String original) {
        Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(this);
        if (DPComponent.isPresent() && DPComponent.get().isDiscarded()) {
            return "slim";
        }
        return original;
    }


}
