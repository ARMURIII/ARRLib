package arr.armuriii.arrlib.mixin.client;

import arr.armuriii.arrlib.ARRLib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GameRenderer.class, priority = 5000)
public abstract class GameRendererMixin {


    @Shadow abstract void loadPostProcessor(Identifier id);

    @Inject(method = "onCameraEntitySet",at = @At(value = "HEAD"))
    private void ARRLibTESTING$addCoolEffect(Entity entity, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            if (entity instanceof PlayerEntity player) {
                Identifier shaderId = ARRLib.MOD_HELPER.id("shaders/post/astroplane.json");
                client.execute(() -> {
                    try {
                        this.loadPostProcessor(shaderId);
                    } catch (Throwable throwable) {
                        ARRLib.MOD_HELPER.logError("Failed to load shader due to an exception!");
                    }
                    ARRLib.MOD_HELPER.logInfo("Loading shader");
                });
            }
        }
    }

}