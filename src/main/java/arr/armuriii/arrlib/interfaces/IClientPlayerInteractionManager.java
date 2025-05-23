package arr.armuriii.arrlib.interfaces;

public interface IClientPlayerInteractionManager {

    default void ARRLib$forceSyncSelectedSlot(boolean updateLastSelectedSlot) {}
    default void ARRLib$forceSyncSelectedSlot(int slot) {}
    default int ARRLib$getSelectedSlot() {return 0;}
}
