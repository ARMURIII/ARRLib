package arr.armuriii.arrlib;

import arr.armuriii.arrlib.cca.Immunity.DamageImmunityComponent;
import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import arr.armuriii.arrlib.util.ModHelper;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

public class ARRLib implements ModInitializer, EntityComponentInitializer {
    public static final ModHelper MOD_HELPER = new ModHelper("arrlib");

    public static final ComponentKey<DamageImmunityComponent> DAMAGE_IMMUNITY = ComponentRegistry.getOrCreate(MOD_HELPER.id("damage_immunity"), DamageImmunityComponent.class);

    @Override
    public void onInitialize() {
        ARRLibEntityAttributes.register(MOD_HELPER);
    }

    public static Arm getPreferredArm(LivingEntity entity) {
        Arm arm = entity.getMainArm();
        return entity.preferredHand == Hand.MAIN_HAND ? arm : arm.getOpposite();
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(LivingEntity.class, DAMAGE_IMMUNITY).end(DamageImmunityComponent::new);
    }
}
