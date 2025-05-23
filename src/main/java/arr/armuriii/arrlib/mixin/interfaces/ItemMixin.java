package arr.armuriii.arrlib.mixin.interfaces;

import arr.armuriii.arrlib.interfaces.IItem;
import arr.armuriii.arrlib.util.item.ARRLibItemInternals;
import arr.armuriii.arrlib.init.ARRLibTiers;
import arr.armuriii.arrlib.util.item.tier.Tier;
import arr.armuriii.arrlib.util.item.tier.TierUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin implements IItem {
    @Unique
    private @Nullable Tier tier = ARRLibTiers.UNSET;
    @Unique
    private Boolean importantItem = false;
    @Unique
    private Boolean explosionProof = false;

    @Inject(
            method = {"<init>"},
            at = {@At("RETURN")}
    )
    private void onConstruct(Item.Settings settings, CallbackInfo info) {
        ARRLibItemInternals.onBuild(settings, (Item)(Object)this);
    }

    @ModifyReturnValue(method = "getRarity", at = @At("RETURN"))
    private net.minecraft.util.Rarity changeRarity(net.minecraft.util.Rarity original) {
        if (tier != ARRLibTiers.UNSET)
            if (tier != null && TierUtil.translate(tier) != null)
                return TierUtil.translate(tier);
        return original;
    }

    @Override
    public Tier ARRLib$getTier(ItemStack stack) {
        return tier;
    }


    @Override
    public void ARRLib$setTier(Tier tier) {
        this.tier = tier;
    }

    @Override
    public Style ARRLib$getStyle(ItemStack stack, Style original) {
        if (tier != ARRLibTiers.UNSET)
            if (tier != null) return tier.getStyle();
        return original;
    }

    @Override
    public void ARRLib$setImportantItemEntity(boolean bool) {
        this.importantItem = bool;
    }

    @Override
    public boolean ARRLib$isImportantItemEntity(ItemEntity entity) {
        return this.importantItem;
    }

    @Override
    public boolean ARRLib$isExplosionProof() {
        return this.explosionProof;
    }

    @Override
    public void ARRLib$setExplosionProof(boolean bool) {
        this.explosionProof = bool;
    }
}
