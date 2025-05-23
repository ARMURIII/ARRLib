package arr.armuriii.arrlib.util.item;

import arr.armuriii.arrlib.init.ARRLibTiers;
import arr.armuriii.arrlib.util.item.tier.Tier;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.WeakHashMap;

public class ARRLibItemInternals {

    private static final WeakHashMap<Item.Settings, ExtraData> extraData = new WeakHashMap();

    public static ExtraData computeExtraData(Item.Settings settings) {
        return extraData.computeIfAbsent(settings, (s) -> new ExtraData());
    }

    public static void onBuild(Item.Settings settings, Item item) {
        ExtraData data = extraData.get(settings);
        if (data != null) {
            item.ARRLib$setTier(data.tier);
            item.ARRLib$setImportantItemEntity(data.importantItem);
            item.ARRLib$setExplosionProof(data.explosionProof);
        }else {
            item.ARRLib$setTier(ARRLibTiers.UNSET);
            item.ARRLib$setImportantItemEntity(false);
            item.ARRLib$setExplosionProof(false);
        }

    }

    public static final class ExtraData {
        private @Nullable Tier tier;
        private boolean importantItem;
        private boolean explosionProof;

        public ExtraData() {
            importantItem = false;
            explosionProof = false;
        }

        public void tier(Tier tier) {this.tier = tier;}

        public void importantItem(boolean bool) {this.importantItem = bool;}

        public void explosionProof(boolean bool) {this.explosionProof = bool;}
    }
}
