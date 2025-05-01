package arr.armuriii.arrlib.cca;

import arr.armuriii.arrlib.ARRLib;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public class LockPlayerMovementComponent implements AutoSyncedComponent, PlayerComponent {
    private final PlayerEntity entity;
    public boolean keyboard = false;
    public boolean mouseLeft = false;
    public boolean mouseRight = false;
    public boolean mouseMiddle = false;
    public boolean mouseScroll = false;
    public boolean camera = false;
    private final String KEYBOARD_KEY = "lock_keyboard";
    private final String MOUSE_KEY = "lock_mouse_button";
    private final String CAMERA_KEY = "lock_camera";

    public LockPlayerMovementComponent(PlayerEntity entity) {
        this.entity = entity;
    }

    private void sync() {
        ARRLib.LOCK_MOVEMENT.sync(this.entity);
    }

    public void lockMouseButtons(boolean left,boolean right,boolean middle, boolean scroll) {
        this.mouseLeft = left;
        this.mouseRight = right;
        this.mouseMiddle = middle;
        this.mouseScroll = scroll;
        this.sync();
    }

    public void lockMouseLeft(boolean left) {
        this.mouseLeft = left;
        this.sync();
    }

    public void lockMouseRight(boolean right) {
        this.mouseRight = right;
        this.sync();
    }

    public void lockMouseMiddle(boolean bool) {
        this.mouseMiddle = bool;
        this.sync();
    }

    public void lockMouseScroll(boolean bool) {
        this.mouseScroll = bool;
        this.sync();
    }

    public void lockCamera(boolean bool) {
        this.camera = bool;
        if (!bool) onUnlockCamera();
        this.sync();
    }

    private void onUnlockCamera() {
    }

    public void lockKeyboard(boolean keyboard) {
        this.keyboard = keyboard;
        this.sync();
    }

    public boolean isMouseLockedInt(int i) {
        if (i == 0) return mouseLeft;
        if (i == 1) return mouseRight;
        if (i == 2) return mouseMiddle;
        return false;
    }

    public boolean getKeyboard() {
        return keyboard;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbt) {
        this.keyboard = false;
        this.camera = false;
        this.mouseLeft = false;
        this.mouseRight = false;
        this.mouseMiddle = false;
        this.mouseScroll = false;
        if (nbt.contains(KEYBOARD_KEY)) {
            this.keyboard = nbt.getBoolean(KEYBOARD_KEY);
        }
        if (nbt.contains(CAMERA_KEY)) {
            this.camera = nbt.getBoolean(CAMERA_KEY);
        }

        if (nbt.contains(MOUSE_KEY + "_l")) {
            this.mouseLeft = nbt.getBoolean(MOUSE_KEY + "_l");
        }
        if (nbt.contains(MOUSE_KEY + "_r")) {
            this.mouseRight = nbt.getBoolean(MOUSE_KEY + "_r");
        }
        if (nbt.contains(MOUSE_KEY + "_m")) {
            this.mouseMiddle = nbt.getBoolean(MOUSE_KEY + "_m");
        }
        if (nbt.contains(MOUSE_KEY + "_s")) {
            this.mouseScroll = nbt.getBoolean(MOUSE_KEY + "_s");
        }
        this.sync();
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbt) {
        nbt.putBoolean(KEYBOARD_KEY,keyboard);
        nbt.putBoolean(CAMERA_KEY,camera);

        nbt.putBoolean(MOUSE_KEY + "_l",this.mouseLeft);
        nbt.putBoolean(MOUSE_KEY + "_r",this.mouseRight);
        nbt.putBoolean(MOUSE_KEY + "_m",this.mouseMiddle);
        nbt.putBoolean(MOUSE_KEY + "_s",this.mouseScroll);
    }
}
