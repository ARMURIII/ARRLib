package arr.armuriii.arrlib.interfaces;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Arm;

public interface IItem {

    default void ARRLib$animateModel(ItemStack stack, PlayerEntity player, BipedEntityModel<LivingEntity> model, Arm holdingArm, boolean offHand) {
    }

    default DefaultParticleType ARRLib$getSweepAttackParticle(ItemStack stack, PlayerEntity player) {
        return ParticleTypes.SWEEP_ATTACK;
    }
}
