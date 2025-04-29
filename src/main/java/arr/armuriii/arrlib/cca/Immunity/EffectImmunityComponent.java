package arr.armuriii.arrlib.cca.Immunity;

import arr.armuriii.arrlib.ARRLib;
import com.google.common.primitives.Ints;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryOps;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EffectImmunityComponent implements AutoSyncedComponent, PlayerComponent {
    private final PlayerEntity entity;
    private Set<Pair<StatusEffect,Boolean>> immuneEffect = Set.of();

    public EffectImmunityComponent(PlayerEntity entity) {
        this.entity = entity;
    }

    public List<StatusEffect> getImmunity() {
        return immuneEffect.stream().map(Pair::getLeft).toList();
    }

    private List<Integer> getImmunityId() {
        return immuneEffect.stream().map(Pair::getLeft).map(StatusEffect::getRawId).toList();
    }

    private List<Integer> getImmunityBoolean() {
        return immuneEffect.stream().map(Pair::getRight).map((aBoolean -> aBoolean ? 1 : 0)).toList();
    }

    public void addImmunity(StatusEffect statusEffect, boolean keptFromDeath) {
        immuneEffect.add(new Pair<>(statusEffect,keptFromDeath));
    }

    public void removeImmunity(StatusEffect statusEffect, boolean keptFromDeath) {
        immuneEffect.remove(new Pair<>(statusEffect,keptFromDeath));
    }

    public boolean isImmune(StatusEffect effect) {
        return (immuneEffect.contains(new Pair<>(effect,true))) || (immuneEffect.contains(new Pair<>(effect,false)));
    }

    public void forEach(Consumer<Pair<StatusEffect,Boolean>> consumer) {
        immuneEffect.forEach(consumer);
    }

    public void removeIf(Predicate<Pair<StatusEffect,Boolean>> predicate) {
        immuneEffect.removeIf(predicate);
    }

    public static EffectImmunityComponent get(LivingEntity living) {
        return ARRLib.EFFECT_IMMUNITY.get(living);
    }

    private void sync() {
        ARRLib.EFFECT_IMMUNITY.sync(this.entity);
    }

    @Override
    public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
        return true;
    }

    @Override
    public void copyForRespawn(Component original, boolean lossless, boolean keepInventory, boolean sameCharacter) {
        if (original instanceof EffectImmunityComponent component) {
                component.removeIf(pair -> !pair.getRight());
        }
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbt) {
        this.immuneEffect.clear();
        if (nbt.contains("effect_immunity_id") && nbt.contains("effect_immunity_kept")) {
            for(int i = 0; i < nbt.getIntArray("effect_immunity_id").length; i++) {
                int id = Ints.indexOf(nbt.getIntArray("effect_immunity_id"),i);
                int kept = Ints.indexOf(nbt.getIntArray("effect_immunity_kept"),i);
                this.immuneEffect.add(new Pair<>(StatusEffect.byRawId(id),kept > 0));
            }
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        nbt.putIntArray("effect_immunity_id",this.getImmunityId());
        nbt.putIntArray("effect_immunity_kept",this.getImmunityBoolean());
    }
}
