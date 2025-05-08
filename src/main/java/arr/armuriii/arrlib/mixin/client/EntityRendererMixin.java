package arr.armuriii.arrlib.mixin.client;

import arr.armuriii.arrlib.interfaces.IEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin implements IEntityRenderer {

    @Unique
    private EntityRendererFactory.Context ctx;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void mischief$getCTX(EntityRendererFactory.Context ctx, CallbackInfo ci) {
        this.ctx = ctx;
    }

    @Override
    public EntityRendererFactory.Context ARRLib$getContext() {
        return ctx;
    }
}