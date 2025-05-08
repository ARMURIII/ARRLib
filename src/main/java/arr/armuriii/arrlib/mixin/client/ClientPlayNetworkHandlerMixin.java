package arr.armuriii.arrlib.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    /*@Inject(method = "onPlayerList", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;",ordinal = 0))
    private void ARRLib$RemoveDiscardedPlayers(PlayerListS2CPacket packet, CallbackInfo ci, @Local PlayerListS2CPacket.Entry entry) {

    }*/
}
