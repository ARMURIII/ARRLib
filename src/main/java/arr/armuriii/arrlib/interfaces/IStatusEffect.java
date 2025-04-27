package arr.armuriii.arrlib.interfaces;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

public interface IStatusEffect {

    default Identifier ARRLib$getBackgroundTexture(StatusEffectInstance instance) {
        return null;
    }

    default Identifier ARRLib$getHeartTexture(StatusEffectInstance instance, InGameHud.HeartType type, boolean halfHeart, boolean blinking) {
        return null;
    }
}
