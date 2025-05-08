package arr.armuriii.arrlib.interfaces;

import net.minecraft.client.render.entity.EntityRendererFactory;

@SuppressWarnings({"unused"})
public interface IEntityRenderer {

    default EntityRendererFactory.Context ARRLib$getContext() {
        return null;
    }
}
