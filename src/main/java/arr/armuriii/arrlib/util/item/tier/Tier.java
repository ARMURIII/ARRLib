package arr.armuriii.arrlib.util.item.tier;

import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

@SuppressWarnings("unused")
public interface Tier {

    Style getStyle();

    static Tier create(Style style) {
        return new BetterRarity(style);
    }

    static Tier create(Formatting formatting) {
        return new BetterRarity(formatting);
    }

    static Tier create(Formatting... formattings) {
        return new BetterRarity(formattings);
    }

    static Tier create(int color) {
        return new BetterRarity(color);
    }
}
