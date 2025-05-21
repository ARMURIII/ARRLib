package arr.armuriii.arrlib;

import arr.armuriii.arrlib.cca.DiscardPlayerComponent;
import arr.armuriii.arrlib.cca.EntityEnchantmentComponent;
import arr.armuriii.arrlib.cca.Immunity.DamageImmunityComponent;
import arr.armuriii.arrlib.cca.Immunity.EffectImmunityComponent;
import arr.armuriii.arrlib.cca.LockPlayerMovementComponent;
import arr.armuriii.arrlib.init.ARRLIbCommands;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import arr.armuriii.arrlib.init.ARRLibPackets;
import arr.armuriii.arrlib.init.ARRLibRarity;
import arr.armuriii.arrlib.items.TestItem;
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
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static net.minecraft.server.command.CommandManager.literal;

public class ARRLib implements ModInitializer, EntityComponentInitializer {
    public static final ModHelper MOD_HELPER = new ModHelper("arrlib");

    public static final ComponentKey<DamageImmunityComponent> DAMAGE_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("damage_immunity"), DamageImmunityComponent.class);
    public static final ComponentKey<EffectImmunityComponent> EFFECT_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("effect_immunity"), EffectImmunityComponent.class);

    public static final ComponentKey<EntityEnchantmentComponent> ENTITY_ENCHANTMENT = ComponentRegistry.getOrCreate(MOD_HELPER.id("player_enchantment"), EntityEnchantmentComponent.class);

    public static final ComponentKey<LockPlayerMovementComponent> LOCK_MOVEMENT = ComponentRegistry.getOrCreate(MOD_HELPER.id("lock_client_movement"), LockPlayerMovementComponent.class);

    public static final ComponentKey<DiscardPlayerComponent> DISCARD_PLAYER = ComponentRegistry.getOrCreate(MOD_HELPER.id("discard_player"), DiscardPlayerComponent.class);

    @Override
    public void onInitialize() {

        ARRLibEntityAttributes.register(MOD_HELPER);
        MOD_HELPER.registerItem("test0", new TestItem(ToolMaterials.IRON,5,2, new FabricItemSettings().rarity(ARRLibRarity.CURSED)));
        MOD_HELPER.registerItem("test1", new TestItem(ToolMaterials.IRON,5,2, new FabricItemSettings().rarity(ARRLibRarity.BLESSED)));
        MOD_HELPER.registerItem("test2", new TestItem(ToolMaterials.IRON,5,2, new FabricItemSettings().rarity(ARRLibRarity.WARDEN)));
        ARRLibPackets.registerPackets();
        ARRLIbCommands.registerCommands();

    }

    public static void updateSelectedItem(PlayerEntity player) {
        if (player instanceof ClientPlayerEntity clientPlayer) {
            ClientPlayerInteractionManager manager = new ClientPlayerInteractionManager(MinecraftClient.getInstance(),clientPlayer.networkHandler);
            manager.syncSelectedSlot();
        }
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
        registry.registerForPlayers(DISCARD_PLAYER,DiscardPlayerComponent::new,RespawnCopyStrategy.ALWAYS_COPY);
    }
}

