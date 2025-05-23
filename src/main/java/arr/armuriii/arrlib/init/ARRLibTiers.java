package arr.armuriii.arrlib.init;

import arr.armuriii.arrlib.util.BuiltinColors;
import arr.armuriii.arrlib.util.item.tier.Tier;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

@SuppressWarnings("unused")
public enum ARRLibTiers implements Tier {
    UNSET(Formatting.RESET),
    COMMON(Formatting.WHITE),
    UNCOMMON(Formatting.YELLOW),
    RARE(Formatting.AQUA),
    EPIC(Formatting.LIGHT_PURPLE),
    LEGENDARY(Formatting.GOLD),
    COSMIC(Formatting.DARK_PURPLE),
    CELESTE(BuiltinColors.CELESTE),
    CURSED(BuiltinColors.MAUVE),
    BLESSED(BuiltinColors.MELLOW),
    BLOOD(Formatting.DARK_RED),
    WARDEN(BuiltinColors.PRUSSIAN),
    REDACTED(Formatting.STRIKETHROUGH,Formatting.RED);

    private final Style style;

    ARRLibTiers(Style style) {
        this.style = style;
    }
    ARRLibTiers(Formatting formatting) {
        this.style = Style.EMPTY.withFormatting(formatting);
    }
    ARRLibTiers(Formatting... formatting) {
        this.style = Style.EMPTY.withFormatting(formatting);
    }
    ARRLibTiers(Integer color) {
        this.style = Style.EMPTY.withColor(color);
    }

    @Override
    public Style getStyle() {
        return style;
    }
}
