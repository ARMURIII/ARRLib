package arr.armuriii.arrlib.init;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.DiscardPlayerComponent;
import arr.armuriii.arrlib.cca.EntityEnchantmentComponent;
import arr.armuriii.arrlib.cca.Immunity.DamageImmunityComponent;
import arr.armuriii.arrlib.cca.Immunity.EffectImmunityComponent;
import arr.armuriii.arrlib.cca.LockPlayerMovementComponent;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collection;
import java.util.Optional;

import static net.minecraft.server.command.CommandManager.literal;

public class ARRLIbCommands {

    public static final String PLAYER_ENCHANTMENT_COMMAND_KEY = "enchantplayer";
    public static final String LOCK_CONTROLS_COMMAND_KEY = "lock";
    public static final SuggestionProvider<ServerCommandSource> LOCK_MOUSE_SUGGESTIONS_PROVIDER;
    public static final String IMMUNITY_COMMAND_KEY = "immunity";

    public static void registerCommands() {
        registerDiscard();
        registerLockControl();
        registerPlayerEnchant();
        registerImmunities();
    }

    private static void registerDiscard() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("discard")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .executes(context -> {
                            final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                            targets.forEach(player -> {
                                Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(player);
                                DPComponent.ifPresent(component -> component.setDiscarded(true));
                            });
                            return 1;
                        })
                        .then((CommandManager.argument("boolean", BoolArgumentType.bool()))
                                .executes(context -> {
                                    final Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(context, "targets");
                                    final boolean bool = BoolArgumentType.getBool(context,"boolean");
                                    targets.forEach(player -> {
                                        Optional<DiscardPlayerComponent> DPComponent = ARRLib.DISCARD_PLAYER.maybeGet(player);
                                        DPComponent.ifPresent(component -> component.setDiscarded(bool));
                                    });
                                    return 1;
                                })))));
    }

    private static void registerLockControl() {
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
    }

    private static void registerPlayerEnchant() {
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
    }

    private static void registerImmunities() {
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

    static {
        LOCK_MOUSE_SUGGESTIONS_PROVIDER =  (context, builder) -> CommandSource.suggestMatching(new String[]{"left","right","middle","scroll","camera"},builder);
    }
}
