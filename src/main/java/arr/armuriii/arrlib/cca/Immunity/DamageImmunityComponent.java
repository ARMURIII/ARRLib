package arr.armuriii.arrlib.cca.Immunity;

import arr.armuriii.arrlib.ARRLib;
import com.google.common.primitives.Ints;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"unused"})
public class DamageImmunityComponent implements AutoSyncedComponent, PlayerComponent {
    private final PlayerEntity entity;
    private Set<Pair<DamageType,Boolean>> immuneDamage = Set.of();

    public DamageImmunityComponent(PlayerEntity entity) {
        this.entity = entity;
    }

    public void addImmunity(DamageType damageType, boolean keptFromDeath) {
        immuneDamage.add(new Pair<>(damageType,keptFromDeath));
        this.sync();
    }

    public void addImmunity(RegistryKey<DamageType> damageType, boolean keptFromDeath) {
        immuneDamage.add(new Pair<DamageType,Boolean>(this.registryKeyToDamageType(damageType),keptFromDeath));
        this.sync();
    }

    public List<DamageType> getImmunity() {
        return immuneDamage.stream().map(Pair::getLeft).toList();
    }

    private Stream<String> getImmunityId() {
        return immuneDamage.stream().map(Pair::getLeft).map(DamageType::msgId);
    }

    private List<Integer> getImmunityBoolean() {
        return immuneDamage.stream().map(Pair::getRight).map((aBoolean -> aBoolean ? 1 : 0)).toList();
    }

    public boolean isImmune(RegistryKey<DamageType> damageType) {
        return (immuneDamage.contains(new Pair<DamageType,Boolean>(this.registryKeyToDamageType(damageType),true))) || (immuneDamage.contains(new Pair<>(this.registryKeyToDamageType(damageType),false)));
    }

    public boolean isImmune(DamageType damageType) {
        return (immuneDamage.contains(new Pair<DamageType,Boolean>(damageType,true))) || (immuneDamage.contains(new Pair<>(damageType,false)));
    }

    public void removeImmunity(DamageType damageType, boolean keptFromDeath) {
        immuneDamage.remove(new Pair<DamageType,Boolean>(damageType,keptFromDeath));
        this.sync();
    }

    public void forEach(Consumer<Pair<DamageType,Boolean>> consumer) {
        immuneDamage.forEach(consumer);
        this.sync();
    }

    public void removeIf(Predicate<Pair<DamageType,Boolean>> predicate) {
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
    public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
        return true;
    }

    @Override
    public void copyForRespawn(Component original, boolean lossless, boolean keepInventory, boolean sameCharacter) {
        if (original instanceof DamageImmunityComponent component) {
                component.removeIf(pair -> !pair.getRight());
        }
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbt) {
        this.immuneDamage.clear();
        if (nbt.contains("damage_immunity_id") && nbt.contains("damage_immunity_kept")) {
            for(int i = 0; i < nbt.getIntArray("damage_immunity_id").length; i++) {
                String id = nbt.getList("damage_immunity_id", NbtElement.STRING_TYPE).stream().toList().get(i).asString();
                int kept = Ints.indexOf(nbt.getIntArray("damage_immunity_kept"),i);
                this.immuneDamage.add(new Pair<>(ARRLib.getDamageTypeByMsgId(id,getRegistry()),kept > 0));
            }
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        NbtList list = new NbtList();
        List<String> ids = this.getImmunityId().toList();
        for (String id : ids) {
            list.add(NbtString.of(id));
        }
        nbt.put("effect_immunity_id",list);
        nbt.putIntArray("effect_immunity_kept",this.getImmunityBoolean());
    }

    private DamageType registryKeyToDamageType(RegistryKey<DamageType> damageType) {
        DynamicRegistryManager manager = entity.getWorld().getRegistryManager();
        Registry<DamageType> registry = manager.get(RegistryKeys.DAMAGE_TYPE);
        return registry.get(damageType);
    }

    private Registry<DamageType> getRegistry() {
        DynamicRegistryManager manager = entity.getWorld().getRegistryManager();
        return manager.get(RegistryKeys.DAMAGE_TYPE);
    }
}
