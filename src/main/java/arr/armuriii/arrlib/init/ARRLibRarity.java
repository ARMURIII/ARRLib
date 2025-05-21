package arr.armuriii.arrlib.init;

import com.chocohead.mm.api.ClassTinkerers;
import com.sun.source.tree.ClassTree;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.Locale;
import java.util.Objects;

public class ARRLibRarity {
    public static final Rarity LEGENDARY = ClassTinkerers.getEnum(Rarity.class,"LEGENDARY");
    public static final Rarity COSMIC = ClassTinkerers.getEnum(Rarity.class,"COSMIC");
    public static final Rarity CURSED = ClassTinkerers.getEnum(Rarity.class,"CURSED");
    public static final Rarity BLESSED = ClassTinkerers.getEnum(Rarity.class,"BLESSED");
    public static final Rarity BLOOD = ClassTinkerers.getEnum(Rarity.class,"BLOOD");
    public static final Rarity WARDEN = ClassTinkerers.getEnum(Rarity.class,"WARDEN");
}
