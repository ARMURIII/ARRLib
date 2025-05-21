package arr.armuriii.arrlib.util;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

import static io.github.fabricators_of_create.porting_lib.asm.ASMUtils.mapC;

public class ASMUtil implements Runnable {

    @Override
    public void run() {
        extendEnums();
    }

    private static void extendEnums() {
        //Formatting
        /*ClassTinkerers.enumBuilder(mapC("class_124"), "Ljava/lang/String;", "C", "I", "Ljava/lang/Integer;")
                .addEnum("MAUVE", () -> new Object[] {"MAUVE",'p',16,14725375})
                .build();
        ClassTinkerers.enumBuilder(mapC("class_124"), "Ljava/lang/String;", "C", "I", "Ljava/lang/Integer;")
                .addEnum("MELLOW", () -> new Object[] {"MELLOW",'q',17,16309886})
                .build();
        ClassTinkerers.enumBuilder(mapC("class_124"), "Ljava/lang/String;", "C", "I", "Ljava/lang/Integer;")
                .addEnum("SCULK", () -> new Object[] {"SCULK",'s',18,288381})
                .build();*/
        //Rarity
        ClassTinkerers.enumBuilder(mapC("class_1814"), "L" + mapC("class_124") + ";")  // Rarity // Formatting
                .addEnum("LEGENDARY", () -> new Object[] { Formatting.GOLD})
                .build();
        ClassTinkerers.enumBuilder(mapC("class_1814"), "L" + mapC("class_124") + ";")  // Rarity // Formatting
                .addEnum("COSMIC", () -> new Object[] { Formatting.LIGHT_PURPLE})
                .build();
        ClassTinkerers.enumBuilder(mapC("class_1814"), "L" + mapC("class_124") + ";")  // Rarity // Formatting
                .addEnum("CURSED", () -> new Object[] {Formatting.WHITE})
                .build();
        ClassTinkerers.enumBuilder(mapC("class_1814"), "L" + mapC("class_124") + ";")  // Rarity // Formatting
                .addEnum("BLESSED", () -> new Object[] {Formatting.WHITE})
                .build();
        ClassTinkerers.enumBuilder(mapC("class_1814"), "L" + mapC("class_124") + ";")  // Rarity // Formatting
                .addEnum("BLOOD", () -> new Object[] {Formatting.DARK_RED})
                .build();
        ClassTinkerers.enumBuilder(mapC("class_1814"), "L" + mapC("class_124") + ";")  // Rarity // Formatting
                .addEnum("WARDEN", () -> new Object[] {Formatting.WHITE})
                .build();
    }

    public static String prefix(String name) {
        return "arrlib:" + name.toLowerCase(Locale.ROOT);
    }
}