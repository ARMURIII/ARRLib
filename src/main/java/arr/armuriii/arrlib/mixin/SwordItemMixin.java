package arr.armuriii.arrlib.mixin;

import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(SwordItem.class)
public class SwordItemMixin {

    @Inject(
            method = {"<init>"},
            at = {@At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;",ordinal = 1, shift = At.Shift.AFTER)}
    )
    public void ARRLib$addSweepingAttribute(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item.Settings settings, CallbackInfo ci, @Local ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder) {
        UUID SWEEP_RANGE_MODIFIER_ID = UUID.fromString("AF3F57D3-645C-4F38-A581-8C23A33DB5CF");
        builder.put(ARRLibEntityAttributes.SWEEPING, new EntityAttributeModifier(SWEEP_RANGE_MODIFIER_ID, "Weapon modifier", 1, EntityAttributeModifier.Operation.ADDITION));
    }
}
