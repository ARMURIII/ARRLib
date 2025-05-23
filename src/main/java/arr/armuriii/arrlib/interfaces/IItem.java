package arr.armuriii.arrlib.interfaces;

import arr.armuriii.arrlib.init.ARRLibTiers;
import arr.armuriii.arrlib.util.item.tier.Tier;
import arr.armuriii.arrlib.util.item.tier.TierUtil;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Style;
import net.minecraft.util.Arm;

@SuppressWarnings({"unused"})
public interface IItem {

    default void ARRLib$animateModel(ItemStack stack, PlayerEntity player, BipedEntityModel<LivingEntity> model, Arm holdingArm, boolean offHand) {
    }

    default DefaultParticleType ARRLib$getSweepAttackParticle(ItemStack stack, PlayerEntity player) {
        return ParticleTypes.SWEEP_ATTACK;
    }

    default DamageSource ARRLib$getCustomDamageSource(ItemStack stack, LivingEntity living, DamageSource original) {
        return original;
    }

    default Style ARRLib$getStyle(ItemStack stack, Style original) {
        if (ARRLib$getTier(stack) != ARRLibTiers.UNSET) {
            return TierUtil.mix(ARRLib$getTier(stack).getStyle(),original);
        }
        return original;
    }

    default Tier ARRLib$getTier(ItemStack stack) {
        return TierUtil.translate(stack.getRarity());
    }
    default void ARRLib$setTier(Tier rarity){}

    default void ARRLib$entityTick() {}

    default boolean ARRLib$isImportantItemEntity(ItemEntity entity) {return false;}
    default void ARRLib$setImportantItemEntity(boolean bool) {}

    default boolean ARRLib$isExplosionProof() {return false;}
    default void ARRLib$setExplosionProof(boolean bool) {}

}
