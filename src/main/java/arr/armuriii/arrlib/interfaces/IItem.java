package arr.armuriii.arrlib.interfaces;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Formatting;

@SuppressWarnings({"unused"})
public interface IItem {

    default void ARRLib$animateModel(ItemStack stack, PlayerEntity player, BipedEntityModel<LivingEntity> model, Arm holdingArm, boolean offHand) {
    }

    default DefaultParticleType ARRLib$getSweepAttackParticle(ItemStack stack, PlayerEntity player) {
        return ParticleTypes.SWEEP_ATTACK;
    }

    default DamageSource ARRLib$getCustomDamageSource(ItemStack stack, LivingEntity living, DamageSource original) {
        DynamicRegistryManager manager = living.getWorld().getRegistryManager();
        Registry<DamageType> registry = manager.get(RegistryKeys.DAMAGE_TYPE);
        if (living instanceof PlayerEntity player) {
            return new DamageSource(registry.entryOf(DamageTypes.PLAYER_ATTACK), player);
        }
        return new DamageSource(registry.entryOf(DamageTypes.MOB_ATTACK), living);
    }

    default Formatting ARRLib$getFormatting(ItemStack stack, Formatting original) {
        if (stack.isOf(Items.ENDER_EYE)) return Formatting.GOLD;
        return original;
    }

    default Style ARRLib$getStyle(ItemStack stack, Style original) {
        if (stack.isOf(Items.ENDER_EYE)) return original.withFormatting(Formatting.GOLD);
        return original;
    }
}
