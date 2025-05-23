package arr.armuriii.arrlib.util.item.tier;

import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

@SuppressWarnings("unused")
public class BetterRarity implements Tier {

    private final Style style;

    public BetterRarity(Style style) {
        this.style = style;
    }

    public BetterRarity(Formatting formatting) {
        this.style = Style.EMPTY.withFormatting(formatting);
    }

    public BetterRarity(Formatting... formattings) {
        this.style = Style.EMPTY.withFormatting(formattings);
    }

    public BetterRarity(int color) {
        this.style = Style.EMPTY.withColor(color);
    }

    @Override
    public Style getStyle() {
        return style;
    }
}
