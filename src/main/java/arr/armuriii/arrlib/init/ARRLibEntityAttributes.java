package arr.armuriii.arrlib.init;

import arr.armuriii.arrlib.ARRLib;
import arr.armuriii.arrlib.util.ModHelper;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

public class ARRLibEntityAttributes {
    public static final EntityAttribute SWEEPING = create("sweeping", 0.0, 0.0, 16.0);

    private static EntityAttribute create(final String name, final double base, final double min, final double max) {
        return new ClampedEntityAttribute("attribute.name.generic." + ARRLib.MOD_HELPER.getModID()  + "." + name, base, min, max).setTracked(true);
    }

    public static void register(ModHelper modHelper) {
        modHelper.registerEntityAttribute("sweeping", SWEEPING);
    }
}
