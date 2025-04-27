package arr.armuriii.arrlib.cca.Immunity;

import arr.armuriii.arrlib.ARRLib;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.CopyableComponent;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings({"unused"})
public class DamageImmunityComponent implements AutoSyncedComponent, PlayerComponent {
    private final LivingEntity entity;
    private List<Pair<DamageType,Boolean>> immuneDamage = List.of();

    public DamageImmunityComponent(LivingEntity entity) {
        this.entity = entity;
    }

    public void addImmunity(DamageType damageType, boolean keptFromDeath) {
        immuneDamage.add(new Pair<>(damageType,keptFromDeath));
    }

    public void removeImmunity(DamageType damageType, boolean keptFromDeath) {
        immuneDamage.remove(new Pair<>(damageType,keptFromDeath));
    }

    public void forEach(Consumer<Pair<DamageType,Boolean>> consumer) {
        immuneDamage.forEach(consumer);
    }

    public void removeIf(Predicate<Pair<DamageType,Boolean>> predicate) {
        immuneDamage.removeIf(predicate);
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
    public void readFromNbt(@NotNull NbtCompound nbtCompound) {

    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbtCompound) {

    }
}
