package arr.armuriii.arrlib.interfaces;

import net.minecraft.entity.Entity;

public interface IPlayerTargeting {

    default Entity ARRLib$getTarget() {return null;}

    default int ARRLib$getDecayTicks() {
        return 0;
    }

    default void ARRLib$setTarget(Entity entity) {}

    default void ARRLib$setTarget(Entity entity,int ticks) {}

    default void ARRLib$setDecayTicks(int ticks) {}

    default void ARRLib$addDecayTicks(int ticks) {}
}
