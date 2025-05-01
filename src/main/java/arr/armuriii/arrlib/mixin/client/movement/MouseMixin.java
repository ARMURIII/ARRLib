package arr.armuriii.arrlib.mixin.client.movement;

import arr.armuriii.arrlib.ARRLib;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int button, int action, int mods, CallbackInfo ci) {
        if (ARRLib.LOCK_MOVEMENT.maybeGet(MinecraftClient.getInstance().player).isPresent() &&
                ARRLib.LOCK_MOVEMENT.maybeGet(MinecraftClient.getInstance().player).get().isMouseLockedInt(button)) {
            ci.cancel();
        }
    }

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (ARRLib.LOCK_MOVEMENT.maybeGet(MinecraftClient.getInstance().player).isPresent() &&
                ARRLib.LOCK_MOVEMENT.maybeGet(MinecraftClient.getInstance().player).get().mouseScroll) {
            ci.cancel();
        }
    }
}