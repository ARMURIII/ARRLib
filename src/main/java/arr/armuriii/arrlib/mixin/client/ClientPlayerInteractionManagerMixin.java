package arr.armuriii.arrlib.mixin.client;

import arr.armuriii.arrlib.interfaces.IClientPlayerInteractionManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private ClientPlayNetworkHandler networkHandler;

    @Shadow private int lastSelectedSlot;

    @Override
    public void ARRLib$forceSyncSelectedSlot(boolean updateLastSelected) {
        int i = this.client.player.getInventory().selectedSlot;
        if (updateLastSelected) {
            this.lastSelectedSlot = i;
        }
        this.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(i));
    }

    @Override
    public void ARRLib$forceSyncSelectedSlot(int slot) {
        int i = this.client.player.getInventory().selectedSlot;
        this.lastSelectedSlot = slot;
        this.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    @Override
    public int ARRLib$getSelectedSlot() {
        return this.lastSelectedSlot;
    }
}
