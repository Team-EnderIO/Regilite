package com.enderio.regilite.examplemod;

import com.enderio.regilite.examplemod.exampleclasses.ExampleColors;
import com.enderio.regilite.holder.RegiliteItem;
import com.enderio.regilite.registry.ItemRegistry;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

public class Items {
    public static final ItemRegistry ITEMS = ItemRegistry.createRegistry(ExampleMod.MODID);

    public static final RegiliteItem<Item> EXAMPLE_ITEM = ITEMS.registerItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
                    .alwaysEat().nutrition(1).saturationMod(2f).build()))
            .addItemTags(ItemTags.WOOL)
            .setTranslation("Test Example Item")
            .setColorSupplier(() -> ExampleColors.ITEM)
            .setTab(null);

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
