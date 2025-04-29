package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.cca.Immunity.DamageImmunityComponent;
import arr.armuriii.arrlib.cca.Immunity.EffectImmunityComponent;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;

@Mixin(SwordItem.class)
public class SwordItemMixin {

    @Inject(
            method = {"<init>"},
            at = {@At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;",ordinal = 1, shift = At.Shift.AFTER)}
    )
    public void ARRLib$addSweepingAttribute(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item.Settings settings, CallbackInfo ci, @Local ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder) {
        UUID SWEEP_RANGE_MODIFIER_ID = UUID.fromString("AF3F57D3-645C-4F38-A581-8C23A33DB5CF");
        builder.put(ARRLibEntityAttributes.SWEEPING, new EntityAttributeModifier(SWEEP_RANGE_MODIFIER_ID, "Weapon modifier", 1, EntityAttributeModifier.Operation.ADDITION));
    }

    /*@Inject(
            method = {"postHit"},
            at = {@At(value = "HEAD")}
    )
    public void ARRLib$test(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (attacker instanceof PlayerEntity player) {

            /*Optional<DamageImmunityComponent> DComponent = ARRLib.DAMAGE_IMMUNITY.maybeGet(player);
            if (DComponent.isPresent()) {
                DComponent.get().addImmunity(DamageTypes.CACTUS, true);
                DComponent.get().addImmunity(DamageTypes.LAVA, false);
            }

            Optional<EffectImmunityComponent> EComponent = ARRLib.EFFECT_IMMUNITY.maybeGet(player);
            if(EComponent.isPresent()) {
                EComponent.get().addImmunity(StatusEffects.BLINDNESS, true);
                EComponent.get().addImmunity(StatusEffects.DARKNESS, false);
            }
        }
    }*/
}
