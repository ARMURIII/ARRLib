package arr.armuriii.arrlib;

import arr.armuriii.arrlib.cca.DiscardPlayerComponent;
import arr.armuriii.arrlib.cca.EntityEnchantmentComponent;
import arr.armuriii.arrlib.cca.Immunity.DamageImmunityComponent;
import arr.armuriii.arrlib.cca.Immunity.EffectImmunityComponent;
import arr.armuriii.arrlib.cca.LockPlayerMovementComponent;
import arr.armuriii.arrlib.init.ARRLIbCommands;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import arr.armuriii.arrlib.init.ARRLibPackets;
import arr.armuriii.arrlib.items.TestItem;
import arr.armuriii.arrlib.util.BuiltinColors;
import arr.armuriii.arrlib.util.ModHelper;
import arr.armuriii.arrlib.util.item.ARRLibItemSettings;
import arr.armuriii.arrlib.init.ARRLibTiers;
import arr.armuriii.arrlib.util.item.tier.Tier;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ARRLib implements ModInitializer, EntityComponentInitializer {
    public static final ModHelper MOD_HELPER = new ModHelper("arrlib");

    //Tier Examples
    public static final Tier COOL_TIER = Tier.create(Formatting.GOLD);
    public static final Tier SUPER_RARITY = Tier.create(Formatting.OBFUSCATED,Formatting.BLUE);
    public static final Tier HELL_TIER = Tier.create(BuiltinColors.MAUVE);
    public static final Tier OMINOUS_TIER = Tier.create(325871);
    public static final Tier TEST = Tier.create(Style.EMPTY.withColor(BuiltinColors.PRUSSIAN).withFont(MOD_HELPER.id("runes")));

    public static final ComponentKey<DamageImmunityComponent> DAMAGE_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("damage_immunity"), DamageImmunityComponent.class);
    public static final ComponentKey<EffectImmunityComponent> EFFECT_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("effect_immunity"), EffectImmunityComponent.class);

    public static final ComponentKey<EntityEnchantmentComponent> ENTITY_ENCHANTMENT = ComponentRegistry.getOrCreate(MOD_HELPER.id("player_enchantment"), EntityEnchantmentComponent.class);

    public static final ComponentKey<LockPlayerMovementComponent> LOCK_MOVEMENT = ComponentRegistry.getOrCreate(MOD_HELPER.id("lock_client_movement"), LockPlayerMovementComponent.class);

    public static final ComponentKey<DiscardPlayerComponent> DISCARD_PLAYER = ComponentRegistry.getOrCreate(MOD_HELPER.id("discard_player"), DiscardPlayerComponent.class);

    @Override
    public void onInitialize() {

        ARRLibEntityAttributes.register(MOD_HELPER);
        MOD_HELPER.registerItem("test0", new TestItem(ToolMaterials.IRON, 5, 2, new ARRLibItemSettings().tier(ARRLibTiers.LEGENDARY)));
        MOD_HELPER.registerItem("test1", new TestItem(ToolMaterials.IRON, 5, 2, new ARRLibItemSettings().tier(ARRLibTiers.CELESTE)));
        MOD_HELPER.registerItem("test2", new TestItem(ToolMaterials.IRON, 5, 2, new ARRLibItemSettings().tier(OMINOUS_TIER)));
        MOD_HELPER.registerItem("test3", new TestItem(ToolMaterials.IRON, 5, 2, new ARRLibItemSettings().tier(Tier.create(Formatting.ITALIC))));
        MOD_HELPER.registerItem("test4", new TestItem(ToolMaterials.IRON, 5, 2, new ARRLibItemSettings().tier(TEST).explosionproof()));
        ARRLibPackets.registerPackets();
        ARRLIbCommands.registerCommands();
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

