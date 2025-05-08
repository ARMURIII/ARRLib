package arr.armuriii.arrlib.cca;

import arr.armuriii.arrlib.ARRLib;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@SuppressWarnings({"unused"})
public class DiscardPlayerComponent implements AutoSyncedComponent {
    private final PlayerEntity entity;
    private boolean discarded = false;
    private final String id = "discarded";

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
        this.sync();
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public DiscardPlayerComponent(PlayerEntity entity) {
        this.entity = entity;
    }

    public static DiscardPlayerComponent get(PlayerEntity living) {
        return ARRLib.DISCARD_PLAYER.get(living);
    }

    private void sync() {
        ARRLib.DISCARD_PLAYER.sync(this.entity);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbt) {
        discarded = false;
        if (nbt.contains(id)) {
            discarded = nbt.getBoolean(id);
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        nbt.putBoolean(id,discarded);
    }
}
