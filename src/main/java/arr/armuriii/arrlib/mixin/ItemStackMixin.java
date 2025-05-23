package arr.armuriii.arrlib.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @ModifyVariable(method = "getTooltip",at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z",ordinal = 0))
    private MutableText ARRLib$changeStyle(MutableText value) {
        ItemStack stack = (ItemStack)(Object)this;
        return value.setStyle(this.getItem().ARRLib$getStyle(stack,value.getStyle()));
    }

    @ModifyReturnValue(method = "toHoverableText", at = @At(value = "RETURN"))
    private Text ARRLib$changeStyle(Text original) {
        ItemStack stack = (ItemStack)(Object)this;
        MutableText text = original.copy();
        return text.setStyle(this.getItem().ARRLib$getStyle(stack,text.getStyle())
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent((ItemStack)(Object)this))));
    }

    @ModifyReturnValue(method = "getName", at = @At(value = "RETURN"))
    private Text ARRLib$changeNameStyle(Text original) {
        ItemStack stack = (ItemStack)(Object)this;
        MutableText text = original.copy();
        return text.setStyle(this.getItem().ARRLib$getStyle(stack,text.getStyle()));
    }
}
