package arr.armuriii.arrlib.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
@Mixin(value = InGameHud.class,priority = 2000)
public abstract class HudMixin {

    @Inject(method = "drawHeart", at = @At(value = "HEAD"), cancellable = true)
    private void ARRLib$drawCustomHeart(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        if (MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player) {
            List<StatusEffectInstance> statusEffectInstances = new java.util.ArrayList<>(player.getStatusEffects().stream().toList());
            statusEffectInstances.removeIf(instance -> instance.getEffectType().ARRLib$getHeartTexture(instance, type, halfHeart, blinking) == null);
            if (!statusEffectInstances.isEmpty()) {
                StatusEffectInstance instance = statusEffectInstances.get(0);
                Identifier id = instance.getEffectType().ARRLib$getHeartTexture(instance, type, halfHeart, blinking);
                context.drawTexture(id, x, y,
                        ((halfHeart?1:0)+(blinking?2:0))*9F,
                        ((player.getWorld().getLevelProperties().isHardcore()?1:0)+(type == InGameHud.HeartType.CONTAINER?2:0))*9F,
                        9, 9,36,36);
                ci.cancel();
            }
        }
    }

    @ModifyArg(method = "renderStatusEffectOverlay", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    protected Identifier ARRLib$renderCustomStatusEffectOverlay(Identifier original, @Local StatusEffectInstance statusEffectInstance) {
        Identifier texture = statusEffectInstance.getEffectType().ARRLib$getBackgroundTexture(statusEffectInstance);
        return texture == null ? original : texture;
    }
}
