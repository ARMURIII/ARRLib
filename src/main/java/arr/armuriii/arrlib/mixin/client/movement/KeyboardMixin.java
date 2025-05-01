package arr.armuriii.arrlib.mixin.client.movement;

import arr.armuriii.arrlib.ARRLib;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (ARRLib.LOCK_MOVEMENT.maybeGet(MinecraftClient.getInstance().player).isPresent() && ARRLib.LOCK_MOVEMENT.maybeGet(MinecraftClient.getInstance().player).get().keyboard) {
            if (MinecraftClient.getInstance().player == null) ci.cancel();
            if (!(MinecraftClient.getInstance().options.commandKey.matchesKey(key,scancode) ||
                    key == 264 ||
                    key == 265 ||
                    key == 257 ||
                    key == 258 ||
                    key == 259 ||
                    key == 13 ||
                    key == 14 ||
                    key == 27)) {
                //MinecraftClient.getInstance().player.sendMessage(Text.literal(""+key));
                KeyBinding.unpressAll();
                ci.cancel();
            }
        }
    }
}