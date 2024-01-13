package com.example.examplemod;

import com.example.regilite.registry.EnderDeferredItem;
import com.example.regilite.registry.EnderItemRegistry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

public class Items {
    public static final EnderItemRegistry ITEMS = EnderItemRegistry.createRegistry(ExampleMod.MODID);

    public static final EnderDeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEat().nutrition(1).saturationMod(2f).build()))
            .addItemTags(ItemTags.WOOL)
            .setTranslation("Test Example Item")
            .setTab(null);

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
