package arr.armuriii.arrlib.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unused"})
public class ModHelper {

    private final String MOD_ID;
    private final Logger MOD_LOGGER;

    public ModHelper(String modID) {
        this.MOD_ID = modID;
        this.MOD_LOGGER = LoggerFactory.getLogger(modID);
    }

    public String getModID() {
        return MOD_ID;
    }

    public Logger getLogger() {
        return MOD_LOGGER;
    }

    public Identifier id(String id) {
        return Identifier.of(getModID(), id);
    }

    // REGISTERING
    public <V, T extends V> T register(Registry<V> registry, String name, T entry) {
        return Registry.register(registry, id(name), entry);
    }

    public Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, id(name), item);
    }

    public void addItemToItemGroup(Item item, RegistryKey<ItemGroup> group) {
        ItemGroupEvents.modifyEntriesEvent(group).register((entries)-> entries.add(item.getDefaultStack()));
    }

    public void addItemToItemGroup(ItemStack item, RegistryKey<ItemGroup> group) {
        ItemGroupEvents.modifyEntriesEvent(group).register((entries)-> entries.add(item));
    }

    public void addItemToItemGroupBefore(ItemStack item, RegistryKey<ItemGroup> group, ItemStack beforeStack) {
        ItemGroupEvents.modifyEntriesEvent(group).register((entries)-> entries.addBefore(beforeStack,item));
    }

    public void addItemToItemGroupAfter(ItemStack item, RegistryKey<ItemGroup> group, ItemStack afterStack) {
        ItemGroupEvents.modifyEntriesEvent(group).register((entries)-> entries.addAfter(afterStack,item));
    }

    public Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, id(name), block);
    }

    public Block registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, id(name),
                new BlockItem(block, new FabricItemSettings()));
        return registerBlock(name,block);
    }

    public Block registerBlockItem(String name, Block block, FabricItemSettings settings) {
        Registry.register(Registries.ITEM, id(name),
                new BlockItem(block, settings));
        return registerBlock(name,block);
    }

    public Block registerBlockItem(String name, Block block, Item item) {
        Registry.register(Registries.ITEM, id(name), item);
        return registerBlock(name,block);
    }

    public StatusEffect registerStatusEffect(String name, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, id(name), effect);
    }

    public EntityAttribute registerEntityAttribute(String name, EntityAttribute attribute) {
        return Registry.register(Registries.ATTRIBUTE, id(name), attribute);
    }

    @Environment(EnvType.CLIENT)
    public void registerModelPredicate(Item item, String name, ClampedModelPredicateProvider predicateProvider) {
        FabricModelPredicateProviderRegistry.register(item, id(name), predicateProvider);
    }

    public void logDebug(String debug) {
        getLogger().debug(debug);
    }

    public void logInfo(String info) {
        getLogger().info(info);
    }

    public void logError(String error) {
        getLogger().error(error);
    }


}
