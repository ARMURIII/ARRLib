package arr.armuriii.arrlib.init;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import static arr.armuriii.arrlib.ARRLib.MOD_HELPER;

public class ARRLibPackets {

    public static final Identifier TARGETING_PACKET = MOD_HELPER.id("targeting");

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(TARGETING_PACKET, (minecraftServer, serverPlayer, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int id = packetByteBuf.readInt();
            minecraftServer.execute(() -> {
                Entity entity = serverPlayer.getWorld().getEntityById(id);
                    if (serverPlayer.ARRLib$getTarget() == entity) serverPlayer.ARRLib$addDecayTicks(2);
                    else serverPlayer.ARRLib$setTarget(entity,60);
            });
        });
    }
}
