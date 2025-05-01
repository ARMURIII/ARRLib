package arr.armuriii.arrlib.cca.Immunity;

import arr.armuriii.arrlib.ARRLib;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings({"unused"})
public class EffectImmunityComponent extends ImmunityComponent<StatusEffect> {

    public EffectImmunityComponent(LivingEntity entity) {
        super(entity,ARRLib.EFFECT_IMMUNITY,RegistryKeys.STATUS_EFFECT,"effect_immunity");
    }
}