package com.example.examplemod;

import com.example.examplemod.exampleclasses.ExampleColors;
import com.example.regilite.holder.RegiliteItem;
import com.example.regilite.registry.ItemRegistry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

public class Items {
    public static final ItemRegistry ITEMS = ItemRegistry.createRegistry(ExampleMod.MODID);

    public static final RegiliteItem<Item> EXAMPLE_ITEM = ITEMS.registerItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEat().nutrition(1).saturationMod(2f).build()))
            .addTags(ItemTags.WOOL)
            .setTranslation("Test Example Item")
            .setColorSupplier(ExampleColors.ITEM)
            .setTab(null);

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
