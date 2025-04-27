package arr.armuriii.arrlib.mixin.interfaces;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.interfaces.IStatusEffect;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements IStatusEffect {

    @Override
    public Identifier ARRLib$getBackgroundTexture(StatusEffectInstance instance) {
        /*StatusEffect that = (StatusEffect)(Object) this;
        if (that.getCategory() == StatusEffectCategory.BENEFICIAL) return Identifier.of(ARRLib.MOD_HELPER.getModID(),
                "/textures/gui/container/mob_effect/beneficial.png");
        if (that.getCategory() == StatusEffectCategory.HARMFUL) return Identifier.of(ARRLib.MOD_HELPER.getModID(),
                "/textures/gui/container/mob_effect/harmful.png");*/
        return null;
    }

    @Override
    public Identifier ARRLib$getHeartTexture(StatusEffectInstance instance, InGameHud.HeartType type, boolean halfHeart, boolean blinking) {
        /*StatusEffect that = (StatusEffect)(Object) this;
        if (blinking) return null;
        if (that.getCategory() == StatusEffectCategory.BENEFICIAL) return Identifier.of(ARRLib.MOD_HELPER.getModID(),
                "/textures/gui/heart/beneficial.png");
        if (that.getCategory() == StatusEffectCategory.HARMFUL) return Identifier.of(ARRLib.MOD_HELPER.getModID(),
                "/textures/gui/heart/harmful.png");*/
        return null;
    }
}
