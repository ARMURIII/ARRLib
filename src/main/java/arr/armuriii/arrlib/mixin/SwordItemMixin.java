package arr.armuriii.arrlib.mixin;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.UUID;

import static arr.armuriii.arrlib.init.ARRLibEntityAttributes.SWEEPING;

@Mixin(SwordItem.class)
public class SwordItemMixin {

    @ModifyVariable(
            method = {"<init>"},
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;",
                    remap = false
            )
    )
    private ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> ARRLib$addSweeping(ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder, ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Item.Settings settings) {
        UUID SWEEP_RANGE_MODIFIER_ID = UUID.fromString("AF3F57D3-645C-4F38-A581-8C23A33DB5CF");
        builder.put(SWEEPING, new EntityAttributeModifier(SWEEP_RANGE_MODIFIER_ID, "Weapon modifier", 1, EntityAttributeModifier.Operation.ADDITION));
        return builder;
    }
}
