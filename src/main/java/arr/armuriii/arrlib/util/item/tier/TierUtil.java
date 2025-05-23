package arr.armuriii.arrlib.util.item.tier;

import arr.armuriii.arrlib.init.ARRLibTiers;
import net.minecraft.text.Style;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class TierUtil {
    public static @Nullable Tier translate(Rarity rarity) {
        for(Tier aRarity : ARRLibTiers.values()) {
            if (Objects.equals(aRarity.toString(), rarity.toString())) {
                return aRarity;
            }
        }
        return null;
    }

    public static @Nullable Rarity translate(Tier aRarity) {
        for(Rarity rarity : Rarity.values()) {
            if (Objects.equals(rarity.toString(), aRarity.toString())) {
                return rarity;
            }
        }
        return null;
    }

    public static Style mix(Style base,Style added) {
        return base.withHoverEvent(added.getHoverEvent()).withInsertion(added.getInsertion()).withClickEvent(added.getClickEvent());
    }
}
