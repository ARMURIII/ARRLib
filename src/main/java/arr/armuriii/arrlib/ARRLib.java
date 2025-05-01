package arr.armuriii.arrlib;

import arr.armuriii.arrlib.cca.EntityEnchantmentComponent;
import arr.armuriii.arrlib.cca.Immunity.DamageImmunityComponent;
import arr.armuriii.arrlib.cca.Immunity.EffectImmunityComponent;
import arr.armuriii.arrlib.cca.LockPlayerMovementComponent;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import arr.armuriii.arrlib.util.ModHelper;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static net.minecraft.server.command.CommandManager.literal;

public class ARRLib implements ModInitializer, EntityComponentInitializer {
    public static final ModHelper MOD_HELPER = new ModHelper("arrlib");
    public static final String PLAYER_ENCHANTMENT_COMMAND_KEY = "enchantplayer";
    public static final String LOCK_CONTROLS_COMMAND_KEY = "lock";
    public static final SuggestionProvider<ServerCommandSource> LOCK_MOUSE_SUGGESTIONS_PROVIDER;
    public static final String IMMUNITY_COMMAND_KEY = "immunity";

    public static final ComponentKey<DamageImmunityComponent> DAMAGE_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("damage_immunity"), DamageImmunityComponent.class);
    public static final ComponentKey<EffectImmunityComponent> EFFECT_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("effect_immunity"), EffectImmunityComponent.class);

    public static final ComponentKey<EntityEnchantmentComponent> ENTITY_ENCHANTMENT = ComponentRegistry.getOrCreate(MOD_HELPER.id("player_enchantment"), EntityEnchantmentComponent.class);

    public static final ComponentKey<LockPlayerMovementComponent> LOCK_MOVEMENT = ComponentRegistry.getOrCreate(MOD_HELPER.id("lock_client_movement"), LockPlayerMovementComponent.class);

    @Override
    public void onInitialize() {
        ARRLibEntityAttributes.register(MOD_HELPER);


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(LOCK_CONTROLS_COMMAND_KEY)
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(literal("keyboard")
                .then((CommandManager.argument("boolean", BoolArgumentType.bool()))
                        .executes(context -> {
                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                            final boolean bool = BoolArgumentType.getBool(context,"boolean");
                            targets.forEach(player -> {
                                Optional<LockPlayerMovementComponent> LPMComponent = ARRLib.LOCK_MOVEMENT.maybeGet(player);
                                LPMComponent.ifPresent(component -> component.lockKeyboard(bool));
                            });
                            return 1;
                        })))
                        .then(literal("mouse")
                                .then(CommandManager.argument("type", StringArgumentType.word()).suggests(LOCK_MOUSE_SUGGESTIONS_PROVIDER)
                                        .then(CommandManager.argument("boolean", BoolArgumentType.bool())
                                        .executes(context -> {
                                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                                            final String i = StringArgumentType.getString(context,"type");
                                            final boolean bool = BoolArgumentType.getBool(context,"boolean");
                                            targets.forEach(player -> {
                                                Optional<LockPlayerMovementComponent> LPMComponent = ARRLib.LOCK_MOVEMENT.maybeGet(player);
                                                if (LPMComponent.isEmpty()) return;
                                                LockPlayerMovementComponent component = LPMComponent.get();
                                                switch (i) {
                                                    case "left" -> component.lockMouseLeft(bool);
                                                    case "right" -> component.lockMouseRight(bool);
                                                    case "middle" -> component.lockMouseMiddle(bool);
                                                    case "scroll" -> component.lockMouseScroll(bool);
                                                    case "camera" -> component.lockCamera(bool);
                                                    default -> throw new IllegalStateException("Unexpected value: " + i + ",only left,right,middle,scroll and camera are allowed");
                                                }
                                            });
                                            return 1;
                                        })))))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(PLAYER_ENCHANTMENT_COMMAND_KEY)
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                .then((CommandManager.argument("enchantment", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.ENCHANTMENT))
                        .executes(context -> {
                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                            final RegistryEntry<Enchantment> enchantment = RegistryEntryArgumentType.getEnchantment(context, "enchantment");
                            targets.forEach(player -> {
                                Optional<EntityEnchantmentComponent> PEComponent = ARRLib.ENTITY_ENCHANTMENT.maybeGet(player);
                                PEComponent.ifPresent(component -> component.addEnchantmentEntry(enchantment));
                            });
                            return 1;
                        }))))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(IMMUNITY_COMMAND_KEY)
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.players()).then(literal("add")
                .then(literal("DamageType")
                .then((CommandManager.argument("damage", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.DAMAGE_TYPE))
                        .executes(context -> {
                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                            final RegistryEntry.Reference<DamageType> damageType = RegistryEntryArgumentType.getRegistryEntry(context, "damage",RegistryKeys.DAMAGE_TYPE);
                            targets.forEach(player -> {
                                Optional<DamageImmunityComponent> DComponent = ARRLib.DAMAGE_IMMUNITY.maybeGet(player);
                                DComponent.ifPresent(component -> component.addImmunity(damageType.value()));
                            });
                            return 1;
                        }))))
                        .then(literal("StatusEffect")
                                .then((CommandManager.argument("effect", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.STATUS_EFFECT))
                                        .executes(context -> {
                                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                                            final RegistryEntry.Reference<StatusEffect> effect = RegistryEntryArgumentType.getStatusEffect(context, "effect");
                                            targets.forEach(player -> {
                                                Optional<EffectImmunityComponent> EComponent = ARRLib.EFFECT_IMMUNITY.maybeGet(player);
                                                EComponent.ifPresent(component -> component.addImmunity(effect.value()));
                                            });
                                            return 1;
                                        }))))))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(IMMUNITY_COMMAND_KEY)
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.players()).then(literal("remove")
                .then(literal("DamageType")
                .then((CommandManager.argument("damage", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.DAMAGE_TYPE))
                        .executes(context -> {
                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                            final RegistryEntry.Reference<DamageType> damageType = RegistryEntryArgumentType.getRegistryEntry(context, "damage",RegistryKeys.DAMAGE_TYPE);
                            targets.forEach(player -> {
                                Optional<DamageImmunityComponent> DComponent = ARRLib.DAMAGE_IMMUNITY.maybeGet(player);
                                DComponent.ifPresent(component -> component.removeImmunity(damageType.value()));
                            });
                            return 1;
                        }))))
                        .then((CommandManager.argument("effect", RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.STATUS_EFFECT))
                                        .executes(context -> {
                                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                                            final RegistryEntry.Reference<StatusEffect> effect = RegistryEntryArgumentType.getStatusEffect(context, "effect");
                                            targets.forEach(player -> {
                                                Optional<EffectImmunityComponent> EComponent = ARRLib.EFFECT_IMMUNITY.maybeGet(player);
                                                EComponent.ifPresent(component -> component.removeImmunity(effect.value()));
                                            });
                                            return 1;
                                        })))))));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(IMMUNITY_COMMAND_KEY)
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.players()).then(literal("clear")
                        .then(literal("DamageType")
                        .executes(context -> {
                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                            targets.forEach(player -> {
                                Optional<DamageImmunityComponent> DComponent = ARRLib.DAMAGE_IMMUNITY.maybeGet(player);
                                DComponent.ifPresent(DamageImmunityComponent::clear);
                            });
                            return 1;}))
                        .then(literal("StatusEffect")
                                .executes(context -> {
                                    final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                                    targets.forEach(player -> {
                                        Optional<EffectImmunityComponent> EComponent = ARRLib.EFFECT_IMMUNITY.maybeGet(player);
                                        EComponent.ifPresent(EffectImmunityComponent::clear);
                                    });
                                    return 1;
                                }))))));
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(LivingEntity.class, DAMAGE_IMMUNITY)
                .impl(DamageImmunityComponent.class).respawnStrategy(RespawnCopyStrategy.LOSSLESS_ONLY).end(DamageImmunityComponent::new);
        registry.beginRegistration(LivingEntity.class, EFFECT_IMMUNITY)
                .impl(EffectImmunityComponent.class).respawnStrategy(RespawnCopyStrategy.LOSSLESS_ONLY).end(EffectImmunityComponent::new);

        registry.beginRegistration(LivingEntity.class, ENTITY_ENCHANTMENT)
                .impl(EntityEnchantmentComponent.class).respawnStrategy(RespawnCopyStrategy.INVENTORY).end(EntityEnchantmentComponent::new);

        registry.registerForPlayers(LOCK_MOVEMENT,LockPlayerMovementComponent::new,RespawnCopyStrategy.NEVER_COPY);
    }

    static {
        LOCK_MOUSE_SUGGESTIONS_PROVIDER =  (context, builder) -> CommandSource.suggestMatching(new String[]{"left","right","middle","scroll","camera"},builder);
    }
}

