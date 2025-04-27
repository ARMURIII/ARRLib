package arr.armuriii.arrlib.client.datagen;

import arr.armuriii.arrlib.init.ARRLibEntityAttributes;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ARRLibEntityAttributesGenerator extends FabricLanguageProvider {

    protected ARRLibEntityAttributesGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ARRLibEntityAttributes.SWEEPING,"Sweeping Range");
    }
}
