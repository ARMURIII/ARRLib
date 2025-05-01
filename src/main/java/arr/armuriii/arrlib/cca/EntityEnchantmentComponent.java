package arr.armuriii.arrlib.cca;

import arr.armuriii.arrlib.ARRLib;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.enchantment.Enchantment;
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
import java.util.stream.Stream;

@SuppressWarnings({"unused"})
public class EntityEnchantmentComponent implements AutoSyncedComponent {
    private final LivingEntity entity;
    private Set<RegistryEntry<Enchantment>> playerEnchantment = new HashSet<>();
    private final String id = "body_enchantment";

    public EntityEnchantmentComponent(LivingEntity entity) {
        this.entity = entity;
    }

    public Set<RegistryEntry<Enchantment>> getEnchantmentEntry() {
        return playerEnchantment;
    }

    public Stream<Enchantment> getEnchantment() {
        return playerEnchantment.stream().map(RegistryEntry::value);
    }

    public void addEnchantmentEntry(RegistryEntry<Enchantment> enchantment) {
        playerEnchantment.add(enchantment);
    }

    public void addEnchantment(Enchantment enchantment) {
        playerEnchantment.add(this.getRegistryEntry(enchantment));
    }

    public void removeEnchantment(Enchantment enchantment) {
        playerEnchantment.remove(this.getRegistryEntry(enchantment));
    }

    public boolean hasEnchantment(Enchantment enchantment) {
        return playerEnchantment.contains(this.getRegistryEntry(enchantment));
    }

    public static EntityEnchantmentComponent get(LivingEntity living) {
        return ARRLib.ENTITY_ENCHANTMENT.get(living);
    }

    private void sync() {
        ARRLib.ENTITY_ENCHANTMENT.sync(this.entity);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbt) {
        this.playerEnchantment.clear();
        if (nbt.contains(id)) {
            RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, RegistryWrapper.WrapperLookup
                    .of(getRegistryManager().streamAllRegistries().map((entry -> entry.value().getTagCreatingWrapper()))));
            RegistryCodecs.entryList(this.getRegistryKey()).parse(ops, nbt.get(id))
                    .result().ifPresent(list -> list.forEach(this::addEnchantmentEntry));
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        if (!this.playerEnchantment.isEmpty()) {
            RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, RegistryWrapper.WrapperLookup
                    .of(getRegistryManager().streamAllRegistries().map((entry -> entry.value().getTagCreatingWrapper()))));
            RegistryCodecs.entryList(this.getRegistryKey()).encodeStart(ops, RegistryEntryList.of(this.playerEnchantment.stream().toList()))
                    .result().ifPresent((nbtElement) -> nbt.put(id, nbtElement));
        }
    }


    private Enchantment registryKeyToEnchantment(RegistryKey<Enchantment> enchantment) {
        return this.getRegistry().get(enchantment);
    }

    private Registry<Enchantment> getRegistry() {
        return getRegistryManager().get(RegistryKeys.ENCHANTMENT);
    }

    private RegistryKey<Registry<Enchantment>> getRegistryKey() {
        return RegistryKeys.ENCHANTMENT;
    }

    private DynamicRegistryManager getRegistryManager() {
        return entity.getWorld().getRegistryManager();
    }

    private RegistryEntry<Enchantment> getRegistryEntry(Enchantment enchantment) {
        return this.getRegistry().getEntry(enchantment);
    }
}
