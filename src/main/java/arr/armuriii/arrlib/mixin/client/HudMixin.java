package arr.armuriii.arrlib.mixin.client;

import com.google.common.base.Suppliers;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static arr.armuriii.arrlib.ARRLib.MOD_HELPER;

@Environment(EnvType.CLIENT)
@Mixin(value = InGameHud.class,priority = 2000)
public abstract class HudMixin {

    private static final Identifier DOT = MOD_HELPER.id("textures/gui/dot.png");
    private static final Identifier TARGET = MOD_HELPER.id("textures/gui/target.png");

    @Shadow @Final private MinecraftClient client;

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

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

    @ModifyArgs(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",ordinal = 0))
    private void ARRLib$modifyCrossHair(Args args, DrawContext context) {
        PlayerEntity player = this.client.player;
        if (ARRLib$hasTarget() && player != null) {
            context.drawTexture(DOT, (this.scaledWidth - 15) / 2, (this.scaledHeight - 15) / 2, 0, 0, 15, 15);

            float xOffset = ARRLib$getAngleOffset().getRight();
            float yOffset = ARRLib$getAngleOffset().getLeft();
            args.set(0,TARGET);
            args.set(1,Math.round(ARRLib$convertDegreeToScreePos(-xOffset/2,this.scaledWidth))-7);
            args.set(2,Math.round(ARRLib$convertDegreeToScreePos(-yOffset,this.scaledHeight)));
        }
    }

    @Unique
    private boolean ARRLib$hasTarget() {
        PlayerEntity player = this.client.player;
        if (player == null) {
            return false;
        }
        ClientPlayerInteractionManager interactionManager = this.client.interactionManager;
        if (interactionManager == null || interactionManager.getCurrentGameMode() == GameMode.SPECTATOR) {
            return false;
        }
        Entity target = player.ARRLib$getTarget();
        if (target == null || !target.isAlive() || target.isRemoved()) {
            return false;
        }
        if (this.client.world == null) {
            return false;
        }
        return target != player;
    }

    @Unique
    private Pair<Float, Float> ARRLib$getAngleOffset() {
        PlayerEntity player = this.client.player;
        if (player != null && player.ARRLib$getTarget() != null) {
            Vec3d vec3d = player.getEyePos();
            Vec3d target = player.ARRLib$getTarget().getEyePos();
            vec3d.add(0,-1,0);
            double d = target.x - vec3d.x;
            double e = target.y - vec3d.y;
            double f = target.z - vec3d.z;
            double g = Math.sqrt(d * d + f * f);
            float pitch =(MathHelper.wrapDegrees((float)(-(MathHelper.atan2(e, g) * 57.2957763671875))));
            float yaw = (MathHelper.wrapDegrees((float)(MathHelper.atan2(f, d) * 57.2957763671875) - 90.0F));
            float pitchOffset = MathHelper.wrapDegrees(player.getPitch()) - pitch;
            float yawOffset = MathHelper.wrapDegrees(player.getYaw()) - yaw;
            return new Pair<>(pitchOffset,yawOffset);
        }
        return new Pair<>(0f,0f);
    }

    @Unique
    private float ARRLib$convertDegreeToScreePos(float degree, int border) {
        float halfFov = client.options.getFov().getValue() / 2.0f;
        float normalized = degree / halfFov;
        return (normalized + 1.0f) / 2.0f * border;
    }
}
