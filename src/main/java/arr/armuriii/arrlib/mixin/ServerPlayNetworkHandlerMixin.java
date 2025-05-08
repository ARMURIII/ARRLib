package arr.armuriii.arrlib.mixin;


import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.DiscardPlayerComponent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Debug(export = true)
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow @Final private MinecraftServer server;

    @ModifyVariable(method = "sendPacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At(value = "HEAD", ordinal = 0), argsOnly = true)
    private Packet<?> ARRLib$RemoveDiscardedPlayerFromTabList(Packet<?> packet) {
        if ((Object) this instanceof ServerPlayPacketListener listener) {
            if (packet instanceof PlayerListS2CPacket list) {
                ObjectArrayList<ServerPlayerEntity> shownPlayers = new ObjectArrayList<>();
                int shown = 0;
                for (PlayerListS2CPacket.Entry entry : list.getEntries()) {
                    ServerPlayerEntity player = server.getPlayerManager().getPlayer(entry.profileId());
                    if (player != null) {
                        Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(player);
                        if (DPComponent.isEmpty() || !DPComponent.get().isDiscarded()) {
                            shown++;
                            shownPlayers.add(player);
                        }
                    }
                }
                if (shown != list.getEntries().size()) {
                    return new PlayerListS2CPacket(list.getActions(), shownPlayers);
                }
            }
        }
        return packet;
    }
}
