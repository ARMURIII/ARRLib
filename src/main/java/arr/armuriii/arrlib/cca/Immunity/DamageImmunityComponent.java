package arr.armuriii.arrlib.cca.Immunity;

import arr.armuriii.arrlib.ARRLib;
import com.eightsidedsquare.potluck.common.util.ModUtil;
import com.google.common.primitives.Ints;
import com.mojang.serialization.DynamicOps;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Dynamic;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"unused"})
public class DamageImmunityComponent implements AutoSyncedComponent {
    private final PlayerEntity entity;
    private Set<RegistryEntry<DamageType>> immuneDamage = Set.of();

    public DamageImmunityComponent(PlayerEntity entity) {
        this.entity = entity;
    }

    public void addImmunity(DamageType damageType) {
        immuneDamage.add(this.getRegistryEntry(damageType));
        this.sync();
    }

    public void addImmunity(RegistryKey<DamageType> damageType) {
        this.addImmunity(this.registryKeyToDamageType(damageType));
    }

    public List<DamageType> getImmunity() {
        return immuneDamage.stream().map(RegistryEntry::value).toList();
    }

    private Stream<String> getImmunityId() {
        return immuneDamage.stream().map(RegistryEntry::value).map(DamageType::msgId);
    }

    public boolean isImmune(RegistryKey<DamageType> damageType) {
        return this.isImmune(this.registryKeyToDamageType(damageType));
    }

    public boolean isImmune(DamageType damageType) {
        return immuneDamage.contains(this.getRegistryEntry(damageType));
    }

    public void removeImmunity(DamageType damageType) {
        immuneDamage.remove(this.getRegistryEntry(damageType));
        this.sync();
    }

    public void removeImmunity(RegistryKey<DamageType> damageType) {
        this.removeImmunity(this.registryKeyToDamageType(damageType));
    }

    public void forEach(Consumer<RegistryEntry<DamageType>> consumer) {
        immuneDamage.forEach(consumer);
        this.sync();
    }

    public void removeIf(Predicate<RegistryEntry<DamageType>> predicate) {
        immuneDamage.removeIf(predicate);
        this.sync();
    }

    public static DamageImmunityComponent get(LivingEntity living) {
        return ARRLib.DAMAGE_IMMUNITY.get(living);
    }

    private void sync() {
        ARRLib.DAMAGE_IMMUNITY.sync(this.entity);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbt) {
        this.immuneDamage.clear();
        if (nbt.contains("damage_immunity") && nbt.contains("damage_immunity")) {
            for(int i = 0; i < nbt.getIntArray("damage_immunity").length; i++) {
                String id = nbt.getList("damage_immunity", NbtElement.STRING_TYPE).stream().toList().get(i).asString();
                this.immuneDamage.add(this.getRegistryEntry(ARRLib.getDamageTypeByMsgId(id,getRegistry())));
            }
        }
        this.immuneDamage.clear();
        if (nbt.contains("damage_immunity")) {
            RegistryOps<NbtElement> ops = RegistryOps.of(NbtOps.INSTANCE, new );
            this.getRegistry().createEntryCodec().parse(, nbt.get("damage_immunity")).ifSuccess((list) -> {
                list.forEach(this::addImmunity);
            });
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        NbtList list = new NbtList();
        List<String> ids = this.getImmunityId().toList();
        for (String id : ids) {
            list.add(NbtString.of(id));
        }
        nbt.put("effect_immunity",list);
    }

    private DamageType registryKeyToDamageType(RegistryKey<DamageType> damageType) {
        Registry<DamageType> registry = this.getRegistry();
        return registry.get(damageType);
    }

    private Registry<DamageType> getRegistry() {
        DynamicRegistryManager manager = entity.getWorld().getRegistryManager();
        return manager.get(RegistryKeys.DAMAGE_TYPE);
    }

    private RegistryEntry<DamageType> getRegistryEntry(DamageType damageType) {
        Registry<DamageType> registry = this.getRegistry();
        return registry.getEntry(damageType);
    }
}
