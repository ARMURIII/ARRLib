package arr.armuriii.arrlib;

import arr.armuriii.arrlib.cca.Immunity.DamageImmunityComponent;
import arr.armuriii.arrlib.cca.Immunity.EffectImmunityComponent;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import arr.armuriii.arrlib.util.ModHelper;
import com.mojang.serialization.Codec;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class ARRLib implements ModInitializer, EntityComponentInitializer {
    public static final ModHelper MOD_HELPER = new ModHelper("arrlib");

    public static final Codec<RegistryEntryList<StatusEffect>> STATUS_EFFECT_CODEC = RegistryCodecs.entryList(RegistryKeys.STATUS_EFFECT);
    public static final Codec<RegistryEntryList<DamageType>> DAMAGE_TYPE_CODEC = RegistryCodecs.entryList(RegistryKeys.DAMAGE_TYPE);

    public static final ComponentKey<DamageImmunityComponent> DAMAGE_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("damage_immunity"), DamageImmunityComponent.class);
    public static final ComponentKey<EffectImmunityComponent> EFFECT_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("effect_immunity"), EffectImmunityComponent.class);

    @Override
    public void onInitialize() {
        ARRLibEntityAttributes.register(MOD_HELPER);
    }

    public static Arm getPreferredArm(LivingEntity entity) {
        Arm arm = entity.getMainArm();
        return entity.preferredHand == Hand.MAIN_HAND ? arm : arm.getOpposite();
    }

    public static DamageType getDamageTypeByMsgId(String id,Registry<DamageType> registry) {
        for(int i = 0; i < registry.size();i++)  {
            if (registry.get(i) == null) return null;
            if (Objects.equals(Objects.requireNonNull(registry.get(i)).msgId(), id)) {
                return registry.get(i);
            }
        }
        return null;
    }
    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DAMAGE_IMMUNITY,DamageImmunityComponent::new);
        registry.registerForPlayers(EFFECT_IMMUNITY,EffectImmunityComponent::new);
    }
}
