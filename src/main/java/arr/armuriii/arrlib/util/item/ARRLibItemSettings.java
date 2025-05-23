package arr.armuriii.arrlib.util.item;

import arr.armuriii.arrlib.init.ARRLibTiers;
import arr.armuriii.arrlib.util.item.tier.Tier;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.resource.featuretoggle.FeatureFlag;

@SuppressWarnings("unused")
public class ARRLibItemSettings extends FabricItemSettings {

    public ARRLibItemSettings() {
        super();
        this.tier(ARRLibTiers.UNSET);
        this.explosionproofSetup();
        this.importantSetup();
    }

    public ARRLibItemSettings equipmentSlot(EquipmentSlotProvider equipmentSlotProvider) {
        super.equipmentSlot(equipmentSlotProvider);
        return this;
    }

    public ARRLibItemSettings customDamage(CustomDamageHandler handler) {
        super.customDamage(handler);
        return this;
    }

    public ARRLibItemSettings tier(Tier tier) {
        ARRLibItemInternals.computeExtraData(this).tier(tier);
        return this;
    }

    public ARRLibItemSettings important() {
        ARRLibItemInternals.computeExtraData(this).importantItem(true);
        return this;
    }

    private ARRLibItemSettings importantSetup() {
        ARRLibItemInternals.computeExtraData(this).importantItem(false);
        return this;
    }

    public ARRLibItemSettings food(FoodComponent foodComponent) {
        super.food(foodComponent);
        return this;
    }

    public ARRLibItemSettings maxCount(int maxCount) {
        super.maxCount(maxCount);
        return this;
    }

    public ARRLibItemSettings maxDamageIfAbsent(int maxDamage) {
        super.maxDamageIfAbsent(maxDamage);
        return this;
    }

    public ARRLibItemSettings maxDamage(int maxDamage) {
        super.maxDamage(maxDamage);
        return this;
    }

    public ARRLibItemSettings recipeRemainder(Item recipeRemainder) {
        super.recipeRemainder(recipeRemainder);
        return this;
    }

    public ARRLibItemSettings rarity(net.minecraft.util.Rarity rarity) {
        super.rarity(rarity);
        return this;
    }

    public ARRLibItemSettings fireproof() {
        super.fireproof();
        return this;
    }

    public ARRLibItemSettings explosionproof() {
        ARRLibItemInternals.computeExtraData(this).explosionProof(true);
        return this;
    }

    private ARRLibItemSettings explosionproofSetup() {
        ARRLibItemInternals.computeExtraData(this).explosionProof(false);
        return this;
    }

    public ARRLibItemSettings requires(FeatureFlag... features) {
        super.requires(features);
        return this;
    }
}
