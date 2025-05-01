package arr.armuriii.arrlib.cca.Immunity;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"unused"})
public class ImmunityComponent<T> implements AutoSyncedComponent {
    private final LivingEntity entity;
    Set<RegistryEntry<T>> immunities = new HashSet<>();
    private final String id;
    private final ComponentKey<? extends Component> componentKey;
    private final RegistryKey<Registry<T>> registryKey;

    public ImmunityComponent(LivingEntity entity, ComponentKey<? extends Component> key, RegistryKey<Registry<T>> registryKey, String id) {
        this.entity = entity;
        this.componentKey = key;
        this.registryKey = registryKey;
        this.id = id;
    }

    private String getId() {
        return id;
    }

    private Registry<T> getRegistry() {
        return getRegistryManager().get(getRegistryKey());
    }

    private RegistryKey<Registry<T>> getRegistryKey() {
        return registryKey;
    }

    public Set<RegistryEntry<T>> getImmunityEntry() {
        return immunities;
    }

    public Stream<T> getImmunity() {
        return immunities.stream().map(RegistryEntry::value);
    }

    public void addImmunityEntry(RegistryEntry<T> statusEffect) {
        immunities.add(statusEffect);
        this.sync();
    }

    public void addImmunity(T immunity) {
        immunities.add(this.getRegistryEntry(immunity));
        this.sync();
    }

    public void removeImmunity(T immunity) {
        immunities.remove(this.getRegistryEntry(immunity));
        this.sync();
    }

    public boolean isImmuneTo(T immunity) {
        return immunities.contains(this.getRegistryEntry(immunity));
    }

    public void forEach(Consumer<RegistryEntry<T>> consumer) {
        immunities.forEach(consumer);
        this.sync();
    }

    public void removeIf(Predicate<RegistryEntry<T>> predicate) {
        immunities.removeIf(predicate);
        this.sync();
    }

    public void clear() {
        immunities.clear();
        this.sync();
    }

    void sync() {
        componentKey.sync(this.entity);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbt) {
        this.immunities.clear();
        if (nbt.contains(id)) {
            RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, RegistryWrapper.WrapperLookup
                    .of(getRegistryManager().streamAllRegistries().map((entry -> entry.value().getTagCreatingWrapper()))));
            RegistryCodecs.entryList(this.getRegistryKey()).parse(ops, nbt.get(id))
                    .result().ifPresent(list -> list.forEach(this::addImmunityEntry));
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        if (!this.immunities.isEmpty()) {
            RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, RegistryWrapper.WrapperLookup
                    .of(getRegistryManager().streamAllRegistries().map((entry -> entry.value().getTagCreatingWrapper()))));
            RegistryCodecs.entryList(this.getRegistryKey()).encodeStart(ops, RegistryEntryList.of(this.immunities.stream().toList()))
                    .result().ifPresent((nbtElement) -> nbt.put(id, nbtElement));
        }
    }


    private T registryKeyToStatusEffect(RegistryKey<T> immunity) {
        return this.getRegistry().get(immunity);
    }

    private DynamicRegistryManager getRegistryManager() {
        return entity.getWorld().getRegistryManager();
    }

    RegistryEntry<T> getRegistryEntry(T immunity) {
        return this.getRegistry().getEntry(immunity);
    }
}
